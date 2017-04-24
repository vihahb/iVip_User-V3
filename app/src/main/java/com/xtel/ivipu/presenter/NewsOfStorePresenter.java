package com.xtel.ivipu.presenter;

import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_ListNews;
import com.xtel.ivipu.model.RESP.RESP_NewEntity;
import com.xtel.ivipu.model.RESP.RESP_Voucher_list;
import com.xtel.ivipu.view.activity.inf.INewsOfStoreView;
import com.xtel.ivipu.view.fragment.inf.IListVoucherView;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class NewsOfStorePresenter {
    private INewsOfStoreView view;
    private int PAGE = 1;
    private String session = LoginManager.getCurrentSession();

    private int store_id = -1;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            HomeModel.getInstance().getNewsInStore(store_id, PAGE, new ResponseHandle<RESP_ListNews>(RESP_ListNews.class) {
                @Override
                public void onSuccess(RESP_ListNews obj) {
                    if (view.getActivity() != null) {
                        PAGE++;
                        view.onGetVoucherSuccess(obj.getData());
                    }
                }

                @Override
                public void onError(Error error) {
                    if (view.getActivity() != null) {
                        if (error.getCode() == 2)
                            view.getNewSession(iCmd);
                        else
                            view.onGetVoucherError(error);
                    }
                }
            });
        }
    };

    public NewsOfStorePresenter(INewsOfStoreView view) {
        this.view = view;
    }

    public void getData() {
        try {
            store_id = view.getActivity().getIntent().getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (store_id == -1)
            view.ongetDataError();
        else
            view.ongetDataSuccess();
    }

    /**
    * Kiểm tra người dùng đã đăng nhập chưa
    * Kiểm tra kết nối internet
    * Kiểm tra lấy dữ liệu từ đầu hay tiếp tục
    * Bắt đầu lấy dữ liệu từ server
    */
    public void getVoucher(boolean isClear) {
        if (session == null) {
            view.onNotLogged();
            return;
        }

        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (isClear)
            PAGE = 1;

        iCmd.execute();
    }
}