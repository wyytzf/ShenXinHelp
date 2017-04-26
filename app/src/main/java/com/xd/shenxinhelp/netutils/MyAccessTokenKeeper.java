package com.xd.shenxinhelp.netutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.xd.shenxinhelp.model.QQAccessToken;

import java.util.Map;

/**
 * Created by MMY on 2017/4/20.
 */

public class MyAccessTokenKeeper {
    private static final String PREFERENCES_NAME = "shenxinbang_access_token";

    //微博
    private static final String KEY_UID           = "weibo_uid";
    private static final String KEY_ACCESS_TOKEN  = "weibo_access_token";
    private static final String KEY_EXPIRES_IN    = "weibo_expires_in";
    //qq
    private static final String QQ_UID           = "qq_uid";
    private static final String QQ_ACCESS_TOKEN  = "qq_access_token";
    private static final String QQ_EXPIRES_IN    = "qq_expires_in";
    private static final String QQ_OPENKEY    = "qq_openkey";
    private static final String QQ_PFKEY    = "qq_pfkey";
    private static final String QQ_PF    = "qq_pf";

    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param context 应用程序上下文环境
     * @param map   Token 的map 对象
     */
    public static void writeWeiboAccessToken(Context context, Map<String, String> map) {
        if (null == context || null == map) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_UID, map.get("uid"));
        editor.putString(KEY_ACCESS_TOKEN, map.get("access_token"));
        String expires=map.get("expire_time");
        editor.putLong(KEY_EXPIRES_IN, Long.parseLong(expires));
        editor.commit();
    }
    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @param context 应用程序上下文环境
     *
     * @return 返回 Token 对象
     */
    public static Oauth2AccessToken readWeiboAccessToken(Context context) {
        if (null == context) {
            return null;
        }

        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        return token;
    }
    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param context 应用程序上下文环境
     * @param map   Token 的map 对象
     */
    public static void writeQQAccessToken(Context context, Map<String, String> map) {
        if (null == context || null == map) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(QQ_UID, map.get("openid"));
        editor.putString(QQ_ACCESS_TOKEN, map.get("access_token"));
        String expires=map.get("expires_in");
        editor.putLong(QQ_EXPIRES_IN, Long.parseLong(expires));
        editor.putString(QQ_OPENKEY, map.get("access_token"));
        editor.putString(QQ_PFKEY, map.get("pfkey"));

        editor.putString(QQ_PF, map.get("pf"));

        editor.commit();
    }
    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @param context 应用程序上下文环境
     *
     * @return 返回 Token 对象
     */
    public static QQAccessToken readQQAccessToken(Context context) {
        if (null == context) {
            return null;
        }

        QQAccessToken token = new QQAccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setQqOpenID(pref.getString(QQ_UID, ""));
        token.setmAccessToken(pref.getString(QQ_ACCESS_TOKEN, ""));
        token.setmExpiresTime(pref.getLong(QQ_EXPIRES_IN, 0));
        token.setPf(pref.getString(QQ_PF,""));
        token.setPfKey(pref.getString(QQ_PFKEY,""));
        token.setOpenKey(pref.getString(QQ_OPENKEY,""));
        return token;
    }


}
