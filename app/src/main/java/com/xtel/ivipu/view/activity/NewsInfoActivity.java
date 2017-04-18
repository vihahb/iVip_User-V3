package com.xtel.ivipu.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_NewsObject;
import com.xtel.ivipu.presenter.NewsInfoPresenter;
import com.xtel.ivipu.view.activity.inf.INewsInfoView;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.sdk.callback.DialogListener;

public class NewsInfoActivity extends IActivity implements INewsInfoView {
    protected CallbackManager callbackManager;
    protected NewsInfoPresenter presenter;

    protected ImageView img_banner;
    protected ImageButton img_share, img_like;
    protected TextView txt_comment, txt_rate, txt_like, txt_store_name, txt_get_voucher, txt_desc, txt_view_comment, txt_more_news;
    protected Button btn_rate;
    protected RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        callbackManager = CallbackManager.create(this);
        presenter = new NewsInfoPresenter(this);

        initToolbar(R.id.news_info_toolbar, null);
        initView();
        initRatingBar();

        presenter.getData();
    }

    /**
     * Khởi tạo các view trong layout
     * */
    protected void initView() {
        img_banner = findImageView(R.id.news_info_header_img_banner);
        img_share = findImageButton(R.id.news_info_header_img_share);
        img_like = findImageButton(R.id.news_info_header_img_like);

        txt_comment = findTextView(R.id.news_info_header_txt_comment);
        txt_rate = findTextView(R.id.news_info_header_txt_rate);
        txt_like = findTextView(R.id.news_info_header_txt_like);
        txt_store_name = findTextView(R.id.news_info_header_txt_store_name);
        txt_get_voucher = findTextView(R.id.news_info_header_txt_voucher);

        txt_desc = findTextView(R.id.news_info_content_txt_desc);
        txt_view_comment = findTextView(R.id.news_info_content_txt_comment);
        txt_more_news = findTextView(R.id.news_info_content_txt_more_news);

        btn_rate = findButton(R.id.news_info_content_btn_rate);
    }

    /**
     * Khởi tạo label đánh giá
     * */
    protected void initRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.news_info_content_rating_bar);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.bottom_navigatin_default), PorterDuff.Mode.SRC_ATOP); // Không chọn
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.bottom_navigatin_selected), PorterDuff.Mode.SRC_ATOP);
    }



    /**
     * Hiển thị progress bar thông báo
     * */
    @Override
    public void showProgressBar(String message) {
        showProgressBar(false, false, null, message);
    }

    /**
     * Lấy dữ liệu trên server thành công
     * */
    @Override
    public void onGetDataaSuccess(RESP_NewsObject obj) {
        closeProgressBar();

        WidgetHelper.getInstance().setImageURL(img_banner, obj.getBanner());
        WidgetHelper.getInstance().setTextViewNumber(txt_comment, obj.getComment());
        WidgetHelper.getInstance().setTextViewNumber(txt_rate, obj.getRate());
        WidgetHelper.getInstance().setTextViewNumber(txt_like, obj.getLike());

        WidgetHelper.getInstance().setTextViewWithResult(txt_store_name, obj.getStore_name(), getString(R.string.message_not_update_store_name));
        WidgetHelper.getInstance().setTextViewHtml(txt_desc, obj.getDescription());
        WidgetHelper.getInstance().setTextViewNoResult(txt_view_comment, getString(R.string.layout_had) + " " + obj.getComment() + " " + getString(R.string.layout_comment));

        ratingBar.setRating(((float) obj.getRate()));
    }

    /**
     * Lấy dữ liệu trên server thất bại
     * Thông báo cho người dùng
     * */
    @Override
    public void onGetDataError(String message) {
        closeProgressBar();

        showMaterialDialog(false, false, null, message, null, getString(R.string.message_ok), new DialogListener() {
            @Override
            public void onClicked(Object object) {

            }

            @Override
            public void onCancel() {
                closeDialog();
                finish();
            }
        });
    }

    /**
     * Lấy phiên làm việc mới khi phiên làm việc cũ đã hết hạn
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

    @Override
    public Activity getActivity() {
        return this;
    }








    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
