package com.xtel.ivipu.presenter;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.xtel.ivipu.model.LoginModel;
import com.xtel.ivipu.model.RESP.RESP_Short;
import com.xtel.ivipu.view.activity.HomeActivity;
import com.xtel.ivipu.view.activity.LoginActivity;
import com.xtel.ivipu.view.activity.inf.ILoginView;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.commons.Constants;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.nipservicesdk.utils.SharedUtils;
import com.xtel.sdk.commons.NetWorkInfo;
import com.xtel.sdk.utils.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by vihahb on 1/10/2017.
 */

public class LoginPresenter {


    String mess = "";
    String tokenFacebook;
    String TAG = "Login presenter ";
    private ILoginView view;
    private CallbackManager callbackManager;
    private com.xtel.nipservicesdk.CallbackManager nipCallbackManager;
    private AccessTokenTracker tokenTracker;
    private AccessToken accessToken;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        createCallBackManager();
        createNipCallbackManager();
    }

    private void createNipCallbackManager() {
        nipCallbackManager = com.xtel.nipservicesdk.CallbackManager.create(view.getActivity());
    }

    public void onRequestCallbackPermission(int requestCode, String[] permission, int[] grantResult) {
        nipCallbackManager.onRequestPermissionsResult(requestCode, permission, grantResult);
    }

    private void createCallBackManager() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mess = "Success" + loginResult.getAccessToken().getUserId();
                view.showShortToast(mess);
                tokenFacebook = loginResult.getAccessToken().getToken();
                onLoginNipAccFb(tokenFacebook);
            }

            @Override
            public void onCancel() {
                mess = "Login Cancel";
                view.showShortToast(mess);
            }

            @Override
            public void onError(FacebookException error) {
                mess = "Login Error: " + error.getMessage();
                view.showShortToast(mess);
            }
        });
    }

    public void onLoginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(view.getActivity(),
                Arrays.asList("public_profile", "email", "user_birthday"));
    }

    public void onResultFacebookLogin(int requestCode, int resultCode, Intent data) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.onNetworkDisable();
                }
            }, 500);
            return;
        } else {
            if (callbackManager.onActivityResult(requestCode, resultCode, data))
                return;
        }
    }

    public void checkAccessToken() {
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                //Đã đăng nhập, lưu token lại và làm việc sau đăng nhập
                accessToken = currentAccessToken;
            }
        };
        //If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("Get Access Token:", String.valueOf(AccessToken.getCurrentAccessToken().getToken()));
    }

    private void onLoginNipAccFb(String token) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.onNetworkDisable();
                }
            }, 500);
            return;
        } else {
            nipCallbackManager.LoginFaceook(token, new CallbacListener() {
                @Override
                public void onSuccess(RESP_Login success) {
                    view.showShortToast("Login Success");
                    String sesion = success.getSession();
                    Log.d(TAG + "session", sesion);
                    SharedUtils.getInstance().putStringValue(Constants.SESSION, sesion);
                    onGetShortUser(sesion);
                }

                @Override
                public void onError(Error error) {
                    view.showShortToast("Login Exception. Error code: " + error.getCode());
                }
            });
        }
    }

    public void onGetShortUser(final String session) {

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

                String url_profile = com.xtel.sdk.commons.Constants.SERVER_IVIP + com.xtel.sdk.commons.Constants.GET_USER_IVIP_SORT;
                Log.e(TAG + "url", url_profile);

                LoginModel.getInstance().getUser(url_profile, session, new ResponseHandle<RESP_Short>(RESP_Short.class) {
                    @Override
                    public void onSuccess(RESP_Short obj) {
                        int notification = obj.getNew_notify();
                        String full_name = obj.getFullname();
                        String avatar = obj.getAvatar();
                        Log.e("New notification", String.valueOf(notification));
                        SharedPreferencesUtils.getInstance().putIntValue(com.xtel.sdk.commons.Constants.NOTIFY_VALUE, notification);
                        SharedPreferencesUtils.getInstance().putStringValue(com.xtel.sdk.commons.Constants.SORT_AVA, avatar);
                        SharedPreferencesUtils.getInstance().putStringValue(com.xtel.sdk.commons.Constants.PROFILE_FULL_NAME, full_name);
                        view.startActivityAndFinish(HomeActivity.class);
                    }

                    @Override
                    public void onError(com.xtel.nipservicesdk.model.entity.Error error) {
                        if (error != null) {
                            int code_err = error.getCode();
                            if (code_err == 2) {
                                com.xtel.nipservicesdk.CallbackManager.create(view.getActivity()).getNewSesion(new CallbacListener() {
                                    @Override
                                    public void onSuccess(RESP_Login success) {
                                        onGetShortUser(session);
                                    }

                                    @Override
                                    public void onError(com.xtel.nipservicesdk.model.entity.Error error) {
                                        int code = error.getCode();
                                        view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), code, null));
                                        view.startActivitys(LoginActivity.class);
                                    }
                                });
                            } else {
                                view.showShortToast(JsonParse.getCodeMessage(view.getActivity(), code_err, null));
                            }
                        }
                    }
                });
            }
        } else {
            return;
        }
    }

    private String convertLong2Time(long time) {
        Date date = new Date(time * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+7"));
        String formatTime = dateFormat.format(date);
        return formatTime;
    }

    private void checkTime(long login_time, long expired_time) {
        String time = "login: " + convertLong2Time(login_time) + ", Expired: " + convertLong2Time(expired_time);
        Log.v("Time login 2 SV: ", time);
    }
}
