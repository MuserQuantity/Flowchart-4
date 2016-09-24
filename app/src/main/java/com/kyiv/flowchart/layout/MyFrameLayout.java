package com.kyiv.flowchart.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {
    public static int WITHOUT_MAX_HEIGHT_VALUE = -1;
    private int maxHeight = WITHOUT_MAX_HEIGHT_VALUE;

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (maxHeight != WITHOUT_MAX_HEIGHT_VALUE
                    && heightSize > maxHeight) {
                heightSize = maxHeight;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            }
            getLayoutParams().height = heightSize;
        } catch (Exception e) {

        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
