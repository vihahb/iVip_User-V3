package com.xtel.ivipu.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Lê Công Long Vũ on 11/4/2016
 */

public class RectangleLayout extends RelativeLayout {

    public RectangleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int percent = getMeasuredWidth() / 16;
        setMeasuredDimension(getMeasuredWidth(), (percent * 9));
    }
}