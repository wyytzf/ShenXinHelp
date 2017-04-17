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

    //private static final String BASE_URL= "http://10.170.67.107:8080/";
    //private static final String BASE_URL = "http://192.168.0.21:8080/BodyMindHelper/";
    private static final String BASE_URL = "http://192.168.0.75:8080/BodyMindHelper/";
    private static final String BASE_AUTH_URL = getBaseUrl() + "authz/oauth/token";
    private static final String BASE_RS_URL = getBaseUrl() + "rs/rs/";


    public static final String LOGIN = getBaseUrl() + "Login?";
    public static final String REGISTER = getBaseUrl() + "Register?";
    public static final String GETSCHOOL = getBaseUrl() + "GetAllSchool";
    public static final String GETCLASS = getBaseUrl() + "GetClassBySchoolID?";
    public static final String ADDPERSONINFO = getBaseUrl() + "AddPersonInfo?";
    public static final String AddTeacherInfo = getBaseUrl() + "AddTeacherInfo?";
    public static final String AddParentInfo = getBaseUrl() + "AddParentInfo?";


    public static final String GETEXERCISETOFOUR = getBaseUrl() + "GetExerciseToFour?";
    public static final String GETDOEXERCISEINFO = getBaseUrl() + "GetDoExerciseInfo?";
    public static String GetHomePageImages = getBaseUrl() + "GetHomePageImages";
    public static String GetExerciseItem = getBaseUrl() + "GetExerciseItem?";
    public static String GetAllMyRing= getBaseUrl() +"GetAllMyRing";
    public static String GetRingMember= getBaseUrl() +"GetRingMember";
    public static String Feedback = getBaseUrl() + "Feedback";
    public static String GetAllPosts= getBaseUrl() +"GetAllPosts";
    public static String GetPlanByDate= getBaseUrl() +"GetPlanByDate";
    public static String CreatePK= getBaseUrl() +"CreatePK";
    public static String GetAllPKRecords= getBaseUrl() +"GetAllPKRecords";
    public static String CreateRing=getBaseUrl() +"CreateRing";
    public static String AddPersonToRing=getBaseUrl() +"AddPersonToRing";
    public static String GetAllExercise= getBaseUrl() +"GetAllExercise";
    public static String CustomPlan= getBaseUrl() +"CustomPlan";
    public static String ShareToRing= getBaseUrl() +"ShareToRing";
    public static String DOEXERCISE = getBaseUrl() + "DoExercise?";
    public static String AddCloseRelationship = getBaseUrl() + "AddCloseRelationship?";
    public static String GetAllRelationshipType = getBaseUrl() + "GetAllRelationshipType";
    public static String GetMyAllRelationship = getBaseUrl() + "GetMyAllRelationship";
    public static String GetMyChild = getBaseUrl() + "GetMyChild";
    public static String GetAChildConsumedCaloriesADay = getBaseUrl() + "GetAChildConsumedCaloriesADay";
    public static String GetAChildConsumedCaloriesAWeek = getBaseUrl() + "GetAChildConsumedCaloriesAWeek";
    public static String GetAChildConsumedCaloriesAMonth = getBaseUrl() + "GetAChildConsumedCaloriesAMonth";
    public static String GetAChildConsumedCaloriesAYear = getBaseUrl() + "GetAChildConsumedCaloriesAYear";

    public static String GetTopThreeStudent = getBaseUrl() + "GetTopThreeStudent";
    public static String GetLastThreeStudent = getBaseUrl() + "GetLastThreeStudent";
    public static String GetUpTopThreeStudent = getBaseUrl() + "GetUpTopThreeStudent";

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
