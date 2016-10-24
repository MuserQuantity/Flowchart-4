package com.kyiv.flowchart.block;

import android.graphics.Canvas;
import android.graphics.Color;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public abstract class Block implements Serializable{
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
    private int warningColor;
    private boolean isWarning;
    private int radius;

    protected String nameNode = "";

    public abstract Point getInPoint();

    public abstract int getNumberOfOutPoint();
    public abstract boolean setLinkedInBlock(Block block);
    public abstract boolean setLinkedOutBlock(int i, Block block);
    public abstract void removeLinkedBlock(Block block);
    public abstract void draw(Canvas canvas);
    public abstract void setNameNode(String nameNode);
    public abstract Point getOutPoint(int i);
    public abstract List<Block> getLinkedInBlocks();
    public abstract HashMap<Integer, Block> getLinkedOutBlocks();

    Block(int X, int Y, int width, int height, BlockType blockType, int color, String text, int textSize, int id){
        this.X = X;
        this.Y = Y;
        this.width = width;
        this.height = height;
        this.blockType = blockType;
        this.color = color;
        this.text = text;
        this.textSize = textSize;
        warningColor = Color.RED;
        if (id > lastId) {
            this.id = id;
            lastId = id;
        }
        else {
            lastId++;
            id = lastId;
        }
    }

    protected Block(int X, int Y, int radius, BlockType blockType, int color, String text, int textSize){
        this.X = X;
        this.Y = Y;
        this.radius = radius;
        this.width = radius*2;
        this.height = this.width;
        this.blockType = blockType;
        this.color = color;
        this.text = text;
        this.textSize = textSize;
        lastId++;
        id = lastId;
        warningColor = Color.RED;
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
        warningColor = Color.RED;
    }

    public BlockType getBlockType(){
        return blockType;
    }

    public static void clear(){
        lastId = -1;
    }

    public String getNameNode(){
        return nameNode;
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getId(){
        return id;
    }

    public int getTextSize(){
        return textSize;
    }

    public void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public int getColor(){
        if (isWarning)
            return warningColor;
        return color;
    }

    public void setWarning(boolean isWarning){
        this.isWarning = isWarning;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
