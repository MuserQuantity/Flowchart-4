package com.kyiv.flowchart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Function {
    private List<int[]> code = new ArrayList();
    private HashMap<String, List<String>> conditions = new HashMap<>();
    private List<Integer> result = new ArrayList<>();
    private int codeSize;
    private String name;

    public Function(String name, int codeSize){
        this.codeSize = codeSize;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    private void newRow(){
        for (List<String> list : conditions.values()){
            list.add("-");
        }
    }

    public void addCode(int[] code){
        if (code.length != codeSize)
            throw new RuntimeException("Не співпадає розмір коду з заданим");
        this.code.add(code);
        newRow();
    }

    public void deleteRepeat(){
        List<Integer> deleteIndex = new ArrayList<>();
        for (int i = 0; i < code.size(); i++)
            if (!deleteIndex.contains(i)){
                String termI = "";
                for (int a : code.get(i))
                    termI += a;
                for (List<String> cond : conditions.values())
                    termI += cond.get(i);
                for (int j = i + 1; j < code.size(); j++) {
                    String termJ = "";
                    for (int b : code.get(j))
                        termJ += b;
                    for (List<String> cond : conditions.values())
                        termJ += cond.get(j);
                    if (termI.equals(termJ)){
                        deleteIndex.add(j);
                    }
                }
        }
        for (int index : deleteIndex) {
            code.remove(index);
            for (List<String> cond : conditions.values())
                cond.remove(index);
            result.remove(index);

        }
    }

    public void addCondition(String name, String value){
        if (conditions.containsKey(name)){
            List<String> condition = conditions.get(name);
            condition.remove(condition.size() - 1);
            condition.add(value);
        }
        else{
            List<String> newCond = new ArrayList<>();
            for (int i = 0; i < code.size() - 1; i++){
                newCond.add("-");
            }
            newCond.add(value);
            conditions.put(name, newCond);
        }
    }

    public void addResult(int result){
        this.result.add(result);
    }

    public String getStringFunction(){
        String function = "";
        for (int i = 0; i < code.size(); i++)
            if (result.get(i) == 1){
                if(i != 0 )
                    function += 'v';
                for (int j : code.get(i))
                    function += j;
                for (List<String> condition : conditions.values())
                    function += condition.get(i);
            }
        return function;
    }

    public String getStringFunctionTextForm(){
        String function = "";
        String[] terms = getTermTemplate();
        for (int i = 0; i < code.size(); i++)
            if (result.get(i) == 1){
                if(i != 0 )
                    function += Kwaine_Minimization.V_CHAR;
                int[] currCode = code.get(i);
                for (int j = 0; j < currCode.length; j++) {
                    if (currCode[j] == 1)
                        function += terms[j];
                    else{
                        function += "not(" + terms[j] + ")";
                    }
                    if (j < currCode.length -1)
                        function += Kwaine_Minimization.AND_CHAR;
                }
                int iCond = currCode.length;
                for (List<String> condition : conditions.values())
                    if(condition.get(i).equals("1")){
                        function += terms[iCond];
                        iCond++;
                    }
            }
        return function;
    }

    public String[] getTermTemplate(){
        String[] terms = new String[code.get(0).length + conditions.size()];
        for (int j = 0; j < code.get(0).length; j++)
            terms[j] = "Q" + j;
        int j = 0;
        for (String key : conditions.keySet()) {
            terms[code.get(0).length + j] = key;
            j++;
        }
        return terms;
    }
}
