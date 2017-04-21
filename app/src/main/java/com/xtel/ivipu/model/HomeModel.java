package com.xtel.ivipu.model;

import android.util.Log;

import com.xtel.ivipu.model.RESP.RESP_News;
import com.xtel.ivipu.model.RESP.RESP_NewsObject;
import com.xtel.ivipu.model.entity.NewsActionEntity;
import com.xtel.ivipu.model.entity.RateObject;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;

/**
 * Created by vivhp on 2/14/2017
 */

public class HomeModel extends Model {

    public static HomeModel instance = new HomeModel();

    public static HomeModel getInstance() {
        return instance;
    }

    public void getNews(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getNewsInfomation(String url_news_info, String session, ResponseHandle<RESP_NewsObject> responseHandle) {
        requestServer.getApi(url_news_info, session, responseHandle);
    }

    public void getNewsInfo(int news_id, ResponseHandle responseHandle) {
        String url = Constants.SERVER_IVIP + Constants.NEWS_INFO + news_id;
        String session = LoginManager.getCurrentSession();

        Log.e("getNewsInfo", "url " + url + "  session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void postNewsAction(String url_new_action, String action_object, String session, ResponseHandle responseHandle) {
        requestServer.postApi(url_new_action, action_object, session, responseHandle);
    }

    public void likeNews(int news_id, ResponseHandle responseHandle) {
        String url = Constants.SERVER_IVIP + Constants.NEWS_ACTION;
        String session = LoginManager.getCurrentSession();

        NewsActionEntity newsAction = new NewsActionEntity();
        newsAction.setNews_id(news_id);
        newsAction.setAction(1);

        Log.e("likeNews", "url " + url + "  session " + session);
        Log.e("likeNews", "object " + JsonHelper.toJson(newsAction));
        requestServer.postApi(url, JsonHelper.toJson(newsAction), session, responseHandle);
    }

    public void postChekinAction(String url_checkin, String checkInObj, String session, ResponseHandle responseHandle) {
        requestServer.postApi(url_checkin, checkInObj, session, responseHandle);
    }

    public void getMyShopCheckin(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getNewsComment(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getStoreInfo(RESP_News resp_news, ResponseHandle responseHandle) {
        Integer id = null;
        String type = null;

        if (resp_news.getChain_store_id() != null)
            id = resp_news.getChain_store_id();
        else if (resp_news.getStore_id() != null)
            id = resp_news.getStore_id();

        if (resp_news.getChain_store_id() != null)
            type = "Chain";
        else if (resp_news.getStore_id() != null)
            type = "Store";

        if (id == null || type == null) {
            Log.e("getStoreInfo", " null ");
            responseHandle.onError(new Error(-1, "ERROR_PARSER_RESPONSE", "HAVE_ERROR"));
            return;
        }

        String url = Constants.SERVER_IVIP + STORE_INFO_ID + id + STORE_INFO_TYPE + type;
        String session = LoginManager.getCurrentSession();

        Log.e("getStoreInfo", "url " + url + "  session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void postNewsComment(String url, String jsonObject, String session, ResponseHandle responseHandle) {
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }

    public void postNewsRates(String url, String jsonObject, String session, ResponseHandle responseHandle) {
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }

    public void rateNews(int news_id, double rate_value, ResponseHandle responseHandle) {
        String url = Constants.SERVER_IVIP + Constants.RATE_ACTION;
        String session = LoginManager.getCurrentSession();

        RateObject rateObject = new RateObject();
        rateObject.setNews_id(news_id);
        rateObject.setRates(rate_value);

        Log.e("rateNews", "url " + url + "  session " + session);
        Log.e("rateNews", "object " + JsonHelper.toJson(rateObject));
        requestServer.postApi(url, JsonHelper.toJson(rateObject), session, responseHandle);
    }

    public void getNewsVoucher(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getNewsVoucher(int news_id, ResponseHandle responseHandle) {
        String url = Constants.SERVER_IVIP + "v0.1/news/" + news_id + "/voucher";
        String session = LoginManager.getCurrentSession();

        Log.e("getNewsVoucher", "url " + url + "  session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void getNewsSuggestion(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getGalleryArray(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getAddress(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getHistory(String url, String session, ResponseHandle responseHandle){
        requestServer.getApi(url, session, responseHandle);
    }

    public void getFavorite(String url, String session, ResponseHandle responseHandle){
        requestServer.getApi(url, session, responseHandle);
    }

    public void getMemberCard(String url, String session, ResponseHandle responseHandle){
        requestServer.getApi(url, session, responseHandle);
    }

    public void getMemberCard(int page, ResponseHandle responseHandle){
        String url = Constants.SERVER_IVIP + "v0.1/user/member_card?page=" + page + "&pagesize=" + 10;
        String session = LoginManager.getCurrentSession();

        Log.e("getMemberCard", "url " + url + "  session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void getHistoryTransactionMemberCard(String url, String session, ResponseHandle responseHandle){
        requestServer.getApi(url, session, responseHandle);
    }

    public void getHistoryTransaction(int id, int page, ResponseHandle responseHandle) {
        String url = Constants.SERVER_IVIP + "v0.1/user/member_card/" + id + "/history?page=" + page + "&pagesize=" + 10;
        String session = LoginManager.getCurrentSession();

        Log.e("getHistoryTransaction", "url " + url + "  session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void getListVoucher(int page, ResponseHandle responseHandle){
        String url = Constants.SERVER_IVIP + "v0.1/user/vouchers?page=" + page + "&pagesize=" + 10;
        String session = LoginManager.getCurrentSession();

        Log.e("getListVoucher", "url " + url + "  session " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void getNotifyNumber(String url, String session, ResponseHandle responseHandle) {
        requestServer.getApi(url, session, responseHandle);
    }

    public void getPolyLine(double from_lat, double from_lng, double to_lat, double to_lng, ResponseHandle responseHandle) {
        String url = Constants.POLY_HTTP + from_lat + "," + from_lng + Constants.POLY_DESTINATION + to_lat + "," + to_lng;
        requestServer.getApi(url, null, responseHandle);
    }
}
