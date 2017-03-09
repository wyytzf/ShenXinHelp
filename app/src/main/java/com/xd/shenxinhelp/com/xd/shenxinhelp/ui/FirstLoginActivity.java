package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.School;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstLoginActivity extends AppCompatActivity {


    private RadioGroup mSex;
    private RadioButton mMale;
    private RadioButton mFemale;
    private EditText mAge;
    private EditText mHeight;
    private EditText mWeight;
    private Button mRegisterButton;
    private Spinner mSchool;
    private List<School> schools;


    private int check_age = 0;


    private String userId;
    private String schoolId;
    private ArrayList<Object> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        userId = getIntent().getStringExtra("userID");
        initViews();
        requestSchool();
        requestClass();
    }


    private void initViews() {

        mSex = (RadioGroup) findViewById(R.id.register_sex_group);
        mMale = (RadioButton) findViewById(R.id.register_radio_male);
        mFemale = (RadioButton) findViewById(R.id.register_radio_female);
        mAge = (EditText) findViewById(R.id.register_age);
        mHeight = (EditText) findViewById(R.id.register_height);
        mWeight = (EditText) findViewById(R.id.register_weight);
        mSchool = (Spinner) findViewById(R.id.register_spinner);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mMale.setChecked(true);

        mSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mMale.getId() == checkedId) {
                    check_age = 0;
                } else if (mFemale.getId() == checkedId) {
                    check_age = 1;
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }


    private void attemptRegister() {

        // Reset errors.
        mAge.setError(null);
        mHeight.setError(null);
        mWeight.setError(null);

        // Store values at the time of the login attempt.

        String age = mAge.getText().toString();
        String height = mHeight.getText().toString();
        String weight = mWeight.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(age)) {
            mAge.setError(getString(R.string.error_field_required));
            focusView = mAge;
            cancel = true;
        }
        if (TextUtils.isEmpty(height)) {
            mHeight.setError(getString(R.string.error_field_required));
            focusView = mHeight;
            cancel = true;
        }
        if (TextUtils.isEmpty(weight)) {
            mWeight.setError(getString(R.string.error_field_required));
            focusView = mWeight;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            OkHttp.get(AppUtil.ADDPERSONINFO, new OkHttp.ResultCallBack() {
                @Override
                public void onError(String str, Exception e) {

                }

                @Override
                public void onResponse(String str) {

                }
            });

        }


    }


    private void requestSchool() {
        schools = new ArrayList<>();
        OkHttp.get(AppUtil.GETSCHOOL, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {

            }

            @Override
            public void onResponse(String str) {
                parseSchool(str);
                completeSpinner();
            }
        });
    }

    private void requestClass() {
        classes = new ArrayList<>();
        OkHttp.get(AppUtil.GETSCHOOL, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {

            }

            @Override
            public void onResponse(String str) {
                parseSchool(str);
                completeSpinner();
            }
        });
    }


    private void completeSpinner() {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter();
        mSchool.setAdapter(spinnerAdapter);

    }

    private void parseSchool(String str) {

        try {
            JSONObject js = new JSONObject(str);
            JSONArray ja = js.getJSONArray("schools");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                School sc = new School();
                sc.setId(jb.getString("schoolid"));
                sc.setName(jb.getString("schoolName"));
                schools.add(sc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class SpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return schools.size();
        }

        @Override
        public Object getItem(int position) {
            return schools.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(FirstLoginActivity.this);
            View inflate = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            if (inflate != null) {
                TextView textView = (TextView) inflate.findViewById(android.R.id.text1);
                textView.setText(schools.get(position).getName());
            }
            return inflate;
        }
    }
}
