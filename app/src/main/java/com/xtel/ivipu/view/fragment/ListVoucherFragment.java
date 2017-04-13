package com.xtel.ivipu.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.entity.VoucherListObj;
import com.xtel.ivipu.presenter.ListVoucherPresenter;
import com.xtel.ivipu.view.activity.LoginActivity;
import com.xtel.ivipu.view.adapter.AdapterVoucherList;
import com.xtel.ivipu.view.fragment.inf.IListVoucherView;
import com.xtel.ivipu.view.widget.NewProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/13/2017
 */

public class ListVoucherFragment extends BasicFragment implements IListVoucherView {
    protected CallbackManager callbackManager;
    protected ListVoucherPresenter presenter;

    protected AdapterVoucherList adapter;
    protected ArrayList<VoucherListObj> listData;
    protected NewProgressView progressView;
    protected boolean isClearData = false;

    public static ListVoucherFragment newInstance() {
        return new ListVoucherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_voucher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.create(getActivity());

        presenter = new ListVoucherPresenter(this);
        initProgressView(view);
    }

    /**
    * Khởi tạo ProgressView
    * Chức năng:
    * Hiển thị danh sách voucher
    * Hiển thị thông báo
    * */
    protected void initProgressView(View view) {
        progressView = new NewProgressView(null, view);
        progressView.initData(R.mipmap.ic_error_voucher, getString(R.string.message_no_voucher));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new AdapterVoucherList(listData, this);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.hideData();
                presenter.getVoucher(true);
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
                progressView.setRefreshing(true);
                progressView.hideData();
                presenter.getVoucher(true);
            }
        });
    }

    /**
    * Lấy dữ liệu trên server
    * Xóa dữ liệu đã có
    * */
    protected void getDataAgain() {
        isClearData = true;
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();
        progressView.setRefreshing(true);
        progressView.hideData();
        presenter.getVoucher(true);
    }

    /**
    * Kiểm tra dữ liệu có lấy từ trên server
    * */
    protected void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            progressView.showData();
            adapter.notifyDataSetChanged();
        } else {
            progressView.updateData(R.mipmap.ic_error_voucher, getString(R.string.message_no_voucher));
            progressView.hideData();
        }
    }

    /**
    * Gọi method lấy thêm dữ liệu trên server
    * */
    @Override
    public void onLoadMore() {
        presenter.getVoucher(false);
    }

    /**
    * Sự kiệm khi lấy dữ liệu trên server thành công
    * Add thêm dữ liệu và kiểm tra dữ liệu
    * */
    @Override
    public void onGetVoucherSuccess(final ArrayList<VoucherListObj> arrayList) {
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
    * */
    @Override
    public void onGetVoucherError(Error error) {
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();

        if (listData.size() > 0)
            showShortToast(JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateData(R.mipmap.ic_error_network, JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error_touch_to_try_again)));
            progressView.hideData();

            listData.clear();
            adapter.notifyDataSetChanged();
            if (isClearData)
                isClearData = false;
        }
    }

    /**
    * Sự kiện khi không có kết nối mạng
    * Kiểm tra xem đã có dữ liệu chưa để có thông báo phù hợp
    * */
    @Override
    public void onNoNetwork() {
        progressView.setRefreshing(false);

        if (listData.size() > 0)
            showShortToast(getString(R.string.error_no_internet));
        else {
            progressView.updateData(R.mipmap.ic_error_network, getString(R.string.message_no_internet_click_to_try_again));
            progressView.hideData();
        }
    }

    /**
    * Lấy session mới khi session cũ đã hết phiên làm việc
    * Lấy thành công -> gọi lại request đang thực hiện
    * Lấy thất bại -> thông báo hết phiên và bắt đăng nhập lại
    * */
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
                startActivityAndFinish(LoginActivity.class);
            }
        });
    }

    @Override
    public void onNotLogged() {
        showShortToast(getString(R.string.need_login_to_action));
        startActivityAndFinish(LoginActivity.class);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}