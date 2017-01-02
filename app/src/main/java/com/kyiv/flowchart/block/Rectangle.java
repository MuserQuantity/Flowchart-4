package com.kyiv.flowchart.block;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Rectangle extends Block implements Serializable{

    private int numberOfOutPoints = 1;
    private List<Block> linkedInBlocks;
    private HashMap<Integer, Block> linkedOutBlocks;
    public Rectangle(int X, int Y, int color, String text, int textSize){
        super(X, Y, 100, 100, BlockType.RECT, color, text, textSize);
        linkedInBlocks = new ArrayList<>();// блоки, лінки яких входять в даний блок
        linkedOutBlocks = new HashMap<Integer, Block>();
    }

    public Rectangle(int X, int Y, int color, String text, int textSize, int id){
        super(X, Y, 100, 100, BlockType.RECT, color, text, textSize, id);
        linkedInBlocks = new ArrayList<>();// блоки, лінки яких входять в даний блок
        linkedOutBlocks = new HashMap<Integer, Block>();
    }

    @Override
    public Point getInPoint() {
        return new Point(getX(), getY() - getHeight()/2);
    }

    @Override
    public Point getOutPoint(int i) {
        if (i == 0)
            return new Point(getX(), getY() + getHeight()/2);
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
        if (i == 0 && linkedOutBlocks.get(i) == null) {
            linkedOutBlocks.put(i, block);
            return true;
        }
        return false;
    }

    @Override
    public void removeLinkedBlock(Block block){
        if (linkedOutBlocks.get(0) == block)
            linkedOutBlocks.remove(0);
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
        RectF rectF = new RectF(getX()-getWidth()/2, getY() - getHeight()/2, getX() + getWidth()/2, getY() + getHeight()/2);
        canvas.drawRect(rectF, p);
        p.setColor(Color.BLACK);
        canvas.drawCircle(getX() - getWidth()/2, getY(), 10, p);
        p.setColor(Color.rgb(218, 124, 23));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(getTextSize());
        canvas.drawText(getText(), getX(), getY(), p);

        canvas.drawText("Z" + getNumberNode(), getX() - getWidth()/2-30, getY(), p);
    }

    @Override
    public void setNumberNode(int numberNode) {
        this.numberNode = numberNode;
    }

    public List<Block> getLinkedInBlocks(){
        return linkedInBlocks;
    }

    public HashMap<Integer, Block> getLinkedOutBlocks(){
        return linkedOutBlocks;
    }
}
