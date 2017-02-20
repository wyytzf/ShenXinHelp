package com.xd.shenxinhelp.model;

import java.util.List;

/**
 * Created by MMY on 2017/2/14.
 */

public class Group {
    private String name;
    private String des;
    private String urlStr;

    private List<GroupDetail> groupList;

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

    public void setGroupList(List<GroupDetail> groupList) {
        this.groupList = groupList;
    }

    public List<GroupDetail> getGroupList() {
        return groupList;
    }
}
