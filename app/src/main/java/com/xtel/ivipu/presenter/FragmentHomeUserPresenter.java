package com.xtel.ivipu.presenter;

import com.xtel.ivipu.view.fragment.inf.IFragmentHomeUser;

/**
 * Created by vivhp on 4/15/2017.
 */

public class FragmentHomeUserPresenter {

    private IFragmentHomeUser view;

    public FragmentHomeUserPresenter(IFragmentHomeUser view) {
        this.view = view;
    }

    public void showQrCode(String url_qr) {
        if (url_qr != null) {
            view.onShowQrCode(url_qr);
        }
    }
}
