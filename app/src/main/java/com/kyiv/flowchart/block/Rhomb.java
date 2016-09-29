package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.List;

public class Rhomb extends Block {
    private int numberOfOutPoints = 2;
    public Rhomb(int X, int Y, int width, int height, int color, String text, int textSize) {
        super(X, Y, width, height, BlockType.RHOMB, color, text, textSize);
    }

    public Rhomb(int X, int Y, int color, String text, int textSize) {
        super(X, Y, 100, 100, BlockType.RHOMB, color, text, textSize);
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
}
