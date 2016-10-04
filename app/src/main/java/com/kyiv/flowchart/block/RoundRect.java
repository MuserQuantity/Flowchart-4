package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RoundRect extends Block{
    private int numberOfOutPoints = 1;
    public RoundRect(int X, int Y, int width, int height, int color, String text, int textSize) {
        super(X, Y, width, height, BlockType.ROUNDRECT, color, text, textSize);
        linkedInBlocks = new ArrayList<>();
        linkedOutBlocks = new HashMap<Integer, Block>();
    }

    public RoundRect(int X, int Y, int color, String text, int textSize){
        super(X, Y, 100, 50, BlockType.ROUNDRECT, color, text, textSize);
        linkedInBlocks = new ArrayList<>();
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
    public void removeLinkedBlock(Block block) {
        if (linkedOutBlocks.get(0) == block)
            linkedOutBlocks.remove(0);
        Iterator iter = linkedInBlocks.iterator();
        while (iter.hasNext()){
            Block b = (Block) iter.next();
            if (b == block)
                iter.remove();
        }
    }
}
