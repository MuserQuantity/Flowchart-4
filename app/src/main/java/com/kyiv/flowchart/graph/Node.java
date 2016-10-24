package com.kyiv.flowchart.graph;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.kyiv.flowchart.R;
import com.kyiv.flowchart.block.Block;
import com.kyiv.flowchart.block.BlockType;
import com.kyiv.flowchart.block.Link;
import com.kyiv.flowchart.block.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node{
    private List<Node> linkedOutNodes;
    private List<LinkGraph> links;
    private int X;
    private int Y;
    private static int radius;
    private int color;
    private String nameNode;
    private String operators;
    private int textSize;

    public Node(int X, int Y, int radius, int color, String nameNode, String operators, int textSize) {
        this.X = X;
        this.Y = Y;
        linkedOutNodes = new ArrayList<>();
        links = new ArrayList<>();
        this.radius = radius;
        this.color = color;
        this.nameNode = nameNode;
        this.operators = operators;
        this.textSize = textSize;
    }

    public void setXY(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getX(){
        return X;
    }

    public int getY(){
        return Y;
    }

    public static int getRadius(){
        return radius;
    }

    public Point getCenter() {
        return new Point(getX(), getY());
    }

    public List<Node> getLinkedOutNodes() {
        return linkedOutNodes;
    }

    public LinkGraph getLinkWith(Node node){
        for (LinkGraph link : links)
            if (link.getInNode() == node)
                return link;
        return null;
    }

    public boolean setLinkedOutNode(String condition, Node node) {
        Log.d("graphCondition", this.getNameNode() + "->" + node.getNameNode() + " = " + condition);
        linkedOutNodes.add(node);
        LinkGraph newLink = new LinkGraph(condition);
        LinkGraph l;
        if((l = node.getLinkWith(this)) != null){
            l.setMudslide(-10);
            l.setColor(Color.RED);
            newLink.setMudslide(+10);
        }
        newLink.setInNode(node);
        newLink.setOutNode(this);
        links.add(newLink);

        return true;
    }

    public int getColor(){
        return color;
    }

    public int getTextSize(){
        return textSize;
    }

    public String getNameNode(){
        return nameNode;
    }

    public String getOperators(){
        return operators;
    }

    public void drawLinks(Canvas canvas){
        for(LinkGraph link : links)
            link.draw(canvas);
    }

    public void drawNode(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(getColor());
        canvas.drawCircle(getX(), getY(), getRadius(), p);
        p.setColor(Color.RED);
        canvas.drawLine(getX() - getRadius(), getY(), getX() + getRadius(), getY(), p);
        p.setColor(Color.rgb(218, 124, 23));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(getTextSize());
        canvas.drawText(getNameNode(), getX(), getY() - getRadius()/2, p);
        canvas.drawText(getOperators(), getX(), getY() + getRadius()/2, p);
    }


    public void setOperators(String operators) {
        this.operators = operators;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setNameNode(String nameNode) {
        this.nameNode = nameNode;
    }
}
