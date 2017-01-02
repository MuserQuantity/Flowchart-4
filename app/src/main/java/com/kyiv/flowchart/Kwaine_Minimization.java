package com.kyiv.flowchart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class Kwaine_Minimization {

    public static double count_type; //количество неминимизированных входов
    public static double count_mini; //количество минимизированных входов
    public static double block_type; //количество элементов И, ИЛИ до минимизации
    public static double block_mini; //количество элементов И, ИЛИ после минимизации

    public static final int Q_COLUMN = 1;
    public static final int CONDITION_COLUMN = 3;
    private static final int FUNCTION_COLUMN = 4;
    public static final int TRIGER_COLUMN = 5;
    public static final char X_CHAR = 'x';
    public static final char ONE_CHAR = '1';
    public static final char V_CHAR = '▼';
    public static final char AND_CHAR = '•';
    public static final String DELETED = "deleted";

    private static boolean canMerge(String term1, String term2){
        int differences = 0;
        for (int i = 0; i < term1.length(); i++){
            if (term1.charAt(i) != term2.charAt(i)){
                if (term1.charAt(i) == '-' | term2.charAt(i) == '-')
                    return false;
                differences++;
            }
        }
        return differences == 1;
    }

    public static String merge(String term1, String term2){
        String res = null;
        if (canMerge(term1, term2)){
            for (int i = 0; i < term1.length(); i++)
                if (term1.charAt(i) != term2.charAt(i))
                    res = term1.substring(0, i) + '-' + term1.substring(i+1);
        }
        return res;
    }

    public static int getCharNumber(String str, char ch){
        int n = 0;
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == ch)
                n++;
        }
        return n;
    }

    //формат: "0000v0110v10-1" нАпРиммер
    //s - строка для минимизации
    public static String minimize(String s) {
        String[] st = s.split("v"); //вычисляются все импликанты
        List<List<String[]>> cube = new ArrayList<>();
        cube.add(new ArrayList<String[]>());
        for (int i = 0; i < st.length; i++){
            int cubeNumber = getCharNumber(st[i], '-');
            while (cubeNumber >= cube.size())
                cube.add(new ArrayList<String[]>());
            cube.get(cubeNumber).add(new String[]{st[i], "0"});
        }
        int cube_number = 0;
        boolean breakCondition = false;
        while(!breakCondition){
            breakCondition = true;
            for (String[] term1 : cube.get(cube_number)){
                for (String[] term2 : cube.get(cube_number)){
                    if (!term1[0].equals(term2[0])) {
                        String mergeTerm = merge(term1[0], term2[0]);
                        if (mergeTerm != null){
                            breakCondition = false;
                            while (cube_number + 1>= cube.size())
                                cube.add(new ArrayList<String[]>());
                            cube.get(cube_number+1).add(new String[]{mergeTerm, "0"});
                        }
                    }
                }
            }
            cube_number++;
        }
        cube_number--;
        //поглинання
        while(cube_number >= 0) {
            for (String[] term1 : cube.get(cube_number)) {
                int cube_number2 = cube_number;
                while (cube_number2 >= 0) {
                    for (String[] term2 : cube.get(cube_number2)) {
                        if (!term1[1].equals("1") && !term2[1].equals("1") && term2 != term1) {
                            for (int i = 0; i < term1[0].length(); i++) {
                                term2[1] = "1";
                                if (term1[0].charAt(i) != '-' && term1[0].charAt(i) != term2[0].charAt(i))
                                    term2[1] = "0";
                            }
                        }
                    }
                    cube_number2--;
                }
            }
            cube_number--;
        }

        String function = "";
        cube_number = 0;
        while(cube_number < cube.size()) {
            for (String[] term1 : cube.get(cube_number)) {
                if (!term1[1].equals("1")) {
                    if (function.length() != 0)
                        function += 'v';
                    function += term1[0];
                }
            }
            cube_number++;
        }
        return function;
    }

    //эффективность минимизации по количеству входов
    public static double getTermEfficient() {
        return count_type / count_mini;
    }
    //эффективность минимизации по количеству элементов/блоков
    public static double getBlockEfficient() {
        return block_type / block_mini;
    }
}
