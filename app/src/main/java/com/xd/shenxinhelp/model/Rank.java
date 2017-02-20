package com.xd.shenxinhelp.model;

/**
 * Created by MMY on 2017/2/14.
 */

public class Rank {
    private String name;
    private String healthValue;
    private String photoUrl;

    public void setName(String name) {
        this.name = name;
    }

    public void setHealthValue(String healthValue) {
        this.healthValue = healthValue;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getHealthValue() {
        return healthValue;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
