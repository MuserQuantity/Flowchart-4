package com.kyiv.flowchart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by mikola on 12.12.2016.
 */

public class BooleanFunctionNotAndOr {
    private String data;
    ArrayList<String> functions;

    ArrayList<Component> components = new ArrayList<>();
    ArrayList<Signal> signals = new ArrayList<>();

    HashMap<String, String> ports = new HashMap<>();

    private final int AND_NOT_NUMBER = 2;
    int signalCount = 0;
    public static final String S = "SIGNAL";


    public BooleanFunctionNotAndOr(String data) {
        this.data = data.replace(" ", "").replace("\n", "");
        functions = new ArrayList<>();
        Collections.addAll(functions, this.data.split(";"));
        makeNot2And2OrView();

    }


    private void makeNot2And2OrView() {
        countInSignals();
        countOutSignals();

        for (String signal : ports.keySet()) {
            signals.add(new Signal(signal, signal));
            if (ports.get(signal) == IN) {
                String newSignal = "not(" + signal + ")";
                Component c = new Component(Component.NOT, searchSignal(signal), null, searchSignal(newSignal));
                addNewComponent(c);
            }
        }


        for (int i = 0; i < functions.size(); i++) {
            String Func = functions.get(i).substring(0, functions.get(i).indexOf("="));
            String OR = functions.get(i).substring(functions.get(i).indexOf("=") + 1);
            functions.set(i, Func + " = " + normalizeOR(OR, Func) + ";");
        }
        String s = new String("jf");
    }


    private void addNewComponent(Component c) {
        for (Component component : components) {
            if (c.getOut().getVALUE().equals(component.getOut().getVALUE()))
                return;
        }
        components.add(c);
    }


    private void countOutSignals() {
        for (int i = 0; i < functions.size(); i++) {
            String Func = functions.get(i).substring(0, functions.get(i).indexOf("="));
            searchPorts(Func, OUT);
        }
    }

    private void countInSignals() {
        for (int i = 0; i < functions.size(); i++) {

            String OR = functions.get(i).substring(functions.get(i).indexOf("=") + 1);
            OR = new String(cropString(OR));
            ArrayList<String> orList = new ArrayList<>();

            Collections.addAll(orList, OR.split(String.valueOf(Kwaine_Minimization.V_CHAR)));

            for (int j = 0; j < orList.size(); j++) {
                String AND = orList.get(j);
                while (AND.charAt(0) == '(')
                    AND = new String(cropString(AND));

                ArrayList<String> andList = new ArrayList<>();
                Collections.addAll(andList, AND.split(String.valueOf(Kwaine_Minimization.AND_CHAR)));

                for (String s : andList) {
                    while (s.charAt(0) == '(')
                        s = new String(cropString(s));
                    searchPorts(s, IN);
                }
            }

        }


    }

    private String normalizeOR(String OR, String func) {
        String or = new String(cropString(OR));
        ArrayList<String> orList = new ArrayList<>();
        Collections.addAll(orList, or.split(String.valueOf(Kwaine_Minimization.V_CHAR)));


        for (int j = 0; j < orList.size(); j++) {
            String AND = orList.get(j);
            while (AND.charAt(0) == '(')
                AND = new String(cropString(AND));

            String resAND = normalizeAND(AND);
            orList.set(j, resAND);
        }

        while (orList.size() > 2)
            orList = buildTreeAnd(orList, Kwaine_Minimization.V_CHAR);
        String res;

        if (orList.size() == 2) {
            res = packString(orList.get(0) + Kwaine_Minimization.V_CHAR + orList.get(1));
            if(!searchOutSignal(res)){
                Component c = new Component(Component.OR, searchSignal(orList.get(0)), searchSignal(orList.get(1)), searchSignal(func));
                addNewComponent(c);
            }
            return res;
        } else {
            res = packString(orList.get(0) + Kwaine_Minimization.V_CHAR + orList.get(0));
            if(!searchOutSignal(res)){
                Component c = new Component(Component.OR, searchSignal(orList.get(0)), searchSignal(orList.get(0)), searchSignal(func));
                addNewComponent(c);}
            return res;
        }


    }

    private String normalizeAND(String AND) {
        String res;
        ArrayList<String> andList = new ArrayList<>();
        Collections.addAll(andList, AND.split(String.valueOf(Kwaine_Minimization.AND_CHAR)));

        while (andList.size() > 2)
            andList = buildTreeAnd(andList, Kwaine_Minimization.AND_CHAR);


        if (andList.size() == 2) {
            res = packString(andList.get(0) + Kwaine_Minimization.AND_CHAR + andList.get(1));
            if (!searchOutSignal(res)) {
                Component c = new Component(Component.AND, searchSignal(andList.get(0)), searchSignal(andList.get(1)), searchSignal(res));
                addNewComponent(c);
            }
            return res;
        } else {
            res = packString(andList.get(0) + Kwaine_Minimization.AND_CHAR + andList.get(0));
            if (!searchOutSignal(res)) {
                Component c = new Component(Component.AND, searchSignal(andList.get(0)), searchSignal(andList.get(0)), searchSignal(res));
                addNewComponent(c);
            }
            return res;
        }

    }


    private void searchPorts(String s, String type) {
        for (String sign : ports.keySet()) {
            if (sign.equals(deleteNot(s)))
                return;
        }
        ports.put(deleteNot(s), type);
    }




    private ArrayList<String> buildTreeAnd(ArrayList<String> andList, char function) {
        ArrayList<String> newList = new ArrayList<>();
        String res = new String();
        String var1 = new String();
        String var2 = new String();
        String type;
        if (function == Kwaine_Minimization.AND_CHAR)
            type = Component.AND;
        else type = Component.OR;


        for (int j = 0; j < andList.size(); j++) {
            String var = andList.get(j);
            if (j % AND_NOT_NUMBER == 0)
                if (j == andList.size() - 1) {
                    res = packString(var + function + var);
                    var1 = var;
                    if (!searchOutSignal(res)) {
                        Component c = new Component(type, searchSignal(var1), searchSignal(var1), searchSignal(res));
                        addNewComponent(c);
                    }

                    newList.add(res);
                } else {
                    res = "(" + var + function;
                    var1 = var;
                }

            if (j % AND_NOT_NUMBER == 1) {
                res += var + ")";
                var2 = var;
                if (!searchOutSignal(res)) {
                    Component c = new Component(type, searchSignal(var1), searchSignal(var2), searchSignal(res));
                    addNewComponent(c);
                }

                newList.add(res);
            }

        }
        if (newList.size() % 2 != 0)
            newList.add(newList.get(newList.size() - 1));
        return newList;
    }

    private boolean searchOutSignal(String s) {
        for (Signal signal : signals) {
            if (signal.getVALUE().equals(s))
                return true;
        }
        return false;
    }



    private Signal searchSignal(String s) {
        for (Signal signal : signals) {
            if (signal.getVALUE().equals(s))
                return signal;
        }
        signalCount++;
        Signal newSignal = new Signal(S + signalCount, s);
        signals.add(newSignal);
        return newSignal;
    }


    private String cropString(String s) {
        if (s.length() > 1)
            return s.substring(1, s.length() - 1);
        else return s;
    }

    private String packString(String s) {
        return "(" + s + ")";
    }

    private String deleteNot(String s) {
        if (s.contains("not("))
            return s.substring(s.indexOf("not(") + 4, s.lastIndexOf(")"));
        else return s;
    }

    /**
     * -------------------------------------------------------------------------
     * Генерація VHDL
     * @return
     * -------------------------------------------------------------------------
     */
    public String generateVHDL() {
        String res = new String();
        res += putPort();
        res += putSignals();
        res += putTerms();
        return res;

    }

    private String putSignals() {
        String res = new String("architecture LABA of LABA is \n signal ");
        HashSet<String> sign = new HashSet<>();
        for (Signal s : signals) {
            sign.add(s.getKEY());
        }

        for (String s : sign) {
            if (s.startsWith(S))
                res += s + ",";

        }
        res = new String(res.substring(0, res.length() - 1));
        return res + " : " + STD_LOGIC + ";\n";
    }

    private String putTerms() {
        String res = new String("begin\n");
        int i = 0;


        for (Component c : components) {
            String type = c.type;
            if (type.equals(Component.AND))
                res += andProcess(c, i);
            else if (type.equals(Component.OR))
                res += orProcess(c, i);
            else if (type.equals(Component.NOT))
                res += notProcess(c, i);
            i++;
        }
        return res + "\nend LABA;";
    }

    private String andProcess(Component c, int i) {
        return (TERM + i) + " : process(" + c.getInFirst().getKEY() + "," + c.getInSecond().getKEY() + ")\n" +
                "\tbegin\n " +
                c.getOut().getKEY() + "<=(" + c.getInFirst().getKEY() + " and " + c.getInSecond().getKEY() + ");\n" +
                "\tend process " + (TERM + i) + ";\n\n";
    }

    private String orProcess(Component c, int i) {
        return (TERM + i) + " : process(" + c.getInFirst().getKEY() + "," + c.getInSecond().getKEY() + ")\n" +
                "\tbegin\n " +
                c.getOut().getKEY() + "<=(" + c.getInFirst().getKEY() + " or " + c.getInSecond().getKEY() + ");\n" +
                "\tend process " + (TERM + i) + ";\n\n";
    }

    private String notProcess(Component c, int i) {
        return (TERM + i) + " : process(" + c.getInFirst().getKEY() + ")\n" +
                "\tbegin\n " +
                c.getOut().getKEY() + "<=( not " + c.getInFirst().getKEY() + ");\n" +
                "\tend process " + (TERM + i) + ";\n\n";
    }

    private String putPort() {
        String res = new String("entity LABA is \nport(\n");

        for (String port : ports.keySet()) {
            res += port + " : " + ports.get(port) + " " + STD_LOGIC + ";\n";
        }
        res = new String(res.substring(0, res.length() - 2));
        return res + ");\n end LABA;\n";
    }

    public static final String IN = "in";
    public static final String OUT = "out";
    public static final String STD_LOGIC = "bit";
    public static final String TERM = "T";


}