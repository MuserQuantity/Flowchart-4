package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.List;

public class RoundRect extends Block{
    private int numberOfOutPoints = 1;
    public RoundRect(int X, int Y, int width, int height, int color, String text, int textSize) {
        super(X, Y, width, height, BlockType.ROUNDRECT, color, text, textSize);
    }

    public RoundRect(int X, int Y, int color, String text, int textSize){
        super(X, Y, 100, 50, BlockType.ROUNDRECT, color, text, textSize);
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
}
