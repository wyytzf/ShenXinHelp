package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.auth.WeiboAuth;
//import com.sina.weibo.sdk.auth.WeiboAuthListener;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.tsy.sdk.social.PlatformConfig;
//import com.tsy.sdk.social.PlatformType;
//import com.tsy.sdk.social.SocialApi;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ConnectUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.QQAccessToken;
import com.xd.shenxinhelp.netutils.MyAccessTokenKeeper;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private static final int ROLETYPE = 0;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView register;

    private boolean isNetConnect = false;
    private String type;

//    /** 微博 Web 授权类，提供登陆等功能  */
//    private WeiboAuth mWeiboAuth;
//    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
//    private Oauth2AccessToken mAccessToken;
//    /** 显示认证后的信息，如 AccessToken */
//    private TextView mTokenText;
//    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
//    private SsoHandler mSsoHandler;
    // private AuthInfo mAuthInfo;


    private RadioGroup r;
    private RadioButton r1;
    private RadioButton r2;
    private RadioButton r3;


    private static final String WX_APPID = "your wx appid";    //申请的wx appid
    private static final String QQ_APPID = "1105442761";    //申请的qq appid
    private static final String SINA_WB_APPKEY = "606791959";       //申请的新浪微博 appkey

    //
    //  private SocialApi mSocialApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
//        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        r = (RadioGroup) findViewById(R.id.radio_role_group);
        r1 = (RadioButton) findViewById(R.id.radio_role_student);
        r2 = (RadioButton) findViewById(R.id.radio_role_teacher);
        r3 = (RadioButton) findViewById(R.id.radio_role_parent);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


//        SMSSDK.getVerificationCode("86","17802929571");
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        // 创建微博实例
//        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 创建授权认证信息
        //mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

//        PlatformConfig.setWeixin(WX_APPID);
//        PlatformConfig.setQQ(QQ_APPID);
//        PlatformConfig.setSinaWB(SINA_WB_APPKEY);
//
//        mSocialApi = SocialApi.get(getApplicationContext());


        //findViewById(R.id.weibo_login).setWeiboAuthInfo(mAuthInfo, mLoginListener);
        findViewById(R.id.weibo_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSsoHandler = new SsoHandler(LoginActivity.this, mWeiboAuth);
//                mSsoHandler.authorize(new AuthListener());
                // mWeiboAuth.anthorize(new AuthListener());
                // 或者使用：mWeiboAuth.authorize(new AuthListener(), Weibo.OBTAIN_AUTH_TOKEN);
                //Toast.makeText(LoginActivity.this, "功能开发中……", Toast.LENGTH_LONG).show();
                //      mSocialApi.doOauthVerify(LoginActivity.this, PlatformType.SINA_WB, new MyAuthListener());
            }
        });

        findViewById(R.id.weixin_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "功能开发中……", Toast.LENGTH_LONG).show();
                //  mSocialApi.doOauthVerify(LoginActivity.this, PlatformType.WEIXIN , new MyAuthListener());
            }
        });

        findViewById(R.id.qq_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(LoginActivity.this, "功能开发中……", Toast.LENGTH_LONG).show();
                //  mSocialApi.doOauthVerify(LoginActivity.this, PlatformType.QQ, new MyAuthListener());
            }
        });
    }

    //    public class MyAuthListener implements com.tsy.sdk.social.listener.AuthListener {
//        @Override
//        public void onComplete(PlatformType platform_type, Map<String, String> map) {
//
//            if (platform_type.equals(PlatformType.SINA_WB)){
//                //Log.i("mmm", "-----------------login onComplete:" + map);
//                MyAccessTokenKeeper.writeWeiboAccessToken(LoginActivity.this,map);
//                String accesstoken=map.get("access_token");
//                String uid=map.get("uid");
//                if (accesstoken==null||accesstoken.equals("")){
//                    Toast.makeText(LoginActivity.this, platform_type + " 登录失败:" , Toast.LENGTH_SHORT).show();
//                }else {
//                    getIsAssociated("0",uid);
//                    //getWeiboStatusInfo(accesstoken,uid);
//
//                }
//            }else if (platform_type.equals(PlatformType.WEIXIN)){
//
//            }
//            else if (platform_type.equals(PlatformType.QQ)){
//                Log.i("mmm", "-----------------login onComplete:" + map);
//                MyAccessTokenKeeper.writeQQAccessToken(LoginActivity.this,map);
//                QQAccessToken aa=MyAccessTokenKeeper.readQQAccessToken(LoginActivity.this);
//                getQQStatusInfo(aa);
//            }
//            //Toast.makeText(LoginActivity.this, platform_type + " login onComplete", Toast.LENGTH_SHORT).show();
//            //Log.i("tsy", "login onComplete:" + map);
//        }
//
//        @Override
//        public void onError(PlatformType platform_type, String err_msg) {
//            Toast.makeText(LoginActivity.this, platform_type + " 登录失败:" + err_msg, Toast.LENGTH_SHORT).show();
//            //Log.i("tsy", "login onError:" + err_msg);
//        }
//
//        @Override
//        public void onCancel(PlatformType platform_type) {
//            //Toast.makeText(LoginActivity.this, platform_type + " login onCancel", Toast.LENGTH_SHORT).show();
//            //Log.i("tsy", "login onCancel");
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // mSocialApi.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public void getIsAssociated(final String type, final String uid) {


        new Thread() {
            @Override
            public void run() {
                String urlget = AppUtil.getBaseUrl() + "IsAssociated?associate_type=" + type + "&associate_account=" + uid;
                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);
                            String status = result.getString("reCode");
                            if (status.equals("SUCCESS")) {
                                parseResponse(jsonStr);
                                gotoMainPage(result.getString("type"));

                            } else {

                            }
                            //Log.i("mmm","-------------------------"+result.toString());

                        } catch (JSONException e) {
                            Log.e("mmm", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });
            }
        }.start();
    }

    public void gotoMainPage(String type) {
        Intent intent = null;
        switch (type) {
            case "student":
                intent.setClass(LoginActivity.this, ContainerActivity.class);
                break;
            case "teacher":
                intent.setClass(LoginActivity.this, TeacherMainActivity.class);
                break;
            case "parents":
                intent.setClass(LoginActivity.this, ParentMainActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }

    public void getQQStatusInfo(final QQAccessToken accessToken) {


        new Thread() {
            @Override
            public void run() {
                String yuanchuan = "GET&" + ConnectUtil.encodeParameters("/v3/user/get_info") + "&" +
                        ConnectUtil.encodeParameters("appid=" + QQ_APPID + "&openid=" + accessToken.getQqOpenID()
                                + "&openkey=" + accessToken.getOpenKey() + "&pf=" + accessToken.getPf());
                String key = "CTC0PEaCbCMXqeys" + "&";
                String sign = "";
                try {
                    SecretKeySpec localSecretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");//加密密钥
                    Mac localMac = Mac.getInstance("HmacSHA1");
                    localMac.init(localSecretKeySpec);
                    localMac.update(yuanchuan.getBytes("UTF-8"));//加密内容，这里使用时间
                    sign = Base64.encodeToString(localMac.doFinal(), 0).trim(); //获取加密结果并转BASE64
                } catch (Exception e) {

                }
                String urlget = "http://openapi.sparta.html5.qq.com/v3/user/get_info?" +
                        "appid=" + QQ_APPID + "&openid=" + accessToken.getQqOpenID() + "&openkey=" + accessToken.getOpenKey()
                        + "&pf=" + accessToken.getPf() + "&sig=" + sign;

                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);
                            //Log.i("mmm","-------------------------"+result.toString());

                        } catch (JSONException e) {
                            Log.e("mmm", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });
            }
        }.start();
    }

    public void getWeiboStatusInfo(final String accesstoken, final String uid) {


        new Thread() {
            @Override
            public void run() {
                String urlget = "https://api.weibo.com/2/users/show.json" + "?access_token=" + accesstoken + "&uid=" + uid;
                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);

                            //Log.i("mmm","-------------------------"+result.toString());

                        } catch (JSONException e) {
                            Log.e("mmm", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });
            }
        }.start();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }


        int yy = r.getCheckedRadioButtonId();
        if (yy == R.id.radio_role_student) {
            type = "0";
        } else if (yy == R.id.radio_role_teacher) {
            type = "2";
        } else {
            type = "1";
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isAccountValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_account));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isAccountValid(String account) {
        //TODO: Replace this with your own logic
        return account.length() > 1;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            // TODO: register the new account here.
            // 返回true代表登录成功
            boolean result = false;
            try {
                // 解析拿到的String字符串，判断是否登录成功


                String synchronous = OkHttp.getSynchronous(AppUtil.LOGIN + "account=" + mEmail + "&" + "psw=" + mPassword + "&" + "role=" + ROLETYPE);
                //Log.i("mmm",AppUtil.LOGIN + "account=" + mEmail + "&" + "psw=" + mPassword);
                result = parseResponse(synchronous);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            // isNetConnect = true;
            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            // mAuthTask = null;
            //showProgress(false);
//
            // sharePreference
//            if (!isNetConnect) {
//                Toast.makeText(LoginActivity.this, "网络链接不可用,请检查网络", Toast.LENGTH_LONG).show();
//                return;
//            }
//            Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
//            startActivity(intent);
//            finish();
            if (success) {
                SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("account", mEmail);
                editor.putString("password", mPassword);
                editor.commit();
                Intent intent = new Intent();
//                switch (type) {
//                    case "student":
//                        intent.setClass(LoginActivity.this, ContainerActivity.class);
//                        break;
//                    case "teacher":1
//                        intent.setClass(LoginActivity.this, TeacherMainActivity.class);
//                        break;
//                    case "parents":
//                        intent.setClass(LoginActivity.this, ParentMainActivity.class);
//                        break;
//                }
                intent.setClass(LoginActivity.this, ContainerActivity.class);
                startActivity(intent);
                finish();
            } else {
                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                onCancelled();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private boolean parseResponse(String synchronous) {
        //Log.e("reCode", synchronous);
        boolean result = false;
        String recode = "";
        try {
            JSONObject js = new JSONObject(synchronous);
            recode = js.getString("reCode");
            if (recode.equals("SUCCESS")) {
                SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                switch (js.getString("type")) {
                    case "student":
                        editor.putString("userid", js.getString("userid"));
                        editor.putString("sex", js.getString("sex"));
                        editor.putString("age", js.getString("age"));
                        editor.putString("height", js.getString("height"));
                        editor.putString("weight", js.getString("weight"));
                        editor.putString("credits", js.getString("credits"));
                        editor.putString("health_degree", js.getString("health_degree"));
                        editor.putString("level", js.getString("level"));
                        editor.putString("head_url", js.getString("head_url"));
                        editor.putString("class_id", js.getString("class_id"));
                        editor.putString("school_id", js.getString("school_id"));
                        editor.putString("className", js.getString("className"));
                        editor.putString("schoolName", js.getString("schoolName"));
                        editor.putString("heatLiang", js.getString("heatLiang"));
                        editor.putString("name", js.getString("name"));
                        editor.putString("type", js.getString("type"));
                        type = "student";
                        break;
                    case "teacher":
                        editor.putString("userid", js.getString("teacherid"));
                        editor.putString("sex", js.getString("sex"));
                        editor.putString("age", js.getString("age"));
                        editor.putString("head_url", js.getString("head_url"));
                        editor.putString("name", js.getString("name"));
                        editor.putString("school_id", js.getString("schoolid"));
                        editor.putString("schoolName", js.getString("schoolName"));
                        editor.putString("class_id", js.getString("classid"));
                        editor.putString("className", js.getString("className"));
                        editor.putString("type", js.getString("type"));
                        type = "teacher";
                        break;
                    case "parents":
                        editor.putString("userid", js.getString("parentid"));
                        editor.putString("sex", js.getString("sex"));
                        editor.putString("age", js.getString("age"));
                        editor.putString("head_url", js.getString("head_url"));
                        editor.putString("name", js.getString("name"));
                        editor.putString("type", js.getString("type"));
                        type = "parents";
                        break;
                }
                editor.commit();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (recode.equals("SUCCESS")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}

