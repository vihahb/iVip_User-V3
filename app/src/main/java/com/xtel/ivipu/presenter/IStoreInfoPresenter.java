package com.xtel.ivipu.presenter;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_News;
import com.xtel.ivipu.model.RESP.RESP_StoreInfo;
import com.xtel.ivipu.view.activity.inf.IStoreInfoView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Vulcl on 4/20/2017
 */

public class IStoreInfoPresenter {
    protected IStoreInfoView view;
    protected RESP_News resp_news = null;

    protected ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                switch (type) {
                    case 1:
                        HomeModel.getInstance().getStoreInfo(resp_news, new ResponseHandle<RESP_StoreInfo>(RESP_StoreInfo.class) {
                            @Override
                            public void onSuccess(RESP_StoreInfo obj) {
                                view.getStoreInfoSuccess(obj);
                            }

                            @Override
                            public void onError(Error error) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.getDataError();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public IStoreInfoPresenter(IStoreInfoView view) {
        this.view = view;
    }

    public void getData() {
        try {
            resp_news = (RESP_News) view.getActivity().getIntent().getSerializableExtra(Constants.OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resp_news == null)
            view.getDataError();
        else {
            view.getDataSuccess();
            view.showProgressBar(false, false, null, view.getActivity().getString(R.string.doing_get_data));
            iCmd.execute(1);
        }
    }
}