package com.xtel.ivipu.model;

import com.xtel.nipservicesdk.callback.RequestServer;

/**
 * Created by vivhp on 12/28/2016
 */

public abstract class Model {
    protected final String STORE_INFO_ID = "v0.1/info/";
    protected final String STORE_INFO_TYPE = "?type=";

    protected final String ADDRESS_STORE_ID = "v0.1/address/";
    protected final String ADDRESS_TYPE = "?type=";

    protected final String NEWS_IN_STORE_ID = "v0.1/store/";
    protected final String NEWS_IN_STORE_PAGE = "/news?page=";
    protected final String NEWS_IN_STORE_PAGESIZE = "&pagesize=10";

    protected RequestServer requestServer = new RequestServer();

}
