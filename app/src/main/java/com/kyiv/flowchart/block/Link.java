package com.kyiv.flowchart.block;

import android.graphics.Color;

import java.io.Serializable;

public class Link implements Serializable{
    private Block out;
    private int outIndex = -1;
    private Block in;
    private int color;
    private boolean isWarning = false;
    private int warningColor;

    public Link(int color){
        this.color = color;
        warningColor = Color.RED;
    }

    public void setWarning(boolean isWarning){
        this.isWarning = isWarning;
    }

    public void setOutBlock(Block out){
        this.out = out;
    }

    public void setOutIndex(int outIndex){
        this.outIndex = outIndex;
    }

    public void setInBlock(Block in){
        this.in = in;
    }

    public Block getOutBlock(){
        return out;
    }

    public int getOutIndex() {
        return outIndex;
    }

    public Block getInBlock(){
        return in;

    }

    public Point getOutPoint(){
        return out.getOutPoint(outIndex);
    }

    public Point getInPoint(){
        return in.getInPoint();
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor(){
        if (isWarning)
            return warningColor;
        return color;
    }
}