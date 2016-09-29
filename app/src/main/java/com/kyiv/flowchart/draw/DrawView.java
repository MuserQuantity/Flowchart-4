package com.kyiv.flowchart.draw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.kyiv.flowchart.MainActivity;
import com.kyiv.flowchart.R;
import com.kyiv.flowchart.block.Block;
import com.kyiv.flowchart.block.BlockType;
import com.kyiv.flowchart.block.Model;
import com.kyiv.flowchart.block.Point;
import com.kyiv.flowchart.block.EditBlock;
import com.kyiv.flowchart.block.Link;
import com.kyiv.flowchart.block.Rectangle;
import com.kyiv.flowchart.block.Rhomb;
import com.kyiv.flowchart.block.RoundRect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private Context context;
    private DrawThread drawThread;
    private Block touchBlock;
    public static Block editBlock;
    private Block removeBlock;
    private String hint = "";
    private Link newLink = null;
    private String[] listOutPoints;
    private Model model;

    public DrawView(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        model = new Model();
        setOnTouchListener(this);
    }

    public void removeBlock(Block removeBlock){
        if (removeBlock == null)
            return;
        model.removeBlockAndLinks(removeBlock);
        this.removeBlock = null;
    }



    public void drawLink(Block in, Point inPoint, Block out, Point outPoint, int outIndex, Canvas canvas){
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.block_color));
        if (outPoint.getY() > inPoint.getY()) {
            if (out.getBlockType() != BlockType.RHOMB) {
                canvas.drawLine(outPoint.getX(), outPoint.getY(), outPoint.getX(), outPoint.getY() + 10, p);
                if (outPoint.getX() <= inPoint.getX()) {
                    if (outPoint.getX() + out.getWidth() / 2 > inPoint.getX() - in.getWidth() / 2) {
                        canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, outPoint.getX() + out.getWidth() / 2 + 10, outPoint.getY() + 10, p);
                        canvas.drawLine(outPoint.getX() + out.getWidth() / 2 + 10, outPoint.getY() + 10, outPoint.getX() + out.getWidth() / 2 + 10, inPoint.getY() + in.getHeight() + 10, p);
                        canvas.drawLine(outPoint.getX() + out.getWidth() / 2 + 10, inPoint.getY() + in.getHeight() + 10, inPoint.getX() - in.getWidth() / 2 - 10, inPoint.getY() + in.getHeight() + 10, p);
                        canvas.drawLine(inPoint.getX() - in.getWidth() / 2 - 10, inPoint.getY() + in.getHeight() + 10, inPoint.getX() - in.getWidth() / 2 - 10, inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX() - in.getWidth() / 2 - 10, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    } else {
                        canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, (outPoint.getX() + out.getWidth() / 2 + inPoint.getX() - in.getWidth() / 2) / 2, outPoint.getY() + 10, p);
                        canvas.drawLine((outPoint.getX() + out.getWidth() / 2 + inPoint.getX() - in.getWidth() / 2) / 2, outPoint.getY() + 10, (outPoint.getX() + out.getWidth() / 2 + inPoint.getX() - in.getWidth() / 2) / 2, inPoint.getY() - 10, p);
                        canvas.drawLine((outPoint.getX() + out.getWidth() / 2 + inPoint.getX() - in.getWidth() / 2) / 2, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    }
                } else {
                    if (outPoint.getX() - out.getWidth() / 2 < inPoint.getX() + in.getWidth() / 2) {
                        canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, outPoint.getX() - out.getWidth() / 2 - 10, outPoint.getY() + 10, p);
                        canvas.drawLine(outPoint.getX() - out.getWidth() / 2 - 10, outPoint.getY() + 10, outPoint.getX() - out.getWidth() / 2 - 10, inPoint.getY() + in.getHeight() + 10, p);
                        canvas.drawLine(outPoint.getX() - out.getWidth() / 2 - 10, inPoint.getY() + in.getHeight() + 10, inPoint.getX() + in.getWidth() / 2 + 10, inPoint.getY() + in.getHeight() + 10, p);
                        canvas.drawLine(inPoint.getX() + in.getWidth() / 2 + 10, inPoint.getY() + in.getHeight() + 10, inPoint.getX() + in.getWidth() / 2 + 10, inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX() + in.getWidth() / 2 + 10, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    } else {
                        canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, (outPoint.getX() - out.getWidth() / 2 + inPoint.getX() + in.getWidth() / 2) / 2, outPoint.getY() + 10, p);
                        canvas.drawLine((outPoint.getX() - out.getWidth() / 2 + inPoint.getX() + in.getWidth() / 2) / 2, outPoint.getY() + 10, (outPoint.getX() - out.getWidth() / 2 + inPoint.getX() + in.getWidth() / 2) / 2, inPoint.getY() - 10, p);
                        canvas.drawLine((outPoint.getX() - out.getWidth() / 2 + inPoint.getX() + in.getWidth() / 2) / 2, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    }
                }
            } else {
                if (outIndex == 0){
                    if (outPoint.getX() > inPoint.getX() + in.getWidth()/2){
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), (outPoint.getX() + inPoint.getX() + in.getWidth()/2)/2, outPoint.getY(), p);
                        canvas.drawLine((outPoint.getX() + inPoint.getX() + in.getWidth()/2)/2, outPoint.getY(), (outPoint.getX() + inPoint.getX() + in.getWidth()/2)/2, inPoint.getY() - 10, p);
                        canvas.drawLine((outPoint.getX() + inPoint.getX() + in.getWidth()/2)/2, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    }
                    else {
                        int leftX = Math.min(inPoint.getX() - in.getWidth()/2 - 10, outPoint.getX() - 10);
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), leftX, outPoint.getY(), p);
                        canvas.drawLine(leftX, outPoint.getY(), leftX, inPoint.getY() - 10, p);
                        canvas.drawLine(leftX, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    }
                } else {
                    if (outPoint.getX() < inPoint.getX() - in.getWidth()/2){
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), (outPoint.getX() + inPoint.getX() - in.getWidth()/2)/2, outPoint.getY(), p);
                        canvas.drawLine((outPoint.getX() + inPoint.getX() - in.getWidth()/2)/2, outPoint.getY(), (outPoint.getX() + inPoint.getX() - in.getWidth()/2)/2, inPoint.getY() - 10, p);
                        canvas.drawLine((outPoint.getX() + inPoint.getX() - in.getWidth()/2)/2, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    }
                    else {
                        int rightX = Math.max(inPoint.getX() + in.getWidth()/2 + 10, outPoint.getX() + 10);
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), rightX, outPoint.getY(), p);
                        canvas.drawLine(rightX, outPoint.getY(), rightX, inPoint.getY() - 10, p);
                        canvas.drawLine(rightX, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    }
                }
            }
        }
        else{
            if (out.getBlockType() == BlockType.RHOMB){
                if (outIndex == 0){
                    if (outPoint.getX() >= inPoint.getX()){
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), inPoint.getX(), outPoint.getY(), p);
                        canvas.drawLine(inPoint.getX(), outPoint.getY(), inPoint.getX(), inPoint.getY(), p);
                    }
                    else{
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), outPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, p);
                        canvas.drawLine(outPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, inPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, p);
                        canvas.drawLine(inPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, inPoint.getX(), inPoint.getY(), p);
                    }
                }
                else{
                    if (outPoint.getX() < inPoint.getX()){
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), inPoint.getX(), outPoint.getY(), p);
                        canvas.drawLine(inPoint.getX(), outPoint.getY(), inPoint.getX(), inPoint.getY(), p);
                    }
                    else{
                        canvas.drawLine(outPoint.getX(), outPoint.getY(), outPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, p);
                        canvas.drawLine(outPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, inPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, p);
                        canvas.drawLine(inPoint.getX(), (float)(outPoint.getY() + out.getWidth()/Math.pow(2, 0.5) + inPoint.getY())/2, inPoint.getX(), inPoint.getY(), p);
                    }
                }
            }
            else {
                canvas.drawLine(outPoint.getX(), outPoint.getY(), outPoint.getX(), outPoint.getY() + 10, p);
                canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, inPoint.getX(), outPoint.getY() + 10, p);
                canvas.drawLine(inPoint.getX(), outPoint.getY() + 10, inPoint.getX(), inPoint.getY(), p);
            }
        }
    }

    public void drawView(Canvas canvas) {
        Paint p = new Paint();
        if (removeBlock != null){
            removeBlock(removeBlock);
        }

        p.setColor(getResources().getColor(R.color.block_color));
        List<Link> linkList = model.getLinkList();
        for (int index = 0; index < linkList.size(); index++){
            Link link = linkList.get(index);
            drawLink(link.getInBlock(), link.getInPoint(), link.getOutBlock(), link.getOutPoint(), link.getOutIndex(), canvas);
        }

        List<Block> blockList = model.getBlockList();
        for (int index = 0; index < blockList.size(); index++) {
            Block block = blockList.get(index);
            p.setColor(block.getColor());
            switch(block.getBlockType()) {
                case ROUNDRECT:
                    RectF rectF = new RectF(block.getX()-block.getWidth()/2, block.getY() - block.getHeight()/2, block.getX() + block.getWidth()/2, block.getY() + block.getHeight()/2);
                    canvas.drawRoundRect(rectF, 10, 10, p);
                    break;
                case RECT:
                    canvas.drawRect(block.getX() - block.getWidth()/2, block.getY() - block.getHeight()/2, block.getX() + block.getWidth()/2, block.getY() + block.getHeight()/2, p);
                    break;
                case RHOMB:
                    canvas.rotate(45, block.getX(), block.getY());
                    canvas.drawRect(block.getX() - block.getWidth()/2, block.getY() - block.getHeight()/2, block.getX() + block.getWidth()/2, block.getY() + block.getHeight()/2, p);
                    canvas.rotate(-45, block.getX(), block.getY());
                    break;
                default:
                    break;
            }
            p.setColor(Color.RED);
            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(block.getTextSize());
            canvas.drawText(block.getText(), block.getX(), block.getY(), p);

            p.setTextAlign(Paint.Align.LEFT);
            p.setTextSize(50);
            canvas.drawText(hint, 50, 50, p);
        }
    }

    public void showHint(String hint){
        this.hint = hint;
    }

    public void hideHint(){
        hint = "";
    }

    public boolean isHint(){
        return hint.equals("");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), context, this);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x;
        float y;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = motionEvent.getX();
                y = motionEvent.getY();
                switch (MainActivity.ACTION){
                    case ADDRECT:
                        Rectangle rectangle = new Rectangle((int)x, (int)y, getResources().getColor(R.color.block_color), "", 20);
                        model.addBlock(rectangle);
                        MainActivity.ACTION = MainActivity.Action.MOVE;

                        editBlock(rectangle);
                        break;
                    case ADDRHOMB:
                        Rhomb rhomb = new Rhomb((int)x, (int)y, getResources().getColor(R.color.block_color), "", 20);
                        model.addBlock(rhomb);
                        MainActivity.ACTION = MainActivity.Action.MOVE;

                        editBlock(rhomb);
                        break;
                    case ADDROUNDRECT:
                        RoundRect roundRect = new RoundRect((int) x, (int) y, getResources().getColor(R.color.block_color), "", 20);
                        model.addBlock(roundRect);
                        MainActivity.ACTION = MainActivity.Action.MOVE;

                        editBlock(roundRect);
                        break;
                    case DELETE:
                        removeBlock = model.findBlock(x, y);
                        MainActivity.ACTION = MainActivity.Action.MOVE;
                        break;
                    case EDIT:
                        editBlock(model.findBlock(x, y));
                        break;
                    case MOVE:
                        touchBlock = model.findBlock(x, y);
                        break;
                    case ADDLINK:
                        if (newLink == null){
                            Block block = model.findBlock(x, y);
                            if (block != null) {
                                showHint("Виберіть елемент 2");
                                newLink = new Link();
                                newLink.setOutBlock(block);
                                if (block.getNumberOfOutPoint() == 1) {
                                    newLink.setOutIndex(0);
                                }
                                else{
                                    listOutPoints = new String[block.getNumberOfOutPoint()];
                                    for (int i = 0; i < listOutPoints.length; i++)
                                        listOutPoints[i] = "" + i;
                                    ((MainActivity) context).startDialog(listOutPoints, MainActivity.DIALOG_OUT_POINT);
                                }
                            }
                        }else{
                            Block block = model.findBlock(x, y);
                            hideHint();
                            if (block != null){
                                newLink.setInBlock(block);
                                model.addLink(newLink);
                                newLink = null;
                                MainActivity.ACTION = MainActivity.Action.MOVE;
                            }
                        }
                        break;
                }
                return true;
            case MotionEvent.ACTION_UP:
                touchBlock = null;
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                return true;
            case MotionEvent.ACTION_HOVER_MOVE:
            case MotionEvent.ACTION_MOVE:
                if (touchBlock != null){
                    touchBlock.setX((int)motionEvent.getX());
                    touchBlock.setY((int)motionEvent.getY());
                }
                return true;
        }
        return false;
    }

    public void editBlock(Block block){
        if (block != null) {
            editBlock = block;
            Intent intent = new Intent(getContext(), EditBlock.class);
            getContext().startActivity(intent);
            MainActivity.ACTION = MainActivity.Action.MOVE;
        }
    }

    public void setLinkOutIndex(int index){
        newLink.setOutIndex(index);
    }

    public void deleteNewLink(){
        newLink = null;
        hideHint();
    }
}