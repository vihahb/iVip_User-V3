package com.xtel.ivipu.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by vivhp on 2/14/2017.
 */

public class VoucherObj {

    @Expose
    private String code;
    @Expose
    private String qr_code;
    @Expose
    private String bar_code;
    @Expose
    private Long create_time;
    @Expose
    private Long expired_time;
    @Expose
    private Integer status;

    public VoucherObj() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Long expired_time) {
        this.expired_time = expired_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VoucherObj{" +
                "code='" + code + '\'' +
                ", qr_code='" + qr_code + '\'' +
                ", bar_code='" + bar_code + '\'' +
                ", create_time=" + create_time +
                ", expired_time=" + expired_time +
                ", status=" + status +
                '}';
    }
}
