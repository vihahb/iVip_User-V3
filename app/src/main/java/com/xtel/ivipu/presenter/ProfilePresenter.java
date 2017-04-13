package com.xtel.ivipu.presenter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.xtel.ivipu.model.LoginModel;
import com.xtel.ivipu.model.ProfileModel;
import com.xtel.ivipu.model.RESP.RESP_Profile;
import com.xtel.ivipu.model.entity.UserInfo;
import com.xtel.ivipu.view.activity.LoginActivity;
import com.xtel.ivipu.view.activity.inf.IProfileActivityView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.RequestWithStringListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;
import com.xtel.sdk.utils.Task;

/**
 * Created by vihahb on 1/13/2017.
 */

public class ProfilePresenter {
    public static final String TAG = "Profile_presenter";
    String session = LoginManager.getCurrentSession();
    private IProfileActivityView view;
    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                if (type == 1) {
                    LoginModel.getInstance().getUser(new ResponseHandle<RESP_Profile>(RESP_Profile.class) {
                        @Override
                        public void onSuccess(RESP_Profile obj) {
                            Log.d(TAG + "succ", JsonHelper.toJson(obj));
                            LoginModel.getInstance().saveUserInfo(obj);
                            view.setProfileSuccess(LoginModel.getInstance().getUserInfo());
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2) {
                                view.getNewSession(iCmd, params);
                            } else {
//                                Log.e(TAG + "err", error.getType());
                                view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), error.getCode(), ""));
                            }
                        }
                    });
                }
            }
        }
    };

    public ProfilePresenter(IProfileActivityView view) {
        this.view = view;
    }

    public void getProfileData() {
        if (session != null) {
            if (!NetWorkInfo.isOnline(view.getActivity())) {
                view.onNetworkDisable();
            } else {
                UserInfo userInfo = LoginModel.getInstance().getUserInfo();

                if (TextUtils.isEmpty(userInfo.getFullname())) {
                    iCmd.execute(1);
                } else {
                    view.setProfileSuccess(userInfo);
                }
            }
        }
    }

    public void reloadDataProfile() {
        if (session != null) {
            if (!NetWorkInfo.isOnline(view.getActivity())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.onNetworkDisable();
                    }
                }, 500);
                return;
            } else {
                String session = LoginManager.getCurrentSession();
                Log.e(TAG + "ses", session);
                String url_profile = Constants.SERVER_IVIP + Constants.GET_USER_IVIP_FULL;
                Log.e(TAG + "url", url_profile);
                LoginModel.getInstance().getUser(url_profile, session, new ResponseHandle<RESP_Profile>(RESP_Profile.class) {
                    @Override
                    public void onSuccess(RESP_Profile obj) {
                        Log.d(TAG + "succ", JsonHelper.toJson(obj));
                        view.reloadProfile(obj);
                    }

                    @Override
                    public void onError(com.xtel.nipservicesdk.model.entity.Error error) {
                        Log.e(TAG + "err", error.getMessage());
                        if (error.getCode() == 2) {
                            CallbackManager.create(view.getActivity()).getNewSesion(new CallbacListener() {
                                @Override
                                public void onSuccess(RESP_Login success) {
                                    reloadDataProfile();
                                }

                                @Override
                                public void onError(com.xtel.nipservicesdk.model.entity.Error error) {
                                    view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), error.getCode(), null));
                                    view.startActivityAndFinish(LoginActivity.class);
                                }
                            });
                        }
                        view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), error.getCode(), null));
                    }
                });
            }
        } else {
            return;
        }
    }

    public void setData(final UserInfo profile_object) {
        if (session != null) {
            if (!NetWorkInfo.isOnline(view.getActivity())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.onNetworkDisable();
                    }
                }, 500);
                return;
            } else {
                final String session = LoginManager.getCurrentSession();
                String url = Constants.SERVER_IVIP + Constants.UPDATE_USER;
                Log.e("Project user", JsonHelper.toJson(profile_object));
                ProfileModel.getInstance().onUpdateUser(url, JsonHelper.toJson(profile_object), session, new ResponseHandle<com.xtel.nipservicesdk.model.entity.RESP_None>(com.xtel.nipservicesdk.model.entity.RESP_None.class) {
                    @Override
                    public void onSuccess(com.xtel.nipservicesdk.model.entity.RESP_None obj) {
                        Log.e("respond success", "ádasdaweawe");
                        view.updateProfileSucc();
                    }

                    @Override
                    public void onError(com.xtel.nipservicesdk.model.entity.Error error) {
                        Log.e("respond err", "ádasdaweaweád");
                        if (error.getCode() == 2) {
                            CallbackManager.create(view.getActivity()).getNewSesion(new CallbacListener() {
                                @Override
                                public void onSuccess(RESP_Login success) {
                                    setData(profile_object);
                                }

                                @Override
                                public void onError(com.xtel.nipservicesdk.model.entity.Error error) {
                                    view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), error.getCode(), null));
                                    view.startActivityAndFinish(LoginActivity.class);
                                }
                            });
                        } else {
                            view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), error.getCode(), null));
                        }
                    }

                });
                reloadDataProfile();
            }
        } else {
            return;
        }
    }

    public void postImage(Bitmap bitmap) {
        if (session != null) {
            if (!NetWorkInfo.isOnline(view.getActivity())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.onNetworkDisable();
                    }
                }, 500);
                return;
            } else {
                new Task.ConvertImage(view.getActivity(), true, new RequestWithStringListener() {
                    @Override
                    public void onSuccess(String url, String server_path) {
                        Log.e("Url", url);
                        view.onPostPictureSuccess(url, server_path);
                    }

                    @Override
                    public void onError() {
                        view.onPostPictureError("Xảy ra lỗi. Vui lòng thử lại");
                    }
                }).execute(bitmap);
            }
        } else {
            return;
        }
    }
}
