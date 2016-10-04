package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Rhomb extends Block {
    private int numberOfOutPoints = 2;

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
}
