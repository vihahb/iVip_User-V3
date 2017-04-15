package com.xtel.ivipu.presenter;

import android.util.Log;

import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_ListHistoryTransaction;
import com.xtel.ivipu.model.RESP.RESP_ListMember;
import com.xtel.ivipu.view.fragment.inf.IFragmentMemberCard;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.NetWorkInfo;

/**
 * Created by vuhavi on 07/03/2017
 */

public class FragmentNavMemberCardPresenter {
    private IFragmentMemberCard view;

    private String session = LoginManager.getCurrentSession();
    private int PAGE_MEMBER = 1, PAGE_HISTORY = 1;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                switch ((int) params[0]) {
                    case 1:
                        HomeModel.getInstance().getMemberCard(PAGE_MEMBER, new ResponseHandle<RESP_ListMember>(RESP_ListMember.class) {
                            @Override
                            public void onSuccess(RESP_ListMember obj) {
                                if (view.getFragment() != null) {
                                    PAGE_MEMBER++;
                                    view.onGetMemberCardSuccess(obj.getData());
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                if (view.getFragment() != null)
                                    if (error.getCode() == 2)
                                        view.getNewSession(iCmd, params);
                                    else
                                        view.onGetMemberCardError(error);
                            }
                        });
                        break;
                    case 2:
                        HomeModel.getInstance().getHistoryTransaction((int) params[1], PAGE_HISTORY, new ResponseHandle<RESP_ListHistoryTransaction>(RESP_ListHistoryTransaction.class) {
                            @Override
                            public void onSuccess(RESP_ListHistoryTransaction obj) {
                                if (view.getFragment() != null) {
                                    PAGE_MEMBER++;
                                    view.onGetHistorySuccess(obj.getData());
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                if (view.getFragment() != null)
                                    if (error.getCode() == 2)
                                        view.getNewSession(iCmd, params);
                                    else
                                        view.onGetHistoryError(error);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public FragmentNavMemberCardPresenter(IFragmentMemberCard view) {
        this.view = view;
    }

    /**
    * Kiểm tra người dùng đã đăng nhập chưa
    * Kiểm tra kết nối internet
    * Kiểm tra lấy dữ liệu từ đầu hay tiếp tục
    * Bắt đầu lấy dữ liệu từ server
    */
    public void getMemberCard(boolean isClear) {
        if (session == null) {
            view.onNotLogged();
            return;
        }

        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNetworkDisable();
            return;
        }

        if (isClear)
            PAGE_MEMBER = 1;

        iCmd.execute(1);
    }

    /**
     * Kiểm tra người dùng đã đăng nhập chưa
     * Kiểm tra kết nối internet
     * Kiểm tra lấy dữ liệu từ đầu hay tiếp tục
     * Bắt đầu lấy dữ liệu từ server
     */
    public void getHistory(boolean isClear, int id) {
        Log.e("getHistory", "ok " + id);
        if (session == null) {
            view.onNotLogged();
            return;
        }

        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNetworkDisable();
            return;
        }

        if (isClear)
            PAGE_HISTORY = 1;

        iCmd.execute(2, id);
    }
}