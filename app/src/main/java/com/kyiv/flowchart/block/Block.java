package com.kyiv.flowchart.block;

public abstract class Block {
    private int width;
    private int height;
    private int X;
    private int Y;
    private BlockType blockType;
    private int color;
    private String text;
    private int textSize;

    Block(int X, int Y, int width, int height, BlockType blockType, int color, String text, int textSize){
        this.X = X;
        this.Y = Y;
        this.width = width;
        this.height = height;
        this.blockType = blockType;
        this.color = color;
        this.text = text;
        this.textSize = textSize;
    }

    public int getTextSize(){
        return textSize;
    }

    void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public abstract Point getInPoint();
    public abstract Point getOutPoint(int i);
    public abstract int getNumberOfOutPoint();

    public BlockType getBlockType(){
        return blockType;
    }

    public int getColor(){
        return color;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }
}
