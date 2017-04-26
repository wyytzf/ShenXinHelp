package com.xd.shenxinhelp.model;

/**
 * Created by MMY on 2017/4/21.
 */

public class QQAccessToken {
    private String qqOpenID;
    private String mAccessToken ;
    private String mRefreshToken ;
    private long mExpiresTime ;
    private String qqKey;
    private String pf;
    private String pfKey;
    private String openKey;

    public void setOpenKey(String openKey) {
        this.openKey = openKey;
    }

    public String getOpenKey() {
        return openKey;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public void setPfKey(String pfKey) {
        this.pfKey = pfKey;
    }

    public String getPf() {
        return pf;
    }

    public String getPfKey() {
        return pfKey;
    }

    public void setQqOpenID(String qqOpenID) {
        this.qqOpenID = qqOpenID;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public void setmRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    public void setmExpiresTime(long mExpiresTime) {
        this.mExpiresTime = mExpiresTime;
    }

    public void setQqKey(String qqKey) {
        this.qqKey = qqKey;
    }

    public String getQqOpenID() {
        return qqOpenID;
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public String getmRefreshToken() {
        return mRefreshToken;
    }

    public long getmExpiresTime() {
        return mExpiresTime;
    }

    public String getQqKey() {
        return qqKey;
    }
}
