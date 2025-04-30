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

import java.util.Vector;

import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Random;
import com.lushprojects.circuitjs1.client.util.Locale;

// circuit element class
public abstract class CircuitElm implements Editable {
    static double voltageRange = 5;
    static int colorScaleCount = 201; // odd so ground = gray 
    static Color colorScale[];
    static double currentMult, powerMult;
    
    // scratch points for convenience
    static Point ps1, ps2;
    
    static CirSim sim;
    static public Color whiteColor, lightGrayColor, selectColor;
    static public Color positiveColor, negativeColor, neutralColor, currentColor;
    static Font unitsFont;

    static NumberFormat showFormat, shortFormat, fixedFormat;
    static final double pi = 3.14159265358979323846;
    static CircuitElm mouseElmRef = null;

    static final int SCALE_AUTO = 0;
    static final int SCALE_1 = 1;
    static final int SCALE_M = 2;
    static final int SCALE_MU = 3;
    
    static int decimalDigits, shortDecimalDigits;
 
    // initial point where user created element.  For simple two-terminal elements, this is the first node/post.
    int x, y;
    
    // point to which user dragged out element.  For simple two-terminal elements, this is the second node/post
    int x2, y2;
    
    int flags, nodes[], voltSource;
    
    // length along x and y axes, and sign of difference
    int dx, dy, dsign;

    int lastHandleGrabbed=-1;
    
    // length of element
    double dn;
    
    double dpx1, dpy1;
    
    // (x,y) and (x2,y2) as Point objects
    Point point1, point2;
    
    // lead points (ends of wire stubs for simple two-terminal elements)  
    Point lead1, lead2;
    
    // voltages at each node
    double volts[];
    
    double current, curcount;
    Rectangle boundingBox;
    
    // if subclasses set this to true, element will be horizontal or vertical only 
    boolean noDiagonal;
    
    public boolean selected;
    
    boolean hasWireInfo; // used in calcWireInfo()
    
//    abstract int getDumpType();
    int getDumpType() {
	
	throw new IllegalStateException(); // Seems necessary to work-around what appears to be a compiler
	// bug affecting OTAElm to make sure this method (which should really be abstract) throws
	// an exception.  If you're getting this, try making small update to CompositeElm.java and try again
 }
    
    // leftover from java, doesn't do anything anymore. 
    Class getDumpClass() { return getClass(); }
    
    int getDefaultFlags() { return 0; }

    boolean hasFlag(int f) { return (flags & f) != 0; }
    
    static void initClass(CirSim s) {
	unitsFont = new Font("SansSerif", 0, 12);
	sim = s;
	
	colorScale = new Color[colorScaleCount];
	
	
	ps1 = new Point();
	ps2 = new Point();
	
	Storage stor = Storage.getLocalStorageIfSupported();
	decimalDigits = 3;
	shortDecimalDigits = 1;
	if (stor != null) {
	    String s1 = stor.getItem("decimalDigits");
	    String s2 = stor.getItem("decimalDigitsShort");
	    if (s1 != null)
		decimalDigits = Integer.parseInt(s1);
	    if (s2 != null)
		shortDecimalDigits = Integer.parseInt(s2);
	}
	setDecimalDigits(decimalDigits, false, false);
	setDecimalDigits(shortDecimalDigits, true, false);
    }

    static void setDecimalDigits(int num, boolean sf, boolean save) {
	if (sf)
	    shortDecimalDigits = num;
	else
	    decimalDigits = num;
	
	String s = "####.";
	int ct = num;
	for (; ct > 0; ct--)
	    s += '#';
	NumberFormat nf = NumberFormat.getFormat(s);
	if (sf)
	    shortFormat = nf;
	else
	    showFormat = nf;
	
	if (save) {
	    Storage stor = Storage.getLocalStorageIfSupported();
	    if (stor != null)
		stor.setItem(sf ? "decimalDigitsShort" : "decimalDigits", Integer.toString(num));
	}
	
	if (!sf) {
	    s = "####.";
	    ct = num;
	    for (; ct > 0; ct--)
		s += '0';
	    fixedFormat = NumberFormat.getFormat(s);
	}
    }
    
    static void setColorScale() {

	int i;

	if (positiveColor == null)
	    positiveColor = Color.green;
	if (negativeColor == null)
	    negativeColor = Color.red;
	if (neutralColor == null)
	    neutralColor = Color.gray;
	
	for (i = 0; i != colorScaleCount; i++) {
	    double v = i * 2. / colorScaleCount - 1;
	    if (v < 0) {
		colorScale[i] = new Color(neutralColor, negativeColor, -v);
	    } else {
		colorScale[i] = new Color(neutralColor, positiveColor, v);
	    }
	}

    }
    
    // create new element with one post at xx,yy, to be dragged out by user
    CircuitElm(int xx, int yy) {
	x = x2 = xx;
	y = y2 = yy;
	flags = getDefaultFlags();
	allocNodes();
	initBoundingBox();
    }
    
    // create element between xa,ya and xb,yb from undump
    CircuitElm(int xa, int ya, int xb, int yb, int f) {
	x = xa; y = ya; x2 = xb; y2 = yb; flags = f;
	allocNodes();
	initBoundingBox();
    }
    
    void initBoundingBox() {
	boundingBox = new Rectangle();
	boundingBox.setBounds(min(x, x2), min(y, y2),
			      abs(x2-x)+1, abs(y2-y)+1);
    }
    
    // allocate nodes/volts arrays we need
    void allocNodes() {
	int n = getPostCount() + getInternalNodeCount();
	// preserve voltages if possible
	if (nodes == null || nodes.length != n) {
	    nodes = new int[n];
	    volts = new double[n];
	}
    }
    
    // dump component state for export/undo
    String dump() {
	int t = getDumpType();
	return (t < 127 ? ((char)t)+" " : t+" ") + x + " " + y + " " +
	    x2 + " " + y2 + " " + flags;
    }
    
    // handle reset button
    void reset() {
	int i;
	for (i = 0; i != getPostCount()+getInternalNodeCount(); i++)
	    volts[i] = 0;
	curcount = 0;
    }
    void draw(Graphics g) {}
    
    // set current for voltage source vn to c.  vn will be the same value as in a previous call to setVoltageSource(n, vn) 
    void setCurrent(int vn, double c) { current = c; }
    
    // get current for one- or two-terminal elements
    double getCurrent() { return current; }

    void setParentList(Vector<CircuitElm> elmList) {}
    
    // stamp matrix values for linear elements.
    // for non-linear elements, use this to stamp values that don't change each iteration, and call stampRightSide() or stampNonLinear() as needed
    void stamp() {}
    
    // stamp matrix values for non-linear elements
    void doStep() {}
    
    void delete() {
	if (mouseElmRef==this)
	    mouseElmRef=null;
	sim.deleteSliders(this);
    }
    void startIteration() {}
    
    // get voltage of x'th node
    double getPostVoltage(int x) { return volts[x]; }
    
    // set voltage of x'th node, called by simulator logic
    void setNodeVoltage(int n, double c) {
	volts[n] = c;
	calculateCurrent();
    }
    
    // calculate current in response to node voltages changing
    void calculateCurrent() {}
    
    // calculate post locations and other convenience values used for drawing.  Called when element is moved 
    void setPoints() {
    	dx = x2-x; dy = y2-y;
    	dn = Math.sqrt(dx*dx+dy*dy);
    	dpx1 = dy/dn;
    	dpy1 = -dx/dn;
    	dsign = (dy == 0) ? sign(dx) : sign(dy);
    	point1 = new Point(x , y );
    	point2 = new Point(x2, y2);
    }
    
    // calculate lead points for an element of length len.  Handy for simple two-terminal elements.
    // Posts are where the user connects wires; leads are ends of wire stubs drawn inside the element.
    void calcLeads(int len) {
	if (dn < len || len == 0) {
	    lead1 = point1;
	    lead2 = point2;
	    return;
	}
	lead1 = interpPoint(point1, point2, (dn-len)/(2*dn));
	lead2 = interpPoint(point1, point2, (dn+len)/(2*dn));
    }

    // adjust leads so that the point exactly between them is a grid point (so we can place a terminal there)
    void adjustLeadsToGrid(boolean flipX, boolean flipY) {
        int cx = (point1.x+point2.x)/2;
        int cy = (point1.y+point2.y)/2;

	// when flipping, it changes the rounding direction.  need to adjust for this
	int roundx = (flipX) ? 1 : -1;
	int roundy = (flipY) ? 1 : -1;

        int adjx = sim.snapGrid(cx+roundx)-cx;
        int adjy = sim.snapGrid(cy+roundy)-cy;
        lead1.move(adjx, adjy);
        lead2.move(adjx, adjy);
    }

    // calculate point fraction f between a and b, linearly interpolated
    Point interpPoint(Point a, Point b, double f) {
	Point p = new Point();
	interpPoint(a, b, p, f);
	return p;
    }
    
    // calculate point fraction f between a and b, linearly interpolated, return it in c
    void interpPoint(Point a, Point b, Point c, double f) {
	c.x = (int) Math.floor(a.x*(1-f)+b.x*f+.48);
	c.y = (int) Math.floor(a.y*(1-f)+b.y*f+.48);
    }
    
    /**
     * Returns a point fraction f along the line between a and b and offset perpendicular by g
     * @param a 1st Point
     * @param b 2nd Point
     * @param f Fraction along line
     * @param g Fraction perpendicular to line
     * Returns interpolated point in c
     */
    void interpPoint(Point a, Point b, Point c, double f, double g) {
	int gx = b.y-a.y;
	int gy = a.x-b.x;
	g /= Math.sqrt(gx*gx+gy*gy);
	c.x = (int) Math.floor(a.x*(1-f)+b.x*f+g*gx+.48);
	c.y = (int) Math.floor(a.y*(1-f)+b.y*f+g*gy+.48);
    }
    
    /**
     * Returns a point fraction f along the line between a and b and offset perpendicular by g
     * @param a 1st Point
     * @param b 2nd Point
     * @param f Fraction along line
     * @param g Fraction perpendicular to line
     * @return Interpolated point
     */
    Point interpPoint(Point a, Point b, double f, double g) {
	Point p = new Point();
	interpPoint(a, b, p, f, g);
	return p;
    }
    
    
    /**
     * Calculates two points fraction f along the line between a and b and offest perpendicular by +/-g
     * @param a 1st point (In)
     * @param b 2nd point (In)
     * @param c 1st point (Out)
     * @param d 2nd point (Out)
     * @param f Fraction along line
     * @param g Fraction perpendicular to line
     */
    void interpPoint2(Point a, Point b, Point c, Point d, double f, double g) {
//	int xpd = b.x-a.x;
//	int ypd = b.y-a.y;
	int gx = b.y-a.y;
	int gy = a.x-b.x;
	g /= Math.sqrt(gx*gx+gy*gy);
	c.x = (int) Math.floor(a.x*(1-f)+b.x*f+g*gx+.48);
	c.y = (int) Math.floor(a.y*(1-f)+b.y*f+g*gy+.48);
	d.x = (int) Math.floor(a.x*(1-f)+b.x*f-g*gx+.48);
	d.y = (int) Math.floor(a.y*(1-f)+b.y*f-g*gy+.48);
    }
    
    void draw2Leads(Graphics g) {
	// draw first lead
	setVoltageColor(g, volts[0]);
	drawThickLine(g, point1, lead1);

	// draw second lead
	setVoltageColor(g, volts[1]);
	drawThickLine(g, lead2, point2);
    }
    Point [] newPointArray(int n) {
	Point a[] = new Point[n];
	while (n > 0)
	    a[--n] = new Point();
	return a;
    }

    final int CURRENT_TOO_FAST = 100;

    // draw current dots from point a to b
    void drawDots(Graphics g, Point pa, Point pb, double pos) {
	 if ((!sim.simIsRunning()) || pos == 0 || !sim.dotsCheckItem.getState())
	    return;
	int dx = pb.x-pa.x;
	int dy = pb.y-pa.y;
	double dn = Math.sqrt(dx*dx+dy*dy);
	g.setColor(currentColor);
	int ds = 16;
	if (pos == CURRENT_TOO_FAST || pos == -CURRENT_TOO_FAST) {
	    // current is moving too fast, avoid aliasing by drawing dots at
	    // random position with transparent yellow line underneath
	    g.save();
	    Context2d ctx = g.context;
	    ctx.setLineWidth(4);
	    ctx.setGlobalAlpha(.5);
	    ctx.beginPath();
	    ctx.moveTo(pa.x, pa.y);
	    ctx.lineTo(pb.x, pb.y);
	    ctx.stroke();
	    g.restore();
	    pos = Random.nextDouble()*ds;
	}
	pos %= ds;
	if (pos < 0)
	    pos += ds;
	double di = 0;
	for (di = pos; di < dn; di += ds) {
	    int x0 = (int) (pa.x+di*dx/dn);
	    int y0 = (int) (pa.y+di*dy/dn);
	    g.fillRect(x0-2, y0-2, 4, 4);
	}
    }

    double addCurCount(double c, double a) {
	if (c == CURRENT_TOO_FAST || c == -CURRENT_TOO_FAST)
	    return c;
	return c+a;
    }
    
    Polygon calcArrow(Point a, Point b, double al, double aw) {
	Polygon poly = new Polygon();
	Point p1 = new Point();
	Point p2 = new Point();
	int adx = b.x-a.x;
	int ady = b.y-a.y;
	double l = Math.sqrt(adx*adx+ady*ady);
	poly.addPoint(b.x, b.y);
	interpPoint2(a, b, p1, p2, 1-al/l, aw);
	poly.addPoint(p1.x, p1.y);
	poly.addPoint(p2.x, p2.y);
	return poly;
    }
    Polygon createPolygon(Point a, Point b, Point c) {
	Polygon p = new Polygon();
	p.addPoint(a.x, a.y);
	p.addPoint(b.x, b.y);
	p.addPoint(c.x, c.y);
	return p;
    }
    Polygon createPolygon(Point a, Point b, Point c, Point d) {
	Polygon p = new Polygon();
	p.addPoint(a.x, a.y);
	p.addPoint(b.x, b.y);
	p.addPoint(c.x, c.y);
	p.addPoint(d.x, d.y);
	return p;
    }
    Polygon createPolygon(Point a[]) {
	Polygon p = new Polygon();
	int i;
	for (i = 0; i != a.length; i++)
	    p.addPoint(a[i].x, a[i].y);
	return p;
    }
    
    // draw second point to xx, yy
    void drag(int xx, int yy) {
	xx = sim.snapGrid(xx);
	yy = sim.snapGrid(yy);
	if (noDiagonal) {
	    if (Math.abs(x-xx) < Math.abs(y-yy)) {
		xx = x;
	    } else {
		yy = y;
	    }
	}
	x2 = xx; y2 = yy;
	setPoints();
    }
    
    void move(int dx, int dy) {
	x += dx; y += dy; x2 += dx; y2 += dy;
	boundingBox.translate(dx, dy);
	setPoints();
    }

    // called when an element is done being dragged out; returns true if it's zero size and should be deleted
    boolean creationFailed() {
	return (x == x2 && y == y2);
    }

    // this is used to set the position of an internal element so we can draw it inside the parent
    void setPosition(int x_, int y_, int x2_, int y2_) {
	x = x_;
	y = y_;
	x2 = x2_;
	y2 = y2_;
	setPoints();
    }
    
    // determine if moving this element by (dx,dy) will put it on top of another element
    boolean allowMove(int dx, int dy) {
	int nx = x+dx;
	int ny = y+dy;
	int nx2 = x2+dx;
	int ny2 = y2+dy;
	int i;
	for (i = 0; i != sim.elmList.size(); i++) {
	    CircuitElm ce = sim.getElm(i);
	    if (ce.x == nx && ce.y == ny && ce.x2 == nx2 && ce.y2 == ny2)
		return false;
	    if (ce.x == nx2 && ce.y == ny2 && ce.x2 == nx && ce.y2 == ny)
		return false;
	}
	return true;
    }
    
    void movePoint(int n, int dx, int dy) {
    	// modified by IES to prevent the user dragging points to create zero sized nodes
    	// that then render improperly
    	int oldx=x;
    	int oldy=y;
    	int oldx2=x2;
    	int oldy2=y2;
    	if (noDiagonal) {
    	    if (x == x2)
    		dx = 0;
    	    else
    		dy = 0;
    	}
    	if (n == 0) {
    		x += dx; y += dy;
    	} else {
    		x2 += dx; y2 += dy;
    	}
    	if (x==x2 && y==y2) {
    		x=oldx;
    		y=oldy;
    		x2=oldx2;
    		y2=oldy2;
    	}
    	setPoints();
    }
    
    void flipX(int center2, int count) {
	x =  center2-x;
	x2 = center2-x2;
	initBoundingBox();
	setPoints();
    }

    void flipY(int center2, int count) {
	y =  center2-y;
	y2 = center2-y2;
	initBoundingBox();
	setPoints();
    }

    void flipXY(int xmy, int count) {
	int nx = y+xmy;
	int ny = x-xmy;
	int nx2 = y2+xmy;
	int ny2 = x2-xmy;
	x = nx; y = ny; x2 = nx2; y2 = ny2;
	initBoundingBox();
	setPoints();
    }

    void drawPosts(Graphics g) {
	// we normally do this in updateCircuit() now because the logic is more complicated.
	// we only handle the case where we have to draw all the posts.  That happens when
	// this element is selected or is being created
	if (sim.dragElm == null && !needsHighlight())
	    return;
	if (sim.mouseMode == CirSim.MODE_DRAG_ROW ||
	    sim.mouseMode == CirSim.MODE_DRAG_COLUMN)
	    return;
	int i;
	for (i = 0; i != getPostCount(); i++) {
	    Point p = getPost(i);
	    drawPost(g, p);
	}
    }
    
    int getNumHandles() {
	return getPostCount();
    }
    
    void drawHandles(Graphics g, Color c) {
    	g.setColor(c);
    	if (lastHandleGrabbed==-1)
    		g.fillRect(x-3, y-3, 7, 7);
    	else if (lastHandleGrabbed==0)
    		g.fillRect(x-4, y-4, 9, 9);
    	if (getNumHandles() > 1) {
    		if (lastHandleGrabbed==-1)
    			g.fillRect(x2-3, y2-3, 7, 7);
    		else if (lastHandleGrabbed==1)
    			g.fillRect(x2-4, y2-4, 9, 9);
    	}
    }
    
    int getHandleGrabbedClose(int xtest, int ytest, int deltaSq, int minSize) {
    	lastHandleGrabbed=-1;
    	if ( Graphics.distanceSq(x , y , x2, y2)>=minSize) {
    		if (Graphics.distanceSq(x, y, xtest,ytest) <= deltaSq)
    			lastHandleGrabbed=0;
    		else if (getNumHandles() > 1 && Graphics.distanceSq(x2, y2, xtest,ytest) <= deltaSq)
    			lastHandleGrabbed=1;
    	}
    	return lastHandleGrabbed;
    }
    
    // number of voltage sources this element needs 
    int getVoltageSourceCount() { return 0; }
    
    // number of internal nodes (nodes not visible in UI that are needed for implementation)
    int getInternalNodeCount() { return 0; }
    
    // notify this element that its pth node is n.  This value n can be passed to stampMatrix()
    void setNode(int p, int n) { nodes[p] = n; }
    
    // notify this element that its nth voltage source is v.  This value v can be passed to stampVoltageSource(), etc and will be passed back in calls to setCurrent()
    void setVoltageSource(int n, int v) {
	// default implementation only makes sense for subclasses with one voltage source.  If we have 0 this isn't used, if we have >1 this won't work 
	voltSource = v;
    }
    
//    int getVoltageSource() { return voltSource; } // Never used except for debug code which is commented out
    
    double getVoltageDiff() {
	return volts[0] - volts[1];
    }
    boolean nonLinear() { return false; }
    int getPostCount() { return 2; }
    
    // get (global) node number of nth node
    int getNode(int n) { return nodes[n]; }
    
    // get position of nth node
    Point getPost(int n) {
	return (n == 0) ? point1 : (n == 1) ? point2 : null;
    }
    
    // return post we're connected to (for wires, so we can optimize them out in calculateWireClosure())
    Point getConnectedPost() {
	return point2;
    }
    
    int getNodeAtPoint(int xp, int yp) {
	int i;
	for (i = 0; i != getPostCount(); i++) {
	    Point p = getPost(i);
	    if (p.x == xp && p.y == yp)
		return i;
	}
	return 0;
    }
    
    /*
    void drawPost(Graphics g, int x0, int y0, int n) {
	if (sim.dragElm == null && !needsHighlight() &&
	    sim.getCircuitNode(n).links.size() == 2)
	    return;
	if (sim.mouseMode == CirSim.MODE_DRAG_ROW ||
	    sim.mouseMode == CirSim.MODE_DRAG_COLUMN)
	    return;
	drawPost(g, x0, y0);
    }
    */
    static void drawPost(Graphics g, Point pt) {
	g.setColor(whiteColor);
	g.fillOval(pt.x-3, pt.y-3, 7, 7);
    }
    
    // set/adjust bounding box used for selecting elements.  getCircuitBounds() does not use this!
    void setBbox(int x1, int y1, int x2, int y2) {
	if (x1 > x2) { int q = x1; x1 = x2; x2 = q; }
	if (y1 > y2) { int q = y1; y1 = y2; y2 = q; }
	boundingBox.setBounds(x1, y1, x2-x1+1, y2-y1+1);
    }
    
    // set bounding box for an element from p1 to p2 with width w
    void setBbox(Point p1, Point p2, double w) {
	setBbox(p1.x, p1.y, p2.x, p2.y);
	int dpx = (int) (dpx1*w);
	int dpy = (int) (dpy1*w);
	adjustBbox(p1.x+dpx, p1.y+dpy, p1.x-dpx, p1.y-dpy);
    }

    // enlarge bbox to contain an additional rectangle
    void adjustBbox(int x1, int y1, int x2, int y2) {
	if (x1 > x2) { int q = x1; x1 = x2; x2 = q; }
	if (y1 > y2) { int q = y1; y1 = y2; y2 = q; }
	x1 = min(boundingBox.x, x1);
	y1 = min(boundingBox.y, y1);
	x2 = max(boundingBox.x+boundingBox.width,  x2);
	y2 = max(boundingBox.y+boundingBox.height, y2);
	boundingBox.setBounds(x1, y1, x2-x1, y2-y1);
    }
    void adjustBbox(Point p1, Point p2) {
	adjustBbox(p1.x, p1.y, p2.x, p2.y);
    }
    
    // needed for calculating circuit bounds (need to special-case centered text elements)
    boolean isCenteredText() { return false; }
    
    void drawCenteredText(Graphics g, String s, int x, int y, boolean cx) {
	// FontMetrics fm = g.getFontMetrics();
	//int w = fm.stringWidth(s);
//    	int w=0;
//	if (cx)
//	    x -= w/2;
//	g.drawString(s, x, y+fm.getAscent()/2);
//	adjustBbox(x, y-fm.getAscent()/2,
//		   x+w, y+fm.getAscent()/2+fm.getDescent());
    	int w=(int)g.context.measureText(s).getWidth();
    	int h2=(int)g.currentFontSize/2;
		g.save();
		g.context.setTextBaseline("middle");
		if (cx) {
			g.context.setTextAlign("center");
			adjustBbox(x-w/2,y-h2,x+w/2,y+h2);
		} else {
			adjustBbox(x,y-h2,x+w,y+h2);
		}
		
		if (cx)
			g.context.setTextAlign("center");
		g.drawString(s, x, y);
		g.restore();
    }
    
    // draw component values (number of resistor ohms, etc).  hs = offset
    void drawValues(Graphics g, String s, double hs) {
	if (s == null)
	    return;
	g.setFont(unitsFont);
	//FontMetrics fm = g.getFontMetrics();
	int w = (int)g.context.measureText(s).getWidth();
	g.setColor(whiteColor);
	int ya = (int)g.currentFontSize/2;
	int xc, yc;
	if (this instanceof RailElm || this instanceof SweepElm) {
	    xc = x2;
	    yc = y2;
	} else {
	    xc = (x2+x)/2;
	    yc = (y2+y)/2;
	}
	int dpx = (int) (dpx1*hs);
	int dpy = (int) (dpy1*hs);
	if (dpx == 0)
	    g.drawString(s, xc-w/2, yc-abs(dpy)-2);
	else {
	    int xx = xc+abs(dpx)+2;
	     if (this instanceof VoltageElm || (x < x2 && y > y2))
		xx = xc-(w+abs(dpx)+2);
	    g.drawString(s, xx, yc+dpy+ya);
	}
    }
    
    void drawLabeledNode(Graphics g, String str, Point pt1, Point pt2) {
	boolean lineOver = false;
	if (str.startsWith("/")) {
	    lineOver = true;
	    str = str.substring(1);
	}
        int w=(int)g.context.measureText(str).getWidth();
        int h=(int)g.currentFontSize;
        g.save();
        g.context.setTextBaseline("middle");
        int x = pt2.x, y = pt2.y;
        if (pt1.y != pt2.y) {
            x -= w/2;
            y += sign(pt2.y-pt1.y)*h;
        } else {
            if (pt2.x > pt1.x)
        	x += 4;
            else
        	x -= 4+w;
        }
        g.drawString(str, x, y);
        adjustBbox(x, y-h/2, x+w, y+h/2);
        g.restore();
	if (lineOver) {
	    int ya = y-h/2-1;
	    g.drawLine(x, ya, x+w, ya);
	}	
    }    
    
    void drawCoil(Graphics g, int hs, Point p1, Point p2,
		  double v1, double v2) {
	double len = distance(p1, p2);

	g.save();
	g.context.setLineWidth(3.0);
	g.context.transform(((double)(p2.x-p1.x))/len, ((double)(p2.y-p1.y))/len,
		-((double)(p2.y-p1.y))/len,((double)(p2.x-p1.x))/len,p1.x,p1.y);
	if (sim.voltsCheckItem.getState() ) {
	    CanvasGradient grad = g.context.createLinearGradient(0,0,len,0);
	    grad.addColorStop(0, getVoltageColor(g,v1).getHexValue());
	    grad.addColorStop(1.0, getVoltageColor(g,v2).getHexValue());
	    g.context.setStrokeStyle(grad);
	}
	g.context.setLineCap(LineCap.ROUND);
	g.context.scale(1, hs > 0 ? 1 : -1);

	int loop;
	// draw more loops for a longer coil
	int loopCt = (int)Math.ceil(len/11);
	for (loop = 0; loop != loopCt; loop++) {
	    g.context.beginPath();
	    double start = len*loop/loopCt;
	    g.context.moveTo(start,0);
	    g.context.arc(len*(loop+.5)/loopCt, 0, len/(2*loopCt), Math.PI, Math.PI*2);
	    g.context.lineTo(len*(loop+1)/loopCt, 0);
	    g.context.stroke();
	}

	g.restore();
    }
    
    static void drawThickLine(Graphics g, int x, int y, int x2, int y2) {
    	g.setLineWidth(3.0);
    	g.drawLine(x,y,x2,y2);
    	g.setLineWidth(1.0);
    }

    static void drawThickLine(Graphics g, Point pa, Point pb) {
    	g.setLineWidth(3.0);
    	g.drawLine(pa.x, pa.y, pb.x, pb.y);
    	g.setLineWidth(1.0);
    }

    static void drawThickPolygon(Graphics g, int xs[], int ys[], int c) {
//	int i;
//	for (i = 0; i != c-1; i++)
//	    drawThickLine(g, xs[i], ys[i], xs[i+1], ys[i+1]);
//	drawThickLine(g, xs[i], ys[i], xs[0], ys[0]);
    	g.setLineWidth(3.0);
    	g.drawPolyline(xs, ys, c);
    	g.setLineWidth(1.0);
    }
    
    static void drawThickPolygon(Graphics g, Polygon p) {
	drawThickPolygon(g, p.xpoints, p.ypoints, p.npoints);
    }
    
    static void drawPolygon(Graphics g, Polygon p) {
    	g.drawPolyline(p.xpoints, p.ypoints, p.npoints);
/*	int i;
	int xs[] = p.xpoints;
	int ys[] = p.ypoints;
	int np = p.npoints;
	np -= 3;
	for (i = 0; i != np-1; i++)
	    g.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);
	g.drawLine(xs[i], ys[i], xs[0], ys[0]);*/
    }
    
    static void drawThickCircle(Graphics g, int cx, int cy, int ri) {
    	g.setLineWidth(3.0);
    	g.context.beginPath();
    	g.context.arc(cx, cy, ri*.98, 0, 2*Math.PI);
    	g.context.stroke();
    	g.setLineWidth(1.0);
    }
    
    Polygon getSchmittPolygon(float gsize, float ctr) {
	Point pts[] = newPointArray(6);
	float hs = 3*gsize;
	float h1 = 3*gsize;
	float h2 = h1*2;
	double len = distance(lead1, lead2);
	pts[0] = interpPoint(lead1, lead2, ctr-h2/len, hs);
	pts[1] = interpPoint(lead1, lead2, ctr+h1/len,  hs);
	pts[2] = interpPoint(lead1, lead2, ctr+h1/len, -hs);
	pts[3] = interpPoint(lead1, lead2, ctr+h2/len, -hs);
	pts[4] = interpPoint(lead1, lead2, ctr-h1/len, -hs);
	pts[5] = interpPoint(lead1, lead2, ctr-h1/len, hs);
	return createPolygon(pts); 
    }

    static String getVoltageDText(double v) {
	return getUnitText(Math.abs(v), "V");
    }
    static String getVoltageText(double v) {
	return getUnitText(v, "V");
    }
    static String getTimeText(double v) {
	if (v >= 60) {
	    double h = Math.floor(v/3600);
	    v -= 3600*h;
	    double m = Math.floor(v/60);
	    v -= 60*m;
	    if (h == 0)
		return m + ":" + ((v >= 10) ? "" : "0") + showFormat.format(v);
	    return h + ":" + ((m >= 10) ? "" : "0") + m + ":" + ((v >= 10) ? "" : "0") + showFormat.format(v); 
	}
	return getUnitText(v, "s");
    }
    
    static String format(double v, boolean sf) {
//	if (sf && Math.abs(v) > 10)
//	    return shortFormat.format(Math.round(v));
	return (sf ? shortFormat : showFormat).format(v);
    }
    
    static String getUnitText(double v, String u) {
    	return getUnitText(v,u, false);
    }

    static String getShortUnitText(double v, String u) {
    	return getUnitText(v,u, true);
    }
    
    private static String getUnitText(double v, String u, boolean sf) {
	String sp = sf ? "" : " ";
	double va = Math.abs(v);
	if (va < 1e-14)
	    // this used to return null, but then wires would display "null" with 0V
	    return "0" + sp + u;
	if (va < 1e-9)
	    return format(v*1e12, sf) + sp + "p" + u;
	if (va < 1e-6)
	    return format(v*1e9, sf) + sp + "n" + u;
	if (va < 1e-3)
	    return format(v*1e6, sf) + sp + Locale.muString + u;
	if (va < 1)
	    return format(v*1e3, sf) + sp + "m" + u;
	if (va < 1e3)
	    return format(v, sf) + sp + u;
	if (va < 1e6)
	    return format(v*1e-3, sf) + sp + "k" + u;
	if (va < 1e9)
	    return format(v*1e-6, sf) + sp + "M" + u;
	if (va < 1e12)
	    return format(v*1e-9, sf) + sp + "G" + u;
	return NumberFormat.getFormat("#.##E000").format(v) + sp + u;
    }
    
    static String getCurrentText(double i) {
	return getUnitText(i, "A");
    }
    static String getCurrentDText(double i) {
	return getUnitText(Math.abs(i), "A");
    }

    static String getUnitTextWithScale(double val, String utext, int scale) { return getUnitTextWithScale(val, utext, scale, false); }

    static String getUnitTextWithScale(double val, String utext, int scale, boolean fixed) {
	if (Math.abs(val) > 1e12)
	    return getUnitText(val, utext);
	NumberFormat nf = fixed ? fixedFormat : showFormat;
	if (scale == SCALE_1)
	    return nf.format(val) + " " + utext;
	if (scale == SCALE_M)
	    return nf.format(1e3*val) + " m" + utext;
	if (scale == SCALE_MU)
	    return nf.format(1e6*val) + " " + Locale.muString + utext;
	return getUnitText(val, utext);
    }

    // update dot positions (curcount) for drawing current (simple case for single current)
    void updateDotCount() {
	curcount = updateDotCount(current, curcount);
    }

    // update dot positions (curcount) for drawing current (general case for multiple currents)
    double updateDotCount(double cur, double cc) {
  
	 if (!sim.simIsRunning())
	    return cc;
	double cadd = cur*currentMult;
	if (cadd > 6 || cadd < -6)
	    return CURRENT_TOO_FAST;
	if (cc == CURRENT_TOO_FAST)
	    cc = 0;
	cadd %= 8;
	return cc + cadd;
    }
    
    // update and draw current for simple two-terminal element
    void doDots(Graphics g) {
	updateDotCount();
	if (sim.dragElm != this)
	    drawDots(g, point1, point2, curcount);
    }
    
    void doAdjust() {}
    void setupAdjust() {}
    
    // get component info for display in lower right
    void getInfo(String arr[]) {
    }
    
    int getBasicInfo(String arr[]) {
	arr[1] = "I = " + getCurrentDText(getCurrent());
	arr[2] = "Vd = " + getVoltageDText(getVoltageDiff());
	return 3;
    }
    String getScopeText(int v) {
        String info[] = new String[10];
        getInfo(info);
        return info[0];
    }
    
    Color getVoltageColor(Graphics g, double volts) {
    	if (needsHighlight()) {
    	    	return (selectColor);
    	}
    	if (!sim.voltsCheckItem.getState()) {
    	    	return(whiteColor);
    	}
    	int c = (int) ((volts+voltageRange)*(colorScaleCount-1)/
    		       (voltageRange*2));
    	if (c < 0)
    	    c = 0;
    	if (c >= colorScaleCount)
    	    c = colorScaleCount-1;
    	return (colorScale[c]);
    }
    
    void setVoltageColor(Graphics g, double volts) {
    	g.setColor(getVoltageColor(g, volts));
    }
    
    // yellow argument is unused, can't remember why it was there
    void setPowerColor(Graphics g, boolean yellow) {

	/*if (conductanceCheckItem.getState()) {
	  setConductanceColor(g, current/getVoltageDiff());
	  return;
	  }*/
	if (!sim.powerCheckItem.getState() )
	    return;
	setPowerColor(g, getPower());
    }
    
    void setPowerColor(Graphics g, double w0) {
	if (!sim.powerCheckItem.getState() )
	    return;
    	if (needsHighlight()) {
	    	g.setColor(selectColor);
	    	return;
    	}
	w0 *= powerMult;
	//System.out.println(w);
	int i = (int) ((colorScaleCount/2)+(colorScaleCount/2)*-w0);
	if (i<0)
	    i=0;
	if (i>=colorScaleCount)
	    i=colorScaleCount-1;
	 g.setColor(colorScale[i]);
    }
    void setConductanceColor(Graphics g, double w0) {
	w0 *= powerMult;
	//System.out.println(w);
	double w = (w0 < 0) ? -w0 : w0;
	if (w > 1)
	    w = 1;
	int rg = (int) (w*255);
	g.setColor(new Color(rg, rg, rg));
    }
    double getPower() { return getVoltageDiff()*current; }
    double getScopeValue(int x) {
	return (x == Scope.VAL_CURRENT) ? getCurrent() :
	    (x == Scope.VAL_POWER) ? getPower() : getVoltageDiff();
    }
    int getScopeUnits(int x) {
	return (x == Scope.VAL_CURRENT) ? Scope.UNITS_A :
	    (x == Scope.VAL_POWER) ? Scope.UNITS_W : Scope.UNITS_V;
    }
    public EditInfo getEditInfo(int n) { return null; }
    public void setEditValue(int n, EditInfo ei) {}
    
    // get number of nodes that can be retrieved by getConnectionNode()
    int getConnectionNodeCount() { return getPostCount(); }
    
    // get nodes that can be passed to getConnection(), to test if this element connects
    // those two nodes; this is the same as getNode() for all but labeled nodes.
    int getConnectionNode(int n) { return getNode(n); }
    
    // are n1 and n2 connected by this element?  this is used to determine
    // unconnected nodes, and look for loops
    boolean getConnection(int n1, int n2) { return true; }
    
    // is n1 connected to ground somehow?
    boolean hasGroundConnection(int n1) { return false; }
    
    // is this a wire or equivalent to a wire?  (used for circuit validation)
    boolean isWireEquivalent() { return false; }
    
    // is this a wire we can remove?
    boolean isRemovableWire() { return false; }
    
    boolean isIdealCapacitor() { return false; }

    boolean canViewInScope() { return getPostCount() <= 2; }
    boolean canFlipX() { return true; }
    boolean canFlipY() { return true; }
    boolean canFlipXY() { return canFlipX() || canFlipY(); }
    boolean comparePair(int x1, int x2, int y1, int y2) {
	return ((x1 == y1 && x2 == y2) || (x1 == y2 && x2 == y1));
    }
    boolean needsHighlight() { 
	return mouseElmRef==this || selected || sim.plotYElm == this ||
		// Test if the current mouseElm is a ScopeElm and, if so, does it belong to this elm
		(mouseElmRef instanceof ScopeElm && ((ScopeElm) mouseElmRef).elmScope.getElm()==this); 
    }
    boolean isSelected() { return selected; }
    boolean canShowValueInScope(int v) { return false; }
    void setSelected(boolean x) { selected = x; }
    void selectRect(Rectangle r, boolean add) {
	if (r.intersects(boundingBox))
	    selected = true;
	else if (!add)
	    selected = false;
    }
    static int abs(int x) { return x < 0 ? -x : x; }
    static int sign(int x) { return (x < 0) ? -1 : (x == 0) ? 0 : 1; }
    static int min(int a, int b) { return (a < b) ? a : b; }
    static int max(int a, int b) { return (a > b) ? a : b; }
    static double distance(Point p1, Point p2) {
	double x = p1.x-p2.x;
	double y = p1.y-p2.y;
	return Math.sqrt(x*x+y*y);
    }
    Rectangle getBoundingBox() { return boundingBox; }
    boolean needsShortcut() { return getShortcut() > 0; }
    int getShortcut() { return 0; }

    boolean isGraphicElmt() { return false; }
    
    void setMouseElm(boolean v) {
	if (v)
	    mouseElmRef=this;
	else if (mouseElmRef==this)
	    mouseElmRef=null;
    }
    void draggingDone() {}
    
    int lineDistanceSq(int xa, int ya, int xb, int yb, int gx, int gy) {
	int dtop = (yb-ya)*gx - (xb-xa)*gy + xb*ya - yb*xa;
	int dbot = (yb-ya)*(yb-ya) + (xb-xa)*(xb-xa);
	return dtop*dtop/dbot;
    }

    int getMouseDistance(int gx, int gy) {
	if (getPostCount() == 0)
	    return Graphics.distanceSq(gx, gy, (x2+x)/2, (y2+y)/2);
	return lineDistanceSq(x, y, x2, y2, gx, gy);
    }

    String dumpModel() { return null; }
    
    boolean isMouseElm() {
	return mouseElmRef==this; 
    }
    
    void updateModels() {}
    void stepFinished() {}
    
    // get current flowing into node n out of this element
    double getCurrentIntoNode(int n) {
	// if we take out the getPostCount() == 2 it gives the wrong value for rails
	if (n==0 && getPostCount() == 2)
	    return -current;
	else
	    return current;
    }
    
    void flipPosts() {
	int oldx = x;
	int oldy = y;
	x = x2;
	y = y2;
	x2 = oldx;
	y2 = oldy;
	setPoints();
    }
    
    String getClassName() { return getClass().getName().replace("com.lushprojects.circuitjs1.client.", ""); }
    
    native JsArrayString getJsArrayString() /*-{ return []; }-*/;
    
    JsArrayString getInfoJS() {
	JsArrayString jsarr = getJsArrayString();
	String arr[] = new String[20];
	getInfo(arr);
	int i;
	for (i = 0; arr[i] != null; i++)
	    jsarr.push(arr[i]);
	return jsarr;
    }
    
    double getVoltageJS(int n) {
	if (n >= volts.length)
	    return 0;
	return volts[n];
    }
    
    native void addJSMethods() /*-{
        var that = this;
        this.getType = $entry(function() { return that.@com.lushprojects.circuitjs1.client.CircuitElm::getClassName()(); });
        this.getInfo = $entry(function() { return that.@com.lushprojects.circuitjs1.client.CircuitElm::getInfoJS()(); });
        this.getVoltageDiff = $entry(function() { return that.@com.lushprojects.circuitjs1.client.CircuitElm::getVoltageDiff()(); });
        this.getVoltage = $entry(function(n) { return that.@com.lushprojects.circuitjs1.client.CircuitElm::getVoltageJS(I)(n); });
        this.getCurrent = $entry(function() { return that.@com.lushprojects.circuitjs1.client.CircuitElm::getCurrent()(); });
        this.getLabelName = $entry(function() { return that.@com.lushprojects.circuitjs1.client.LabeledNodeElm::getName()(); });
        this.getPostCount = $entry(function() { return that.@com.lushprojects.circuitjs1.client.CircuitElm::getPostCount()(); });
    }-*/;
    
    native JavaScriptObject getJavaScriptObject() /*-{ return this; }-*/;
    
}
