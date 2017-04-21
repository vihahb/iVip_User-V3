package com.xtel.ivipu.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by vivhp on 4/19/2017.
 */

public class CityCodeObj implements Serializable {

    @Expose
    private String area_code;
    @Expose
    private String area_name;

    public CityCodeObj() {
    }

    public CityCodeObj(String area_code, String area_name) {
        this.area_code = area_code;
        this.area_name = area_name;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    @Override
    public String toString() {
        return "CityCodeObj{" +
                "area_code='" + area_code + '\'' +
                ", area_name='" + area_name + '\'' +
                '}';
    }
}
