package com.xtel.ivipu.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.entity.CityCodeObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivhp on 4/19/2017.
 */

public class SpinnerAdapter extends ArrayAdapter {
    private Activity activity;
    private int resource;
    private ArrayList<CityCodeObj> arrayList;
    private LayoutInflater inflater;

    public SpinnerAdapter(@NonNull Activity activity, @LayoutRes int resource, @NonNull ArrayList<CityCodeObj> arrayList) {
        super(activity, resource, arrayList);
        this.resource = resource;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        if (row == null){
            inflater = activity.getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
        }

        TextView tv_code = (TextView) row.findViewById(R.id.tv_area_code);
        CityCodeObj cityCodeObj = arrayList.get(position);
        TextView tv_name = (TextView) row.findViewById(R.id.tv_area_name);

        //Set value Item
        tv_code.setText(cityCodeObj.getArea_code());
        tv_name.setText(cityCodeObj.getArea_name());
        return row;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null){
            inflater = activity.getLayoutInflater();
            row = inflater.inflate(R.layout.item_spinner_custom_dropdown, parent, false);
        }

        CityCodeObj cityCodeObj = arrayList.get(position);
        TextView tv_code = (TextView) row.findViewById(R.id.tv_area_code);
        TextView tv_name = (TextView) row.findViewById(R.id.tv_area_name);

        //Set value Item
        tv_code.setText(cityCodeObj.getArea_code());
        tv_name.setText(cityCodeObj.getArea_name());
        return row;
    }

    @Nullable
    @Override
    public CityCodeObj getItem(int position) {
        return arrayList.get(position);
    }
}
