package com.lushprojects.circuitjs1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lushprojects.circuitjs1.client.util.Locale;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.Window;

public class ImportFromDropboxDialog extends Dialog {


    VerticalPanel vp;
    Button cancelButton;
    Button chooserButton;
    Button importButton;
    TextArea ta;
    Label la;
    HorizontalPanel hp;
    ImportFromDropbox importFromDropbox;
    static CirSim sim;


    static public void setSim(CirSim csim) {
        sim = csim;
    }

    static public void doLoadCallback(String s) {
        sim.pushUndo();
        sim.readCircuit(s);
        sim.allowSave(false);
    }


    static public final native void doDropboxImport(String link)  /*-{
		try {
			var xhr= new XMLHttpRequest();
		  	xhr.addEventListener("load", function reqListener() { 
	//			console.log(xhr.responseText);
				var text = xhr.responseText;
	       		@com.lushprojects.circuitjs1.client.ImportFromDropboxDialog::doLoadCallback(Ljava/lang/String;)(text);
			});
			xhr.open("GET", link, false);
			xhr.send();
		}
		catch(err) {

		}

 	}-*/;

    static public void doImportDropboxLink(String link, Boolean validateIsDropbox) {
        if (validateIsDropbox && link.indexOf("https://www.dropbox.com/") != 0) {
            Window.alert("Dropbox links must start https://www.dropbox.com/");
            return;
        }
        // Work-around to allow CORS access to dropbox links - see
        // https://www.dropboxforum.com/t5/API-support/CORS-issue-when-trying-to-download-shared-file/m-p/82466
        link = link.replace("www.dropbox.com", "dl.dropboxusercontent.com");
        doDropboxImport(link);

    }

    public ImportFromDropboxDialog(CirSim csim) {
        super();
        setSim(csim);

        closeOnEnter = false;
        vp = new VerticalPanel();
        setWidget(vp);
        setText(Locale.LS("Import from Dropbox"));
        if (ImportFromDropbox.isSupported()) {
            vp.add(new Label(Locale.LS("To open a file in your dropbox account using the chooser click below.")));
            chooserButton = new Button(Locale.LS("Open Dropbox Chooser"));
            vp.add(chooserButton);
            chooserButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    closeDialog();
                    importFromDropbox = new ImportFromDropbox(sim);
                }
            });
            la = new Label(Locale.LS("To open a shared Dropbox file from a Dropbox link paste the link below..."));
        } else {
            vp.add(new Label("This site, or your browser doesn't support the Dropbox chooser so you can't pick a file from your dropbox account."));
            la = new Label("You can open a shared Dropbox file if you have a link. Paste the Dropbox link below...");
            la.setStyleName("topSpace");
        }

        vp.add(la);
        ta = new TextArea();
        ta.setWidth("300px");
        ta.setHeight("200px");
        vp.add(ta);
        hp = new HorizontalPanel();
        hp.setWidth("100%");
        vp.add(hp);
        hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        importButton = new Button(Locale.LS("Import From Dropbox Link"));
        importButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                closeDialog();
                doImportDropboxLink(ta.getText(), true);
            }
        });
        hp.add(importButton);
        hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        cancelButton = new Button(Locale.LS("Cancel"));
        hp.add(cancelButton);
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                closeDialog();
            }
        });
        this.center();
    }
}

