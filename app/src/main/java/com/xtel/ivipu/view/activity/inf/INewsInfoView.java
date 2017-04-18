package com.xtel.ivipu.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipu.model.RESP.RESP_NewsObject;
import com.xtel.nipservicesdk.callback.ICmd;

/**
 * Created by Vulcl on 4/18/2017
 */

public interface INewsInfoView {

    void showProgressBar(String message);
    void onGetDataaSuccess(RESP_NewsObject obj);
    void onGetDataError(String message);

    void getNewSession(ICmd iCmd, Object... params);

    Activity getActivity();
}