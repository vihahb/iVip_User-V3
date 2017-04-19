package com.xtel.ivipu.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipu.model.RESP.RESP_News;
import com.xtel.ivipu.model.RESP.RESP_Voucher;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Vulcl on 4/18/2017
 */

public interface INewsInfoView {

    void showProgressBar(String message);
    void onGetDataaSuccess(RESP_News obj);
    void onGetDataError(String message);
    void onGetVoucherSuccess(RESP_Voucher obj);
    void onLikeSuccess(int favorite);
    void onRateSuccess(double current_rate);

    void getNewSession(ICmd iCmd, Object... params);

    void onRequestError(int type, Error error);
    void showShortToast(String message);
    void onNoInternet();
    Activity getActivity();
}