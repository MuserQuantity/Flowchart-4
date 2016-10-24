package com.kyiv.flowchart.draw;

import android.graphics.Canvas;
import android.view.SurfaceHolder;


class DrawThread extends Thread {

     private boolean running = false;
     private SurfaceHolder surfaceHolder;
     private DrawView drawView;

     DrawThread(SurfaceHolder surfaceHolder, DrawView drawView) {
        this.surfaceHolder = surfaceHolder;
        this.drawView = drawView;
    }

     void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas == null)
                    continue;
                drawView.drawView(canvas);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}