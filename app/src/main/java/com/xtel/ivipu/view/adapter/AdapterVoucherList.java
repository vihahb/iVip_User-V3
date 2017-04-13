package com.xtel.ivipu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.entity.VoucherListObj;
import com.xtel.ivipu.view.fragment.inf.IListVoucherView;
import com.xtel.ivipu.view.widget.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by vivhp on 4/5/2017
 */

public class AdapterVoucherList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IListVoucherView view;
    private ArrayList<VoucherListObj> arrayList;
    private boolean isLoadMore = false;
    private int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public AdapterVoucherList(ArrayList<VoucherListObj> arrayList, IListVoucherView view) {
        this.arrayList = arrayList;
        this.view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.v2_item_voucher, parent, false));
        } else if (viewType == TYPE_LOAD) {
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == arrayList.size()) {
            view.onLoadMore();
        }

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            VoucherListObj obj = arrayList.get(position);
            Log.e("Arr voucher adapter", arrayList.toString());

            if (obj.getState() == 0) {
                WidgetHelper.getInstance().setViewBackgroundDrawable(viewHolder.ln_state, R.drawable.background_color_unused);
            } else if (obj.getState() == 1) {
                WidgetHelper.getInstance().setViewBackgroundDrawable(viewHolder.ln_state, R.drawable.background_color_used);
            } else if (obj.getState() == 2) {
                WidgetHelper.getInstance().setViewBackgroundDrawable(viewHolder.ln_state, R.drawable.background_color_expired);
            }

            WidgetHelper.getInstance().setImageURL(viewHolder.img_voucher_banner, obj.getBanner());
        } else {
            ViewProgressBar viewProgressBar = (ViewProgressBar) holder;
            viewProgressBar.progressBar.getIndeterminateDrawable()
                    .setColorFilter(
                            view.getActivity().getResources().getColor(R.color.colorPrimary),
                            android.graphics.PorterDuff.Mode.MULTIPLY
                    );
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == arrayList.size()) {
            return TYPE_LOAD;
        } else {
            return TYPE_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        if (isLoadMore && arrayList.size() > 0) {
            return arrayList.size() + 1;
        } else {
            return arrayList.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_voucher_banner;
        private View ln_state;

        public ViewHolder(View itemView) {
            super(itemView);
            img_voucher_banner = (ImageView) itemView.findViewById(R.id.img_voucher_banner);
            ln_state = itemView.findViewById(R.id.ln_state);
        }
    }

    private class ViewProgressBar extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        ViewProgressBar(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.item_progress_bar);
        }
    }

    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }
}
