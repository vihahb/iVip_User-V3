package com.xtel.ivipu.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_StoreInfo;
import com.xtel.ivipu.model.entity.Address;
import com.xtel.ivipu.presenter.StoreOnMapPresenter;
import com.xtel.ivipu.view.activity.inf.IStoreOnMapView;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.NetWorkInfo;

import java.io.IOException;
import java.net.URL;

public class StoreOnMapActivity extends IActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener, IStoreOnMapView {
    protected StoreOnMapPresenter presenter;

    protected GoogleMap mMap;
//    protected BottomSheetBehavior bottomSheetBehavior;
    protected LocationManager locationManager;
    protected Polyline polyline;

//    protected ImageView img_logo, img_direction;
//    protected TextView txt_title, txt_address, txt_phone, txt_content;

    protected String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    protected final int REQUEST_PERMISSION = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_on_map);

        initToolbar(R.id.store_on_map_toolbar, null);
        presenter = new StoreOnMapPresenter(this);
        presenter.getData();
    }

    /**
     * Khởi tạo map
     */
    protected void initGoogleMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.store_on_map_map);
        mapFragment.getMapAsync(this);
    }

//    /**
//     * Khởi tạo các view trong bottm sheet
//     */
//    protected void initBottomSheetView() {
//        img_direction = findImageView(R.id.store_on_map_address_img_direction);
//        img_logo = findImageView(R.id.store_on_map_address_img_logo);
//        txt_title = findTextView(R.id.store_on_map_address_txt_title);
//        txt_address = findTextView(R.id.store_on_map_address_txt_address);
//        txt_phone = findTextView(R.id.store_on_map_address_txt_phone);
//        txt_content = findTextView(R.id.store_on_map_address_txt_content);
//    }

    /**
     * Lắng nghe location
     */
    protected void initLocationListener() {
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    /**
     * Bo tròn bitmap
     */
    protected Bitmap getCroppedBitmap(Bitmap bitmap) {
        @SuppressLint("InflateParams") View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView background = (ImageView) customMarkerView.findViewById(R.id.custom_marker_background);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.custom_marker);
        markerImageView.setImageBitmap(bitmap);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, background.getMeasuredWidth(), background.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    /**
     * Kiểm tra permission
     */
    protected boolean checkPermission() {
        return PermissionHelper.checkListPermission(permission, this, REQUEST_PERMISSION);
    }

    /**
     * Lấy vị trí hiện tại của thiết bị
     */
    protected Location getLastKnowLocation() {
        if (checkPermission() &&  NetWorkInfo.checkGPS(StoreOnMapActivity.this)) {
            Location gps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location network = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gps == null && network == null) {
                showShortToast(getString(R.string.message_can_not_get_location));
                return null;
            }

            return (gps != null) ? gps : network;
        }

        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (checkPermission())
            mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.030947, 105.787434), 15));

        presenter.getStoreList();
    }

    /**
     * Sự kiện khi người dùng click vào marker
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Location location = getLastKnowLocation();
        Address address = (Address) marker.getTag();

        if (location == null) {
            showShortToast(getString(R.string.error_can_not_get_location));
            return false;
        } else if (address == null) {
            showShortToast(getString(R.string.error_can_not_direction));
            return false;
        }

        presenter.direction(location.getLatitude(), location.getLongitude(), address.getLocation_lat(), address.getLocation_lng());
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onGetDataSuccess() {
        NetWorkInfo.checkGPS(StoreOnMapActivity.this);
        initGoogleMaps();
        initLocationListener();
    }

    @Override
    public void onGetStoreList(RESP_StoreInfo resp_storeInfo) {
        for (Address address : resp_storeInfo.getAddress()) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(address.getLocation_lat(), address.getLocation_lng()));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));

            Marker marker = mMap.addMarker(markerOptions);

            try {
                URL url = new URL(resp_storeInfo.getLogo());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(bmp)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            marker.setTag(address);
            marker.showInfoWindow();
        }
    }

    @Override
    public void onGetPolylineSuccess(LatLng latLng, PolylineOptions polylineOptions) {
        closeProgressBar();

        if (polyline != null)
            polyline.remove();

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        polyline = mMap.addPolyline(polylineOptions);
        polyline.setWidth(16);
        polyline.setColor(Color.parseColor("#62B1F6"));
    }

    @Override
    public void onGetDataError() {
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

    @Override
    public void onGetPolyLineError() {
        closeProgressBar();

        showShortToast(getString(R.string.error_can_not_direction));
    }

    @Override
    public void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        super.showProgressBar(isTouchOutside, isCancel, title, message);
    }

    @Override
    public void onNoInternet() {
        showShortToast(getString(R.string.error_no_internet));
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

        if (requestCode == REQUEST_PERMISSION) {
            if (PermissionHelper.checkAllow(grantResults)) {
                NetWorkInfo.checkGPS(StoreOnMapActivity.this);
            }
        }
    }
}
