package com.kyiv.flowchart.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.kyiv.flowchart.block.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinkGraph implements Serializable{
    private Node out;
    private Node in;
    private int color;
    private String condition;
    private int mudslide = 0;


    public LinkGraph(String condition){
        this.condition = condition;

        Random random = new Random();
        color = Color.BLUE;
    }

    public void setMudslide(int mudslide){
        this.mudslide = mudslide;
    }

    public String getCondition() {
        return condition;
    }

    public double getDistance(Point point1, Point point2){
        return Math.sqrt((point1.getX() - point2.getX())*(point1.getX() - point2.getX()) + ((point1.getY() - point2.getY())*(point1.getY() - point2.getY())));
    }

    public List<Point> getCircleLineIntersection(Point circle, float radius, float a, float b){
        //(x - circle.getX())^2 + (y - circle.getY())^2 = radius^2
        //ax + b = y
        List<Point> result = new ArrayList<>();
        float A = 1 + a*a;
        float B = 2*(a*b - a*circle.getY() - circle.getX());
        float C = circle.getX()*circle.getX() + b*b + circle.getY()*circle.getY() - 2*b*circle.getY() - radius*radius;

        double D = B*B - 4*A*C;
        if (D > 0){
            double x1 = (-B + Math.sqrt(D))/(2*A);
            double x2 = (-B - Math.sqrt(D))/(2*A);

            double y1 = a*x1 + b;
            double y2 = a*x2 + b;

            Point point1 = new Point((int)x1, (int)y1);
            result.add(point1);
            Point point2 = new Point((int)x2, (int)y2);
            result.add(point2);
        }
        else if (D == 0){
            double x = (-B + Math.sqrt(D))/(2*A);
            double y = a*x + b;
            result.add(new Point((int)x, (int)y));
        }
        return result;
    }

    private void drawLinkAndArrow(Canvas canvas, Point outPoint, Point inPoint){

        //y = a*x + b
        float a = (outPoint.getY() - inPoint.getY())/(outPoint.getX() - inPoint.getX());
        float b = (inPoint.getY()*outPoint.getX() - inPoint.getX()*outPoint.getY())/(outPoint.getX() - inPoint.getX()) + mudslide;
        List<Point> points = getCircleLineIntersection(inPoint, Node.getRadius(), a, b);
        if(points.size() != 0) {
            double length = Double.MAX_VALUE;
            Point resPoint = null;
            for (Point point : points) {
                double distance = getDistance(inPoint, point);
                double l = Math.sqrt((outPoint.getX() - point.getX()) * (outPoint.getX() - point.getX()) + (outPoint.getY() - point.getY()) * (outPoint.getY() - point.getY()));
                if (l < length) {
                    length = l;
                    resPoint = point;
                }
            }
            Paint paint = new Paint();
            paint.setColor(getColor());
            outPoint.setY(outPoint.getY() + mudslide);
            canvas.drawLine(outPoint.getX(), outPoint.getY(), resPoint.getX(), resPoint.getY(), paint);
            points = getCircleLineIntersection(resPoint, Node.getRadius(), a, b);
            Point arrowPoint = null;
            for (Point point : points){
                if ((point.getX() >= outPoint.getX() & point.getX() <= resPoint.getX()) | (point.getX() >= resPoint.getX() & point.getX() <= outPoint.getX()))
                    if((point.getY() >= outPoint.getY() & point.getY() <= resPoint.getY()) | (point.getY() >= resPoint.getY() & point.getY() <= outPoint.getY()))
                        arrowPoint = point;
            }
            if(arrowPoint != null) {
                float A = -1 / a;
                float B = arrowPoint.getY() - A * arrowPoint.getX();
                points = getCircleLineIntersection(arrowPoint, 10, A, B);
                for (Point point : points)
                    canvas.drawLine(resPoint.getX(), resPoint.getY(), point.getX(), point.getY(), paint);
            }
            paint.setTextSize(20);
            canvas.drawText(getCondition(), (outPoint.getX() + resPoint.getX())/2, (outPoint.getY() + resPoint.getY())/2 + mudslide, paint);
        }
    }

    public void draw(Canvas canvas){

        Node in = getInNode();
        Point inPoint = getInPoint();
        Node out = getOutNode();
        Point outPoint = getOutPoint();

        Paint p = new Paint();
        Random random = new Random();
        p.setColor(getColor());

        drawLinkAndArrow(canvas, outPoint, inPoint);
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setOutNode(Node out){
        this.out = out;
    }

    public void setInNode(Node in){
        this.in = in;
    }

    public Node getOutNode(){
        return out;
    }

    public Node getInNode(){
        return in;

    }

    public Point getOutPoint(){
        return out.getCenter();
    }

    public Point getInPoint(){
        return in.getCenter();
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor(){
        return color;
    }
}