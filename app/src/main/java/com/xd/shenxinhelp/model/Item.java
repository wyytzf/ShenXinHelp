package com.xd.shenxinhelp.model;

/**
 * Created by koumiaojuan on 2017/3/10.
 */

public class Item {
    private String id;
    private String imageUrl;
    private String title;
    private String heat;
    private String type;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
