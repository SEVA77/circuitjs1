package com.lushprojects.circuitjs1.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.lushprojects.circuitjs1.client.util.Locale;

public class TransistorModel implements Editable, Comparable<TransistorModel> {

    static HashMap<String, TransistorModel> modelMap;

    int flags;
    String name, description;
    double satCur, invRollOffF, BEleakCur, leakBEemissionCoeff, invRollOffR, BCleakCur, leakBCemissionCoeff;
    double emissionCoeffF, emissionCoeffR, invEarlyVoltF, invEarlyVoltR, betaR;

    boolean dumped;
    boolean readOnly;
    boolean builtIn;
    boolean internal;

    TransistorModel(String d, double sc) {
        description = d;
        satCur = sc;
        emissionCoeffF = emissionCoeffR = 1;
        leakBEemissionCoeff = 1.5;
        leakBCemissionCoeff = 2;
        betaR = 1;
        updateModel();
    }

    static TransistorModel getModelWithName(String name) {
        createModelMap();
        TransistorModel lm = modelMap.get(name);
        if (lm != null)
            return lm;
        lm = new TransistorModel();
        lm.name = name;
        modelMap.put(name, lm);
        return lm;
    }

    static TransistorModel getModelWithNameOrCopy(String name, TransistorModel oldmodel) {
        createModelMap();
        TransistorModel lm = modelMap.get(name);
        if (lm != null)
            return lm;
        if (oldmodel == null) {
            CirSim.console("model not found: " + name);
            return getDefaultModel();
        }
        lm = new TransistorModel(oldmodel);
        lm.name = name;
        modelMap.put(name, lm);
        return lm;
    }

    static void createModelMap() {
        if (modelMap != null)
            return;
        modelMap = new HashMap<String, TransistorModel>();
        addDefaultModel("default", new TransistorModel("default", 1e-13));
        addDefaultModel("spice-default", new TransistorModel("spice-default", 1e-16));

        // for LM324v2 OpAmpRealElm
        loadInternalModel("xlm324v2-qpi 0 1.01e-16 333.3333333333333 0 1.5 0 0 2 1 1 0.0034482758620689655 0 1");
        loadInternalModel("xlm324v2-qpi 0 1.01e-16 333.3333333333333 0 1.5 0 0 2 1 1 0.0034482758620689655 0 1");
        loadInternalModel("xlm324v2-qpa 0 1.01e-16 333.3333333333333 0 1.5 0 0 2 1 1 0.004081632653061225 0 1");
        loadInternalModel("xlm324v2-qnq 0 1e-16 200 0 1.5 0 0 2 1 1 0 0 1");
        loadInternalModel("xlm324v2-qpq 0 1e-16 333.3333333333333 0 1.5 0 0 2 1 1 0 0 1");

        // for TL431
        loadInternalModel("~tl431ed-qn_ed 0 1e-16 0 0 1.5 0 0 2 1 1 0.0125 0.02 1");
        loadInternalModel("~tl431ed-qn_ed-A1.2 0 1.2e-16 0 0 1.5 0 0 2 1 1 0.0125 0.02 1");
        loadInternalModel("~tl431ed-qn_ed-A2.2 0 2.2000000000000002e-16 0 0 1.5 0 0 2 1 1 0.0125 0.02 1");
        loadInternalModel("~tl431ed-qn_ed-A0.5 0 5e-17 0 0 1.5 0 0 2 1 1 0.0125 0.02 1");
        loadInternalModel("~tl431ed-qp_ed 0 1e-16 0 0 1.5 0 0 2 1 1 0.014285714285714285 0.025 1");
        loadInternalModel("~tl431ed-qn_ed-A5 0 5e-16 0 0 1.5 0 0 2 1 1 0.0125 0.02 1");

        // for LM317
        loadInternalModel("~lm317-qpl-A0.1 0 1e-17 0 0 1.5 0 0 2 1 1 0.02 0 1");
        loadInternalModel("~lm317-qnl-A0.2 0 2e-17 0 0 1.5 0 0 2 1 1 0.01 0 1");
        loadInternalModel("~lm317-qpl-A0.2 0 2e-17 0 0 1.5 0 0 2 1 1 0.02 0 1");
        loadInternalModel("~lm317-qnl-A2 0 2e-16 0 0 1.5 0 0 2 1 1 0.01 0 1");
        loadInternalModel("~lm317-qpl-A2 0 2e-16 0 0 1.5 0 0 2 1 1 0.02 0 1");
        loadInternalModel("~lm317-qnl-A5 0 5e-16 0 0 1.5 0 0 2 1 1 0.01 0 1");
        loadInternalModel("~lm317-qnl-A50 0 5e-15 0 0 1.5 0 0 2 1 1 0.01 0 1");

    }

    static void addDefaultModel(String name, TransistorModel dm) {
        modelMap.put(name, dm);
        dm.readOnly = dm.builtIn = true;
        dm.name = name;
    }

    static TransistorModel getDefaultModel() {
        return getModelWithName("default");
    }

    static void clearDumpedFlags() {
        if (modelMap == null)
            return;
        Iterator it = modelMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TransistorModel> pair = (Map.Entry) it.next();
            pair.getValue().dumped = false;
        }
    }

    static Vector<TransistorModel> getModelList() {
        Vector<TransistorModel> vector = new Vector<TransistorModel>();
        Iterator it = modelMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TransistorModel> pair = (Map.Entry) it.next();
            TransistorModel tm = pair.getValue();
            if (tm.internal)
                continue;
            if (!vector.contains(tm))
                vector.add(tm);
        }
        Collections.sort(vector);
        return vector;
    }

    public int compareTo(TransistorModel dm) {
        return name.compareTo(dm.name);
    }

    String getDescription() {
        if (description == null || description.equals(name))
            return name;
        return name + " (" + Locale.LS(description) + ")";
    }

    TransistorModel() {
        updateModel();
    }

    TransistorModel(TransistorModel copy) {
        flags = copy.flags;
        satCur = copy.satCur;
        invRollOffF = copy.invRollOffF;
        BEleakCur = copy.BEleakCur;
        leakBEemissionCoeff = copy.leakBEemissionCoeff;
        invRollOffR = copy.invRollOffR;
        BCleakCur = copy.BCleakCur;
        leakBCemissionCoeff = copy.leakBCemissionCoeff;
        emissionCoeffF = copy.emissionCoeffF;
        emissionCoeffR = copy.emissionCoeffR;
        invEarlyVoltF = copy.invEarlyVoltF;
        invEarlyVoltR = copy.invEarlyVoltR;
        betaR = copy.betaR;
        updateModel();
    }

    static void loadInternalModel(String s) {
        StringTokenizer st = new StringTokenizer(s);
        TransistorModel tm = undumpModel(st);
        tm.builtIn = tm.internal = true;
    }

    static TransistorModel undumpModel(StringTokenizer st) {
        String name = CustomLogicModel.unescape(st.nextToken());
        TransistorModel dm = TransistorModel.getModelWithName(name);
        dm.undump(st);
        return dm;
    }

    void undump(StringTokenizer st) {
        flags = new Integer(st.nextToken()).intValue();

        satCur = Double.parseDouble(st.nextToken());
        invRollOffF = Double.parseDouble(st.nextToken());
        BEleakCur = Double.parseDouble(st.nextToken());
        leakBEemissionCoeff = Double.parseDouble(st.nextToken());
        invRollOffR = Double.parseDouble(st.nextToken());
        BCleakCur = Double.parseDouble(st.nextToken());
        leakBCemissionCoeff = Double.parseDouble(st.nextToken());
        emissionCoeffF = Double.parseDouble(st.nextToken());
        emissionCoeffR = Double.parseDouble(st.nextToken());
        invEarlyVoltF = Double.parseDouble(st.nextToken());
        invEarlyVoltR = Double.parseDouble(st.nextToken());
        betaR = Double.parseDouble(st.nextToken());

        updateModel();
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            EditInfo ei = new EditInfo("Model Name", 0);
            ei.text = name == null ? "" : name;
            return ei;
        }
        if (n == 1) return new EditInfo("Transport Saturation Current (IS)", satCur);
        if (n == 2) return new EditInfo("Reverse Beta (BR)", betaR);
        if (n == 3) return new EditInfo("Forward Early Voltage (VAF)", 1 / invEarlyVoltF);
        if (n == 4) return new EditInfo("Reverse Early Voltage (VAR)", 1 / invEarlyVoltR);
        if (n == 5) return new EditInfo("Corner For Forward Beta High Current Roll-Off (IKF)", 1 / invRollOffF);
        if (n == 6) return new EditInfo("Corner For Reverse Beta High Current Roll-Off (IKR)", 1 / invRollOffR);
        if (n == 7) return new EditInfo("Forward Current Emission Coefficient (NF)", emissionCoeffF);
        if (n == 8) return new EditInfo("Reverse Current Emission Coefficient (NR)", emissionCoeffR);
        if (n == 9) return new EditInfo("B-E Leakage Saturation Current (ISE)", BEleakCur);
        if (n == 10) return new EditInfo("B-E Leakage Emission Coefficient (NE)", leakBEemissionCoeff);
        if (n == 11) return new EditInfo("B-C Leakage Saturation Current (ISC)", BCleakCur);
        if (n == 12) return new EditInfo("B-C Leakage Emission Coefficient (NC)", leakBCemissionCoeff);
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            name = ei.textf.getText();
            if (name.length() > 0)
                modelMap.put(name, this);
        }
        if (n == 1) satCur = ei.value;
        if (n == 2) betaR = ei.value;
        if (n == 3) invEarlyVoltF = 1 / ei.value;
        if (n == 4) invEarlyVoltR = 1 / ei.value;
        if (n == 5) invRollOffF = 1 / ei.value;
        if (n == 6) invRollOffR = 1 / ei.value;
        if (n == 7) emissionCoeffF = ei.value;
        if (n == 8) emissionCoeffR = ei.value;
        if (n == 9) BEleakCur = ei.value;
        if (n == 10) leakBEemissionCoeff = ei.value;
        if (n == 11) BCleakCur = ei.value;
        if (n == 12) leakBCemissionCoeff = ei.value;
        updateModel();
        CirSim.theSim.updateModels();
    }

    void updateModel() {
    }

    String dump() {
        dumped = true;
        return "32 " + CustomLogicModel.escape(name) + " " + flags + " " +
                satCur + " " + invRollOffF + " " + BEleakCur + " " + leakBEemissionCoeff + " " + invRollOffR + " " +
                BCleakCur + " " + leakBCemissionCoeff + " " + emissionCoeffF + " " + emissionCoeffR + " " + invEarlyVoltF + " " + invEarlyVoltR + " " + betaR;
    }
}
