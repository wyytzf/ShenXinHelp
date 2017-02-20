package com.xd.shenxinhelp.model;

/**
 * Created by MMY on 2017/2/15.
 */

public class LittleGoal {
    private User user;
    private String content;

    public void setUser(User user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }
}
