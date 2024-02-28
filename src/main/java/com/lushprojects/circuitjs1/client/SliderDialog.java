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


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lushprojects.circuitjs1.client.util.Locale;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

// class EditDialog extends Dialog implements AdjustmentListener, ActionListener, ItemListener {
public class SliderDialog extends Dialog {
    CircuitElm elm;
    CirSim sim;
    Button applyButton, okButton, cancelButton;
    EditInfo einfos[];
    int einfocount;
    final int barmax = 1000;
    VerticalPanel vp;
    HorizontalPanel hp;
    NumberFormat noCommaFormat;

    SliderDialog(CircuitElm ce, CirSim f) {
        super(); // Do we need this?
        setText(Locale.LS("Add Sliders"));
        sim = f;
        elm = ce;
        vp = new VerticalPanel();
        setWidget(vp);
        einfos = new EditInfo[10];
        noCommaFormat = NumberFormat.getFormat("####.##########");
        hp = new HorizontalPanel();
        hp.setWidth("100%");
        hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        hp.setStyleName("topSpace");
        vp.add(hp);
        hp.add(applyButton = new Button(Locale.LS("Apply")));
        applyButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                apply();
            }
        });
        hp.add(okButton = new Button(Locale.LS("OK")));
        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                apply();
                closeDialog();
            }
        });
        hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        hp.add(cancelButton = new Button(Locale.LS("Cancel")));
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                closeDialog();
            }
        });
        buildDialog();
        this.center();
    }

    void buildDialog() {
        int i;
        int idx;
        for (i = 0; ; i++) {
            einfos[i] = elm.getEditInfo(i);
            if (einfos[i] == null)
                break;
            EditInfo ei = einfos[i];
            if (!ei.canCreateAdjustable())
                continue;
            Adjustable adj = findAdjustable(i);
            String name = Locale.LS(ei.name);
            idx = vp.getWidgetIndex(hp);

            // remove HTML
            name = name.replaceAll("<[^>]*>", "");
            ei.checkbox = new Checkbox(name, adj != null);
            vp.insert(ei.checkbox, idx++);
            ei.checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                public void onValueChange(ValueChangeEvent<Boolean> e) {
                    itemStateChanged(e);
                }
            });

            if (adj != null) {
                if (!adj.sliderBeingShared()) {
                    // add popup to select which slider to share
                    Choice ch = ei.choice = new Choice();
                    ei.choice.addChangeHandler(new ChangeHandler() {
                        public void onChange(ChangeEvent e) {
                            itemStateChanged(e);
                        }
                    });
                    ch.add("New Slider");
                    int j;
                    for (j = 0; j != sim.adjustables.size(); j++) {
                        Adjustable adji = sim.adjustables.get(j);
                        // don't share with an object sharing with someone else
                        if (adji.sharedSlider != null)
                            break;
                        // don't share with ourselves
                        if (adji == adj)
                            continue;
                        ch.add("Share Slider: " + adji.sliderText);
                        if (adji == adj.sharedSlider)
                            ch.setSelectedIndex(ch.getItemCount() - 1);
                    }
                    vp.insert(ch, idx++);
                }
                vp.insert(new Label(Locale.LS("Min Value")), idx++);
                ei.minBox = new TextBox();
                vp.insert(ei.minBox, idx++);
                vp.insert(new Label(Locale.LS("Max Value")), idx++);
                ei.maxBox = new TextBox();
                vp.insert(ei.maxBox, idx++);
                if (adj.sharedSlider == null) {
                    // select label if this is a new slider
                    vp.insert(new Label(Locale.LS("Label")), idx++);
                    ei.labelBox = new TextBox();
                    ei.labelBox.setText(adj.sliderText);
                    vp.insert(ei.labelBox, idx++);
                }
                ei.minBox.setText(EditDialog.unitString(ei, adj.minValue));
                ei.maxBox.setText(EditDialog.unitString(ei, adj.maxValue));
            }

        }
        einfocount = i;
    }

    Adjustable findAdjustable(int item) {
        return sim.findAdjustable(elm, item);
    }

    void apply() {
        int i;
        for (i = 0; i != einfocount; i++) {
            Adjustable adj = findAdjustable(i);
            if (adj == null)
                continue;
            EditInfo ei = einfos[i];
//		    if (ei.labelBox == null)  // haven't created UI yet?
//			continue;
            try {
                adj.sliderText = ei.labelBox == null ? "" : ei.labelBox.getText();
                CirSim.console("slidertext " + adj.sliderText + " " + ei.labelBox);
                if (adj.label != null)
                    adj.label.setText(adj.sliderText);
                double d = EditDialog.parseUnits(ei.minBox.getText());
                adj.minValue = d;
                d = EditDialog.parseUnits(ei.maxBox.getText());
                adj.maxValue = d;
                adj.setSliderValue(ei.value);
            } catch (Exception e) {
                CirSim.console(e.toString());
            }
        }
    }

    public void itemStateChanged(GwtEvent e) {
        Object src = e.getSource();
        int i;
        boolean changed = false;
        for (i = 0; i != einfocount; i++) {
            EditInfo ei = einfos[i];
            if (ei.checkbox == src) {
                apply();
                if (ei.checkbox.getState()) {
                    Adjustable adj = new Adjustable(elm, i);
                    adj.sliderText = ei.name.replaceAll(" \\(.*\\)$", "");
                    adj.createSlider(sim, ei.value);
                    sim.adjustables.add(adj);
                } else {
                    Adjustable adj = findAdjustable(i);
                    adj.deleteSlider(sim);
                    sim.adjustables.remove(adj);
                }
                changed = true;
            }
            if (ei.choice == src) {
                apply();
                Adjustable adj = findAdjustable(i);
                if (ei.choice.getSelectedIndex() == 0) {
                    // new slider
                    adj.sharedSlider = null;
                    if (adj.sliderText == null || adj.sliderText.length() == 0)
                        adj.sliderText = ei.name.replaceAll(" \\(.*\\)$", "");
                    adj.createSlider(sim, ei.value);
                } else {
                    int j;
                    int ct = 0;
                    for (j = 0; j != sim.adjustables.size(); j++) {
                        Adjustable adji = sim.adjustables.get(j);
                        if (adji.sharedSlider != null)
                            break;
                        if (adji == adj)
                            continue;
                        if (++ct == ei.choice.getSelectedIndex()) {
                            adj.sharedSlider = adji;
                            adj.deleteSlider(sim);
                        }
                    }
                }
                changed = true;
            }
        }
        if (changed) {
            Adjustable.reorderAdjustables();
            clearDialog();
            buildDialog();
        }
    }

    public void clearDialog() {
        while (vp.getWidget(0) != hp)
            vp.remove(0);
    }
}

