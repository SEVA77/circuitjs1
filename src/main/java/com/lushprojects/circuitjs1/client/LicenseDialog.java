/*    
    Copyright (C) Usevalad Khatkevich
*/

package com.lushprojects.circuitjs1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Button;
import com.lushprojects.circuitjs1.client.util.Locale;

public class LicenseDialog extends DialogBox {

    VerticalPanel vp;
    Button okButton;
    CirSim sim;

    LicenseDialog() {
        super();
        vp = new VerticalPanel();
        setWidget(vp);
        setText(Locale.LS("License"));
        vp.setWidth("500px");
        vp.add(new HTML("<iframe style=\"border:0;\" src=\"help/license.html\" width=\"500\" height=\"400\" scrolling=\"auto\" frameborder=\"1\"></iframe>"));
        vp.add(okButton = new Button("OK"));
        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                closeDialog();
            }
        });
        center();
        show();
    }

    protected void closeDialog() {
        hide();
    }
}
