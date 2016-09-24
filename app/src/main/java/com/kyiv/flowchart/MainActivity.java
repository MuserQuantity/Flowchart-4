package com.kyiv.flowchart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kyiv.flowchart.block.Rectangle;
import com.kyiv.flowchart.block.Rhomb;
import com.kyiv.flowchart.block.RoundRect;
import com.kyiv.flowchart.draw.DrawView;

public class MainActivity extends Activity {

    public enum Action{
        ADDRECT, ADDROUNDRECT, ADDRHOMB, DELETE, EDIT, MOVE
    }
    public static Action ACTION = Action.MOVE;
    private DrawView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        drawView = new DrawView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(drawView, layoutParams);

        Rectangle rectangle = new Rectangle(100, 100, getResources().getColor(R.color.block_color), "x += 1", 20);
        drawView.addBlock(rectangle);
        Rhomb rhomb = new Rhomb(300, 300, getResources().getColor(R.color.block_color), "x >= 0", 20);
        drawView.addBlock(rhomb);
        RoundRect roundRect = new RoundRect(200, 100, getResources().getColor(R.color.block_color), "x >= 0", 20);
        drawView.addBlock(roundRect);
    }

    public void addRect(View view){
        ACTION = Action.ADDRECT;
    }

    public void addRoundRect(View view){
        ACTION = Action.ADDROUNDRECT;
    }

    public void addRhomb(View view){
        ACTION = Action.ADDRHOMB;
    }

    public void editBlock(View view){
        ACTION = Action.EDIT;
    }

    public void deleteBlock(View view){
        ACTION = Action.DELETE;
    }
}