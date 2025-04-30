/*    
    Copyright (C) Paul Falstad and Iain Sharp
    
    This file is part of CircuitJS1.

    CircuitJS1 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    CircuitJS1 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CircuitJS1.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lushprojects.circuitjs1.client;

import com.lushprojects.circuitjs1.client.util.Locale;

class CapacitorElm extends CircuitElm {
	double capacitance;
	double compResistance, voltdiff, seriesResistance;
	double initialVoltage;
	int capNode2;
	Point plate1[], plate2[];
	public static final int FLAG_BACK_EULER = 2;
	public static final int FLAG_RESISTANCE = 4;

	public CapacitorElm(int xx, int yy) {
	    super(xx, yy);
	    capacitance = 1e-5;
	    initialVoltage = 1e-3;
	}
	public CapacitorElm(int xa, int ya, int xb, int yb, int f,
			    StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    capacitance = new Double(st.nextToken()).doubleValue();
	    voltdiff = new Double(st.nextToken()).doubleValue();
	    initialVoltage = 1e-3;
	    try {
		initialVoltage = new Double(st.nextToken()).doubleValue();
		if ((flags & FLAG_RESISTANCE) != 0)
		    seriesResistance = new Double(st.nextToken()).doubleValue();

		// if you add more things here, check PolarCapacitorElm.  It loads more state after this
	    } catch (Exception e) {}
	}
	boolean isTrapezoidal() { return (flags & FLAG_BACK_EULER) == 0; }
	
	void reset() {
	    super.reset();
	    current = curcount = curSourceValue = 0;
	    // put small charge on caps when reset to start oscillators
	    voltdiff = initialVoltage;
	}
	void shorted() {
	    super.reset();
	    voltdiff = current = curcount = curSourceValue = 0;
	}
	int getDumpType() { return 'c'; }

	String dump() {
	    flags |= FLAG_RESISTANCE;
	    return super.dump() + " " + capacitance + " " + voltdiff + " " + initialVoltage + " " + seriesResistance;
	}
	
	// used for PolarCapacitorElm
	Point platePoints[];
	
	void setPoints() {
	    super.setPoints();
	    double f = (dn/2-4)/dn;
	    // calc leads
	    lead1 = interpPoint(point1, point2, f);
	    lead2 = interpPoint(point1, point2, 1-f);
	    // calc plates
	    plate1 = newPointArray(2);
	    plate2 = newPointArray(2);
	    interpPoint2(point1, point2, plate1[0], plate1[1], f, 12);
	    interpPoint2(point1, point2, plate2[0], plate2[1], 1-f, 12);
	}
	
	void draw(Graphics g) {
	    int hs = 12;
	    setBbox(point1, point2, hs);
	    
	    // draw first lead and plate
	    setVoltageColor(g, volts[0]);
	    drawThickLine(g, point1, lead1);
	    setPowerColor(g, false);
	    drawThickLine(g, plate1[0], plate1[1]);
	    if (sim.powerCheckItem.getState())
		g.setColor(Color.gray);

	    // draw second lead and plate
	    setVoltageColor(g, volts[1]);
	    drawThickLine(g, point2, lead2);
	    setPowerColor(g, false);
	    if (platePoints == null)
		drawThickLine(g, plate2[0], plate2[1]);
	    else {
		int i;
		for (i = 0; i != platePoints.length-1; i++)
		    drawThickLine(g,  platePoints[i], platePoints[i+1]);
	    }
	    
	    updateDotCount();
	    if (sim.dragElm != this) {
		drawDots(g, point1, lead1, curcount);
		drawDots(g, point2, lead2, -curcount);
	    }
	    drawPosts(g);
	    if (sim.showValuesCheckItem.getState()) {
		String s = getShortUnitText(capacitance, "F");
		drawValues(g, s, hs);
	    }
	}
	void stamp() {
	    if (sim.dcAnalysisFlag) {
		// when finding DC operating point, replace cap with a 100M resistor
		sim.stampResistor(nodes[0], nodes[1], 1e8);
		curSourceValue = 0;
		capNode2 = 1;
		return;
	    }
	    
	    // The capacitor model is between nodes 0 and capNode2.  For an
	    // ideal capacitor, capNode2 is node 1.  If series resistance, capNode2 = 2
	    // and we place a resistor between nodes 2 and 1.
	    // 2 is an internal node, 0 and 1 are the capacitor terminals.
	    capNode2 = (seriesResistance > 0) ? 2 : 1;

	    // capacitor companion model using trapezoidal approximation
	    // (Norton equivalent) consists of a current source in
	    // parallel with a resistor.  Trapezoidal is more accurate
	    // than backward euler but can cause oscillatory behavior
	    // if RC is small relative to the timestep.
	    if (isTrapezoidal())
		compResistance = sim.timeStep/(2*capacitance);
	    else
		compResistance = sim.timeStep/capacitance;
	    sim.stampResistor(nodes[0], nodes[capNode2], compResistance);
	    sim.stampRightSide(nodes[0]);
	    sim.stampRightSide(nodes[capNode2]);
	    if (seriesResistance > 0)
		sim.stampResistor(nodes[1], nodes[2], seriesResistance);
	}
	void startIteration() {
	    if (isTrapezoidal())
		curSourceValue = -voltdiff/compResistance-current;
	    else
		curSourceValue = -voltdiff/compResistance;
	}
	
	void stepFinished() {
	    voltdiff = volts[0]-volts[capNode2];
	    calculateCurrent();
	}
	
	void setNodeVoltage(int n, double c) {
	    // do not calculate current, that only gets done in stepFinished().  otherwise calculateCurrent() may get
	    // called while stamping the circuit, which might discharge the cap (since we use that current to calculate
	    // curSourceValue in startIteration)
	    volts[n] = c;
	}	
	
	void calculateCurrent() {
	    double voltdiff = volts[0] - volts[capNode2];
	    if (sim.dcAnalysisFlag) {
		current = voltdiff/1e8;
		return;
	    }
	    // we check compResistance because this might get called
	    // before stamp(), which sets compResistance, causing
	    // infinite current
	    if (compResistance > 0)
		current = voltdiff/compResistance + curSourceValue;
	}
	double curSourceValue;
	void doStep() {
	    if (sim.dcAnalysisFlag)
		return;
	    sim.stampCurrentSource(nodes[0], nodes[capNode2], curSourceValue);
 	}
	int getInternalNodeCount() { return (!sim.dcAnalysisFlag && seriesResistance > 0) ? 1 : 0; }
	void getInfo(String arr[]) {
	    arr[0] = "capacitor";
	    getBasicInfo(arr);
	    arr[3] = "C = " + getUnitText(capacitance, "F");
	    arr[4] = "P = " + getUnitText(getPower(), "W");
	    //double v = getVoltageDiff();
	    //arr[4] = "U = " + getUnitText(.5*capacitance*v*v, "J");
	}
	@Override
	String getScopeText(int v) {
	    return Locale.LS("capacitor") + ", " + getUnitText(capacitance, "F");
	}
	public EditInfo getEditInfo(int n) {
	    if (n == 0)
		return new EditInfo("Capacitance (F)", capacitance, 1e-6, 1e-3);
	    if (n == 1) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Trapezoidal Approximation", isTrapezoidal());
		return ei;
	    }
	    if (n == 2)
		return new EditInfo("Initial Voltage (on Reset)", initialVoltage);
	    if (n == 3)
		return new EditInfo("Series Resistance (0 = infinite)", seriesResistance);
	    // if you add more things here, check PolarCapacitorElm
	    return null;
	}
	public void setEditValue(int n, EditInfo ei) {
	    if (n == 0)
		capacitance = (ei.value > 0) ? ei.value : 1e-12;
	    if (n == 1) {
		if (ei.checkbox.getState())
		    flags &= ~FLAG_BACK_EULER;
		else
		    flags |= FLAG_BACK_EULER;
	    }
	    if (n == 2)
		initialVoltage = ei.value;
	    if (n == 3)
		seriesResistance = ei.value;
	}
	int getShortcut() { return 'c'; }
	public double getCapacitance() { return capacitance; }
	public double getSeriesResistance() { return seriesResistance; }
	public void setCapacitance(double c) { capacitance = c; }
	public void setSeriesResistance(double c) { seriesResistance = c; }
	public boolean isIdealCapacitor() { return (seriesResistance == 0); }
    }
