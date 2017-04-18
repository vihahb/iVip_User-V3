package com.xtel.ivipu.model;

import android.util.Log;

import com.xtel.ivipu.model.RESP.RESP_Profile;
import com.xtel.ivipu.model.entity.UserInfo;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.SharedPreferencesUtils;

/**
 * Created by vivhp on 1/28/2017.
 */

public class LoginModel extends Model {
    public static LoginModel instance = new LoginModel();

    public static LoginModel getInstance() {
        return instance;
    }

    public void getUser(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getUser(ResponseHandle responseHandle) {
        String url_profile = Constants.SERVER_IVIP + Constants.GET_USER_IVIP_FULL;
        String session = LoginManager.getCurrentSession();

        Log.e("getUser", "url " + url_profile + "    session " + session);
        requestServer.getApi(url_profile, session, responseHandle);
    }

    public void saveUserInfo(RESP_Profile obj) {
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_FULL_NAME, obj.getFullname());
        SharedPreferencesUtils.getInstance().putIntValue(Constants.PROFILE_GENDER, obj.getGender());
        SharedPreferencesUtils.getInstance().putLongValue(Constants.PROFILE_BIRTH_DAY, obj.getBirthday());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_EMAIL, obj.getEmail());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_PHONE_NUM, obj.getPhonenumber());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_ADDRESS, obj.getAddress());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_AVATAR, obj.getAvatar());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_QR_CODE, obj.getQr_code());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_BAR_CODE, obj.getBar_code());
        SharedPreferencesUtils.getInstance().putIntValue(Constants.PROFILE_STATUS, obj.getStatus());
        SharedPreferencesUtils.getInstance().putIntValue(Constants.PROFILE_GENERAL_POINT, obj.getGeneral_point());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_LEVEL, obj.getLevel());
        SharedPreferencesUtils.getInstance().putLongValue(Constants.PROFILE_JOINT_DATE, obj.getJoin_date());
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_AREA_CODE, obj.getArea_code());
    }

    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();

        userInfo.setFullname(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_FULL_NAME));
        userInfo.setGender(SharedPreferencesUtils.getInstance().getIntValue(Constants.PROFILE_GENDER));
        userInfo.setBirthday(SharedPreferencesUtils.getInstance().getLongValue(Constants.PROFILE_BIRTH_DAY));
        userInfo.setEmail(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_EMAIL));
        userInfo.setPhonenumber(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_PHONE_NUM));
        userInfo.setAddress(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_ADDRESS));
        userInfo.setAvatar(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_AVATAR));
        userInfo.setQr_code(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_QR_CODE));
        userInfo.setBar_code(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_BAR_CODE));
        userInfo.setStatus(SharedPreferencesUtils.getInstance().getIntValue(Constants.PROFILE_STATUS));
        userInfo.setGeneral_point(SharedPreferencesUtils.getInstance().getIntValue(Constants.PROFILE_GENERAL_POINT));
        userInfo.setLevel(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_LEVEL));
        userInfo.setJoin_date(SharedPreferencesUtils.getInstance().getLongValue(Constants.PROFILE_JOINT_DATE));
        userInfo.setArea_code(SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_AREA_CODE));
        return userInfo;
    }

    public void postFCMKey(String url, String object, String session, ResponseHandle responseHandle) {
        requestServer.postApi(url, object, session, responseHandle);
    }
}
