package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.List;

public class Rhomb extends Block {
    public Rhomb(int X, int Y, int width, int height, int color, String text, int textSize) {
        super(X, Y, width, height, BlockType.RHOMB, color, text, textSize);
    }

    public Rhomb(int X, int Y, int color, String text, int textSize) {
        super(X, Y, 100, 100, BlockType.RHOMB, color, text, textSize);
    }

    @Override
    public Point getInPoint(){
        return new Point(getX(), getY() - getHeight()/2);
    }

    @Override
    public List<Point> getOutPoints(){
        List<Point> out = new ArrayList<>();
        out.add(new Point(getX() - getWidth()/2, getY()));
        out.add(new Point(getX() + getWidth()/2, getY()));
        return out;
    }
}
