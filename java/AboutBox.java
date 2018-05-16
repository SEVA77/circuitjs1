/*    
    Copyright (C) Paul Falstad, Iain Sharp
    
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
/*The file was changed for desktop program by Usevalad Khatkevich*/
package com.lushprojects.circuitjs1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

public class AboutBox extends DialogBox {
	
	VerticalPanel vp;
	Button okButton;
	CirSim sim;
	
	AboutBox(String version) {
		super();
		vp = new VerticalPanel();
		setWidget(vp);
		setText(sim.LS("About"));
		vp.setWidth("500px");
		vp.add(new HTML("<p>Circuit Simulator version 1.0.1</p>"+
		"<p>Original by Paul Falstad.(http://www.falstad.com/)</p>"+
		"<p>JavaScript conversion by Iain Sharp (version "+version+" for JavaScript).(http://lushprojects.com/)</p>"+
		"<p>The program was ported to Windows, MAC OS and Linux by Usevalad Khatkevich (NW.js version 0.17.0).</p>"+
		"<p style=\"font-size:11px\">Thanks to: Edward Calver for 15 new components and other improvements; Rodrigo Hausen for file import/export and many other UI improvements; "+  
		"J. Mike Rollins for the Zener diode code; Julius Schmidt for the spark gap code and some examples; Dustin Soodak for help with the user interface improvements; "+
		"Jacob Calvert for the T Flip Flop; Ben Hayden for scope spectrum; " +
		"Thomas Reitinger for the German translation; " +
		"Krystian Sławiński for the Polish translation; " +
		"Andre Adrian for improved emitter coupled oscillator; Felthry for many examples; " +
		"Colin Howell for code improvements.</p>"+
		"<p style=\"font-size:10px\">This program is free software: you can redistribute it and/or modify it "+
		"under the terms of the GNU General Public License as published by "+
		"the Free Software Foundation, either version 2 of the License, or "+
		"(at your option) any later version.</p>"+
		"<p style=\"font-size:10px\">This program is distributed in the hope that it will be useful,"+
		"but WITHOUT ANY WARRANTY; without even the implied warranty of "+
		"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the "+
		"GNU General Public License for more details.</p>"+
		"<p style=\"font-size:10px\">For details of licensing see http://www.gnu.org/licenses/.</p>"+
		"<p style=\"font-size:10px\">Source code (Paul): https://github.com/pfalstad/circuitjs1</p>"+
		"<p style=\"font-size:10px\">Source code (Iain): https://github.com/sharpie7/circuitjs1</p>"+
	    "<p style=\"font-size:10px\">Source code (Usevalad): https://github.com/CEBA77/circuitjs1</p>"));
		
		
		vp.add(okButton = new Button("OK"));
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				close();
			}
		});
		center();
		show();
	}

	public void close() {
		hide();
	}
}
