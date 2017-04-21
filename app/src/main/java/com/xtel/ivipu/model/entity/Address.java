package com.xtel.ivipu.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Vulcl on 4/20/2017
 */

public class Address implements Serializable {
    @Expose
    private String address;
    @Expose
    private double location_lng;
    @Expose
    private double location_lat;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLocation_lng() {
        return location_lng;
    }

    public void setLocation_lng(double location_lng) {
        this.location_lng = location_lng;
    }

    public double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }
}