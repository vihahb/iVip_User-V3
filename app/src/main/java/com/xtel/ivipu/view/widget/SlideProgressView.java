package com.xtel.ivipu.view.widget;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.view.MyApplication;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

/**
 * Created by Lê Công Long Vũ on 11/1/2016
 */

public class SlideProgressView {
    private SwipeRefreshLayout swipeRefreshLayout;
    private DiscreteScrollView scrollView;
    private TextView txt_total_point, txt_now_point;

    private LinearLayout layout_message;
    private ImageView imageView;
    private TextView textView_data;

    public SlideProgressView(Activity activity, View view) {
        if (view == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.member_card_swipe);
            layout_message = (LinearLayout) activity.findViewById(R.id.member_card_header_layout_message);

            scrollView = (DiscreteScrollView) activity.findViewById(R.id.member_card_header_list_member);
            txt_total_point = (TextView) activity.findViewById(R.id.member_card_header_txt_total_point);
            txt_now_point = (TextView) activity.findViewById(R.id.member_card_header_txt_now_point);

            imageView = (ImageView) activity.findViewById(R.id.member_card_header_img_message);
            textView_data = (TextView) activity.findViewById(R.id.member_card_header_txt_message);
        } else {
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.member_card_swipe);
            layout_message = (LinearLayout) view.findViewById(R.id.member_card_header_layout_message);

            scrollView = (DiscreteScrollView) view.findViewById(R.id.member_card_header_list_member);
            txt_total_point = (TextView) view.findViewById(R.id.member_card_header_txt_total_point);
            txt_now_point = (TextView) view.findViewById(R.id.member_card_header_txt_now_point);

            imageView = (ImageView) view.findViewById(R.id.member_card_header_img_message);
            textView_data = (TextView) view.findViewById(R.id.member_card_header_txt_message);
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary);
    }

    public void setUpRecyclerView(RecyclerView.Adapter adapter) {
        scrollView.setAdapter(adapter);
        scrollView.getCurrentItem();
        scrollView.scrollToPosition(0);
        scrollView.smoothScrollToPosition(0);
        scrollView.setHasFixedSize(true);
        scrollView.setItemAnimator(new DefaultItemAnimator());
        scrollView.setItemTransformer(new ScaleTransformer.Builder().setMinScale(0.8f).setMaxScale(1.05f).setPivotX(Pivot.X.CENTER).setPivotY(Pivot.Y.CENTER).build());
    }

    public void scrollToPosition(int position) {
        scrollView.scrollToPosition(position);
    }

    public void setOnItemChangedListener(DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> onScrollListener) {
        scrollView.setOnItemChangedListener(onScrollListener);
    }

    public void setValue(int totalPoint, int nowPoint) {
        WidgetHelper.getInstance().setTextViewHtml(txt_total_point, MyApplication.context.getString(R.string.total) + " <b>" + totalPoint + "</b> " + MyApplication.context.getString(R.string.point));
        WidgetHelper.getInstance().setTextViewHtml(txt_now_point, MyApplication.context.getString(R.string.total) + " <b>" + nowPoint + "</b> " + MyApplication.context.getString(R.string.point));
    }

    public void initData(int imageView, String textViewData) {
        if (imageView == -1)
            this.imageView.setVisibility(View.GONE);
        else
            this.imageView.setImageResource(imageView);

        if (textViewData == null)
            this.textView_data.setVisibility(View.GONE);
        else {
            this.textView_data.setText(textViewData);
        }
    }

    public void updateData(int imageView, String textView) {
        if (imageView == -1)
            this.imageView.setVisibility(View.GONE);
        else
            this.imageView.setImageResource(imageView);

        if (textView == null)
            this.textView_data.setVisibility(View.GONE);
        else {
            this.textView_data.setText(textView);
        }
    }

    public void showData() {
        if (scrollView.getVisibility() == View.GONE)
            scrollView.setVisibility(View.VISIBLE);
        if (txt_total_point.getVisibility() == View.GONE)
            txt_total_point.setVisibility(View.VISIBLE);
        if (txt_now_point.getVisibility() == View.GONE)
            txt_now_point.setVisibility(View.VISIBLE);
        if (layout_message.getVisibility() == View.VISIBLE)
            layout_message.setVisibility(View.GONE);
    }

    public void hideData() {
        if (scrollView.getVisibility() == View.VISIBLE)
            scrollView.setVisibility(View.INVISIBLE);
        if (txt_total_point.getVisibility() == View.VISIBLE)
            txt_total_point.setVisibility(View.INVISIBLE);
        if (txt_now_point.getVisibility() == View.VISIBLE)
            txt_now_point.setVisibility(View.INVISIBLE);
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
}