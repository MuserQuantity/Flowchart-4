package com.kyiv.flowchart;

import android.content.Context;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kyiv.flowchart.graph.GraphModel;
import com.kyiv.flowchart.graph.LinkGraph;
import com.kyiv.flowchart.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by sergiynasinnyk on 17.11.2016.
 */

public class Table {
    List<TableRow> rows = new ArrayList<>();

    public List<TableRow> getRows() {
        return rows;
    }

    private Function[] tFunctions;
    private HashMap<String, Function> yFunctions = new HashMap<>();

    public String intArrayToStr(int[] array){
        String result = "";
        for (int i = 0; i < array.length; i++){
            result += array[i] + " ";
        }
        return result;
    }

    private List<Object[]> parseCondition(String condition){
        condition = condition.trim();
        List<Object[]> result = new ArrayList<>();
        boolean breakCondition = true;
        while(breakCondition){
            String condText;
            String condInt;
            int spaceIndex = condition.indexOf(' ');
            if (spaceIndex == -1){
                condText = condition;
                breakCondition = false;
            }
            else{
                condText = condition.substring(0, spaceIndex);
            }


            if (condText.charAt(0) == '!'){
                condInt = "0";
                condText = condText.substring(1);
            }
            else{
                condInt = "1";
            }
            result.add(new Object[]{condText, condInt});
            condition = (condition.substring(spaceIndex + 1, condition.length())).trim();
        }
        return result;

    }

    public void createTable(GraphModel graphModel, TableLayout tableLayout, Context context){
        TableRow row = new TableRow(context);
        TextView tv = new TextView(context);
        tv.setText(" ПС(Код ПС) ");
        row.addView(tv);
        tv = new TextView(context);
        tv.setText(" -> ");
        row.addView(tv);
        tv = new TextView(context);
        tv.setText(" СП(Код СП) ");
        row.addView(tv);
        tv = new TextView(context);
        tv.setText(" Умова ");
        row.addView(tv);
        tv = new TextView(context);
        tv.setText(" Функції збудження тригерів ");
        row.addView(tv);
        rows.add(row);
        List<LinkGraph> links = graphModel.getLinks();
        for (LinkGraph link : links){
            Node in = link.getInNode();
            Node out = link.getOutNode();
            if (tFunctions == null) {
                tFunctions = new Function[out.getCode().length];
                for (int i = 0; i < tFunctions.length; i++)
                    tFunctions[i] = new Function("T" + i, out.getCode().length);
            }
            String[] yArr = out.getOperators().split(",");
            Set<String> yKeys = yFunctions.keySet();
            for (String y : yArr){
                y = y.trim();
                if (y.length() > 0 && !y.equals("start"))
                    if (!yKeys.contains(y)){
                        yFunctions.put(y, new Function(y, out.getCode().length));
                    }
            }
            for (String y : yArr){
                if (y.length() > 0 && !y.equals("start")) {
                    Function function = yFunctions.get(y);
                    function.addResult(1);
                    function.addCode(out.getCode());
                }
            }
            row = new TableRow(context);
            tv = new TextView(context);
            tv.setText(" " + out.getNameNode() + "(" + intArrayToStr(out.getCode()) + ") ");
            row.addView(tv);
            tv = new TextView(context);
            tv.setText(" -> ");
            row.addView(tv);
            tv = new TextView(context);
            tv.setText(" " + in.getNameNode() + "(" + intArrayToStr(in.getCode()) + ") ");
            row.addView(tv);
            tv = new TextView(context);
            String condition = link.getCondition();
            tv.setText(" " + condition + " ");
            row.addView(tv);
            tv = new TextView(context);
            tv.setText(" " + intArrayToStr(in.getCode()) + " ");
            row.addView(tv);
            rows.add(row);

            for (int i = 0; i < tFunctions.length; i++){
                int result = in.getCode()[i];
                if (result == 1) {
                    tFunctions[i].addCode(out.getCode());
                    condition = condition.trim();
                    if (condition.length() != 0) {
                        List<Object[]> conditions = parseCondition(condition);
                        for (Object[] cond : conditions)
                            tFunctions[i].addCondition((String) cond[0], (String) cond[1]);
                    }
                    tFunctions[i].addResult(result);
                }
            }
        }
    }

    public List<Function> getFunctions(){
        List<Function> functions = new ArrayList<>();
        for (Function f : tFunctions) {
            f.deleteRepeat();
            functions.add(f);
        }
        for (Function f : yFunctions.values()) {
            f.deleteRepeat();
            functions.add(f);
        }
        return functions;
    }
}
