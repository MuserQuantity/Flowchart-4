package com.kyiv.flowchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceView;

/**
 * Created by sergiynasinnyk on 19.10.2016.
 */

public abstract class DrawView extends SurfaceView{
    public DrawView(Context context) {
        super(context);
    }

    public abstract void drawView(Canvas canvas);
}
