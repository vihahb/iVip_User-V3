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
//        if (session != null) {
//            if (!NetWorkInfo.isOnline(view.getActivity())) {
//                view.onNetworkDisable();
//            } else {
//                HomeModel.getInstance().getMemberCard(url_member_card, session, new ResponseHandle<RESP_ListMember>(RESP_ListMember.class) {
//                    @Override
//                    public void onSuccess(RESP_ListMember obj) {
//                        view.onGetMemberCardSuccess(obj.getData());
//                    }
//
//                    @Override
//                    public void onError(Error error) {
//                        if (error != null) {
//                            int code = error.getCode();
//                            if (String.valueOf(code) != null) {
//                                if (code == 2) {
//                                    CallbackManager.create(view.getActivity()).getNewSesion(new CallbacListener() {
//                                        @Override
//                                        public void onSuccess(RESP_Login success) {
//                                            getMemberCard(page, pagesize);
//                                        }
//
//                                        @Override
//                                        public void onError(Error error) {
//                                            view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), error.getCode(), null));
//                                            view.startActivityAndFinish(LoginActivity.class);
//                                        }
//                                    });
//                                } else {
//                                    view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), code, null));
//                                }
//
//                            } else {
//                                Log.e(TAG, "Err " + JsonHelper.toJson(error));
//                            }
//                        }
//                    }
//                });
//            }
//        } else {
//            view.startActivityAndFinish(LoginActivity.class);
//            view.showShortToast(view.getActivity().getString(R.string.need_login_to_action));
//        }
    }
}
