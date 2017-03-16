package com.xd.shenxinhelp.model;

import java.io.Serializable;

/**
 * Created by MMY on 2017/2/14.
 */

public class GroupDetail implements Serializable{
    private String name;
    private String des;
    private String urlStr;
    private String id;
    private String ownerid;
    private String type;

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }
}
