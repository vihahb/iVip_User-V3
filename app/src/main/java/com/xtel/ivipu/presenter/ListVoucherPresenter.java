package com.xtel.ivipu.presenter;

import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_Voucher_list;
import com.xtel.ivipu.view.fragment.inf.IListVoucherView;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ListVoucherPresenter {
    private IListVoucherView view;
    private int PAGE = 1;
    private String session = LoginManager.getCurrentSession();

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            HomeModel.getInstance().getListVoucher(PAGE, new ResponseHandle<RESP_Voucher_list>(RESP_Voucher_list.class) {
                @Override
                public void onSuccess(RESP_Voucher_list obj) {
                    if (view.getFragment() != null) {
                        PAGE++;
                        view.onGetVoucherSuccess(obj.getData());
                    }
                }

                @Override
                public void onError(Error error) {
                    if (view.getFragment() != null) {
                        if (error.getCode() == 2)
                            view.getNewSession(iCmd);
                        else
                            view.onGetVoucherError(error);
                    }
                }
            });
        }
    };

    public ListVoucherPresenter(IListVoucherView view) {
        this.view = view;
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