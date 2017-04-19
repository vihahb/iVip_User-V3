package com.xtel.ivipu.presenter;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_NewEntity;
import com.xtel.ivipu.model.RESP.RESP_News;
import com.xtel.ivipu.model.RESP.RESP_Voucher;
import com.xtel.ivipu.view.activity.inf.INewsInfoView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;

/**
 * Created by Vulcl on 4/18/2017
 */

public class NewsInfoPresenter {
    private INewsInfoView view;
    private RESP_News  resp_newsObject;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                switch (type) {
                    case 1:
                        HomeModel.getInstance().getNewsInfo((int) params[1], new ResponseHandle<RESP_News>(RESP_News.class) {
                            @Override
                            public void onSuccess(RESP_News obj) {
                                resp_newsObject = obj;
                                view.onGetDataaSuccess(obj);
                            }

                            @Override
                            public void onError(Error error) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onGetDataError(view.getActivity().getString(R.string.message_can_not_get_data));
                            }
                        });
                        break;
                    case 3:
                        HomeModel.getInstance().likeNews(resp_newsObject.getId(), new ResponseHandle<RESP_None>(RESP_None.class) {
                            @Override
                            public void onSuccess(RESP_None obj) {
                                if (resp_newsObject.getFavorite() == 0)
                                    resp_newsObject.setFavorite(1);
                                else
                                    resp_newsObject.setFavorite(0);

                                view.onLikeSuccess(resp_newsObject.getFavorite());
                            }

                            @Override
                            public void onError(Error error) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError((int) params[0], error);
                            }
                        });
                        break;
                    case 4:
                        HomeModel.getInstance().getNewsVoucher(resp_newsObject.getId(), new ResponseHandle<RESP_Voucher>(RESP_Voucher.class) {
                            @Override
                            public void onSuccess(RESP_Voucher obj) {
                                view.onGetVoucherSuccess(obj);
                            }

                            @Override
                            public void onError(Error error) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError((int) params[0], error);
                            }
                        });
                        break;
                    case 5:
                        HomeModel.getInstance().rateNews(resp_newsObject.getId(), (double) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                            @Override
                            public void onSuccess(RESP_None obj) {
                                view.onRateSuccess((double) params[1]);
                            }

                            @Override
                            public void onError(Error error) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError((int) params[0], error);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public NewsInfoPresenter(INewsInfoView view) {
        this.view = view;
    }

    public void getData() {
        RESP_NewEntity resp_newEntity = null;

        try {
            resp_newEntity = (RESP_NewEntity) view.getActivity().getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resp_newEntity != null) {
            iCmd.execute(1, resp_newEntity.getId());
            view.showProgressBar(view.getActivity().getString(R.string.doing_load_data));
        } else
            view.onGetDataError(view.getActivity().getString(R.string.error_try_again));
    }

    public void likeNews() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        view.showProgressBar(view.getActivity().getString(R.string.doing_do));
        iCmd.execute(3);
    }

    public void getVoucher() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        view.showProgressBar(view.getActivity().getString(R.string.doing_get_voucher));
        iCmd.execute(4);
    }

    public void rateNews(double rate_value) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        if (rate_value == 0) {
            view.showShortToast(view.getActivity().getString(R.string.message_please_choose_star));
            return;
        }

        view.showProgressBar(view.getActivity().getString(R.string.doing_rate_news));
        iCmd.execute(5, rate_value);
    }
}