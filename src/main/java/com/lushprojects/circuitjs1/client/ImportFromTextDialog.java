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

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lushprojects.circuitjs1.client.util.Locale;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;

public class ImportFromTextDialog extends Dialog {

    VerticalPanel vp;
    HorizontalPanel hp;
    CirSim sim;
    // RichTextArea textBox;
    TextArea textArea;

    public ImportFromTextDialog(CirSim asim) {
        super();
        sim = asim;
        closeOnEnter = false;
        Button okButton, cancelButton;
        final Checkbox subCheck;
        vp = new VerticalPanel();
        setWidget(vp);
        setText(Locale.LS("Import from Text"));
        vp.add(new Label(Locale.LS("Paste the text file for your circuit here...")));
//		vp.add(textBox = new RichTextArea());
        vp.add(textArea = new TextArea());
        textArea.setWidth("300px");
        textArea.setHeight("200px");
        vp.add(subCheck = new Checkbox(Locale.LS("Load Subcircuits Only")));
        hp = new HorizontalPanel();
        vp.add(hp);
        hp.add(okButton = new Button(Locale.LS("OK")));
        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                String s;
                sim.pushUndo();
                closeDialog();
//				s=textBox.getHTML();
//				s=s.replace("<br>", "\r");
                s = textArea.getText();
                sim.importCircuitFromText(s, subCheck.getState());
            }
        });
        hp.add(cancelButton = new Button(Locale.LS("Cancel")));
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                closeDialog();
            }
        });
        this.center();
        show();
    }
}
