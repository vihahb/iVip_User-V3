package com.xtel.ivipu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.entity.HistoryTransactionObj;
import com.xtel.ivipu.view.fragment.inf.IFragmentMemberCard;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.sdk.utils.ViewHolderHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 4/14/2017
 */

public class HistoryTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<HistoryTransactionObj> arrayList;
    private IFragmentMemberCard view;

    private boolean isLoadMore = true;
    private int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public HistoryTransactionAdapter(IFragmentMemberCard view, ArrayList<HistoryTransactionObj> arrayList) {
        this.arrayList = arrayList;
        this.view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW) {
            return new ViewHolderHistory(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_transaction_2, parent, false));
        } else if (viewType == TYPE_LOAD) {
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == arrayList.size())
            view.onLoadMoreHistory();

        if (holder instanceof ViewHolderHistory) {
            ViewHolderHistory viewHolderHistory = (ViewHolderHistory) holder;

            HistoryTransactionObj obj = arrayList.get(position);
            WidgetHelper.getInstance().setTextViewDate(viewHolderHistory.txt_date, "", obj.getAction_time());
            WidgetHelper.getInstance().setTextViewWithResult(viewHolderHistory.txt_content, obj.getAction_desc(), view.getActivity().getResources().getString(R.string.message_no_have_desc));
        } else {
            ViewProgressBar viewProgressBar = (ViewProgressBar) holder;
            viewProgressBar.progressBar.getIndeterminateDrawable().setColorFilter(view.getActivity().getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
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

    private class ViewHolderHistory extends ViewHolderHelper {
        private TextView txt_date, txt_point, txt_content;
        private View view;

        ViewHolderHistory(View itemView) {
            super(itemView);

            txt_date = findTextView(R.id.item_history_transaction_date);
            txt_point = findTextView(R.id.item_history_transaction_point);
            txt_content = findTextView(R.id.item_history_transaction_content);
            view = findView(R.id.item_history_transaction_status);
        }
    }

    private class ViewProgressBar extends ViewHolderHelper {
        private ProgressBar progressBar;

        ViewProgressBar(View itemView) {
            super(itemView);
            progressBar = findProgressBar(R.id.item_progress_bar);
        }
    }

    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }
}
