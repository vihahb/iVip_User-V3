package com.xtel.ivipu.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_StoreInfo;
import com.xtel.ivipu.view.activity.StoreOnMapActivity;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ViewHolderHelper;

/**
 * Created by Vulcl on 4/20/2017
 */

public class StoreInfoAddressAdapter extends RecyclerView.Adapter<StoreInfoAddressAdapter.ViewHolder> {
    protected Activity activity;
    protected RESP_StoreInfo resp_storeInfo;
//    protected ArrayList<Address> arrayList;

    public StoreInfoAddressAdapter(Activity activity, RESP_StoreInfo resp_storeInfo) {
        this.activity = activity;
        this.resp_storeInfo = resp_storeInfo;
//        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_info_address, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt_address.setText(resp_storeInfo.getAddress().get(position).getAddress());

        holder.txt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StoreOnMapActivity.class);
                intent.putExtra(Constants.OBJECT, resp_storeInfo);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resp_storeInfo.getAddress().size();
    }

    public class ViewHolder extends ViewHolderHelper {
        private TextView txt_address;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_address = findTextView(R.id.item_store_info_txt_address);
        }
    }
}