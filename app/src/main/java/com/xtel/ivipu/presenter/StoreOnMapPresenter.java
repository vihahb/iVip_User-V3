package com.xtel.ivipu.presenter;

import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xtel.ivipu.R;
import com.xtel.ivipu.model.HomeModel;
import com.xtel.ivipu.model.RESP.RESP_Router;
import com.xtel.ivipu.model.RESP.RESP_StoreInfo;
import com.xtel.ivipu.model.entity.Steps;
import com.xtel.ivipu.view.activity.inf.IStoreOnMapView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vulcl on 4/21/2017
 */

public class StoreOnMapPresenter {
    private IStoreOnMapView view;
    private RESP_StoreInfo resp_storeInfo;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if (params.length > 0) {
                switch ((int) params[0]) {
                    case 1:
                        final LatLng latLng = new LatLng((double) params[1], (double) params[2]);

                        HomeModel.getInstance().getPolyLine(latLng.latitude, latLng.longitude, (double) params[3], (double) params[4], new ResponseHandle<RESP_Router>(RESP_Router.class) {
                            @Override
                            public void onSuccess(final RESP_Router obj) {
                                if (obj != null) {

                                    PolylineOptions polylineOptions = getPolylineOption(obj.getRoutes().get(0).getLegs().get(0).getSteps());

                                    if (polylineOptions != null)
                                        view.onGetPolylineSuccess(latLng, polylineOptions);
                                    else
                                        view.onGetPolyLineError();
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                view.onGetPolyLineError();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public StoreOnMapPresenter(IStoreOnMapView view) {
        this.view = view;
    }

    public void getData() {
        try {
            resp_storeInfo = (RESP_StoreInfo) view.getActivity().getIntent().getSerializableExtra(Constants.OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resp_storeInfo != null) {
            view.onGetDataSuccess();
        } else {
            view.onGetDataError();
        }
    }

    public void getStoreList() {
        view.onGetStoreList(resp_storeInfo);
    }

    public void direction(double from_lat, double from_lng, double to_lat, double to_lng) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        view.showProgressBar(false, false, null, view.getActivity().getString(R.string.doing_load_data));
        iCmd.execute(1, from_lat, from_lng, to_lat, to_lng);
    }

    private PolylineOptions getPolylineOption(ArrayList<Steps> steps) {
        try {
            PolylineOptions polylineOptions = new PolylineOptions();

            for (int i = 0; i < steps.size(); i++) {
                List<LatLng> poly = Constants.decodePoly(steps.get(i).getPolyline().getPoints());

                for (int j = 0; j < poly.size(); j++) {
                    polylineOptions.add(poly.get(j));
                }
            }

            return polylineOptions;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
