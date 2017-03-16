package com.xd.shenxinhelp.model;

import java.io.Serializable;

/**
 * Created by koumiaojuan on 2017/3/16.
 */

public class Team implements Serializable{
    private String account;
    private String headerUrl;
    private int healthDegree;
    private String schoolName;
    private String className;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getHealthDegree() {
        return healthDegree;
    }

    public void setHealthDegree(int healthDegree) {
        this.healthDegree = healthDegree;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


}
