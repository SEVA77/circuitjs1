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

// contributed by Edward Calver

class TriStateElm extends CircuitElm {
    double resistance, r_on, r_off, r_off_ground, highVoltage;

    // Unfortunately we need all three flags to keep track of flipping.
    // FLAG_FLIP_X/Y affect the rounding direction if the elm is an odd grid length.
    // FLAG_FLIP does not.
    final int FLAG_FLIP = 1;
    final int FLAG_FLIP_X = 2;
    final int FLAG_FLIP_Y = 4;

    public TriStateElm(int xx, int yy) {
	super(xx, yy);
	r_on = 0.1;
	r_off = 1e10;
	r_off_ground = 1e8;
	noDiagonal = true;
            
        // copy defaults from last gate edited
        highVoltage = GateElm.lastHighVoltage;
    }

    public TriStateElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
	super(xa, ya, xb, yb, f);
	r_on = 0.1;
	r_off = 1e10;
	r_off_ground = 0;
	noDiagonal = true;
	highVoltage = 5;
	try {
	    r_on = new Double(st.nextToken()).doubleValue();
	    r_off = new Double(st.nextToken()).doubleValue();
	    r_off_ground = new Double(st.nextToken()).doubleValue();
            highVoltage = new Double (st.nextToken()).doubleValue();
	} catch (Exception e) {
	}

    }

    String dump() {
	return super.dump() + " " + r_on + " " + r_off + " " + r_off_ground + " " + highVoltage;
    }

    int getDumpType() {
	return 180;
    }

    boolean open;

    Point ps, point3, lead3;

    Polygon gatePoly;

    void setPoints() {
	super.setPoints();
	int len = 32;
	calcLeads(len);
	adjustLeadsToGrid((flags & FLAG_FLIP_X) != 0, (flags & FLAG_FLIP_Y) != 0);

	ps = new Point();
	int hs = 16;

	int ww = 16;
	if (ww > dn / 2)
	    ww = (int) (dn / 2);
	Point triPoints[] = newPointArray(3);
	interpPoint2(lead1, lead2, triPoints[0], triPoints[1], 0, hs + 2);
	triPoints[2] = interpPoint(lead1, lead2, .5 + (ww - 2) / (double)len);
	gatePoly = createPolygon(triPoints);

	int sign = ((flags & FLAG_FLIP) == 0) ? -1 : 1;
	point3 = interpPoint(lead1, lead2, .5, sign*hs);
	lead3 = interpPoint(lead1, lead2, .5, sign*hs/2);
    }

    void draw(Graphics g) {
	int hs = 16;
	setBbox(point1, point2, hs);

	draw2Leads(g);

	g.setColor(lightGrayColor);
	drawThickPolygon(g, gatePoly);
	setVoltageColor(g, volts[2]);
	drawThickLine(g, point3, lead3);
	curcount = updateDotCount(current, curcount);
	drawDots(g, lead2, point2, curcount);
	drawPosts(g);
    }

    void calculateCurrent() {
	// current from node 3 to node 1
	double current31 = (volts[3]-volts[1])/resistance;
	
	// current from node 1 through pulldown
	double current10 = (r_off_ground == 0) ? 0 : volts[1]/r_off_ground;

	// output current is difference of these
	current = current31-current10;
    }

    double getCurrentIntoNode(int n) {
	if (n == 1)
	    return current;
	return 0;
    }

    // we need this to be able to change the matrix for each step
    boolean nonLinear() {
	return true;
    }

    // node 0: input
    // node 1: output
    // node 2: control input
    // node 3: internal node
    // there is a voltage source connected to node 3, and a resistor (r_off or r_on) from node 3 to 1.
    // then there is a pulldown resistor from node 1 to ground.
    void stamp() {
	sim.stampVoltageSource(0, nodes[3], voltSource);
	sim.stampNonLinear(nodes[3]);
	sim.stampNonLinear(nodes[1]);
    }

    void doStep() {
	open = (volts[2] < highVoltage*.5);
	resistance = (open) ? r_off : r_on;
	sim.stampResistor(nodes[3], nodes[1], resistance);
	
	// Add pulldown resistor for output, so that disabled tristate has output near ground if nothing
	// else is driving the output.  Otherwise people get confused.
	if (r_off_ground > 0)
	    sim.stampResistor(nodes[1], 0, r_off_ground);
	
	sim.updateVoltageSource(0, nodes[3], voltSource, volts[0] > highVoltage*.5 ? highVoltage : 0);
    }

    void drag(int xx, int yy) {
	// use mouse to select which side the buffer enable should be on
	boolean flip = (xx < x) == (yy < y);
	
	xx = sim.snapGrid(xx);
	yy = sim.snapGrid(yy);
	if (abs(x - xx) < abs(y - yy))
	    xx = x;
	else {
	    flip = !flip;
	    yy = y;
	}
	flags = flip ? (flags | FLAG_FLIP) : (flags & ~FLAG_FLIP);
	super.drag(xx, yy);
    }

    int getPostCount() {
	return 3;
    }
    
    int getInternalNodeCount() {
	return 1;
    }

    int getVoltageSourceCount() {
	return 1;
    }

    Point getPost(int n) {
	return (n == 0) ? point1 : (n == 1) ? point2 : point3;
    }

    void getInfo(String arr[]) {
	arr[0] = "tri-state buffer";
	arr[1] = open ? "open" : "closed";
	arr[2] = "Vd = " + getVoltageDText(getVoltageDiff());
	arr[3] = "I = " + getCurrentDText(getCurrent());
	arr[4] = "Vc = " + getVoltageText(volts[2]);
    }

    // there is no current path through the input, but there
    // is an indirect path through the output to ground.
    boolean getConnection(int n1, int n2) {
	return false;
    }

    boolean hasGroundConnection(int n1) {
	return (n1 == 1);
    }

    public EditInfo getEditInfo(int n) {
	if (n == 0)
	    return new EditInfo("On Resistance (ohms)", r_on, 0, 0);
	if (n == 1)
	    return new EditInfo("Off Resistance (ohms)", r_off, 0, 0);
	if (n == 2)
	    return new EditInfo("Output Pulldown Resistance (ohms)", r_off_ground, 0, 0);
        if (n == 3)
            return new EditInfo("High Logic Voltage", highVoltage, 1, 10);
	return null;
    }

    public void setEditValue(int n, EditInfo ei) {

	if (n == 0 && ei.value > 0)
	    r_on = ei.value;
	if (n == 1 && ei.value > 0)
	    r_off = ei.value;
	if (n == 2 && ei.value > 0)
	    r_off_ground = ei.value;
	if (n == 3)
            highVoltage = GateElm.lastHighVoltage = ei.value;
    }

    void flipX(int c2, int count) {
	flags ^= FLAG_FLIP|FLAG_FLIP_X;
	super.flipX(c2, count);
    }

    void flipY(int c2, int count) {
	flags ^= FLAG_FLIP|FLAG_FLIP_Y;
	super.flipY(c2, count);
    }

    void flipXY(int c2, int count) {
	flags ^= FLAG_FLIP;
	super.flipXY(c2, count);
    }
}

