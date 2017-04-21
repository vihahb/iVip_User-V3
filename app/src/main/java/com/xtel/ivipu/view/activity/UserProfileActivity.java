package com.xtel.ivipu.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_Profile;
import com.xtel.ivipu.model.entity.CityCodeObj;
import com.xtel.ivipu.model.entity.UserInfo;
import com.xtel.ivipu.presenter.ProfilePresenter;
import com.xtel.ivipu.view.activity.inf.IProfileActivityView;
import com.xtel.ivipu.view.adapter.SpinnerAdapter;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.nipservicesdk.utils.SharedUtils;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;
import com.xtel.sdk.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vivhp on 4/18/2017.
 */

public class UserProfileActivity extends BasicActivity implements IProfileActivityView, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private LinearLayout ln_update, ln_logout, ln_change_avatar;
    private FrameLayout fr_avatar;
    private ImageView img_avatar, img_cover, img_edit, img_more_action, img_action_change_date, img_user_cancel;
    private TextView tv_full_name_top, tv_time_joint, tv_total_point, tv_current_point, tv_change_avatar;
    private Spinner sp_gender, sp_city;
    private EditText edt_full_name, edt_birth_day, edt_phone_number, edt_address;
    private ArrayAdapter<String> adapterGender;
    private SpinnerAdapter adapterCity;
    ArrayList<CityCodeObj> listCity;
    private ProfilePresenter presenter;

    /**
     * Variable
     * state temp
     * */
    private String cityCode, time_get, time_set;
    private int gender_type;
    private final int CAMERA_REQUEST_CODE = 1002;
    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * Variable update user
     * */
    String u_name, u_address, u_phone, u_area_code;
    int u_gender;
    long u_time_day;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_activity_profile);
        presenter = new ProfilePresenter(this);
        initView();
        initToolbar(R.id.toolbar_user, "Thông tin tài khoản");
    }

    /**
     * Method Create
     * */

    private void initView() {
        ln_update = (LinearLayout) findViewById(R.id.ln_update);
        ln_logout = (LinearLayout) findViewById(R.id.ln_logout);
        ln_change_avatar = (LinearLayout) findViewById(R.id.ln_change_avatar);

        fr_avatar = (FrameLayout) findViewById(R.id.fr_avatar);

        img_avatar = (ImageView) findViewById(R.id.img_user_avatar);
        img_cover = (ImageView) findViewById(R.id.img_cover_avatar);
        img_edit = (ImageView) findViewById(R.id.img_action_edit);
        img_user_cancel = (ImageView) findViewById(R.id.img_user_cancel);
        img_action_change_date = (ImageView) findViewById(R.id.img_action_change_date);

        tv_full_name_top = (TextView) findViewById(R.id.tv_top_full_name);
        tv_time_joint = (TextView) findViewById(R.id.tv_time_joint);
        tv_total_point = (TextView) findViewById(R.id.tv_total_point);
        tv_current_point = (TextView) findViewById(R.id.tv_current_point);
        tv_change_avatar = (TextView) findViewById(R.id.tv_change_avatar);

        sp_gender = (Spinner) findViewById(R.id.sp_profile_gender);
        sp_city = (Spinner) findViewById(R.id.sp_profile_city);

        edt_full_name = (EditText) findViewById(R.id.edit_name);
        edt_birth_day = (EditText) findViewById(R.id.edit_profile_date);
        edt_phone_number = (EditText) findViewById(R.id.edit_profile_phone);
        edt_address = (EditText) findViewById(R.id.edit_profile_address);

        Button btn_action_logout = (Button) findViewById(R.id.btn_action_logout);
        Button btn_action_update = (Button) findViewById(R.id.btn_action_update);
        initSpinner();
        presenter.getProfileData();

        img_edit.setOnClickListener(this);
        img_user_cancel.setOnClickListener(this);
        img_action_change_date.setOnClickListener(this);

        tv_change_avatar.setOnClickListener(this);

        btn_action_update.setOnClickListener(this);
        btn_action_logout.setOnClickListener(this);
    }

    private void initSpinner() {
        String[] gender_spinner_array = getResources().getStringArray(R.array.gender_profile);
        adapterGender = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, gender_spinner_array);
        adapterGender.setDropDownViewResource(R.layout.item_spinner_drop_down);
        sp_gender.setAdapter(adapterGender);
        sp_gender.setOnItemSelectedListener(this);

        initCityList();
        adapterCity = new SpinnerAdapter(getActivity(), R.layout.item_spinner_custom, listCity);
        adapterCity.setDropDownViewResource(R.layout.item_spinner_custom_dropdown);
        sp_city.setAdapter(adapterCity);
        sp_city.setOnItemSelectedListener(this);
        setDisable();
    }

    private void setDisable(){
        ln_change_avatar.setVisibility(View.GONE);
        ln_update.setVisibility(View.GONE);
        ln_logout.setVisibility(View.VISIBLE);
        sp_gender.setEnabled(false);
        sp_city.setEnabled(false);
        edt_full_name.setEnabled(false);
        edt_address.setEnabled(false);
        edt_birth_day.setEnabled(false);
        edt_phone_number.setEnabled(false);
        img_user_cancel.setVisibility(View.GONE);
        img_edit.setVisibility(View.VISIBLE);
        img_action_change_date.setVisibility(View.GONE);
    }

    private void setEnable(){
        ln_change_avatar.setVisibility(View.VISIBLE);
        ln_update.setVisibility(View.VISIBLE);
        ln_logout.setVisibility(View.GONE);
        sp_gender.setEnabled(true);
        sp_city.setEnabled(true);
        edt_full_name.setEnabled(true);
        edt_address.setEnabled(true);
        edt_birth_day.setEnabled(true);
        edt_phone_number.setEnabled(true);
        img_user_cancel.setVisibility(View.VISIBLE);
        img_edit.setVisibility(View.GONE);
        img_action_change_date.setVisibility(View.VISIBLE);
    }

    private void initCityList(){
        listCity = new ArrayList<>();
        listCity.add(new CityCodeObj("0", "Chưa có"));
        listCity.add(new CityCodeObj("A01", "An Giang"));
        listCity.add(new CityCodeObj("A02", "Bà Rịa-Vũng Tàu"));
        listCity.add(new CityCodeObj("A03", "Bạc Liêu"));
        listCity.add(new CityCodeObj("A04", "Bắc Kạn"));
        listCity.add(new CityCodeObj("A05", "Bắc Giang"));
        listCity.add(new CityCodeObj("A06", "Bắc Ninh"));
        listCity.add(new CityCodeObj("A07", "Bến Tre"));
        listCity.add(new CityCodeObj("A08", "Bình Dương"));
        listCity.add(new CityCodeObj("A09", "Bình Định"));
        listCity.add(new CityCodeObj("A010", "Bình Phước"));
        listCity.add(new CityCodeObj("A011", "Bình Thuận"));
        listCity.add(new CityCodeObj("A012", "Cà Mau"));
        listCity.add(new CityCodeObj("A013", "Cao Bằng"));
        listCity.add(new CityCodeObj("A014", "Cần Thơ"));
        listCity.add(new CityCodeObj("A015", "Đà Nẵng"));
        listCity.add(new CityCodeObj("A016", "Đắk Lắk"));
        listCity.add(new CityCodeObj("A017", "Đắk Nông"));
        listCity.add(new CityCodeObj("A018", "Điện Biên"));
        listCity.add(new CityCodeObj("A019", "Đồng Nai"));
        listCity.add(new CityCodeObj("A020", "Đồng Tháp"));
        listCity.add(new CityCodeObj("A021", "Gia Lai"));
        listCity.add(new CityCodeObj("A022", "Hà Giang"));
        listCity.add(new CityCodeObj("A023", "Hà Nam"));
        listCity.add(new CityCodeObj("A024", "Hà Nội"));
        listCity.add(new CityCodeObj("A025", "Hà Tây"));
        listCity.add(new CityCodeObj("A026", "Hà Tĩnh"));
        listCity.add(new CityCodeObj("A027", "Hải Dương"));
        listCity.add(new CityCodeObj("A028", "Hải Phòng"));
        listCity.add(new CityCodeObj("A029", "Hòa Bình"));
        listCity.add(new CityCodeObj("A030", "Hồ Chí Minh"));
        listCity.add(new CityCodeObj("A031", "Hậu Giang"));
        listCity.add(new CityCodeObj("A032", "Hưng Yên"));
        listCity.add(new CityCodeObj("A033", "Khánh Hòa"));
        listCity.add(new CityCodeObj("A034", "Kiên Giang"));
        listCity.add(new CityCodeObj("A035", "Kon Tum"));
        listCity.add(new CityCodeObj("A036", "Lai Châu"));
        listCity.add(new CityCodeObj("A037", "Lào Cai"));
        listCity.add(new CityCodeObj("A038", "Lạng Sơn"));
        listCity.add(new CityCodeObj("A039", "Lâm Đồng"));
        listCity.add(new CityCodeObj("A040", "Long An"));
        listCity.add(new CityCodeObj("A041", "Nam Định"));
        listCity.add(new CityCodeObj("A042", "Nghệ An"));
        listCity.add(new CityCodeObj("A043", "Ninh Bình"));
        listCity.add(new CityCodeObj("A044", "Ninh Thuận"));
        listCity.add(new CityCodeObj("A045", "Phú Thọ"));
        listCity.add(new CityCodeObj("A046", "Phú Yên"));
        listCity.add(new CityCodeObj("A047", "Quảng Bình"));
        listCity.add(new CityCodeObj("A048", "Quảng Nam"));
        listCity.add(new CityCodeObj("A049", "Quảng Ngãi"));
        listCity.add(new CityCodeObj("A050", "Quảng Ninh"));
        listCity.add(new CityCodeObj("A051", "Quảng Trị"));
        listCity.add(new CityCodeObj("A052", "Sóc Trăng"));
        listCity.add(new CityCodeObj("A053", "Sơn La"));
        listCity.add(new CityCodeObj("A054", "Tây Ninh"));
        listCity.add(new CityCodeObj("A055", "Thái Bình"));
        listCity.add(new CityCodeObj("A056", "Thái Nguyên"));
        listCity.add(new CityCodeObj("A057", "Thanh Hóa"));
        listCity.add(new CityCodeObj("A058", "Thừa Thiên - Huế"));
        listCity.add(new CityCodeObj("A059", "Tiền Giang"));
        listCity.add(new CityCodeObj("A060", "Trà Vinh"));
        listCity.add(new CityCodeObj("A061", "Tuyên Quang"));
        listCity.add(new CityCodeObj("A062", "Vĩnh Long"));
        listCity.add(new CityCodeObj("A063", "Vĩnh Phúc"));
        listCity.add(new CityCodeObj("A064", "Yên Bái"));
    }

    private void setTimeBirthDay() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat_time = new SimpleDateFormat("dd-MM-yyyy");
        int Days, months, years;
        if (!time_get.equals(getActivity().getResources().getString(R.string.no_birth_day))) {
            try {
                Date date = dateFormat_time.parse(time_get);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Days = calendar.get(Calendar.DAY_OF_MONTH);
            months = calendar.get(Calendar.MONTH);
            years = calendar.get(Calendar.YEAR);
        } else {
            Days = calendar.get(Calendar.DAY_OF_MONTH);
            months = calendar.get(Calendar.MONTH);
            years = calendar.get(Calendar.YEAR);
        }
        DatePickerDialog pickerDialog = new DatePickerDialog(getContext(),
                R.style.TimePicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String time_change = dayOfMonth + "-" + (month + 1) + "-" + year;
                edt_birth_day.setText(time_change);
                time_set = time_change;
            }
        }, years, months, Days);
        pickerDialog.show();
    }

    /**
     * Valid Data from User Update
     * */

    private boolean validData(){
        boolean check_done = true;

        u_name = edt_full_name.getText().toString();
        u_address = edt_address.getText().toString();
        u_phone = edt_phone_number.getText().toString();
        u_area_code = cityCode;
        u_time_day = WidgetHelper.getInstance().convertTime2Unix(time_set);
        u_gender = gender_type + 1;

        if (u_name.isEmpty()){
            check_done = false;
            showShortToast("Vui lòng nhập tên");
        }
        if (u_address.isEmpty()){
            check_done = false;
            showShortToast("Vui lòng nhập địa chỉ");
        }
        if (u_phone.isEmpty()){
            check_done = false;
            showShortToast("Vui lòng không để trống số điện thoại");
        }
        return check_done;
    }

    private void updateData(){
        if (validData()){
            getDataUpdate(u_name, u_address, u_phone, u_area_code, u_time_day, u_gender);
        }
    }

    private void getDataUpdate(String name, String address, String phone_number, String area_code, long birth_day, int gender){
        UserInfo userInfo = new UserInfo();
        userInfo.setFullname(name);
        userInfo.setAddress(address);
        userInfo.setPhonenumber(phone_number);
        userInfo.setArea_code(area_code);
        userInfo.setBirthday(birth_day);
        userInfo.setGender(gender);
        presenter.setData(userInfo);
    }

    /**
     * Init Camera method*/
    private void initCamera() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        //Create any other intents you want
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Add them to an intent array
        Intent[] intents = new Intent[]{cameraIntent};
        //Create a choose from your first intent then pass in the intent array
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Chọn ảnh");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        startActivityForResult(chooserIntent, 101);
    }


    /**
     * Action Logout
     * */
    private void LogOut(){
        SharedUtils.getInstance().clearData();
        SharedPreferencesUtils.getInstance().clearData();
        LoginManager.getInstance().logOut();
        finishActivityBeforeStartActivity(HomeActivity.class);
    }

    public void checkNetwork(final Context context, int type) {
        if (!NetWorkInfo.isOnline(context)) {
            WidgetHelper.getInstance().showAlertNetwork(context);
        } else {
            if (type == 1) {
                updateData();
            } else if (type == 2) {
                if (PermissionHelper.checkListPermission(permission, getActivity(), CAMERA_REQUEST_CODE)) {
                    initCamera();
                }
            }
        }
    }

    /**
     * Take pickture
     * from Gallery
     * */
    private void onTakePictureGallary(Uri uri) {
        if (!NetWorkInfo.isOnline(getActivity())) {
            showShortToast(getString(R.string.no_internet));
            return;
        } else if (uri == null) {
            showShortToast("Không thể lấy ảnh");
            return;
        }
        showProgressBar(false, false, null, "Đang tải file...");
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        presenter.postImage(bitmap);
    }

    /**
     * Take pickture
     * from Camera
     * */
    private void onTakePictureCamera(Bitmap bitmap) {
        if (!NetWorkInfo.isOnline(getActivity())) {
            showShortToast(getString(R.string.no_internet));
            return;
        } else if (bitmap == null) {
            showShortToast("Không thể lấy ảnh");
            return;
        }
        showProgressBar(false, false, null, "Đang tải file...");
        presenter.postImage(bitmap);
    }

    private void onUpdateAvatar(String server_path) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(server_path);
        presenter.setData(userInfo);
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
        super.showShortToast(mes);
    }

    @Override
    public void startActivitys(Class clazz) {
        super.startActivity(clazz);
    }

    @Override
    public void startActivityAndFinish(Class clazz) {
        super.startActivityFinish(clazz);
    }

    @Override
    public void finishActivityBeforeStartActivity(Class clazz) {
        startActivityAndFinish(clazz);
    }

    @Override
    public void finishActivity() {
        super.finishActivity();
    }

    @Override
    public void setProfileSuccess(UserInfo profile) {
        setData2Control(profile);
    }

    private void setData2Control(UserInfo profile) {
        fr_avatar.setBackground(getResources().getDrawable(R.drawable.layout_circle));
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

        String area_code = profile.getArea_code();



        for (int i = 0; i < listCity.size(); i++){
            if (listCity.get(i).getArea_code().equals(area_code)){
                sp_city.setSelection(i, true);
               break;
            }
        }

//        sp_city.setSelection(getCityPosition(area_code));

        time_get = time_birthday;
        time_set = time_birthday;
        int gender = profile.getGender() - 1;
        sp_gender.setSelection(gender, true);

    }

    private int getCityPosition(String index) {
        return listCity.indexOf(index);
    }

    @Override
    public void getNewSession(ICmd iCmd, Object... params) {

    }

    @Override
    public void reloadProfile(RESP_Profile profile) {

    }

    @Override
    public void updateProfileSucc() {
        setDisable();
        showShortToast("Cập nhật thông tin thành công!");
        presenter.getProfileData();
    }

    @Override
    public void onPostPictureSuccess(String url, String server_path) {
        onUpdateAvatar(server_path);
        WidgetHelper.getInstance().setAvatarImageURL(img_avatar, url);
        SharedPreferencesUtils.getInstance().putStringValue(Constants.PROFILE_AVATAR, url);
        closeProgressBar();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int id_spinner = parent.getId();
        if (id_spinner == R.id.sp_profile_gender){
            gender_type = parent.getSelectedItemPosition();
        } else if (id_spinner == R.id.sp_profile_city){
            cityCode = adapterCity.getItem(position).getArea_code();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_action_edit){
            Log.e("Click", "Edit");
            setEnable();
        } else if (id == R.id.img_user_cancel){
            Log.e("Click", "Cancel");
            setDisable();
        } else if (id == R.id.img_action_change_date){
            setTimeBirthDay();
        } else if (id == R.id.btn_action_update){
            checkNetwork(this, 1);
        } else if (id == R.id.btn_action_logout){
            LogOut();
        } else if (id == R.id.tv_change_avatar){
            checkNetwork(this, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                onTakePictureGallary(uri);
            } else {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                onTakePictureCamera(bitmap);
            }
            Log.e("URI", uri.toString());
        }
    }
}
