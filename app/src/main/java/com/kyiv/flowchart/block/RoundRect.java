package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.List;

public class RoundRect extends Block{
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
    public List<Point> getOutPoints() {
        List<Point> out = new ArrayList<>();
        out.add(new Point(getX(), getY() + getHeight()/2));
        return out;
    }
}