package com.kyiv.flowchart.draw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
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
import java.util.List;

public class DrawFlowchart extends DrawView implements SurfaceHolder.Callback, View.OnTouchListener {

    private Context context;
    private DrawThread drawThread;
    private Block touchBlock;
    private Block removeBlock;
    private String hint = "";
    private Link newLink = null;
    private Model model;
    private Block editBlock;

    public DrawFlowchart(Context context, Model model) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        this.model = model;
        setOnTouchListener(this);
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void removeBlock(Block removeBlock){
        if (removeBlock == null)
            return;
        model.removeBlockAndLinks(removeBlock);
        this.removeBlock = null;
    }



    public void drawLink(Link link, Canvas canvas){
        Block in = link.getInBlock();
        Point inPoint = link.getInPoint();
        Block out = link.getOutBlock();
        Point outPoint = link.getOutPoint();
        int outIndex = link.getOutIndex();

        Paint p = new Paint();
        p.setColor(link.getColor());
        if (outPoint.getY() > inPoint.getY()) {
            if (out.getBlockType() != BlockType.RHOMB) {
                canvas.drawLine(outPoint.getX(), outPoint.getY(), outPoint.getX(), outPoint.getY() + 10, p);
                if (outPoint.getX() <= inPoint.getX()) {
                    if (outPoint.getX() + out.getWidth() / 2 >= inPoint.getX() - in.getWidth() / 2) {
                        canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, inPoint.getX() + in.getWidth() / 2 + 10, outPoint.getY() + 10, p);
                        canvas.drawLine(inPoint.getX() + in.getWidth() / 2 + 10, outPoint.getY() + 10, inPoint.getX() + in.getWidth() / 2 + 10, inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX() + in.getWidth() / 2 + 10, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    } else {
                        float middle = ((outPoint.getX() + out.getWidth() / 2 + inPoint.getX() - in.getWidth() / 2) / 2);
                        canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, middle, outPoint.getY() + 10, p);
                        canvas.drawLine(middle, outPoint.getY() + 10, middle, inPoint.getY() - 10, p);
                        canvas.drawLine(middle, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX(), inPoint.getY() - 10, inPoint.getX(), inPoint.getY(), p);
                    }
                } else {
                    if (outPoint.getX() - out.getWidth() / 2 < inPoint.getX() + in.getWidth() / 2) {
                        canvas.drawLine(outPoint.getX(), outPoint.getY() + 10, inPoint.getX() - in.getWidth() / 2 - 10, outPoint.getY() + 10, p);
                        canvas.drawLine(inPoint.getX() - in.getWidth() / 2 - 10, outPoint.getY() + 10, inPoint.getX() - in.getWidth() / 2 - 10, inPoint.getY() - 10, p);
                        canvas.drawLine(inPoint.getX() - in.getWidth() / 2 - 10, inPoint.getY() - 10, inPoint.getX(), inPoint.getY() - 10, p);
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
                        float leftX = Math.min(inPoint.getX() - in.getWidth()/2 - 10, outPoint.getX() - 10);
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
                        float rightX = Math.max(inPoint.getX() + in.getWidth()/2 + 10, outPoint.getX() + 10);
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

    @Override
    public void drawView(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        if (removeBlock != null){
            removeBlock(removeBlock);
        }

        p.setColor(getResources().getColor(R.color.block_color));
        List<Link> linkList = model.getLinkList();

        for (int index = 0; index < linkList.size(); index++){
            Link link = linkList.get(index);
            drawLink(link, canvas);
        }

        List<Block> blockList = model.getBlockList();
        for (int index = 0; index < blockList.size(); index++) {
            Block block = blockList.get(index);

            block.draw(canvas);

            p.setColor(Color.rgb(218, 124, 23));
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

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), this);
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
                        model.changed();
                        break;
                    case ADDLINK:
                        if (newLink == null){
                            Block block = model.findBlock(x, y);
                            if (block != null) {
                                showHint("Виберіть елемент 2");
                                newLink = new Link(context.getResources().getColor(R.color.block_color));
                                newLink.setOutBlock(block);
                                if (block.getNumberOfOutPoint() == 1 | block.getNumberOfOutPoint() == -1) {
                                    newLink.setOutIndex(0);
                                }
                                else{
                                    String[] listOutPoints = new String[block.getNumberOfOutPoint()];
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
            intent.putExtra("text", block.getText());
            intent.putExtra("text_size", block.getTextSize());
            intent.putExtra("blockType", block.getBlockType().toString());
            String nameNode = block.getNameNode();
            if (nameNode != null){
                intent.putExtra("nameNode", nameNode);
            }
            ((MainActivity)getContext()).startActivityForResult(intent, MainActivity.EDIT_BLOCK);

            MainActivity.ACTION = MainActivity.Action.MOVE;
            model.changed();
        }
    }

    public void saveChangeEditBlock(String text, int textSize, String nameNode){
        if (editBlock != null){
            editBlock.setText(text);
            editBlock.setTextSize(textSize);
            if (editBlock.getBlockType() != BlockType.RHOMB)
                editBlock.setNameNode(nameNode);
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