package com.xtel.ivipu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.entity.MemberObj;
import com.xtel.ivipu.view.fragment.inf.IFragmentMemberCard;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.SharedPreferencesUtils;
import com.xtel.sdk.utils.ViewHolderHelper;

import java.util.ArrayList;

/**
 * Created by vivhp on 4/5/2017
 */

public class AdapterCard extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private IFragmentMemberCard view;
    private ArrayList<MemberObj> arrayList;
    private String User_name;

    private boolean isLoadMore = true;
    private int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public AdapterCard(IFragmentMemberCard view, ArrayList<MemberObj> arrayList) {
        this.view = view;
        this.arrayList = arrayList;
        User_name = SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_FULL_NAME);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_card, parent, false));
        } else if (viewType == TYPE_LOAD) {
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_card_load_more, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == arrayList.size())
            view.onLoadMore();

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.setData(arrayList.get(position));
            viewHolder.setItemClick(position);
        } else if (holder instanceof ViewProgressBar) {
            ViewProgressBar viewProgressBar = (ViewProgressBar) holder;
            //noinspection deprecation
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

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_Card;
        private TextView tv_user_name, tv_create_time;

        public ViewHolder(View itemView) {
            super(itemView);
            img_Card = (ImageView) itemView.findViewById(R.id.img_card_type);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_card_user_name);
            tv_create_time = (TextView) itemView.findViewById(R.id.tv_date_created);
        }

        void setData(MemberObj memberObj) {
            WidgetHelper.getInstance().setAvatarImageURL(img_Card, memberObj.getMember_card());
            WidgetHelper.getInstance().setTextViewWithResult(tv_user_name, User_name, view.getActivity().getString(R.string.message_not_update_full_name));
            WidgetHelper.getInstance().setTextViewDate(tv_create_time, view.getActivity().getString(R.string.date_create) + ": ", memberObj.getCreate_time());
        }

        void setItemClick(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onMemberItemClicked(position);
                }
            });
        }
    }

    private class ViewProgressBar extends ViewHolderHelper {
        private ProgressBar progressBar;

        ViewProgressBar(View itemView) {
            super(itemView);
            progressBar = findProgressBar(R.id.item_history_transaction_progress_bar);
        }
    }

    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }
}
