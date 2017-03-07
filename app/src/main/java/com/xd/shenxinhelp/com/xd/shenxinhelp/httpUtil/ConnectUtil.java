package com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 发起HTTP请求的工具类
 * 
 * @author mmy
 */
public class ConnectUtil {
//	private static String DLTAG = "com.xd.connect.ConnectUtil";

	public static final String HOST_ERROR = "HOST_ERROR";
	public static String HTTP_RE_ERROE_CODE = "";

	public static final String UTF_8 = "UTF-8";
	public static final String POST = "POST";
	public static final String GET = "GET";

	private final static int READ_TIMEOUT = 20000;
	private final static int CONNECT_TIMEOUT = 20000;
	private final static int TEST_TIMEOUT = 20000;

	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";

	// public final static String API_HOST =
	// "http://192.168.0.21:8080/ASEYiYi/";
	public final static String API_HOST = "http://192.168.0.21:8080/BodyMindHelper/";
//	public final static String API_HOST = "http://192.168.0.151:8080/ASEYiYi/";
	public static String GetHomePageImages = API_HOST + "GetHomePageImages";
	public static String GetExerciseItem = API_HOST + "GetExerciseItem";

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connect = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// System.out.println("connect: " + connect);
		if (connect == null) {
			return false;
		} else// get all network info
		{
			NetworkInfo[] info = connect.getAllNetworkInfo();
			// System.out.println("info: " + info);
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}




}
