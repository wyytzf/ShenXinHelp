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

    //    private static final String BASE_URL= "http://10.170.67.107:8080/";
//    private static final String BASE_URL = "http://192.168.0.21:8080/BodyMindHelper/";
    public final static String BASE_URL = "http://10.170.7.137:8080/BodyMindHelper/";


    public static final String LOGIN = getBaseUrl() + "Login?";
    public static final String REGISTER = getBaseUrl() + "Register?";
    public static final String GETSCHOOL = getBaseUrl() + "GetAllSchool";
    public static final String GETCLASS = getBaseUrl() + "GetClassBySchoolID?";
    public static final String ADDPERSONINFO = getBaseUrl() + "AddPersonInfo?";



    public static String GetHomePageImages = getBaseUrl() + "GetHomePageImages";
    public static String GetExerciseItem = getBaseUrl() + "GetExerciseItem";
    public static String GetAllMyRing= getBaseUrl() +"GetAllMyRing";
    public static String GetRingMember= getBaseUrl() +"GetRingMember";
    public static String getBaseUrl() {
        return BASE_URL;
    }



}
