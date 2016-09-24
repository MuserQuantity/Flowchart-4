package com.kyiv.flowchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.kyiv.flowchart.R;


 class DrawThread extends Thread {

    private boolean running = false;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private DrawView drawView;

     DrawThread(SurfaceHolder surfaceHolder, Context context, DrawView drawView) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
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
                canvas.drawColor(context.getResources().getColor(R.color.bg_surface));
                drawView.drawView(canvas);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}