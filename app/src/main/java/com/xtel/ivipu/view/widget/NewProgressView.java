package com.xtel.ivipu.view.widget;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtel.ivipu.R;

/**
 * Created by Lê Công Long Vũ on 11/1/2016
 */

public class NewProgressView {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout layout_message;
    private ImageView img_message;
    private TextView txt_message;

    public NewProgressView(Activity activity, View view) {
        if (view == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.progressview_swipe);
            recyclerView = (RecyclerView) activity.findViewById(R.id.progressview_recyclerview);
            layout_message = (LinearLayout) activity.findViewById(R.id.progressview_layout_message);
            img_message = (ImageView) activity.findViewById(R.id.progressview_img_message);
            txt_message = (TextView) activity.findViewById(R.id.progressview_txt_message);
        } else {
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.progressview_swipe);
            recyclerView = (RecyclerView) view.findViewById(R.id.progressview_recyclerview);
            layout_message = (LinearLayout) view.findViewById(R.id.progressview_layout_message);
            img_message = (ImageView) view.findViewById(R.id.progressview_img_message);
            txt_message = (TextView) view.findViewById(R.id.progressview_txt_message);
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary);
    }

    public void setUpRecyclerView(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onScrollRecyclerview(RecyclerOnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public void refreshList() {
        recyclerView.refreshDrawableState();
    }

    public void updateMessage(int imageView, String textView) {
        if (imageView == -1)
            this.img_message.setVisibility(View.GONE);
        else
            this.img_message.setImageResource(imageView);

        if (textView == null)
            this.txt_message.setVisibility(View.GONE);
        else {
            this.txt_message.setText(textView);
        }
    }

    public void showData() {
        if (recyclerView.getVisibility() == View.GONE)
            recyclerView.setVisibility(View.VISIBLE);
        if (layout_message.getVisibility() == View.VISIBLE)
            layout_message.setVisibility(View.GONE);
    }

    public void hideData() {
        if (recyclerView.getVisibility() == View.VISIBLE)
            recyclerView.setVisibility(View.GONE);
        if (layout_message.getVisibility() == View.GONE)
            layout_message.setVisibility(View.VISIBLE);
    }

    public void onLayoutClicked(View.OnClickListener onClickListener) {
        layout_message.setOnClickListener(onClickListener);
    }

    public void onRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public void setRefreshing(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    public void onSwipeLayoutPost(Runnable runnable) {
        swipeRefreshLayout.post(runnable);
    }

    public void setSwipeEnable(boolean isEnable) {
        swipeRefreshLayout.setEnabled(isEnable);
    }

    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}