package com.xtel.ivipu.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Lê Công Long Vũ on 11/4/2016
 */

public class RectangleLayoutSmall extends FrameLayout {

    public RectangleLayoutSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int percent = getMeasuredWidth() / 16;
//        int width = (int) ((percent * 16) * 0.7);
//        int height = (int) ((percent * 12) * 0.7);

        setMeasuredDimension((int) ((percent * 16) * 0.7), ((percent * 10)));
    }
}