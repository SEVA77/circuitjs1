/*    
    Copyright (C) Paul Falstad, Iain Sharp and Usevalad Khatkevich
    
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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.lushprojects.circuitjs1.client.util.Locale;

public class HelpDialog extends DialogBox {
	
    HorizontalPanel hp;
	VerticalPanel vp;
	Button okButton;
	VerticalPanel vpEN;
	VerticalPanel vpRU;
	VerticalPanel vpPL;
	VerticalPanel vpDE;
	VerticalPanel vpDA;
	CirSim sim;
	
	HelpDialog() {
		super();
		vp = new VerticalPanel();
		setWidget(vp);
		setText(Locale.LS("Help"));
	
		TabPanel tp = new TabPanel();
		vp.add(tp);
		String tab1Title = "EN";
		String tab2Title = "RU";
		String tab3Title = "PL";
		String tab4Title = "DE";
		String tab5Title = "DA";

		      //create tabs 
		tp.add(vpEN = new VerticalPanel(), tab1Title);
		vpEN.setWidth("500px");
		vpEN.add(new HTML("<iframe style=\"border:0;\" src=\"help/EN.html\" width=\"500\" height=\"400\" scrolling=\"auto\" frameborder=\"1\"></iframe>"));
		
		tp.add(vpRU = new VerticalPanel(), tab2Title);
		vpRU.setWidth("500px");
		vpRU.add(new HTML("<iframe style=\"border:0;\" src=\"help/RU.html\" width=\"500\" height=\"400\" scrolling=\"auto\" frameborder=\"1\"></iframe>"));
/*
		tp.add(vpPL = new VerticalPanel(), tab3Title);
		vpPL.setWidth("500px");
		vpPL.add(new HTML("<iframe style=\"border:0;\" src=\"help/PL.html\" width=\"500\" height=\"400\" scrolling=\"auto\" frameborder=\"1\"></iframe>"));

		tp.add(vpDE = new VerticalPanel(), tab4Title);
		vpDE.setWidth("500px");
		vpDE.add(new HTML("<iframe style=\"border:0;\" src=\"help/DE.html\" width=\"500\" height=\"400\" scrolling=\"auto\" frameborder=\"1\"></iframe>"));

		tp.add(vpDA = new VerticalPanel(), tab5Title);
		vpDA.setWidth("500px");
		vpDA.add(new HTML("<iframe style=\"border:0;\" src=\"help/DA.html\" width=\"500\" height=\"400\" scrolling=\"auto\" frameborder=\"1\"></iframe>"));
*/
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hp.setStyleName("topSpace");
		
		//select first tab
		tp.selectTab(0);

		//set width if tabpanel
		tp.setWidth("500");
		
		vp.add(hp);
		hp.add(okButton = new Button("OK"));
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
