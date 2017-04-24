package com.xtel.ivipu.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_NewEntity;
import com.xtel.ivipu.presenter.NewsOfStorePresenter;
import com.xtel.ivipu.view.activity.inf.INewsOfStoreView;
import com.xtel.ivipu.view.adapter.NewsOfStoreAdatper;
import com.xtel.ivipu.view.widget.NewProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.NewDialogListener;
import com.xtel.sdk.commons.Constants;

import java.util.ArrayList;

public class NewsOfStoreActivity extends BasicActivity implements INewsOfStoreView {
    protected CallbackManager callbackManager;
    protected NewsOfStorePresenter presenter;

    protected NewsOfStoreAdatper adapter;
    protected ArrayList<RESP_NewEntity> listData;
    protected NewProgressView progressView;
    protected boolean isClearData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_of_store);

        callbackManager = CallbackManager.create(this);

        presenter = new NewsOfStorePresenter(this);
        presenter.getData();
    }

    /**
     * Khởi tạo ProgressView
     * Chức năng:
     * Hiển thị danh sách voucher
     * Hiển thị thông báo
     */
    protected void initProgressView() {
        progressView = new NewProgressView(this, null);
        progressView.updateMessage(R.mipmap.ic_voucher_empty, getString(R.string.message_no_voucher));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listData = new ArrayList<>();
        adapter = new NewsOfStoreAdatper(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataAgain();
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataAgain();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                getDataAgain();
            }
        });
    }

    /**
     * Lấy dữ liệu trên server
     * Xóa dữ liệu đã có
     */
    protected void getDataAgain() {
        isClearData = true;
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();
        progressView.setRefreshing(true);
        progressView.showData();
        presenter.getVoucher(true);
    }

    /**
     * Kiểm tra dữ liệu có lấy từ trên server
     */
    protected void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            progressView.showData();
            adapter.notifyDataSetChanged();
        } else {
            progressView.updateMessage(R.mipmap.ic_voucher_empty, getString(R.string.message_no_voucher));
            progressView.hideData();
        }
    }

    /**
     * Gọi method lấy thêm dữ liệu trên server
     */
    public void onLoadMore() {
        presenter.getVoucher(false);
    }

    /**
     * Sụ kiện khi item trong news list được click
     * */
    public void onItemClick(RESP_NewEntity resp_newEntity) {
        resp_newEntity.setFinal(true);
        startActivity(NewsInfoActivity.class, Constants.OBJECT, resp_newEntity);
    }

    /**
     * Lấy store id truyền sang thành công
     * */
    @Override
    public void ongetDataSuccess() {
        initToolbar(R.id.news_in_store_toolbar, null);
        initProgressView();
    }

    /**
     * Sự kiệm khi lấy dữ liệu trên server thành công
     * Add thêm dữ liệu và kiểm tra dữ liệu
     */
    @Override
    public void onGetVoucherSuccess(final ArrayList<RESP_NewEntity> arrayList) {
        if (isClearData) {
            listData.clear();
            adapter.setLoadMore(true);
            isClearData = false;
        }

        if (arrayList.size() < 10)
            adapter.setLoadMore(false);

        listData.addAll(arrayList);
        checkListData();
    }

    /**
     * Sự kiện lấy dữ liệu trên server thất bại
     * Kiểm tra xem đã có dữ liệu chưa để có thông báo phù hợp
     */
    @Override
    public void onGetVoucherError(Error error) {
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();

        if (listData.size() > 0)
            showShortToast(JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateMessage(R.mipmap.ic_error_network, JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error_touch_to_try_again)));
            progressView.hideData();

            listData.clear();
            adapter.notifyDataSetChanged();
            if (isClearData)
                isClearData = false;
        }
    }

    /**
     * Lấy store id truyền sang thất bại
     * */
    @Override
    public void ongetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.layout_ok), new NewDialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    /**
     * Sự kiện khi không có kết nối mạng
     * Kiểm tra xem đã có dữ liệu chưa để có thông báo phù hợp
     */
    @Override
    public void onNoNetwork() {
        progressView.setRefreshing(false);

        if (listData.size() > 0)
            showShortToast(getString(R.string.error_no_internet));
        else {
            progressView.updateMessage(R.mipmap.ic_error_network, getString(R.string.message_no_internet_click_to_try_again));
            progressView.hideData();
        }
    }

    /**
     * Lấy session mới khi session cũ đã hết phiên làm việc
     * Lấy thành công -> gọi lại request đang thực hiện
     * Lấy thất bại -> thông báo hết phiên và bắt đăng nhập lại
     */
    @Override
    public void getNewSession(final ICmd iCmd) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute();
            }

            @Override
            public void onError(Error error) {
                showShortToast(getString(R.string.error_end_of_session));
                startActivityFinish(LoginActivity.class);
            }
        });
    }

    /**
     * Sụ kiện khi người dùng chưa đăng nhập
     */
    @Override
    public void onNotLogged() {
        showShortToast(getString(R.string.need_login_to_action));
        startActivityFinish(LoginActivity.class);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
