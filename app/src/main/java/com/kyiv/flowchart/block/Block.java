package com.kyiv.flowchart.block;

import java.util.HashMap;
import java.util.List;

public abstract class Block {
    private int width;
    private int height;
    private int X;
    private int Y;
    private BlockType blockType;
    private int color;
    private String text;
    private int textSize;
    private static int lastId = -1;
    private int id;

    public abstract Point getInPoint();
    public abstract Point getOutPoint(int i);
    public abstract int getNumberOfOutPoint();
    public abstract boolean setLinkedInBlock(Block block);
    public abstract boolean setLinkedOutBlock(int i, Block block);
    public abstract void removeLinkedBlock(Block block);
    List<Block> linkedInBlocks;
    HashMap<Integer, Block> linkedOutBlocks;

    public BlockType getBlockType(){
        return blockType;
    }

    public static void clear(){
        lastId = -1;
    }

    Block(int X, int Y, int width, int height, BlockType blockType, int color, String text, int textSize){
        this.X = X;
        this.Y = Y;
        this.width = width;
        this.height = height;
        this.blockType = blockType;
        this.color = color;
        this.text = text;
        this.textSize = textSize;
        lastId++;
        id = lastId;
    }

    Block(int X, int Y, int width, int height, BlockType blockType, int color, String text, int textSize, int id){
        this.X = X;
        this.Y = Y;
        this.width = width;
        this.height = height;
        this.blockType = blockType;
        this.color = color;
        this.text = text;
        this.textSize = textSize;
        if (id > lastId) {
            this.id = id;
            lastId = id;
        }
        else {
            lastId++;
            id = lastId;
        }
    }

    public int getId(){
        return id;
    }

    public int getTextSize(){
        return textSize;
    }

    void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public List<Block> getLinkedInBlocks(){
        return linkedInBlocks;
    }

    public HashMap<Integer, Block> getLinkedOutBlocks(){
        return linkedOutBlocks;
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
