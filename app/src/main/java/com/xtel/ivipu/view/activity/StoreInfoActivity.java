package com.xtel.ivipu.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_News;
import com.xtel.ivipu.model.RESP.RESP_StoreInfo;
import com.xtel.ivipu.presenter.IStoreInfoPresenter;
import com.xtel.ivipu.view.activity.inf.IStoreInfoView;
import com.xtel.ivipu.view.adapter.StoreInfoAddressAdapter;
import com.xtel.ivipu.view.widget.LayoutManagerNoScroll;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.sdk.callback.DialogListener;

public class StoreInfoActivity extends IActivity implements IStoreInfoView {
    protected CallbackManager callbackManager;
    protected IStoreInfoPresenter presenter;

    protected ImageView img_banner;
    protected RecyclerView recyclerView;
    protected TextView txt_store_name, txt_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        callbackManager = CallbackManager.create(this);

        presenter = new IStoreInfoPresenter(this);
        presenter.getData();
    }

    /**
     * Khởi tạo toàn bộ view
     * */
    protected void initView() {
        img_banner = findImageView(R.id.store_info_header_img_banner);
        txt_store_name = findTextView(R.id.store_info_header_txt_store_name);

        recyclerView = findRecyclerView(R.id.store_info_content_list_address);
        txt_desc = findTextView(R.id.store_info_content_txt_desc);
    }

    /**
     * Khởi tạo danh sách địa chỉ của cửa hàng
     * */
    protected void initRecyclerView(RESP_StoreInfo resp_storeInfo) {
        LayoutManagerNoScroll layoutManagerNoScroll = new LayoutManagerNoScroll(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManagerNoScroll);

        StoreInfoAddressAdapter adapter = new StoreInfoAddressAdapter(this, resp_storeInfo);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Hiển thị thông tin của cửa hàng
     * */
    protected void setStoreInfo(RESP_StoreInfo obj) {
        WidgetHelper.getInstance().setImageURL(img_banner, obj.getBanner());
        WidgetHelper.getInstance().setTextViewWithResult(txt_store_name, obj.getName(), getString(R.string.message_not_update_store_name));
        WidgetHelper.getInstance().setTextViewHtml(txt_desc, obj.getDescription());

        WidgetHelper.getInstance().setTextViewCircleLogo(txt_store_name, obj.getLogo(), false);
    }





    /**
     * Lấy dữ liệu truyền sang thành công
     * */
    @Override
    public void getDataSuccess() {
        initToolbar(R.id.store_info_toolbar, null);
        initView();
    }

    /**
     * Lấy dữ liệu truyền sang thất bại
     * */
    @Override
    public void getDataError() {
        closeProgressBar();

        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.layout_ok), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                finish();
            }

            @Override
            public void onCancel() {
                closeDialog();
                finish();
            }
        });
    }

    /**
     * Lấy info của cửa hàng thành công
     * */
    @Override
    public void getStoreInfoSuccess(RESP_StoreInfo resp_storeInfo) {
        closeProgressBar();

        setStoreInfo(resp_storeInfo);
        initRecyclerView(resp_storeInfo);
    }

//    @Override
//    public void getStoreInfoError(Error error) {
//        closeProgressBar();
//
//        showShortToast(JsonParse.getCodeMessage(this, error.getCode(), getString(R.string.error_try_again)));
//    }

    /**
     * Lấy session mới khi session cũ đã hết hạn
     * */
    @Override
    public void getNewSession(final ICmd iCmd, final Object... params) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(params);
            }

            @Override
            public void onError(Error error) {
                showShortToast(getString(R.string.error_end_of_session));
                startActivityFinish(LoginActivity.class);
            }
        });
    }

    /**
     * Hiển thị progress bar thông báo đang thực hiện 1 action
     * */
    @Override
    public void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        super.showProgressBar(isTouchOutside, isCancel, title, message);
    }

    @Override
    public Activity getActivity() {
        return this;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
