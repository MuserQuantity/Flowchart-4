package com.kyiv.flowchart;

/**
 * Created by mikola on 13.12.2016.
 */

public class Component {


    public static final String AND = "AND";
    public static final String OR  ="OR";
    public static final String NOT = "N";

    public Component(String type, Signal inFirst, Signal inSecond, Signal out) {
        this.type = type;
        this.inFirst = inFirst;
        this.inSecond = inSecond;
        this.out = out;
    }

    String type;
    Signal inFirst;
    Signal inSecond;
    Signal out;


    public Component() {

    }

    public Signal getInFirst() {

        return inFirst;
    }

    public void setInFirst(Signal inFirst) {
        this.inFirst = inFirst;
    }

    public Signal getInSecond() {
        return inSecond;
    }

    public void setInSecond(Signal inSecond) {
        this.inSecond = inSecond;
    }

    public Signal getOut() {
        return out;
    }

    public void setOut(Signal out) {
        this.out = out;
    }
}