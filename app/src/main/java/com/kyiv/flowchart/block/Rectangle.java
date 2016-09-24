package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends Block {
    public Rectangle(int X, int Y, int width, int height, int color, String text, int textSize) {
        super(X, Y, width, height, BlockType.RECT, color, text, textSize);
    }

    public Rectangle(int X, int Y, int color, String text, int textSize){
        super(X, Y, 100, 100, BlockType.RECT, color, text, textSize);
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
