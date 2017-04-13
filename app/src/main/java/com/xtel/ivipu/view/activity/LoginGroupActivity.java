package com.xtel.ivipu.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.presenter.LoginGroupPresenter;
import com.xtel.ivipu.view.activity.inf.ILoginGroup;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.sdk.callback.DialogListener;

/**
 * Created by vihahb on 1/10/2017.
 */

public class LoginGroupActivity extends BasicActivity implements ILoginGroup, View.OnClickListener {

    String user;
    private EditText edt_user, edt_pass;
    private TextView tv_reset;
    private TextView tv_register;
    private String phone_number;


    private LoginGroupPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_group);
        presenter = new LoginGroupPresenter(this);
        presenter.createCallbackManager();
        initView();
    }

    private void initView() {
        edt_user = (EditText) findViewById(R.id.edt_login_phone);
        edt_pass = (EditText) findViewById(R.id.edt_login_password);
        Button btn_login = (Button) findViewById(R.id.btn_login_tonip);

        tv_register = (TextView) findViewById(R.id.tv_signup);
//        tv_active = (TextView) findViewById(R.id.tv_re_active);
        tv_reset = (TextView) findViewById(R.id.tv_reset);

        tv_register.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
//        tv_active.setOnClickListener(this);

        btn_login.setOnClickListener(this);
        setUnderLine();
    }

    private void setUnderLine() {
//        WidgetHelper.getInstance().setUnderLine(getString(R.string.action_active_account), tv_active);
        WidgetHelper.getInstance().setUnderLine(getString(R.string.action_recover_password), tv_reset);
        WidgetHelper.getInstance().setUnderLine(getString(R.string.tv_do_not_have_an_acc), tv_register);
    }

    /**
     * Step 1
     **/
    private void checkDataInput() {
        if (ValidData()) {
            if (!checkPhone()) {
                String mes = "Sai dinh dang so dien thoai.";
                showShortToast(mes);
            } else {
                String mes = "Phone number: " + phone_number;
                phone_number = edt_user.getText().toString();
                showShortToast(mes);
                presenter.onReactiveAccount(phone_number);
            }
        }
    }


    /**
     * Step 2
     **/
    private boolean ValidData() {
        String mes;
        phone_number = edt_user.getText().toString();
        if (phone_number.isEmpty()) {
            mes = "So dien thoai khong duoc de trong";
            showShortToast(mes);
            return false;
        } else {
            mes = "OK, PHONE";
            showShortToast(mes);
            return true;
        }

    }

    /**
     * Step 3
     * if true
     * return step 1 request
     **/
    private boolean checkPhone() {
        return !(phone_number.length() < 10 || phone_number.length() > 11);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.requestPermission(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResultAccountKit(requestCode, resultCode, data);
    }

    @Override
    public void forceActiveAccount() {
        WidgetHelper.getInstance()
                .showAlertMessage(this,
                        "Thông báo!", "Tài khoản này chưa được kích hoạt. Bạn có muốn kích hoạt tài khoản?",
                        "Kích hoạt",
                        "Hủy",
                        new DialogListener() {
                            @Override
                            public void onClicked(Object object) {
                                onReActive();
                            }

                            @Override
                            public void onCancel() {
                                showShortToast("Hủy kích hoạt");
                            }
                        });
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void showShortToast(String mes) {
        super.showShortToast(mes);
    }

    @Override
    public void startActivity(Class clazz) {
        super.startActivity(clazz);
    }

    @Override
    public void startActivityForResults(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivitys(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void finishActivity() {
        super.finishActivity();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onNetworkDisable() {
        showMaterialDialog(true, true, "Thông báo", "Kết nối thất bai.\nVui lòng kiểm tra kết nối internet.", null, "OK", new DialogListener() {
            @Override
            public void onClicked(Object object) {
                onReActive();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login_tonip) {
            onLoginToNIP();
        } else if (id == R.id.tv_reset) {
            onReset();
        }
//        else if (id == R.id.tv_re_active) {
//            onReActive();
//        }
        else if (id == R.id.tv_signup) {
            onSignup();
        }
    }

    private void onSignup() {
        startActivity(RegisterPhone.class);
    }

    private void onReActive() {
        checkDataInput();
    }

    private void onReset() {
        presenter.onRequestAccountKit(2, user);
    }

    private void onLoginToNIP() {
        user = edt_user.getText().toString();
        String pass = edt_pass.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            showShortToast(getString(R.string.please_enter_name_or_password));
        } else {
            presenter.onLoginNip(user, pass, true);
        }
    }
}
