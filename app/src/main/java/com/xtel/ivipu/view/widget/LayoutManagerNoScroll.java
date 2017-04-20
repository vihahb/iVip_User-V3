package com.xtel.ivipu.view.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Vulcl on 4/20/2017
 */

public class LayoutManagerNoScroll extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public LayoutManagerNoScroll(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}