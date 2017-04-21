package com.xtel.sdk.commons;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.view.activity.BasicActivity;
import com.xtel.sdk.callback.DialogListener;

/**
 * Created by Vũ Hà Vi on 11/4/2016.
 */
public class NetWorkInfo {

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkGPS(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            final Dialog dialog = new Dialog(context, R.style.Theme_Transparent);
            dialog.setContentView(R.layout.dialog_material);
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            dialog.findViewById(R.id.dialog_txt_title).setVisibility(View.GONE);
            TextView tv_message = (TextView) dialog.findViewById(R.id.dialog_txt_message);
            Button btn_negative = (Button) dialog.findViewById(R.id.dialog_btn_negative);
            Button btn_positive = (Button) dialog.findViewById(R.id.dialog_btn_positive);

            tv_message.setText(context.getString(R.string.message_please_turn_on_the_gps));
            btn_negative.setText(context.getString(R.string.layout_cancel));
            btn_positive.setText(context.getString(R.string.layout_agree));

            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                }
            });

            dialog.show();

            return false;
        }

        return true;
    }
}