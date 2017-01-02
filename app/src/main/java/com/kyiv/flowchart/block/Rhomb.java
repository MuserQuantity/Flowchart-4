package com.kyiv.flowchart.block;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Rhomb extends Block implements Serializable{
    private int numberOfOutPoints = 2;
    private List<Block> linkedInBlocks;
    private HashMap<Integer, Block> linkedOutBlocks;

    public Rhomb(int X, int Y, int color, String text, int textSize) {
        super(X, Y, 100, 100, BlockType.RHOMB, color, text, textSize);
        linkedInBlocks = new ArrayList<>();
        linkedOutBlocks = new HashMap<Integer, Block>();
    }

    public Rhomb(int X, int Y, int color, String text, int textSize, int id) {
        super(X, Y, 100, 100, BlockType.RHOMB, color, text, textSize, id);
        linkedInBlocks = new ArrayList<>();
        linkedOutBlocks = new HashMap<Integer, Block>();
    }

    @Override
    public Point getInPoint(){
        return new Point(getX(), (int)(getY() - getHeight()*Math.pow(2, 0.5)/2));
    }

    @Override
    public Point getOutPoint(int i){
        switch (i){
            case 0:
                return new Point((int)(getX() - getWidth()*Math.pow(2, 0.5)/2), getY());
            case 1:
                return new Point((int)(getX() + getWidth()*Math.pow(2, 0.5)/2), getY());
        }
        return null;
    }

    @Override
    public int getNumberOfOutPoint() {
        return numberOfOutPoints;
    }

    @Override
    public boolean setLinkedInBlock(Block block) {
        linkedInBlocks.add(block);
        return true;
    }

    @Override
    public boolean setLinkedOutBlock(int i, Block block) {
        if (i >= 0 && i < 2 && linkedOutBlocks.get(i) == null) {
            linkedOutBlocks.put(i, block);
            return true;
        }
        return false;
    }

    @Override
    public void removeLinkedBlock(Block block) {
        for (int index = 0; index < linkedOutBlocks.size(); index++){
            if (linkedOutBlocks.get(index) == block)
                linkedOutBlocks.remove(index);

        }
        Iterator iter = linkedInBlocks.iterator();
        while (iter.hasNext()){
            Block b = (Block) iter.next();
            if (b == block)
                iter.remove();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(getColor());
        canvas.rotate(45, getX(), getY());
        canvas.drawRect(getX() - getWidth()/2, getY() - getHeight()/2, getX() + getWidth()/2, getY() + getHeight()/2, p);
        canvas.rotate(-45, getX(), getY());
        Point outPoint0 = getOutPoint(0);
        Point outPoint1 = getOutPoint(1);
        p.setColor(Color.rgb(218, 124, 23));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(20);
        canvas.drawText("0", outPoint0.getX(), outPoint0.getY(), p);
        canvas.drawText("1", outPoint1.getX(), outPoint1.getY(), p);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(getTextSize());
        canvas.drawText(getText(), getX(), getY(), p);
    }

    @Override
    public void setNumberNode(int numberNode) {
        throw new RuntimeException("Ромб не може бути вузлом графа!");
    }

    public List<Block> getLinkedInBlocks(){
        return linkedInBlocks;
    }

    public HashMap<Integer, Block> getLinkedOutBlocks(){
        return linkedOutBlocks;
    }

    @Override
    public int getNumberNode() {
        return -1;
    }
}
