package com.xtel.ivipu.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by vivhp on 2/14/2017.
 */

public class NewObjEntity {
    @Expose
    private int id;
    @Expose
    private String banner;
    @Expose
    private String logo;
    @Expose
    private String store_name;
    @Expose
    private int like;
    @Expose
    private int comment;
    @Expose
    private int rate;
    @Expose
    private int view;
    @Expose
    private long create_time;
    @Expose
    private int title;
    @Expose
    private int description;
    @Expose
    private int sales;
    @Expose
    private VoucherObj voucher;
    @Expose
    private int store_id;
    @Expose
    private int chain_store_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public VoucherObj getVoucher() {
        return voucher;
    }

    public void setVoucher(VoucherObj voucher) {
        this.voucher = voucher;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getChain_store_id() {
        return chain_store_id;
    }

    public void setChain_store_id(int chain_store_id) {
        this.chain_store_id = chain_store_id;
    }

    @Override
    public String toString() {
        return "RESP_NewsObject{" +
                "id=" + id +
                ", banner='" + banner + '\'' +
                ", logo='" + logo + '\'' +
                ", store_name='" + store_name + '\'' +
                ", like=" + like +
                ", comment=" + comment +
                ", rate=" + rate +
                ", view=" + view +
                ", create_time=" + create_time +
                ", title=" + title +
                ", description=" + description +
                ", sales=" + sales +
                ", voucher=" + voucher +
                ", store_id=" + store_id +
                ", chain_store_id=" + chain_store_id +
                '}';
    }
}
