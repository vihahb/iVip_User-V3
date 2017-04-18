package com.xtel.ivipu.presenter;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_NewEntity;
import com.xtel.ivipu.model.RESP.RESP_NewsObject;
import com.xtel.ivipu.view.activity.inf.INewsInfoView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Vulcl on 4/18/2017
 */

public class NewsInfoPresenter {
    protected INewsInfoView view;

    protected ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                if (type == 1) {
                    HomeModel.getInstance().getNewsInfo((int) params[1], new ResponseHandle<RESP_NewsObject>(RESP_NewsObject.class) {
                        @Override
                        public void onSuccess(RESP_NewsObject obj) {
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
}