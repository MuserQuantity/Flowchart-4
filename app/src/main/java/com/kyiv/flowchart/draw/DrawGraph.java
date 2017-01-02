package com.kyiv.flowchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.kyiv.flowchart.R;
import com.kyiv.flowchart.graph.GraphModel;
import com.kyiv.flowchart.graph.Node;

import java.util.List;

public class DrawGraph extends DrawView implements SurfaceHolder.Callback, View.OnTouchListener {

    private Context context;
    private DrawThread drawThread;
    private GraphModel graphModel;
    private Node touchNode = null;
    public DrawGraph(Context context, GraphModel graphModel) {
        super(context);
        getHolder().addCallback(this);
        this.graphModel = graphModel;
        setOnTouchListener(this);
    }

    public void setModel(GraphModel graphModel){
        this.graphModel = graphModel;
    }

    public void drawView(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();

        p.setColor(getResources().getColor(R.color.block_color));

        List<Node> nodeList = graphModel.getNodeList();

        graphModel.drawLinks(canvas);

        for (int index = 0; index < nodeList.size(); index++) {
            Node node = nodeList.get(index);

            node.drawNode(canvas);
        }
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
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x;
                float y;
                x = event.getX();
                y = event.getY();
                touchNode = graphModel.findNode((int) x, (int) y);
                return true;
            case MotionEvent.ACTION_HOVER_MOVE:
            case MotionEvent.ACTION_MOVE:
                if (touchNode != null)
                    touchNode.setXY((int)event.getX(), (int)event.getY());
                return true;
        }
        return false;
    }
}