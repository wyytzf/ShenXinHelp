package com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil;

import android.app.Application;
import android.content.Context;


/**
 * @author MMY
 * @since 20161221
 * 共享的全局数据
 */
public class AppUtil extends Application {

    private Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Context getContext() {
        return context;
    }

    private static final String BASE_URL= "http://10.170.67.107:8080/";
//    private static final String BASE_URL= "http://192.168.0.151:8080/";
    private static final String BASE_AUTH_URL= getBaseUrl()+"authz/oauth/token";
    private static final String BASE_RS_URL= getBaseUrl()+"rs/rs/";

    public static String getBaseUrl() {
       return BASE_URL;
    }

    public static String getBaseAuthUrl() {
        return BASE_AUTH_URL;
    }

    public static String getBaseRsUrl() {
        return BASE_RS_URL;
    }



}
