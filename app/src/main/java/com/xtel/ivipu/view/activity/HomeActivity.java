package com.xtel.ivipu.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.xtel.ivipu.R;
import com.xtel.ivipu.presenter.HomePresenter;
import com.xtel.ivipu.view.activity.inf.IHome;
import com.xtel.ivipu.view.fragment.FragmentHomeFashionMakeUp;
import com.xtel.ivipu.view.fragment.FragmentHomeFood;
import com.xtel.ivipu.view.fragment.FragmentHomeHealth;
import com.xtel.ivipu.view.fragment.FragmentHomeNewsForMe;
import com.xtel.ivipu.view.fragment.FragmentHomeNewsList;
import com.xtel.ivipu.view.fragment.FragmentHomeOtherService;
import com.xtel.ivipu.view.fragment.FragmentHomeTechnology;
import com.xtel.ivipu.view.fragment.FragmentMemberCard;
import com.xtel.ivipu.view.fragment.ListVoucherFragment;
import com.xtel.ivipu.view.fragment.ProfileFragment;
import com.xtel.ivipu.view.widget.WidgetHelper;

import java.lang.reflect.Field;

/**
 * Created by vivhp on 12/29/2016
 */

public class HomeActivity extends IActivity implements NavigationView.OnNavigationItemSelectedListener, IHome {
    protected final String TAG = "HomeActivity";

    protected HomePresenter presenter;
    protected BottomNavigationView nav_bottom_home;
    protected DrawerLayout drawer;
    protected ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter = new HomePresenter(this);

        initNavigation();
        initBottomNavigation();
        replaceListNews();

        presenter.postFCMKey();
    }

    /**
     * Khởi tạo menu bên trái
     * Lắng nghe sự kiện khi item trong navigation được chọn
     */
    @SuppressWarnings("deprecation")
    private void initNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//        toolbar.setNavigationIcon(R.drawable.ic_drwable_menu_icon);

        NavigationView navigationView = (NavigationView) findViewById(R.id.home_navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Khởi tạo tab chức năng phía dưới
     * Lắng nghe sự kiện khi item cửa tab được chọn
     */
    public void initBottomNavigation() {
        nav_bottom_home = (BottomNavigationView) findViewById(R.id.home_bottom_navigation);
        removeShiftMode();

        nav_bottom_home.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_home_home:

                                break;
                            case R.id.nav_home_voucher:
                                replaceListVoucher();
                                break;
                            case R.id.nav_home_member:
                                replaceMember();
                                break;
                            case R.id.nav_home_favorite:

                                break;
                            case R.id.nav_home_account:
                                replaceProfile();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
    }

    /**
    * Loại bỏ animation của bottom navigation view
    * */
    private void removeShiftMode() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) nav_bottom_home.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to change value of shift mode");
        }
    }

//    /**
//     * Kiểm tra dữ liệu truyền vào
//     */
//    private void getData() {
//        String data = null;
//        try {
//            data = getIntent().getStringExtra(Constants.NOTIFY_KEY);
//            if (data.equals("news")) {
//                replaceListNews();
//            } else if (data.equals("location")) {
//                replaceNewsAround();
//                actionBar.setTitle(getString(R.string.nav_news_for_me));
//                isChecked = true;
//                nav_bottom_home.setSelectedItemId(R.id.nav_list_item);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (data == null)
//            replaceListNews();
//    }

    /**
     * Đổi tiêu đề của toolbar
     * @param StringResource id of string
     */
    private void renameToolbar(int StringResource) {
        actionBar.setTitle(getString(StringResource));
    }

    private void replaceListNews() {
        replaceFragment(R.id.home_frame, FragmentHomeNewsList.newInstance(), "LATEST_NEW");
        renameToolbar(R.string.nav_new_list);
    }

    private void replaceCuisine() {
        replaceFragment(R.id.home_frame, FragmentHomeFood.newInstance(), "CUISINE");
        renameToolbar(R.string.nav_cuisine_and_eating);
    }

    private void replaceFashion() {
        replaceFragment(R.id.home_frame, FragmentHomeFashionMakeUp.newInstance(), "FASHION");
        renameToolbar(R.string.nav_fashion_and_beautify);
    }

    private void replaceElectronic() {
        replaceFragment(R.id.home_frame, FragmentHomeTechnology.newInstance(), "ELECTRONIC");
        renameToolbar(R.string.nav_electronics_and_technology);
    }

    private void replaceHealth() {
        replaceFragment(R.id.home_frame, FragmentHomeHealth.newInstance(), "HEALTH");
        renameToolbar(R.string.nav_health_and_life);
    }

    private void replaceHousehold() {

        renameToolbar(R.string.nav_household_goods_and_consumer_goods);
    }

    private void replaceToy() {

        renameToolbar(R.string.nav_toys_and_games);
    }

    private void replaceOtherService() {
        replaceFragment(R.id.home_frame, FragmentHomeOtherService.newInstance(), "OTHER_SERVICE");
        renameToolbar(R.string.nav_other_services);
    }

    private void replaceNewsAround() {
        replaceFragment(R.id.home_frame, FragmentHomeNewsForMe.newInstance(), "NEWS_AROUND");
        renameToolbar(R.string.nav_news_for_me);
    }

    private void replaceMember() {
        replaceFragment(R.id.home_frame, FragmentMemberCard.newInstance(), "MEMBER");
        renameToolbar(R.string.fragment_member_card_content);
    }

    private void replaceListVoucher() {
        replaceFragment(R.id.home_frame, ListVoucherFragment.newInstance(), "LIST_VOUCHER");
        renameToolbar(R.string.title_list_voucher);
    }

    private void replaceProfile() {
        replaceFragment(R.id.home_frame, ProfileFragment.newInstance(), "PROFILE");
        renameToolbar(R.string.title_list_voucher);
    }

    @Override
    public void startActivityFinish(Class clazz) {
        super.startActivityFinish(clazz);
    }

    @Override
    public void onNetworkDisable() {
        WidgetHelper.getInstance().showAlertNetwork(this);
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home_latest_news:
                replaceListNews();
                break;
            case R.id.nav_home_cuisine:
                replaceCuisine();
                break;
            case R.id.nav_home_fashion:
                replaceFashion();
                break;
            case R.id.nav_home_electronic:
                replaceElectronic();
                break;
            case R.id.nav_home_health:
                replaceHealth();
                break;
            case R.id.nav_home_household:
                replaceHousehold();
                break;
            case R.id.nav_home_toy:
                replaceToy();
                break;
            case R.id.nav_home_other:
                replaceOtherService();
                break;
            case R.id.nav_home_around:
                replaceNewsAround();
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showConfirmExitApp();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home_scan_qr) {
            startActivity(QrCheckIn.class);
        }
        return super.onOptionsItemSelected(item);
    }
}