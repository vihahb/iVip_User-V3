package com.xtel.ivipu.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.entity.HistoryTransactionObj;
import com.xtel.ivipu.model.entity.MemberObj;
import com.xtel.ivipu.presenter.FragmentNavMemberCardPresenter;
import com.xtel.ivipu.view.activity.LoginActivity;
import com.xtel.ivipu.view.adapter.AdapterCard;
import com.xtel.ivipu.view.adapter.HistoryTransactionAdapter;
import com.xtel.ivipu.view.fragment.inf.IFragmentMemberCard;
import com.xtel.ivipu.view.widget.NewProgressView;
import com.xtel.ivipu.view.widget.SlideProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

/**
 * Created by vuhavi on 07/03/2017
 * Fixed by vulcl
 */

public class FragmentMemberCard extends BasicFragment implements DiscreteScrollView.ScrollListener, IFragmentMemberCard {
    protected CallbackManager callbackManager;
    protected FragmentNavMemberCardPresenter presenter;

    protected SlideProgressView slideProgressView;
    protected AdapterCard memberAdapter;
    protected ArrayList<MemberObj> listMember;

    protected ArrayList<HistoryTransactionObj> listHistory;
    protected HistoryTransactionAdapter historyAdapter;
    protected NewProgressView progressView;

    protected boolean isClearMember = false, isClearHistory = false;
    protected int member_card_position = -1;

    public static FragmentMemberCard newInstance() {
        return new FragmentMemberCard();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.create(getActivity());

        presenter = new FragmentNavMemberCardPresenter(this);

        initListMember(view);
        initProgressView(view);
    }

    /**
     * Khởi tạo ProgressView
     * Chức năng:
     * Hiển thị danh sách voucher
     * Hiển thị thông báo
     */
    protected void initListMember(View view) {
        listMember = new ArrayList<>();
        memberAdapter = new AdapterCard(this, listMember);

        slideProgressView = new SlideProgressView(null, view);
        slideProgressView.setUpRecyclerView(memberAdapter);

        slideProgressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMemberAgain();
            }
        });

        slideProgressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberAgain();
            }
        });

        slideProgressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                getMemberAgain();
            }
        });

        slideProgressView.setOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@NonNull RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                if (member_card_position != adapterPosition) {
                    member_card_position = adapterPosition;

//                MemberObj obj = listMember.get(adapterPosition);
                    slideProgressView.setValue(10000, 5000);
                    getHistoryAgain();
                }
            }
        });
    }

    /**
     * Khởi tạo ProgressView
     * Chức năng:
     * Hiển thị danh sách voucher
     * Hiển thị thông báo
     */
    protected void initProgressView(View view) {
        progressView = new NewProgressView(null, view);
        progressView.setSwipeEnable(false);
        progressView.updateMessage(-1, getString(R.string.message_choose_member_to_view_history));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listHistory = new ArrayList<>();
        historyAdapter = new HistoryTransactionAdapter(this, listHistory);
        progressView.setUpRecyclerView(layoutManager, historyAdapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHistoryAgain();
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistoryAgain();
            }
        });
    }

    /**
     * Lấy dữ liệu trên server
     * Xóa dữ liệu đã có
     */
    protected void getMemberAgain() {
        isClearMember = true;
        slideProgressView.setRefreshing(true);
        slideProgressView.showData();
        presenter.getMemberCard(true);
    }

    /**
     * Lấy dữ liệu trên server
     * Xóa dữ liệu đã có
     */
    protected void getHistoryAgain() {
        if (listMember.size() == 0) {
            progressView.setRefreshing(false);
            progressView.updateMessage(-1, getString(R.string.message_choose_member_to_view_history));
            progressView.hideData();
            return;
        }

        isClearHistory = true;
        progressView.setRefreshing(true);
        progressView.showData();
        presenter.getHistory(true, listMember.get(member_card_position).getId());
    }

    /**
     * Kiểm tra dữ liệu lấy từ trên server về có hay không
     */
    protected void checkListMember() {
        slideProgressView.setRefreshing(false);
        memberAdapter.notifyDataSetChanged();

        if (listMember.size() > 0) {
            slideProgressView.setSwipeEnable(false);
            slideProgressView.showData();
        } else {
            slideProgressView.updateData(R.mipmap.ic_member_empty, getString(R.string.message_member_empty));
            slideProgressView.hideData();
        }
    }

    /**
     * Kiểm tra dữ liệu lấy từ trên server về có hay không
     */
    protected void checkListHistory() {
        progressView.setRefreshing(false);
        historyAdapter.notifyDataSetChanged();

        if (listHistory.size() > 0) {
            progressView.showData();
        } else {
            progressView.updateMessage(-1, getString(R.string.message_history_transaction_empty));
            progressView.hideData();
        }
    }

    /**
     * Lấy danh sách member card thành công
     * Kiểm tra dữ liệu lấy về
     */
    @Override
    public void onGetMemberCardSuccess(final ArrayList<MemberObj> arrayList) {
        if (isClearMember) {
            listMember.clear();
            memberAdapter.setLoadMore(true);
            isClearMember = false;
        }

        if (arrayList.size() < 10)
            memberAdapter.setLoadMore(false);

        listMember.addAll(arrayList);
        checkListMember();
    }

    /**
     * Lấy danh sách member card thất bại
     * Thông báo lỗi cho người dùng
     */
    @Override
    public void onGetMemberCardError(Error error) {
        slideProgressView.setSwipeEnable(true);

        if (listMember.size() > 0)
            showShortToast(JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error)));
        else {
            slideProgressView.setRefreshing(false);
            slideProgressView.updateData(R.mipmap.ic_error_network, JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error_touch_to_try_again)));
            slideProgressView.hideData();

            listMember.clear();
            memberAdapter.notifyDataSetChanged();

            if (isClearMember)
                isClearMember = false;
        }
    }

    /**
     * Lấy danh lịch sử tích đổi điểm thành công
     * Kiểm tra dữ liệu lấy về
     * */
    @Override
    public void onGetHistorySuccess(ArrayList<HistoryTransactionObj> arrayList) {
        if (isClearHistory) {
            listHistory.clear();
            historyAdapter.setLoadMore(true);
            isClearHistory = false;
        }

        if (arrayList.size() < 10)
            historyAdapter.setLoadMore(false);

        listHistory.addAll(arrayList);
        checkListHistory();
    }

    /**
     * Lấy danh lịch sử tích đổi điểm thất bại
     * Thông báo lỗi cho người dùng
     * */
    @Override
    public void onGetHistoryError(Error error) {
        historyAdapter.setLoadMore(false);
        historyAdapter.notifyDataSetChanged();

        if (listHistory.size() > 0)
            showShortToast(JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateMessage(-1, JsonParse.getCodeMessage(getActivity(), error.getCode(), getString(R.string.error_touch_to_try_again)));
            progressView.hideData();

            listHistory.clear();
            historyAdapter.notifyDataSetChanged();

            if (isClearHistory)
                isClearHistory = false;
        }
    }

    /**
     * Lấy session mới khi session cũ đã hết phiên làm việc
     * Lấy thành công -> gọi lại request đang thực hiện
     * Lấy thất bại -> thông báo hết phiên và bắt đăng nhập lại
     */
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
                startActivityAndFinish(LoginActivity.class);
            }
        });
    }

    /**
    * Thông báo khi không có kết nối mạng
    * */
    @Override
    public void onNetworkDisable(boolean isMember) {
        slideProgressView.setSwipeEnable(true);

        if (isMember) {
            slideProgressView.setRefreshing(false);

            if (listMember.size() > 0)
                showShortToast(getString(R.string.error_no_internet));
            else {
                slideProgressView.updateData(R.mipmap.ic_error_network, getString(R.string.message_no_internet_click_to_try_again));
                slideProgressView.hideData();
            }
        } else {
            progressView.setRefreshing(false);

            if (listHistory.size() > 0)
                showShortToast(getString(R.string.error_no_internet));
            else {
                progressView.updateMessage(-1, getString(R.string.message_no_internet_click_to_try_again));
                progressView.hideData();
            }
        }
    }

    @Override
    public void onLoadMore() {
        presenter.getMemberCard(false);
    }

    @Override
    public void onLoadMoreHistory() {
        presenter.getHistory(false, listMember.get(member_card_position).getId());
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
    public void onScroll(float scrollPosition, @NonNull RecyclerView.ViewHolder currentHolder, @NonNull RecyclerView.ViewHolder newCurrent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
