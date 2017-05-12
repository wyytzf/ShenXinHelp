package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity {

    private static final int ROLETYPE = 0;


    private UserRegisterTask mAuthTask = null;
    private View mProgressView;
    private View mRegisterFormView;


    private Button mRegisterButton;
    private Button getV;
    private EditText mAccount;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mRegisterVCode;
    private String userID;

    private TimeCount timeCount;


    private Spinner mType;

    private String mVerificationCode;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                attemptRegister2();
            } else if (msg.what == 1) {
                mRegisterButton.setClickable(true);
            } else if (msg.what == 2) {
                mRegisterVCode.setError("验证码输入错误");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {

        getSupportActionBar().setTitle("注册");

        mProgressView = findViewById(R.id.register_progress);
        mRegisterFormView = findViewById(R.id.register_form);
        mRegisterButton = (Button) findViewById(R.id.email_register_button);
        mAccount = (EditText) findViewById(R.id.register_account);
        mPassword = (EditText) findViewById(R.id.register_password);
        mConfirmPassword = (EditText) findViewById(R.id.register_comfirm_password);
        mRegisterVCode = (EditText) findViewById(R.id.register_v_code);


///短信回调
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    // 回调完成
//                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                    // 提交验证码成功
//                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                    // 获取验证码成功
//
//                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
//                    // 返回支持的国家列表
//                } else {
//                    ((Throwable) data).printStackTrace();
//                }

                handler.sendEmptyMessage(1);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    handler.sendEmptyMessage(0);

                } else {
                    ((Throwable) data).printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
        timeCount = new TimeCount(60000, 1000);
        getV = (Button) findViewById(R.id.get_v);
        getV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 发送验证短信
                /// 判断手机号是否合理
                if (!checkPhoneNumber(mAccount.getText().toString())) {
                    mAccount.setError(getString(R.string.error_field_required));
                } else {
                    timeCount.start();
                    SMSSDK.getVerificationCode("86", mAccount.getText().toString());
                }

            }
        });
//        mType = (Spinner) findViewById(R.id.type_chose);
//        mType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"青少年", "家长", "教师"}));


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister1();
            }


        });

    }

    private void attemptRegister1() {
        SMSSDK.submitVerificationCode("86", mAccount.getText().toString(), mRegisterVCode.getText().toString());
        mRegisterButton.setClickable(false);
    }

    private void attemptRegister2() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAccount.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mAccount.getText().toString();
        String password = mPassword.getText().toString();
        String confimePassword = mConfirmPassword.getText().toString();
//        long type = mType.getSelectedItemId();
        //Log.e("type", ""+type);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (!checkPhoneNumber(email)) {
            mAccount.setError(getString(R.string.error_field_required));
            focusView = mAccount;
            cancel = true;
        } else if (!isAccountValid(email)) {
            mAccount.setError(getString(R.string.error_invalid_account));
            focusView = mAccount;
            cancel = true;
        }
        if (TextUtils.isEmpty(confimePassword)) {
            mConfirmPassword.setError(getString(R.string.please_confirm_password));
            focusView = mConfirmPassword;
            cancel = true;
        } else if (!isConfimePasswordValid(password, confimePassword)) {
            mConfirmPassword.setError(getString(R.string.different_password));
            focusView = mConfirmPassword;
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
            mAuthTask = new UserRegisterTask(email, password, 0);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isAccountValid(String account) {
        //TODO: Replace this with your own logic
        return account.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private boolean isConfimePasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
//        private final long mtype;

        UserRegisterTask(String email, String password, long type) {
            mEmail = email;
            mPassword = password;
//            mtype = type;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            // TODO: register the new account here.
            // 返回true代表登录成功
            boolean result;
            try {
                String synchronous = OkHttp.getSynchronous(AppUtil.REGISTER + "account=" + mEmail + "&" + "psw=" + mPassword + "&role=" + ROLETYPE);
                result = parseRegister(synchronous);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(RegisterActivity.this, FirstLoginActivity.class);
                intent.putExtra("userID", userID);
//                intent.putExtra("mtype", mtype);
                startActivity(intent);
                finish();
            } else {
                mAccount.setError(getString(R.string.account_exist));
                mAccount.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private boolean parseRegister(String str) {
        boolean result = false;
        try {
            JSONObject jb = new JSONObject(str);
            String reCode = jb.getString("reCode");
            if (reCode.equals("SUCCESS")) {
                result = true;
                userID = jb.getString("userID");
            } else if (reCode.equals("FAIL")) {
                result = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return result;
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getV.setClickable(false);
            getV.setText(millisUntilFinished / 1000 + "s后重新获取验证码");
        }

        @Override
        public void onFinish() {
            getV.setClickable(true);
            getV.setText("获取验证码");
        }
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
