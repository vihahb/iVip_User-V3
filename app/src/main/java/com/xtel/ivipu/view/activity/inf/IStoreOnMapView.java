package com.xtel.ivipu.view.activity.inf;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xtel.ivipu.model.RESP.RESP_NewsObject;
import com.xtel.ivipu.model.RESP.RESP_StoreInfo;
import com.xtel.ivipu.model.entity.Address;
import com.xtel.ivipu.model.entity.Shop_Address;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 4/21/2017
 */

public interface IStoreOnMapView {

    void onGetDataSuccess();
    void onGetStoreList(RESP_StoreInfo resp_storeInfo);
    void onGetPolylineSuccess(LatLng latLng, PolylineOptions polylineOptions);

    void onGetDataError();
    void onGetPolyLineError();

    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    void onNoInternet();
    Activity getActivity();
}