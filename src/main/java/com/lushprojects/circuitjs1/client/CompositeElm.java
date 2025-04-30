package com.lushprojects.circuitjs1.client;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map.Entry;

// Circuit element made up of a composition of other circuit elements
// Using this will be (relatively) inefficient in terms of simulation performance because
// all the internal workings of the element are simulated from the individual components.
// However, it may allow some types of components to be more quickly programed in to the simulator
// than writing each component from scratch.
//
// It also provides a path to allow user created circuits to be 
// re-imported in to the simuation as new circuit elements.

// Instatiations should:
// - Set the variable "diagonal" in the constructors
// - Override constructors to set up the elements posts/leads etc. and configure the contents of the CompositeElm
// - Override getDumpType, dump, draw, getInfo, setPoints, canViewInScope

public abstract class CompositeElm extends CircuitElm {

    // need to use escape() instead of converting spaces to _'s so composite elements can be nested
    final int FLAG_ESCAPE = 1;
    
    // list of elements contained in this subcircuit
    Vector<CircuitElm> compElmList;
    
    // list of nodes, mapping each one to a list of elements that reference that node
    protected Vector<CircuitNode> compNodeList;
    
    protected int numPosts = 0;
    protected int numNodes = 0;
    protected Point posts[];
    protected Vector<VoltageSourceRecord> voltageSources;

    CompositeElm(int xx, int yy) {
	super(xx, yy);
    }
    
    public CompositeElm(int xa, int ya, int xb, int yb, int f) {
	super(xa, ya, xb, yb, f);
    }
    
    CompositeElm(int xx, int yy, String s, int externalNodes[]) {
	super(xx, yy);
	loadComposite(null, s, externalNodes);
	allocNodes();
    }

    public CompositeElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st, String s, int externalNodes[]) {
	super(xa, ya, xb, yb, f);
	loadComposite(st, s, externalNodes);
	allocNodes();
    }

    boolean useEscape() { return (flags & FLAG_ESCAPE) != 0; }
    
    public void loadComposite(StringTokenizer stIn, String model, int externalNodes[]) {
	HashMap<Integer, CircuitNode> compNodeHash = new HashMap<Integer, CircuitNode>();
	StringTokenizer modelLinet = new StringTokenizer(model, "\r");
	CircuitNode cn;
	CircuitNodeLink cnLink;
	VoltageSourceRecord vsRecord;

	compElmList = new Vector<CircuitElm>();
	compNodeList = new Vector<CircuitNode>();
	voltageSources = new Vector<VoltageSourceRecord>();

	// Build compElmList and compNodeHash from input string

	while (modelLinet.hasMoreTokens()) {
	    String line = modelLinet.nextToken();
	    StringTokenizer stModel = new StringTokenizer(line, " +\t\n\r\f");
	    String ceType = stModel.nextToken();
	    CircuitElm newce = CirSim.constructElement(ceType, 0, 0);
	    if (stIn!=null) {
		int tint = newce.getDumpType();
		String dumpedCe= stIn.nextToken();
		if (useEscape())
		    dumpedCe = CustomLogicModel.unescape(dumpedCe);
		StringTokenizer stCe = new StringTokenizer(dumpedCe, useEscape() ? " " : "_");
		int flags = new Integer(stCe.nextToken()).intValue();
		newce = CirSim.createCe(tint, 0, 0, 0, 0, flags, stCe);
	    }
	    if (newce instanceof GroundElm)
		((GroundElm) newce).setOldStyle();
	    compElmList.add(newce);

	    int thisPost = 0;
	    while (stModel.hasMoreTokens()) {
		int nodeOfThisPost = new Integer(stModel.nextToken()).intValue();

		// node = 0 means ground
		if (nodeOfThisPost == 0) {
		    newce.setNode(thisPost, 0);
		    newce.setNodeVoltage(thisPost, 0);
		    thisPost++;
		    continue;
		}
		cnLink = new CircuitNodeLink();
		cnLink.num = thisPost;
		cnLink.elm = newce;
		if (!compNodeHash.containsKey(nodeOfThisPost)) {
		    cn = new CircuitNode();
		    cn.links.add(cnLink);
		    compNodeHash.put(nodeOfThisPost, cn);
		} else {
		    cn = compNodeHash.get(nodeOfThisPost);
		    cn.links.add(cnLink);
		}
		thisPost++;
	    }
	}

	// Flatten compNodeHash in to compNodeList
	numPosts = externalNodes.length;
	for (int i = 0; i < externalNodes.length; i++) { // External Nodes First
	    if (compNodeHash.containsKey(externalNodes[i])) {
		compNodeList.add(compNodeHash.get(externalNodes[i]));
		compNodeHash.remove(externalNodes[i]);
	    } else
		throw new IllegalArgumentException();
	}
	for (Entry<Integer, CircuitNode> entry : compNodeHash.entrySet()) {
	    int key = entry.getKey();
	    compNodeList.add(compNodeHash.get(key));
	}

	// allocate more nodes for sub-elements' internal nodes
	for (int i = 0; i != compElmList.size(); i++) {
	    CircuitElm ce = compElmList.get(i);
	    int inodes = ce.getInternalNodeCount();
	    for (int j = 0; j != inodes; j++) {
		cnLink = new CircuitNodeLink();
		cnLink.num = j + ce.getPostCount();
		cnLink.elm = ce;
		cn = new CircuitNode();
		cn.links.add(cnLink);
		compNodeList.add(cn);
	    }
	}

	numNodes = compNodeList.size();

//	CirSim.console("Dumping compNodeList");
//	for (int i = 0; i < numNodes; i++) {
//	    CirSim.console("New node" + i + " Size of links:" + compNodeList.get(i).links.size());
//	}

	posts = new Point[numPosts];
	
	// Enumerate voltage sources
	for (int i = 0; i < compElmList.size(); i++) {
	    int cnt = compElmList.get(i).getVoltageSourceCount();
	    for (int j=0;j < cnt ; j++) {
		vsRecord = new VoltageSourceRecord();
		vsRecord.elm = compElmList.get(i);
		vsRecord.vsNumForElement = j;
		voltageSources.add(vsRecord);
	    }
	}
	
	// dump new circuits with escape()
	flags |= FLAG_ESCAPE;
    }

    public boolean nonLinear() {
	for (int i = 0; i < compElmList.size(); i++)
	    if (compElmList.get(i).nonLinear())
		return true;
	return false;
    }

    public String dump() {
	String dumpStr=super.dump();
	dumpStr += dumpElements();
	return dumpStr;
    }

    public String dumpElements() {
	String dumpStr = "";
	for (int i = 0; i < compElmList.size(); i++) {
	    String tstring = compElmList.get(i).dump();
	    tstring = tstring.replaceFirst("[A-Za-z0-9]+ 0 0 0 0 ", ""); // remove unused tint x1 y1 x2 y2 coords for internal components
	    dumpStr += " "+ CustomLogicModel.escape(tstring);
	}
	return dumpStr;
    }

    // dump subset of elements (some of them may not have any state, and/or may be very long, so we avoid dumping them for brevity)
    public String dumpWithMask(int mask) {
	String dumpStr=super.dump();
	return dumpStr + dumpElements(mask);
    }

    public String dumpElements(int mask) {
	String dumpStr = "";
	for (int i = 0; i < compElmList.size(); i++) {
	    if ((mask & (1<<i)) == 0)
		continue;
	    String tstring = compElmList.get(i).dump();
	    tstring = tstring.replaceFirst("[A-Za-z0-9]+ 0 0 0 0 ", ""); // remove unused tint x1 y1 x2 y2 coords for internal components
	    dumpStr += " "+ CustomLogicModel.escape(tstring);
	}
	return dumpStr;
    }

    // are n1 and n2 connected internally somehow?
    public boolean getConnectionSlow(int n1, int n2) {
	Vector<Integer> connectedNodes = new Vector<Integer>();

	// keep list of nodes connected to n1
	connectedNodes.add(n1);
	int i;
	for (i = 0; i < connectedNodes.size(); i++) {
	    // next node in list
	    int n = connectedNodes.get(i);
	    if (n == n2)
		return true;
	    
	    // find all elements connected to n
	    Vector<CircuitNodeLink> cnLinks = compNodeList.get(n).links;
	    for (int j = 0; j < cnLinks.size(); j++) {
		CircuitNodeLink link = cnLinks.get(j);
		CircuitElm lelm = link.elm;
		// loop through all other nodes this element has
		for (int k = 0; k != lelm.getConnectionNodeCount(); k++)
		    // are they connected?
		    if (k != link.num && lelm.getConnection(link.num, k)) {
			int kn = lelm.getConnectionNode(k);
			if (kn == 0)
			    return true;
			int m;
			// find local node number (kn is global) and add it to list
			for (m = 0; m != nodes.length; m++)
			    if (nodes[m] == kn && !connectedNodes.contains(m))
				connectedNodes.add(m);
		    }
	    }
	}
	return false;
    }
    
    HashMap<IntPair, Boolean> connectionMap;
    HashMap<Integer, Boolean> groundConnectionMap;

    public boolean getConnection(int n1, int n2) {
	if (connectionMap == null)
	    connectionMap = new HashMap<IntPair, Boolean>();
	IntPair key = new IntPair(n1, n2);
	Boolean result = connectionMap.get(key);
	if (result != null)
	    return result;
	result = getConnectionSlow(n1, n2);
	connectionMap.put(key, result);
	return result;
    }

    // is n1 connected to ground somehow?
    public boolean hasGroundConnection(int n1) {
	if (groundConnectionMap == null)
	    groundConnectionMap = new HashMap<Integer, Boolean>();
	Integer key = n1;
	Boolean result = groundConnectionMap.get(key);
	if (result != null)
	   return result;
	result = hasGroundConnectionSlow(n1);
	groundConnectionMap.put(key, result);
	return result;
    }

    public boolean hasGroundConnectionSlow(int n1) {
	Vector<Integer> connectedNodes = new Vector<Integer>();

	// keep list of nodes connected to n1
	connectedNodes.add(n1);
	int i;
	for (i = 0; i < connectedNodes.size(); i++) {
	    // next node in list
	    int n = connectedNodes.get(i);	    
	    // find all elements connected to n
	    Vector<CircuitNodeLink> cnLinks = compNodeList.get(n).links;
	    for (int j = 0; j < cnLinks.size(); j++) {
		CircuitNodeLink link = cnLinks.get(j);
		CircuitElm lelm = link.elm;
		if (lelm.hasGroundConnection(link.num))
		    return true;
		// loop through all other nodes this element has
		for (int k = 0; k != lelm.getConnectionNodeCount(); k++)
		    // are they connected?
		    if (k != link.num && lelm.getConnection(link.num, k)) {
			int kn = lelm.getConnectionNode(k);
			int m;
			// find local node number (kn is global) and add it to list
			for (m = 0; m != nodes.length; m++)
			    if (nodes[m] == kn && !connectedNodes.contains(m))
				connectedNodes.add(m);
		    }
	    }
	}
	return false;
    }

    public void reset() {
	for (int i = 0; i < compElmList.size(); i++)
	    compElmList.get(i).reset();
    } 

    int getPostCount() {
	return numPosts;
    }

    int getInternalNodeCount() {
	return numNodes - numPosts;
    }

    Point getPost(int n) {
	return posts[n];
    }

    void setPost(int n, Point p) {
	posts[n] = p;
    }

    void setPost(int n, int x, int y) {
	posts[n].x = x;
	posts[n].y = y;
    }

    public double getPower() {
	double power;
	power = 0;
	for (int i = 0; i < compElmList.size(); i++)
	    power += compElmList.get(i).getPower();
	return power;
    }

    public void stamp() {
	for (int i = 0; i < compElmList.size(); i++) {
	    CircuitElm ce = compElmList.get(i);
	    ce.setParentList(compElmList);
	    ce.stamp();
	}
    }

    public void startIteration() {
	for (int i = 0; i < compElmList.size(); i++)
	    compElmList.get(i).startIteration();
    }
    
    public void doStep() {
	for (int i = 0; i < compElmList.size(); i++)
	    compElmList.get(i).doStep();
    }

    public void stepFinished() {
	for (int i = 0; i < compElmList.size(); i++)
	    compElmList.get(i).stepFinished();
    }

    // called to set node p (local to this element) to equal n (global)
    public void setNode(int p, int n) {
	// nodes[p] = n
	Vector<CircuitNodeLink> cnLinks;
	super.setNode(p, n);
	cnLinks = compNodeList.get(p).links;

        // call setNode() for all elements that use that node
	for (int i = 0; i < cnLinks.size(); i++) {
	    cnLinks.get(i).elm.setNode(cnLinks.get(i).num, n);
	}

    }

    public void setNodeVoltage(int n, double c) {
	// volts[n] = c;
	Vector<CircuitNodeLink> cnLinks;
	super.setNodeVoltage(n, c);
	cnLinks = compNodeList.get(n).links;
	for (int i = 0; i < cnLinks.size(); i++) {
	    cnLinks.get(i).elm.setNodeVoltage(cnLinks.get(i).num, c);
	}
	volts[n]=c;
    }

    public boolean canViewInScope() {
	return false;
    }

    public void delete() {
	for (int i = 0; i < compElmList.size(); i++)
	    compElmList.get(i).delete();
        super.delete();
    }

    public int getVoltageSourceCount() {
	return voltageSources.size();
    }

    // Find the component with the nth voltage
    // and set the
    // appropriate source in that component
    void setVoltageSource(int n, int v) {
	// voltSource(n) = v;
	VoltageSourceRecord vsr;
	vsr=voltageSources.get(n);
	vsr.elm.setVoltageSource(vsr.vsNumForElement, v);
	vsr.vsNode=v;
    }
    
    @Override
     public void   setCurrent(int vsn, double c) {
	for (int i=0;i<voltageSources.size(); i++)
	    if (voltageSources.get(i).vsNode == vsn) {
		voltageSources.get(i).elm.setCurrent(vsn, c);
	    }
	
    }

    double getCurrentIntoNode(int n) {
	double c=0;
	Vector<CircuitNodeLink> cnLinks;
	cnLinks = compNodeList.get(n).links;
	for (int i = 0; i < cnLinks.size(); i++) {
	    c+=cnLinks.get(i).elm.getCurrentIntoNode(cnLinks.get(i).num);
	}
	return c;
    }

}


class VoltageSourceRecord {
	int vsNumForElement;
	int vsNode;
	CircuitElm elm;
}
