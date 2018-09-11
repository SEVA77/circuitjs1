/*    
    Copyright (C) Usevalad Khatkevich
*/

package com.lushprojects.circuitjs1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;

public class LangInLinkBox extends PopupPanel {
	
	VerticalPanel vp;
	HorizontalPanel hp;
	Button okButton;
	CirSim sim;
	
	LangInLinkBox() {
		super();
		vp = new VerticalPanel();
		setWidget(vp);
		vp.setWidth("150px");
		vp.add(new HTML("<div align=\"center\">\r\n" + 
			"<a style=\"display: inline-block; text-decoration: none; text-transform: uppercase; letter-spacing: 1px; margin: 4px; background: #e7e7e7; padding: 3px 3px; font-size: 15px; font-weight: bold; font-family: 'Montserrat', sans-serif; transition: 0.4s ease-in-out; color: black; border-radius: 3px; border: 1px solid #bbb;\" href=\"index.html?lang=en\">English (EN)</a><br>\r\n" + 
			"<a style=\"display: inline-block; text-decoration: none; text-transform: uppercase; letter-spacing: 1px; margin: 4px; background: #e7e7e7; padding: 3px 3px; font-size: 15px; font-weight: bold; font-family: 'Montserrat', sans-serif; transition: 0.4s ease-in-out; color: black; border-radius: 3px; border: 1px solid #bbb;\" href=\"index.html?lang=da\">Dansk (DA)</a><br>\r\n" + 
			"<a style=\"display: inline-block; text-decoration: none; text-transform: uppercase; letter-spacing: 1px; margin: 4px; background: #e7e7e7; padding: 3px 3px; font-size: 15px; font-weight: bold; font-family: 'Montserrat', sans-serif; transition: 0.4s ease-in-out; color: black; border-radius: 3px; border: 1px solid #bbb;\" href=\"index.html?lang=de\">Deutsch (DE)</a><br>\r\n" + 
			"<a style=\"display: inline-block; text-decoration: none; text-transform: uppercase; letter-spacing: 1px; margin: 4px; background: #e7e7e7; padding: 3px 3px; font-size: 15px; font-weight: bold; font-family: 'Montserrat', sans-serif; transition: 0.4s ease-in-out; color: black; border-radius: 3px; border: 1px solid #bbb;\" href=\"index.html?lang=pl\">Polski (PL)</a><br>\r\n" + 
			"<a style=\"display: inline-block; text-decoration: none; text-transform: uppercase; letter-spacing: 1px; margin: 4px; background: #e7e7e7; padding: 3px 3px; font-size: 15px; font-weight: bold; font-family: 'Montserrat', sans-serif; transition: 0.4s ease-in-out; color: black; border-radius: 3px; border: 1px solid #bbb;\" href=\"index.html?lang=ru\">Русский (RU)</a><br>\r\n" + 
			"<hr>\r\n" + 
			"</div>"
        ));
	
		vp.add(okButton = new Button("Close"));
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
}}
