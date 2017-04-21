package com.xtel.ivipu.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipu.model.RESP.RESP_News;
import com.xtel.ivipu.model.RESP.RESP_StoreInfo;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Vulcl on 4/20/2017
 */

public interface IStoreInfoView {

    void getDataSuccess();
    void getDataError();
    void getStoreInfoSuccess(RESP_StoreInfo obj);
//    void getStoreInfoError(Error error);

    void getNewSession(ICmd iCmd, Object... params);

    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    Activity getActivity();
}
