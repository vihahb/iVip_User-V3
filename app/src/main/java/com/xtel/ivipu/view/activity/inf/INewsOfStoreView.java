package com.xtel.ivipu.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipu.model.RESP.RESP_NewEntity;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 4/13/2017
 */

public interface INewsOfStoreView {

    void ongetDataSuccess();
    void onGetVoucherSuccess(ArrayList<RESP_NewEntity> arrayList);
    void onGetVoucherError(Error error);
    void ongetDataError();

    void onNoNetwork();
    void getNewSession(ICmd iCmd);
    void onNotLogged();
    Activity getActivity();
}
