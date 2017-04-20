package com.xtel.ivipu.model;

import com.xtel.nipservicesdk.callback.RequestServer;

/**
 * Created by vivhp on 12/28/2016
 */

public abstract class Model {
    protected final String STORE_INFO_ID = "v0.1/info/";
    protected final String STORE_INFO_TYPE = "?type=";

    protected RequestServer requestServer = new RequestServer();

}
