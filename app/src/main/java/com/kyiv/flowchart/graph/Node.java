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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node implements Serializable{
    private List<Node> linkedOutNodes;
    private List<Node> linkedInNodes;
    private int X;
    private int Y;
    private static int radius;
    private int color;
    private int nodeNumber;
    private String operators;
    private int textSize;
    private int[] code;

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }
    public Node(int X, int Y, int radius, int color, int nodeNumber, String operators, int textSize) {
        this.X = X;
        this.Y = Y;
        linkedOutNodes = new ArrayList<>();
        linkedInNodes = new ArrayList<>();
        Node.radius = radius;
        this.color = color;
        this.nodeNumber = nodeNumber;
        this.operators = operators;
        this.textSize = textSize;
    }

    public void setXY(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    public void removeOutNode(Node node){
        linkedOutNodes.remove(node);
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setRadius(int radius) {
        Node.radius = radius;
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

    public void addLinkedOutNode(Node node) {
        linkedOutNodes.add(node);
    }

    public void addLinkedInNode(Node node) {
        linkedInNodes.add(node);
    }

    public List<Node> getLinkedOutNodes() {
        return linkedOutNodes;
    }

    public int getColor(){
        return color;
    }

    public int getTextSize(){
        return textSize;
    }

    public String getNameNode(){
        return "Z" + nodeNumber;
    }

    public String getOperators(){
        return operators;
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
        String codeStr = "";
        for (int i = 0; i < code.length; i++)
            codeStr += code[i];
        canvas.drawText(codeStr, getX() + getRadius(), getY() - getRadius(), p);
    }


    public void setOperators(String operators) {
        this.operators = operators;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setNumberNode(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public int getNumberNode(){
        return nodeNumber;
    }
}
