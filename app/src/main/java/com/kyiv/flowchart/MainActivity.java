package com.kyiv.flowchart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kyiv.flowchart.block.Rectangle;
import com.kyiv.flowchart.block.Rhomb;
import com.kyiv.flowchart.block.RoundRect;
import com.kyiv.flowchart.draw.DrawView;

public class MainActivity extends Activity {

    private DrawView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        drawView = new DrawView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(drawView, layoutParams);

        Rectangle rectangle = new Rectangle(100, 100, 100, 100, Color.CYAN, "x += 1");
        drawView.addBlock(rectangle);
        Rhomb rhomb = new Rhomb(300, 300, 100, 100, Color.YELLOW, "x >= 0");
        drawView.addBlock(rhomb);
        RoundRect roundRect = new RoundRect(200, 100, 100, 50, Color.YELLOW, "x >= 0");
        drawView.addBlock(roundRect);
    }
}