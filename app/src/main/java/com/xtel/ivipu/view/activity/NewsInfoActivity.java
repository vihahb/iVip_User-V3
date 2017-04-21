package com.xtel.ivipu.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.model.RESP.RESP_News;
import com.xtel.ivipu.model.RESP.RESP_Voucher;
import com.xtel.ivipu.presenter.NewsInfoPresenter;
import com.xtel.ivipu.view.activity.inf.INewsInfoView;
import com.xtel.ivipu.view.widget.WidgetHelper;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;

public class NewsInfoActivity extends IActivity implements View.OnClickListener, INewsInfoView {
    protected CallbackManager callbackManager;
    protected NewsInfoPresenter presenter;

    /*
    * Layout header
    * */
    protected ImageView header_img_banner;
    protected ImageButton header_img_share, header_img_like;
    protected TextView header_txt_comment, header_txt_rate, header_txt_like, header_txt_store_name, header_txt_get_voucher;

    /*
    * Voucher
    * */
    protected LinearLayout voucher_layout_voucher;
    protected ImageView voucher_img_qr_code;
    protected TextView voucher_txt_code, voucher_txt_expired;

    /*
    * Layout content
    * */
    protected LinearLayout content_layout_current_rate;
    protected TextView content_txt_desc, content_txt_current_rate, content_txt_view_comment, content_txt_more_news;
    protected Button content_btn_rate;
    protected RatingBar content_ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        callbackManager = CallbackManager.create(this);
        presenter = new NewsInfoPresenter(this);

        initToolbar(R.id.news_info_toolbar, null);
        initView();
        initRatingBar();
        initListener();

        presenter.getData();
    }

    /**
     * Khởi tạo các view trong layout
     */
    protected void initView() {
//        Header
        header_img_banner = findImageView(R.id.news_info_header_img_banner);
        header_img_share = findImageButton(R.id.news_info_header_img_share);
        header_img_like = findImageButton(R.id.news_info_header_img_like);

        header_txt_comment = findTextView(R.id.news_info_header_txt_comment);
        header_txt_rate = findTextView(R.id.news_info_header_txt_rate);
        header_txt_like = findTextView(R.id.news_info_header_txt_like);
        header_txt_store_name = findTextView(R.id.news_info_header_txt_store_name);
        header_txt_get_voucher = findTextView(R.id.news_info_header_txt_voucher);

//        Voucher
        voucher_layout_voucher = findLinearLayout(R.id.news_info_voucher_itemView);
        voucher_img_qr_code = findImageView(R.id.news_info_voucher_img_qr_code);
        voucher_txt_code = findTextView(R.id.news_info_voucher_txt_code);
        voucher_txt_expired = findTextView(R.id.news_info_voucher_txt_expired);

//        Content
        content_layout_current_rate = findLinearLayout(R.id.news_info_content_layout_current_rate);
        content_txt_desc = findTextView(R.id.news_info_content_txt_desc);
        content_txt_current_rate = findTextView(R.id.news_info_content_txt_current_rate);
        content_txt_view_comment = findTextView(R.id.news_info_content_txt_comment);
        content_txt_more_news = findTextView(R.id.news_info_content_txt_more_news);
        content_btn_rate = findButton(R.id.news_info_content_btn_rate);
    }

    /**
     * Khởi tạo label đánh giá
     */
    @SuppressWarnings("deprecation")
    protected void initRatingBar() {
        content_ratingBar = (RatingBar) findViewById(R.id.news_info_content_rating_bar);

        LayerDrawable stars = (LayerDrawable) content_ratingBar.getProgressDrawable();
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.bottom_navigatin_default), PorterDuff.Mode.SRC_ATOP); // Không chọn
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.bottom_navigatin_selected), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Lăng nghe sự kiện khi chạm của view
     */
    protected void initListener() {
        header_img_share.setOnClickListener(this);
        header_img_like.setOnClickListener(this);
        header_txt_store_name.setOnClickListener(this);
        header_txt_get_voucher.setOnClickListener(this);

        content_btn_rate.setOnClickListener(this);
        content_txt_view_comment.setOnClickListener(this);
        content_txt_more_news.setOnClickListener(this);
    }

    /**
     * Hiển thị thông tin của bản tin
     */
    protected void setNewsInfo(RESP_News obj) {
//        Header
        WidgetHelper.getInstance().setImageURL(header_img_banner, obj.getBanner());
        WidgetHelper.getInstance().setTextViewNumber(header_txt_comment, obj.getComment());
        WidgetHelper.getInstance().setTextViewNumber(header_txt_rate, obj.getRate());
        WidgetHelper.getInstance().setTextViewNumber(header_txt_like, obj.getLike());
        WidgetHelper.getInstance().setTextViewWithResult(header_txt_store_name, obj.getStore_name(), getString(R.string.message_not_update_store_name));
        WidgetHelper.getInstance().setTextViewCircleLogo(header_txt_store_name, obj.getLogo(), true);
        WidgetHelper.getInstance().setImageButtonLike(header_img_like, obj.getFavorite());

//        Content
        WidgetHelper.getInstance().setTextViewHtml(content_txt_desc, obj.getDescription());
        WidgetHelper.getInstance().setTextViewNoResult(content_txt_view_comment, getString(R.string.layout_had) + " " + obj.getComment() + " " + getString(R.string.layout_comment));
    }

    /**
     * Hiển thị thông tin voucher của bản tin
     */
    protected void setVoucher(RESP_Voucher voucherObj) {
        if (voucherObj == null) {
            setVoucherLayoutVisibility(false);
        } else {
            setVoucherLayoutVisibility(true);

            WidgetHelper.getInstance().setImageURL(voucher_img_qr_code, voucherObj.getQr_code());
            WidgetHelper.getInstance().setTextViewWithVisibility(voucher_txt_code, voucherObj.getCode());
            setExpiredDate(voucherObj.getExpired_time());
        }
    }

    /**
     * Hiển thị thời gian sử dụng voucher
     */
    protected void setExpiredDate(Long milliseconds) {
        if (milliseconds == null)
            voucher_txt_expired.setVisibility(View.GONE);
        else {
            if (milliseconds < System.currentTimeMillis()) {
                WidgetHelper.getInstance().setTextViewDateWithResult(voucher_txt_expired, getString(R.string.layout_expired) + " ", milliseconds);
                voucher_layout_voucher.setBackgroundColor(Color.parseColor("#CCCCCC"));
                voucher_txt_expired.setTextColor(Color.parseColor("#C80000"));
            } else {
                WidgetHelper.getInstance().setTextViewDateWithResult(voucher_txt_expired, getString(R.string.layout_expire) + " ", milliseconds);
                voucher_layout_voucher.setBackgroundColor(Color.WHITE);
                voucher_txt_expired.setTextColor(Color.parseColor("#079207"));
            }
        }
    }

    /**
     * Hiển thị layout voucher
     */
    protected void setVoucherLayoutVisibility(boolean visibility) {
        if (visibility) {
            voucher_layout_voucher.setVisibility(View.VISIBLE);
            header_txt_get_voucher.setVisibility(View.GONE);
        } else {
            voucher_layout_voucher.setVisibility(View.GONE);
            header_txt_get_voucher.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Ẩn hay hiện layout rating
     */
    protected void setRate(double current_rate) {
        if (current_rate == 0) {
            setRateLayoutVisibility(false);
        } else {
            setRateLayoutVisibility(true);

            String content = getString(R.string.layout_rated) + " " + current_rate + " " + getString(R.string.layout_star);
            WidgetHelper.getInstance().setTextViewNoResult(content_txt_current_rate, content);
        }
    }

    /**
     * Hiển thị layout rate
     */
    protected void setRateLayoutVisibility(boolean visibility) {
        if (visibility) {
            content_txt_current_rate.setVisibility(View.VISIBLE);
            content_layout_current_rate.setVisibility(View.GONE);
        } else {
            content_txt_current_rate.setVisibility(View.GONE);
            content_layout_current_rate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void startActivity(Class clazz, String key, Object object) {
        super.startActivity(clazz, key, object);
    }

    /**
     * Hiển thị progress bar thông báo
     */
    @Override
    public void showProgressBar(String message) {
        showProgressBar(false, false, null, message);
    }

    /**
     * Lấy dữ liệu trên server thành công
     */
    @Override
    public void onGetDataaSuccess(RESP_News obj) {
        closeProgressBar();

        setNewsInfo(obj);
        setVoucher(obj.getVoucher());
        setRate(obj.getCurrent_rate());
    }

    /**
     * Lấy dữ liệu trên server thất bại
     * Thông báo cho người dùng
     */
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
     * Request lấy mã khuyễn mãi thành công
     * */
    @Override
    public void onGetVoucherSuccess(RESP_Voucher obj) {
        closeProgressBar();

        setVoucher(obj);
    }

    /**
     * Request like hoặc unlike thành công
     * */
    @Override
    public void onLikeSuccess(int favorite) {
        closeProgressBar();

        WidgetHelper.getInstance().setImageButtonLike(header_img_like, favorite);
    }

    /**
     * Request đánh giá thành công
     * */
    @Override
    public void onRateSuccess(double current_rate) {
        closeProgressBar();

        setRate(current_rate);
    }

    /**
     * Lấy phiên làm việc mới khi phiên làm việc cũ đã hết hạn
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
                startActivityFinish(LoginActivity.class);
            }
        });
    }

    /**
     * Request lên server thất bại
     * Đóng thông báo progress bar và thông báo lỗi
     */
    @Override
    public void onRequestError(int type, Error error) {
        closeProgressBar();

        showShortToast(JsonParse.getCodeMessage(this, error.getCode(), getString(R.string.error_try_again)));
    }

    /**
     * Hiển thị thông báo
     */
    @Override
    public void showShortToast(String message) {
        super.showShortToast(message);
    }

    /**
     * Thông báo lỗi không có kết nối mạng
     */
    @Override
    public void onNoInternet() {
        showShortToast(getString(R.string.no_internet));
    }

    @Override
    public void onNotLogged() {
        showShortToast(getString(R.string.need_login_to_action));
        finishAffinity();
        startActivity(LoginActivity.class);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_info_header_img_like:
                presenter.likeNews();
                break;
            case R.id.news_info_header_txt_store_name:
                presenter.viewStoreInfo();
                break;
            case R.id.news_info_header_txt_voucher:
                presenter.getVoucher();
                break;
            case R.id.news_info_content_btn_rate:
                presenter.rateNews(content_ratingBar.getRating());
                break;
            default:
                break;
        }
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
