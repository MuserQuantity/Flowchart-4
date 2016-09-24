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
import com.kyiv.flowchart.block.EditBlock;
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
    private List<Block> blockList;
    public static Block editBlock;
    private Block removeBlock;

    public void drawView(Canvas canvas) {
        Paint p = new Paint();
        Iterator iterator = blockList.iterator();
        while(iterator.hasNext()) {
            Block block = (Block) iterator.next();
            if (block != null && block == removeBlock){
                iterator.remove();
                removeBlock = null;
                continue;
            }
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
        }
    }

    public DrawView(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        blockList = new ArrayList<>();
        setOnTouchListener(this);
    }

    public List<Block> getBlocks(){
        return blockList;
    }

    public void addBlock(Block block){
        blockList.add(block);
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

    public Block findBlock(float x, float y){
        for (int index = 0; index < blockList.size(); index++){
            Block block = blockList.get(index);
            if (x >= block.getX() - block.getWidth()/2 && x <= block.getX() + block.getWidth()/2 && y >= block.getY() - block.getHeight()/2 && y <= block.getY() + block.getHeight()/2)
                return block;
        }
        return null;
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
                        addBlock(rectangle);
                        MainActivity.ACTION = MainActivity.Action.MOVE;

                        editBlock(rectangle);
                        break;
                    case ADDRHOMB:
                        Rhomb rhomb = new Rhomb((int)x, (int)y, getResources().getColor(R.color.block_color), "", 20);
                        addBlock(rhomb);
                        MainActivity.ACTION = MainActivity.Action.MOVE;

                        editBlock(rhomb);
                        break;
                    case ADDROUNDRECT:
                        RoundRect roundRect = new RoundRect((int) x, (int) y, getResources().getColor(R.color.block_color), "", 20);
                        addBlock(roundRect);
                        MainActivity.ACTION = MainActivity.Action.MOVE;

                        editBlock(roundRect);
                        break;
                    case DELETE:
                        removeBlock = findBlock(x, y);
                        MainActivity.ACTION = MainActivity.Action.MOVE;
                        break;
                    case EDIT:
                        editBlock(findBlock(x, y));
                        break;
                    case MOVE:
                        touchBlock = findBlock(x, y);
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
}