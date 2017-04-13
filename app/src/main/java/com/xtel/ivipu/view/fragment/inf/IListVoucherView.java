package com.xtel.ivipu.view.fragment.inf;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.xtel.ivipu.model.entity.VoucherListObj;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 4/13/2017
 */

public interface IListVoucherView {

    void onLoadMore();
    void onGetVoucherSuccess(ArrayList<VoucherListObj> arrayList);
    void onGetVoucherError(Error error);
    void onNoNetwork();
    void getNewSession(ICmd iCmd);
    void onNotLogged();
    Fragment getFragment();
    Activity getActivity();
}
