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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.core.client.ScriptInjector;

public class ModDialog extends DialogBox {
	
	VerticalPanel vp;
	
	HorizontalPanel scaleButtons;
	Button setScale100Button;
	Button setDefaultScaleButton;
	Button setScaleButton;
	HTML scaleScrollbarElm;
	native float getRealScale() /*-{
		return $wnd.nw.Window.get().zoomLevel;
	}-*/;
	native float getDefaultScale() /*-{
		return $wnd.defaultScale;
	}-*/;
	String getScaleScrollbar(float value,int scale){
		return "<input type=\"range\" id=\"scaleUI\" oninput=\"getScaleInfo()\"" +
			"min=\"-0.5\" max=\"2\" step=\"0.1\" value=\""+value+"\"" +
			"style=\"width:455px\"><b><span class=\"scaleInfo\"" +
			"style=\"vertical-align:super\">"+scale+"%</span></b>";
	}

	Button okButton;
	CirSim sim;

	ModDialog() {

		super();
		vp = new VerticalPanel();
		setWidget(vp);
		setText("Modification Setup");
		vp.setWidth("500px");

		vp.add(new HTML("<big><b>UI scale:</b></big>"));
		vp.add(scaleScrollbarElm = new HTML(getScaleScrollbar(getRealScale(),(int)(getRealScale()*100+100))));
		vp.setCellHorizontalAlignment(scaleScrollbarElm, HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(scaleButtons = new HorizontalPanel());
		scaleButtons.setWidth("100%");
		scaleButtons.add(setScale100Button = new Button("100%",
			new ClickHandler(){
				public void onClick(ClickEvent event) {
					vp.remove(scaleScrollbarElm);
					vp.insert(scaleScrollbarElm = new HTML(getScaleScrollbar(0,100)),1);
					vp.setCellHorizontalAlignment(scaleScrollbarElm, HasHorizontalAlignment.ALIGN_CENTER);
					ScriptInjector.fromString("setScaleUI()")
						.setWindow(ScriptInjector.TOP_WINDOW)
						.inject();
					//TODO: Save data to localStorage
				}
			}));
		scaleButtons.add(setDefaultScaleButton = new Button("Default scale<b>*</b>",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					vp.remove(scaleScrollbarElm);
					vp.insert(scaleScrollbarElm = new HTML(getScaleScrollbar(getDefaultScale(),
						(int)(getDefaultScale()*100+100))),1);
					vp.setCellHorizontalAlignment(scaleScrollbarElm, HasHorizontalAlignment.ALIGN_CENTER);
					ScriptInjector.fromString("setScaleUI()")
						.setWindow(ScriptInjector.TOP_WINDOW)
						.inject();
					//TODO: Save data to localStorage
				}
			}));
		scaleButtons.add(setScaleButton = new Button("Set",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					ScriptInjector.fromString("setScaleUI()")
						.setWindow(ScriptInjector.TOP_WINDOW)
						.inject();
					//TODO: Save data to localStorage
				}
			}));
		vp.add(new HTML("<p>* - the default UI scale for your monitor is set to "+
			(int)(getDefaultScale()*100+100)+"%</p>"));
		// Styling buttons:
		setScaleButton.addStyleName("modButtons"); //.setHeight("20px");
		setScaleButton.addStyleName("modSetButtons");
		setScale100Button.addStyleName("modButtons");
		setDefaultScaleButton.addStyleName("modButtons");
		//remove "gwt-Button" style:
		setScaleButton.removeStyleName("gwt-Button");
		setScale100Button.removeStyleName("gwt-Button");
		setDefaultScaleButton.removeStyleName("gwt-Button");
		scaleButtons.setCellHorizontalAlignment(setScale100Button, HasHorizontalAlignment.ALIGN_CENTER);
		scaleButtons.setCellHorizontalAlignment(setDefaultScaleButton, HasHorizontalAlignment.ALIGN_CENTER);
		scaleButtons.setCellHorizontalAlignment(setScaleButton, HasHorizontalAlignment.ALIGN_CENTER);
		/*
		vp.add(new HTML("<hr><big><b>Start/Stop and Reset buttons:</b></big>"));
		vp.add(new HTML("<big><big>(in developing)</big></big>"));
		*/
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
