package com.kyiv.flowchart.block;

public class Link {
    private Block out;
    private int outIndex;
    private Block in;

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

    public Block getInBlock(){
        return in;
    }

    public Point getOutPoint(){
        return out.getOutPoint(outIndex);
    }

    public Point getInPoint(){
        return in.getInPoint();
    }
}