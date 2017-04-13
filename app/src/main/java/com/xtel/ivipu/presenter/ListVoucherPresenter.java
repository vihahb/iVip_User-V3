package com.xtel.ivipu.presenter;

import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_Voucher_list;
import com.xtel.ivipu.view.fragment.inf.IListVoucherView;
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
//    private boolean isExists = true;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if (params.length > 0) {
                if ((int) params[0] == 1) {
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
//                    StoresModel.getInstance().getListChains(TYPE, PAGE, new ResponseHandle<RESP_List_Sort_Store>(RESP_List_Sort_Store.class) {
//                        @Override
//                        public void onSuccess(RESP_List_Sort_Store obj) {
//                            if (isExists) {
//                                PAGE++;
//                                view.onGetStoresSuccess(obj.getData());
//                            }
//                        }
//
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError(Error error) {
//                            if (isExists) {
//                                if (error.getCode() == 2)
//                                    view.getNewSession(iCmd);
//                                else
//                                    view.onGetStoresError(error);
//                            }
//                        }
//                    });
            }
        }
    };

    public ListVoucherPresenter(IListVoucherView view) {
        this.view = view;
    }

    public void getVoucher(boolean isClear) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (isClear)
            PAGE = 1;

        iCmd.execute(1);
    }

//    public void setExists(boolean isExists) {
//        this.isExists = isExists;
//    }
}