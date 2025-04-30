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

    class DFlipFlopElm extends ChipElm {
	final int FLAG_RESET = 2;
	final int FLAG_SET = 4;
	final int FLAG_INVERT_SET_RESET = 8;
	boolean hasReset() { return (flags & FLAG_RESET) != 0 || hasSet(); }
	boolean hasSet() { return (flags & FLAG_SET) != 0; }
	boolean invertSetReset() { return (flags & FLAG_INVERT_SET_RESET) != 0; }
	
	boolean justLoaded;
	
	public DFlipFlopElm(int xx, int yy) {
            super(xx, yy);
	    pins[2].value = !pins[1].value;
        }
	public DFlipFlopElm(int xa, int ya, int xb, int yb, int f,
			    StringTokenizer st) {
	    super(xa, ya, xb, yb, f, st);
	    pins[2].value = !pins[1].value;
	    justLoaded = true;
	}
	String getChipName() { return "D flip-flop"; }
	void setupPins() {
	    sizeX = 2;
	    sizeY = 3;
	    pins = new Pin[getPostCount()];
	    pins[0] = new Pin(0, SIDE_W, "D");
	    pins[1] = new Pin(0, SIDE_E, "Q");
	    pins[1].output = pins[1].state = true;
	    pins[2] = new Pin(hasSet()?1:2, SIDE_E, "Q");
	    pins[2].output = true;
	    pins[2].lineOver = true;
	    pins[3] = new Pin(1, SIDE_W, "");
	    pins[3].clock = true;
           if (!hasSet()) {
            if (hasReset()) {
               pins[4] = new Pin(2, SIDE_W, "R");
               pins[4].bubble = invertSetReset();
            }
           } else {
               pins[5] = new Pin(2, SIDE_W, "S");
               pins[4] = new Pin(2, SIDE_E, "R");
               pins[4].bubble = pins[5].bubble = invertSetReset();
            }
	}
	int getPostCount() {
	    return 4 + (hasReset() ? 1 : 0) + (hasSet() ? 1 : 0);
	}
	int getVoltageSourceCount() { return 2; }
        void reset() {
            super.reset();
	    volts[2] = highVoltage;
	    pins[2].value = true;
        }
	void execute() {
            // if we just loaded then the volts[] array is likely to be all zeroes, which might force us to do a reset, so defer execution until the next iteration
            if (justLoaded) {
                justLoaded = false;
                return;
            }
	    
	    boolean isSet = false;
            boolean isReset = false;
            
	    if (hasSet() && pins[5].value != invertSetReset())
		isSet = true;
	    if (hasReset() && pins[4].value != invertSetReset())
		isReset = true;

	    if (isSet || isReset) {
	    	writeOutput(1, false);
		writeOutput(2, false);
                if (isSet)
                    writeOutput(1, true);
                if (isReset)
                    writeOutput(2, true);
            } else {
                if (pins[3].value && !lastClock)
		    writeOutput(1, pins[0].value);

                writeOutput(2, !pins[1].value);
            }

	    lastClock = pins[3].value;
	}
	int getDumpType() { return 155; }
	public EditInfo getChipEditInfo(int n) {
	    if (n == 0) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Reset Pin", hasReset());
		return ei;
	    }
	    if (n == 1) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Set Pin", hasSet());
		return ei;
	    }
	    if (n == 2) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Invert Set/Reset", invertSetReset());
		return ei;
	    }
	    return super.getChipEditInfo(n);
	}
	public void setChipEditValue(int n, EditInfo ei) {
	    if (n == 0) {
		if (ei.checkbox.getState())
		    flags |= FLAG_RESET;
		else
		    flags &= ~FLAG_RESET|FLAG_SET;
		setupPins();
		allocNodes();
		setPoints();
	    }
	    if (n == 1) {
		if (ei.checkbox.getState())
		    flags |= FLAG_SET;
		else
		    flags &= ~FLAG_SET;
		setupPins();
		allocNodes();
		setPoints();
	    }
	    if (n == 2) {
		flags = ei.changeFlag(flags, FLAG_INVERT_SET_RESET);
		setupPins();
		setPoints();
	    }
	    super.setChipEditValue(n, ei);
	}
    }
