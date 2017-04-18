package com.xtel.ivipu.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_NewEntity;
import com.xtel.ivipu.presenter.FragmentNewsListPresenter;
import com.xtel.ivipu.view.activity.ActivityInfoContent;
import com.xtel.ivipu.view.adapter.AdapterRecycleNewsList;
import com.xtel.ivipu.view.fragment.inf.IFragmentNewsListView;
import com.xtel.ivipu.view.widget.ProgressView;
import com.xtel.sdk.commons.Constants;

import java.util.ArrayList;

/**
 * Created by vihahb on 1/13/2017.
 */

public class FragmentHomeNewsList extends BasicFragment implements IFragmentNewsListView {

    FragmentNewsListPresenter presenter;
    AdapterRecycleNewsList adapter;
    int type = 1, page = 1, pagesize = 10;
    private RecyclerView rcl_new_list;
    private ArrayList<RESP_NewEntity> arrayListNewsList;


    private int position = -1;
    private int REQUEST_VIEW_NEWS_LIST = 99;
    private ProgressView progressView;
    private RecyclerView.LayoutManager layoutManager;

    public static FragmentHomeNewsList newInstance(int type) {
        FragmentHomeNewsList newsList = new FragmentHomeNewsList();
        Bundle bundle = new Bundle();
        bundle.putInt("Key", type);
        newsList.setArguments(bundle);
        return newsList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_news_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new FragmentNewsListPresenter(this);
//        initArrayList();
        getBundle();
        initRecylerView(view);
        initProgressView(view);
    }

    private void getBundle() {
        try {
            type = getArguments().getInt("Key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setType(int type) {
        this.type = type;
        if (this.type == 10) {
            arrayListNewsList.clear();
            adapter.onSetLoadMore(true);
            getFavorite();
            adapter.notifyDataSetChanged();
        } else {
            page = 1;
            arrayListNewsList.clear();
            adapter.onSetLoadMore(true);
            getData();
            adapter.notifyDataSetChanged();
        }
    }

    private void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(R.mipmap.ic_error_network, getString(R.string.no_news), getString(R.string.try_again), getString(R.string.loading_data), Color.parseColor("#696969"));
        progressView.setUpWithView(rcl_new_list);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adapter.onSetLoadMore(true);
                if (type == 10) {
                    adapter.notifyDataSetChanged();
                    getFavorite();
                } else {
                    adapter.notifyDataSetChanged();
                    getData();
                }
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type == 10) {
                    page = 1;
                    arrayListNewsList.clear();
                    adapter.onSetLoadMore(true);
                    getFavorite();
                    adapter.notifyDataSetChanged();
                } else {
                    page = 1;
                    arrayListNewsList.clear();
                    adapter.onSetLoadMore(true);
                    getData();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                if (type == 10) {
                    getFavorite();
                    adapter.notifyDataSetChanged();
                    progressView.hideData();
                } else {
                    getData();
                    adapter.notifyDataSetChanged();
                    progressView.hideData();
                }
            }
        });
    }

    private void getData() {
//        progressView.hideData();
        progressView.setRefreshing(true);
//        adapter.onSetLoadMore(true);
        presenter.getNewsList(type, page, pagesize);
    }

    private void getFavorite() {
        progressView.setRefreshing(true);
        presenter.getFavorite(page, pagesize);
    }

    private void initRecylerView(View view) {
        rcl_new_list = (RecyclerView) view.findViewById(R.id.rcl_ivip);
        rcl_new_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rcl_new_list.setLayoutManager(layoutManager);

        arrayListNewsList = new ArrayList<>();
        adapter = new AdapterRecycleNewsList(arrayListNewsList, this);
        rcl_new_list.setAdapter(adapter);
    }

    private void setDataRecyclerView(ArrayList<RESP_NewEntity> newEntities) {
        arrayListNewsList.addAll(newEntities);
        adapter.notifyDataSetChanged();
    }

    private void checkListData(int type) {
//        progressView.disableSwipe();
        progressView.setRefreshing(false);

        if (arrayListNewsList.size() == 0) {
            if (type == 1) {
                progressView.updateData(R.mipmap.ic_err_news_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 2) {
                progressView.updateData(R.mipmap.ic_err_food_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 3) {
                progressView.updateData(R.mipmap.ic_err_fashion_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 4) {
                progressView.updateData(R.mipmap.ic_err_tech_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 5) {
                progressView.updateData(R.mipmap.ic_err_health_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 6) {
                progressView.updateData(R.mipmap.ic_err_other_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 7) {
                progressView.updateData(R.mipmap.ic_err_near_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 8) {
                progressView.updateData(R.mipmap.ic_err_cook_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 9) {
                progressView.updateData(R.mipmap.ic_err_toy_empty, getString(R.string.no_news), getString(R.string.try_again));
            } else if (type == 10) {
                progressView.updateData(R.mipmap.ic_launcher, getString(R.string.no_news), getString(R.string.try_again));
            }
            progressView.show();
        } else {
            rcl_new_list.getAdapter().notifyDataSetChanged();
            adapter.notifyDataSetChanged();
            progressView.hide();
        }
    }


    /**
     * Method in INF
     */

    @Override
    public void onGetNewsListSuccess(ArrayList<RESP_NewEntity> arrayList) {
//        progressView.setRefreshing(false);
        Log.e("arr news entity", arrayList.toString());
        Log.e("arr size", String.valueOf(arrayList.size()));
        if (arrayList.size() < 10) {
            adapter.onSetLoadMore(false);
        }
        setDataRecyclerView(arrayList);
        checkListData(type);
    }

    @Override
    public void onGetFavoriteSuccess(ArrayList<RESP_NewEntity> data) {
        Log.e("arr favorite", data.toString());
        Log.e("arr size", String.valueOf(data.size()));

        if (data.size() < 10) {
            adapter.onSetLoadMore(false);
        }
        setDataRecyclerView(data);
        checkListData(type);
    }


    @Override
    public void onGetNewsListErr() {
    }


    @Override
    public void startActivityAndFinish(Class clazz) {
        super.startActivityAndFinish(clazz);
    }

    @Override
    public void onLoadMore() {
        page++;
        if (type == 10) {
            getFavorite();
        } else {
            getData();
        }
    }


    @Override
    public void showShortToast(String message) {
        super.showShortToast(message);
    }

    @Override
    public void showLongToast(String message) {
        super.showLongToast(message);
    }

    @Override
    public void onItemClick(int position, RESP_NewEntity testRecycle, View view) {
        this.position = position;
//        if (Build.VERSION.SDK_INT >= 20) {
//            int id = R.id.card_view_shop;
//            View viewStart = view.findViewById(R.id.card_view_shop);
//            View view_ava = view.findViewById(R.id.img_view);
//            View view_img = view.findViewById(R.id.img_banner_shop);
//            View view_name = view.findViewById(R.id.tv_shop_name);
//            View view_content = view.findViewById(R.id.shop_content);
//
//            String main = getString(R.string.transition_name_shop);
//            String imga = getString(R.string.transition_shop_img);
//            String icon = getString(R.string.transition_shop_ava);
//            String name = getString(R.string.transition_shop_name);
//            String content = getString(R.string.transition_shop_content);
////            startActivityForResultWithTransition(ActivityInfoContent.class,
////                    Constants.RECYCLER_MODEL,
////                    testRecycle,
////                    R.string.transition_name_shop,
////                    view,
////                    id,
////                    REQUEST_VIEW_NEWS_LIST
////            );
//            Intent intent = new Intent(getActivity(), ActivityInfoContent.class);
//            intent.putExtra(Constants.RECYCLER_MODEL, testRecycle);
//            Pair<View, String> p1 = Pair.create(viewStart, main);
//            Pair<View, String> p2 = Pair.create(view_ava, icon);
//            Pair<View, String> p3 = Pair.create(view_img, imga);
//            Pair<View, String> p4 = Pair.create(view_name, name);
//            Pair<View, String> p5 = Pair.create(view_content, content);
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3, p4);
//            ActivityCompat.startActivityForResultObject(getActivity(), intent, REQUEST_VIEW_NEWS_LIST, options.toBundle());
//        } else {
        startActivityForResultObject(ActivityInfoContent.class, Constants.RECYCLER_MODEL, testRecycle, REQUEST_VIEW_NEWS_LIST);
//        }
    }

    @Override
    public void onNetworkDisable() {
        progressView.setRefreshing(false);
        progressView.updateData(R.mipmap.ic_error_network, getString(R.string.no_internet), getString(R.string.try_again));
        progressView.showData();
    }

    /**
     * !End Method in INF
     */


    @Override
    public void onResume() {
        super.onResume();
//        presenter.getNewsList(type, page, pagesize);
    }
}
