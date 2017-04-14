package com.xtel.ivipu.view.fragment.inf;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xtel.ivipu.model.entity.HistoryTransactionObj;
import com.xtel.ivipu.model.entity.MemberObj;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by vuhavi on 07/03/2017
 */

public interface IFragmentMemberCard {

    void onGetMemberCardSuccess(ArrayList<MemberObj> arrayList);
    void onGetMemberCardError(Error error);
    void onGetHistorySuccess(ArrayList<HistoryTransactionObj> arrayList);
    void onGetHistoryError(Error error);

    void getNewSession(ICmd iCmd, Object... params);
    void onNetworkDisable();
    void showShortToast(String mes);
    void startActivityAndFinish(Class clazz);
    void onLoadMore();
    void onLoadMoreHistory();
    void onNotLogged();
    Fragment getFragment();
    Activity getActivity();
    Context getContext();
}
