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

    abstract class GateElm extends CircuitElm {
	final int FLAG_SMALL = 1<<0;
	final int FLAG_SCHMITT = 1<<1;
	final int FLAG_INVERT_INPUTS = 1<<2;
	int inputCount = 2;
	boolean lastOutput;
	double highVoltage;
	public static double lastHighVoltage = 5;
	static boolean lastSchmitt = false;
	
	public GateElm(int xx, int yy) {
	    super(xx, yy);
	    noDiagonal = true;
	    inputCount = 2;
	    
	    // copy defaults from last gate edited
	    highVoltage = lastHighVoltage;
	    if (lastSchmitt)
		flags |= FLAG_SCHMITT;
	    
	    setSize(sim.smallGridCheckItem.getState() ? 1 : 2);
	}
	public GateElm(int xa, int ya, int xb, int yb, int f,
			StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    inputCount = new Integer(st.nextToken()).intValue();
	    double lastOutputVoltage = new Double (st.nextToken()).doubleValue();
	    noDiagonal = true;
	    highVoltage = 5;
	    try {
		highVoltage = new Double(st.nextToken()).doubleValue();
	    } catch (Exception e) { }
	    lastOutput = lastOutputVoltage > highVoltage*.5;
	    setSize((f & FLAG_SMALL) != 0 ? 1 : 2);
	    allocNodes();
	    setupVolts();
	}
	boolean isInverting() { return false; }
	int gsize, gwidth, gwidth2, gheight, hs2;
	void setSize(int s) {
	    gsize = s;
	    gwidth = 7*s;
	    gwidth2 = 14*s;
	    gheight = 8*s;
	    flags &= ~FLAG_SMALL;
	    flags |= (s == 1) ? FLAG_SMALL : 0;
	}
	String dump() {
	    return super.dump() + " " + inputCount + " " + volts[inputCount] + " " + highVoltage;
	}
	Point inPosts[], inGates[];
	boolean inputStates[];
	int ww;
	void setPoints() {
	    super.setPoints();
	    inputStates = new boolean[inputCount];
	    if (dn > 150 && this == sim.dragElm)
		setSize(2);
	    int hs = gheight;
	    int i;
	    ww = gwidth2; // was 24
	    if (ww > dn/2)
		ww = (int) (dn/2);
	    if (isInverting() && ww+8 > dn/2)
		ww = (int) (dn/2-8);
	    calcLeads(ww*2);
	    inPosts = new Point[inputCount];
	    inGates = new Point[inputCount];
	    int i0 = -inputCount/2;
	    if (hasFlag(FLAG_INVERT_INPUTS))
		icircles = new Point[inputCount];
	    else
		icircles = null;
	    for (i = 0; i != inputCount; i++, i0++) {
		if (i0 == 0 && (inputCount & 1) == 0)
		    i0++;
		double adj = getLeadAdjustment(i);
                inPosts[i] = interpPoint(point1, point2, 0, hs*i0);
		inGates[i] = interpPoint(lead1,  lead2,  icircles != null ? -8/(ww*2.)+adj : adj, hs*i0);
		if (icircles != null)
		    icircles[i] = interpPoint(lead1, lead2,  -4/(ww*2.), hs*i0);
	    }
	    hs2 = gwidth*(inputCount/2+1);
	    setBbox(point1, point2, hs2);
	    if (hasSchmittInputs())
		schmittPoly = getSchmittPolygon(gsize, .47f);
	}
	
	// Restore state if loading from file or volts is reallocated.
	void setupVolts() {
	    int i;
	    // We don't remember all the inputs, just the last output.
	    // Fill inputs with something that keeps output the same.
	    for (i = 0; i != inputCount; i++)
		volts[i] = (lastOutput ^ isInverting()) ? highVoltage : 0;
	}

	double getLeadAdjustment(int ix) { return 0; }
	
	void createEuroGatePolygon() {
	    Point pts[] = newPointArray(4);
	    interpPoint2(lead1, lead2, pts[0], pts[1], 0, hs2);
	    interpPoint2(lead1, lead2, pts[3], pts[2], 1, hs2);
	    gatePoly = createPolygon(pts);
	}

	String getGateText() { return null; }
	static boolean useEuroGates() { return sim.euroGatesCheckItem.getState(); }

	void drawGatePolygon(Graphics g) {
	    drawThickPolygon(g, gatePoly);
	}
	
	void draw(Graphics g) {
	    int i;
	    for (i = 0; i != inputCount; i++) {
		setVoltageColor(g, volts[i]);
		drawThickLine(g, inPosts[i], inGates[i]);
	    }
	    setVoltageColor(g, volts[inputCount]);
	    drawThickLine(g, lead2, point2);
	    g.setColor(needsHighlight() ? selectColor : lightGrayColor);
	    if (useEuroGates()) {
		drawThickPolygon(g, gatePoly);
		Point center = interpPoint(point1, point2, .5);
		drawCenteredText(g, getGateText(), center.x, center.y-6*gsize, true);
	    } else
	        drawGatePolygon(g);
	    g.setLineWidth(2);
	    if (hasSchmittInputs())
		drawPolygon(g, schmittPoly);
	    g.setLineWidth(1);
	    if (linePoints != null)
		for (i = 0; i != linePoints.length-1; i++)
		    drawThickLine(g, linePoints[i], linePoints[i+1]);
	    if (isInverting())
		drawThickCircle(g, pcircle.x, pcircle.y, 3);
	    if (icircles != null)
		for (i = 0; i != inputCount; i++)
		    drawThickCircle(g, icircles[i].x, icircles[i].y, 3);
	    curcount = updateDotCount(current, curcount);
	    drawDots(g, lead2, point2, curcount);
	    drawPosts(g);
	}
	Polygon gatePoly, schmittPoly;
	Point pcircle, linePoints[], icircles[];
	int getPostCount() { return inputCount+1; }
	Point getPost(int n) {
	    if (n == inputCount)
		return point2;
	    return inPosts[n];
	}
	int getVoltageSourceCount() { return 1; }
	abstract String getGateName();
	void getInfo(String arr[]) {
	    arr[0] = getGateName();
	    arr[1] = "Vout = " + getVoltageText(volts[inputCount]);
	    arr[2] = "Iout = " + getCurrentText(getCurrent());
	}
	void stamp() {
	    sim.stampVoltageSource(0, nodes[inputCount], voltSource);
	}
	boolean hasSchmittInputs() { return (flags & FLAG_SCHMITT) != 0; }
	boolean getInput(int x) {
	    boolean high = !hasFlag(FLAG_INVERT_INPUTS);
	    if (!hasSchmittInputs())
		return (volts[x] > highVoltage*.5) ? high : !high;
	    boolean res = volts[x] > highVoltage*(inputStates[x] ? .35 : .55);
	    inputStates[x] = res;
	    return res ? high : !high;
	}
	abstract boolean calcFunction();
	
	int oscillationCount;
	double lastTime;
	
	void doStep() {
	    boolean f = calcFunction();
	    if (isInverting())
		f = !f;
	    
	    if (lastTime != sim.t) {
		// detect oscillation (using same strategy as Atanua)
		if (lastOutput == !f) {
		    if (oscillationCount++ > 50) {
			// output is oscillating too much, randomly leave output the same
			oscillationCount = 0;
			if (sim.getrand(10) > 5)
			    f = lastOutput;
		    }
		} else
		    oscillationCount = 0;
	    
		lastOutput = f;
		lastTime = sim.t;
	    }
	    
	    double res = f ? highVoltage : 0;
	    sim.updateVoltageSource(0, nodes[inputCount], voltSource, res);
	}
	public EditInfo getEditInfo(int n) {
	    if (n == 0)
		return new EditInfo("# of Inputs", inputCount, 1, 8).
		    setDimensionless();
	    if (n == 1)
		return new EditInfo("High Logic Voltage", highVoltage, 1, 10);
	    if (n == 2)
		return EditInfo.createCheckbox("Schmitt Inputs", hasSchmittInputs());
	    if (n == 3)
		return EditInfo.createCheckbox("Invert Inputs", hasFlag(FLAG_INVERT_INPUTS));
	    return null;
	}

	public void setEditValue(int n, EditInfo ei) {
	    if (n == 0 && ei.value >= 1) {
		inputCount = (int) ei.value;
		allocNodes();
		setupVolts();
		setPoints();
	    }
	    if (n == 1)
		highVoltage = lastHighVoltage = ei.value;
	    if (n == 2) {
		if (ei.checkbox.getState())
		    flags |= FLAG_SCHMITT;
		else
		    flags &= ~FLAG_SCHMITT;
		lastSchmitt = hasSchmittInputs();
		setPoints();
	    }
	    if (n == 3) {
		flags = ei.changeFlag(flags, FLAG_INVERT_INPUTS);
		setPoints();
	    }
	}
	// there is no current path through the gate inputs, but there
	// is an indirect path through the output to ground.
	boolean getConnection(int n1, int n2) { return false; }
	boolean hasGroundConnection(int n1) {
	    return (n1 == inputCount);
	}
	
	double getCurrentIntoNode(int n) {
	    if (n == inputCount)
		return current;
	    return 0;
	}
    }

