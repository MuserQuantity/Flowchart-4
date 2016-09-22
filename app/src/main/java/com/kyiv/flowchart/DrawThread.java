package com.kyiv.flowchart;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {

    private boolean running = false;
    private SurfaceHolder surfaceHolder;
    private Context context;

    public DrawThread(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
    }

    public void setRunning(boolean running) {
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
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}