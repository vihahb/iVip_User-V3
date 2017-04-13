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
import com.xtel.ivipu.presenter.RegisterPhonePresenter;
import com.xtel.ivipu.view.activity.inf.IRegisterPhoneView;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by vihahb on 1/10/2017.
 */

public class RegisterPhone extends BasicActivity implements IRegisterPhoneView, View.OnClickListener {

    private String user_name, password, re_password;
    private EditText edt_name, edt_password, edt_re_password;
    private Button btn_reg;
    private TextView tv_callback;

    private RegisterPhonePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_action);

        presenter = new RegisterPhonePresenter(this);
        presenter.createCallbackManager();
        initView();
    }

    private void initView() {
        edt_name = (EditText) findViewById(R.id.edt_user_name);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_re_password = (EditText) findViewById(R.id.edt_re_password);

        tv_callback = (TextView) findViewById(R.id.tv_callback);
        tv_callback.setOnClickListener(this);

        btn_reg = (Button) findViewById(R.id.btn_reg);

        btn_reg.setOnClickListener(this);
        WidgetHelper.getInstance().setUnderLine(getString(R.string.tv_do_not_have_an_acc), tv_callback);
    }

    /**
     * Step 2
     **/
    private boolean ValidData() {
        String mes;
        user_name = edt_name.getText().toString();
        password = edt_password.getText().toString();
        re_password = edt_re_password.getText().toString();

        if (user_name.isEmpty()) {
            showShortToast(getString(R.string.name_not_null));
            return false;
        } else if (password.isEmpty()) {
            showShortToast(getString(R.string.pass_not_null));
            return false;
        } else if (re_password.isEmpty()) {
            showShortToast(getString(R.string.please_fill_pass));
            return false;
        } else {
            mes = "OK, PHONE";
            return true;
        }

    }

    /**
     * Step 3
     **/
    private boolean checkPassword() {
        if (!re_password.equals(password)) {
            String mes = getString(R.string.please_enter_a_password);
            showShortToast(mes);
            return false;
        } else {
            String mes = getString(R.string.check_ok);
            showShortToast(mes);
            return true;
        }
    }

    /**
     * Step 4
     * if true
     * return step 1 request
     **/
    private boolean checkPhone() {
        return !(user_name.length() < 10 || user_name.length() > 11);

    }

    /**
     * Step 1
     **/
    private void checkDataInput() {
        if (ValidData()) {
            if (checkPassword()) {
                if (!checkPhone()) {
                    String mes = getString(R.string.err_phone_number_type);
                    showShortToast(mes);
                } else {
                    String mes = "Phone: " + user_name;
                    showShortToast(mes);
                    presenter.onRegisterPhone(user_name, password);
                }
            }
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(Error error) {

    }

    public void showShortToast(String mes) {
        super.showShortToast(mes);
    }

    @Override
    public void showLongToast(String message) {
        super.showLongToast(message);
    }

    @Override
    public void startActivitys(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResults(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResultsAccountKit(requestCode, resultCode, data);
    }

    @Override
    public void finishActivitys() {
        super.finishActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        WidgetHelper.getInstance().showAlertNetwork(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_reg) {
            checkDataInput();
        } else if (id == R.id.tv_callback) {
            startActivity(LoginActivity.class);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.requestPermission(requestCode, permissions, grantResults);
    }
}
