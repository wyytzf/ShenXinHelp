package com.xd.shenxinhelp.model;

/**
 * Created by weiyuyang on 17-3-15.
 */

public class News {
    private String title;
    private String imageUrl;
    private String webUrl;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getTitle() {

        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
