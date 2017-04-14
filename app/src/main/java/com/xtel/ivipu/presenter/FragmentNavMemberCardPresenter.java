package com.xtel.ivipu.presenter;

import com.xtel.ivipu.model.HomeModel;
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
    private int PAGE = 1;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            HomeModel.getInstance().getMemberCard(PAGE, new ResponseHandle<RESP_ListMember>(RESP_ListMember.class) {
                @Override
                public void onSuccess(RESP_ListMember obj) {
                    if (view.getFragment() != null) {
                        PAGE++;
                        view.onGetMemberCardSuccess(obj.getData());
                    }
                }

                @Override
                public void onError(Error error) {
                    if (view.getFragment() != null)
                        if (error.getCode() == 2)
                            view.getNewSession(iCmd);
                        else
                            view.onGetMemberCardError(error);
                }
            });
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
            PAGE = 1;

        iCmd.execute();
    }
}
