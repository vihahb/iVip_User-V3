package com.xtel.ivipu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.entity.Address;
import com.xtel.sdk.utils.ViewHolderHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 4/20/2017
 */

public class StoreInfoAddressAdapter extends RecyclerView.Adapter<StoreInfoAddressAdapter.ViewHolder> {
    protected ArrayList<Address> arrayList;

    public StoreInfoAddressAdapter(ArrayList<Address> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_info_address, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt_address.setText(arrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends ViewHolderHelper {
        private TextView txt_address;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_address = findTextView(R.id.item_store_info_txt_address);
        }
    }
}