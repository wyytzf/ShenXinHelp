package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.netutils.OkHttp;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private UserRegisterTask mAuthTask = null;
    private View mProgressView;
    private View mRegisterFormView;


    private Button mRegisterButton;
    private EditText mAccount;
    private EditText mPassword;
    private EditText mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mProgressView = findViewById(R.id.register_progress);
        mRegisterFormView = findViewById(R.id.register_form);
        mRegisterButton = (Button) findViewById(R.id.email_register_button);
        mAccount = (EditText) findViewById(R.id.register_account);
        mPassword = (EditText) findViewById(R.id.register_password);
        mConfirmPassword = (EditText) findViewById(R.id.register_comfirm_password);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }


        });

    }

    private void attemptRegister() {
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

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
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
            mAuthTask = new UserRegisterTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isAccountValid(String account) {
        //TODO: Replace this with your own logic
        return account.length() > 6;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
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

        UserRegisterTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            // TODO: register the new account here.
            // 返回true代表登录成功
            try {
                OkHttp.getSynchronous("http://baidu.com");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
//                Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
//                startActivity(intent);
                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
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
}
