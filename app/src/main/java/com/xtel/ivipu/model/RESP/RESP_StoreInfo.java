package com.xtel.ivipu.model.RESP;

import com.google.gson.annotations.Expose;
import com.xtel.ivipu.model.entity.Address;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vulcl on 4/20/2017
 */

public class RESP_StoreInfo extends RESP_Basic implements Serializable {
    @Expose
    private String description;
    @Expose
    private String phone;
    @Expose
    private String name;
    @Expose
    private String logo;
    @Expose
    private String banner;
    @Expose
    private ArrayList<Address> address;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }
}