package com.xtel.ivipu.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_Profile;
import com.xtel.ivipu.model.entity.UserInfo;
import com.xtel.ivipu.presenter.ProfilePresenter;
import com.xtel.ivipu.view.activity.inf.IProfileActivityView;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.nipservicesdk.callback.ICmd;

/**
 * Created by vivhp on 4/18/2017.
 */

public class UserProfileActivity extends BasicActivity implements IProfileActivityView {

    private ImageView img_avatar, img_cover, img_edit, img_more_action, img_action_change_date;
    private TextView tv_full_name_top, tv_time_joint, tv_total_point, tv_current_point;
    private Spinner sp_gender, sp_city;
    private EditText edt_full_name, edt_birth_day, edt_phone_number, edt_address;
    private Button btn_more_action;
    private ProfilePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_activity_profile);
        presenter = new ProfilePresenter(this);
        initView();
        initToolbar(R.id.toolbar_user, "Thông tin tài khoản");
    }

    private void initView() {
        img_avatar = (ImageView) findViewById(R.id.img_user_avatar);
        img_cover = (ImageView) findViewById(R.id.img_cover_avatar);
        img_edit = (ImageView) findViewById(R.id.img_action_edit);
        img_more_action = (ImageView) findViewById(R.id.img_user_avatar);
        img_action_change_date = (ImageView) findViewById(R.id.img_action_change_date);

        tv_full_name_top = (TextView) findViewById(R.id.tv_top_full_name);
        tv_time_joint = (TextView) findViewById(R.id.tv_time_joint);
        tv_total_point = (TextView) findViewById(R.id.tv_total_point);
        tv_current_point = (TextView) findViewById(R.id.tv_current_point);

        sp_gender = (Spinner) findViewById(R.id.sp_profile_gender);
        sp_city = (Spinner) findViewById(R.id.sp_profile_city);

        edt_full_name = (EditText) findViewById(R.id.edit_name);
        edt_birth_day = (EditText) findViewById(R.id.edit_profile_date);
        edt_phone_number = (EditText) findViewById(R.id.edit_profile_phone);
        edt_address = (EditText) findViewById(R.id.edit_profile_address);

        btn_more_action = (Button) findViewById(R.id.btn_more_action);

        presenter.getProfileData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method Override Interface
     */

    @Override
    public void showShortToast(String mes) {

    }

    @Override
    public void startActivitys(Class clazz) {

    }

    @Override
    public void startActivityAndFinish(Class clazz) {

    }

    @Override
    public void finishActivityBeforeStartActivity(Class clazz) {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void setProfileSuccess(UserInfo profile) {
        setData2Control(profile);
    }

    private void setData2Control(UserInfo profile) {
        WidgetHelper.getInstance().setAvatarImageURL(img_avatar, profile.getAvatar());
        WidgetHelper.getInstance().setImageURL(img_cover, profile.getAvatar());
        WidgetHelper.getInstance().setTextViewNoResult(tv_full_name_top, profile.getFullname());
        String result = "Thành viên từ: " + WidgetHelper.getInstance().convertLong2Time(profile.getJoin_date());
        WidgetHelper.getInstance().setTextViewNoResult(tv_time_joint, result);
        String result_total = "Tổng \n" + profile.getGeneral_point();
        WidgetHelper.getInstance().setTextViewFromHtml(tv_total_point, result_total);
        WidgetHelper.getInstance().setTextViewFromHtml(tv_current_point, result_total);

        String time_birthday = WidgetHelper.getInstance().convertLong2Time(profile.getBirthday());
        WidgetHelper.getInstance().setEditTextNoResult(edt_full_name, profile.getFullname());
        WidgetHelper.getInstance().setEditTextNoResult(edt_birth_day, time_birthday);
        WidgetHelper.getInstance().setEditTextNoResult(edt_phone_number, profile.getPhonenumber());
        WidgetHelper.getInstance().setEditTextNoResult(edt_address, profile.getAddress());

    }

    @Override
    public void getNewSession(ICmd iCmd, Object... params) {

    }

    @Override
    public void reloadProfile(RESP_Profile profile) {

    }

    @Override
    public void updateProfileSucc() {

    }

    @Override
    public void onPostPictureSuccess(String url, String server_path) {

    }

    @Override
    public void onPostPictureError(String mes) {

    }

    @Override
    public void onEnableView() {

    }

    @Override
    public void onDisableView() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onNetworkDisable() {

    }
}
