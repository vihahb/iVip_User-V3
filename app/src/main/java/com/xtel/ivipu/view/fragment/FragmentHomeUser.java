package com.xtel.ivipu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.presenter.FragmentHomeUserPresenter;
import com.xtel.ivipu.view.activity.LoginActivity;
import com.xtel.ivipu.view.activity.UserProfileActivity;
import com.xtel.ivipu.view.fragment.inf.IFragmentHomeUser;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;
import com.xtel.sdk.utils.SharedPreferencesUtils;

/**
 * Created by vivhp on 4/15/2017.
 */

public class FragmentHomeUser extends  IFragment implements IFragmentHomeUser, View.OnClickListener {

    String session = LoginManager.getCurrentSession();
    private LinearLayout ln_action_user, ln_action_notify;
    private ImageView img_avatar;
    private TextView tv_action_user, tv_badge_notification, tv_user_history, tv_history_checkin, tv_action_qr;
    private FragmentHomeUserPresenter presenter;
    public static FragmentHomeUser newInstance() {
        return new FragmentHomeUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new FragmentHomeUserPresenter(this);
        initView(view);
    }

    private void initView(View view) {

        tv_user_history = (TextView) view.findViewById(R.id.tv_action_history);
        tv_history_checkin = (TextView) view.findViewById(R.id.tv_action_history_checkin);
        tv_action_qr = (TextView) view.findViewById(R.id.tv_action_qr);
        ln_action_user = (LinearLayout) view.findViewById(R.id.ln_action_user);
        ln_action_notify = (LinearLayout) view.findViewById(R.id.ln_action_notify);

        ln_action_user.setOnClickListener(this);
        tv_action_qr.setOnClickListener(this);

        img_avatar = (ImageView) view.findViewById(R.id.img_user_avatar);
        tv_action_user = (TextView) view.findViewById(R.id.tv_action_user);
        tv_badge_notification = (TextView) view.findViewById(R.id.badge_notification);
        checkData();
    }

    /**
     * Action khi đã hoặc chưa login
     * Đã login: enable
     * Chưa login: disable
     */
    private void ActionItemDisable() {
        tv_action_qr.setVisibility(View.GONE);
        ln_action_notify.setClickable(false);
        tv_history_checkin.setClickable(false);
        tv_user_history.setClickable(false);
        ln_action_notify.setBackgroundColor(getActivity().getResources().getColor(R.color.layout_disable));
        tv_history_checkin.setBackgroundColor(getActivity().getResources().getColor(R.color.layout_disable));
        tv_user_history.setBackgroundColor(getActivity().getResources().getColor(R.color.layout_disable));
    }


    /**
     * Check session != null thì enable
     * == null thì disable
     */
    private void checkData() {
        if (session == null) {
            ActionItemDisable();
            tv_badge_notification.setVisibility(View.GONE);
        } else {
            tv_action_qr.setVisibility(View.VISIBLE);
            int badge_count_notification = SharedPreferencesUtils.getInstance().getIntValue(Constants.NOTIFY_VALUE);
            String avatar = SharedPreferencesUtils.getInstance().getStringValue(Constants.SORT_AVA);
            String result_name = "Xin Chào <b>" +
                    SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_FULL_NAME)
                    + "</b>";
            WidgetHelper.getInstance().setAvatarRoundImageURL(img_avatar, avatar);
            WidgetHelper.getInstance().setTextViewFromHtml(tv_action_user, result_name);
            if (badge_count_notification != 0 && badge_count_notification > 0) {
                tv_badge_notification.setVisibility(View.VISIBLE);
                WidgetHelper.getInstance().setTextViewNoResult(tv_badge_notification, String.valueOf(badge_count_notification));
            } else if (badge_count_notification > 99) {
                tv_badge_notification.setVisibility(View.VISIBLE);
                WidgetHelper.getInstance().setTextViewNoResult(tv_badge_notification, "99");
            }
        }
    }

    /**
     * Check network
     * */
    private void checkNetwork(int type){
        final Context context = getContext();
        if (!NetWorkInfo.isOnline(context)) {
            WidgetHelper.getInstance().showAlertNetwork(context);
        } else {
            if (type == 1){
                initQrCode();
            }
        }
    }

    /**
     * Show Qr Code
     * */
    private void initQrCode(){
        String qr_code = SharedPreferencesUtils.getInstance().getStringValue(Constants.PROFILE_QR_CODE);
        presenter.showQrCode(qr_code);
    }


    @Override
    public void getShortUser(String avatar) {

    }

    @Override
    public void showShortToast(String mes) {

    }

    @Override
    public void onShowQrCode(String url_qr) {
        if (NetWorkInfo.isOnline(getContext())) {
            showQrCode(url_qr);
        } else {
            showShortToast(getString(R.string.no_connection));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ln_action_user) {
            if (session != null) {
                startActivity(UserProfileActivity.class);
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (id == R.id.tv_action_qr){
            checkNetwork(1);
        }
    }
}
