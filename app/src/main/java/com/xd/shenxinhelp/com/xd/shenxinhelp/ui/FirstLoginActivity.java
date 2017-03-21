package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.Class;
import com.xd.shenxinhelp.model.School;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstLoginActivity extends AppCompatActivity {


    private EditText mName;
    private RadioGroup mSex;
    private RadioButton mMale;
    private RadioButton mFemale;
    private EditText mAge;
    private EditText mHeight;
    private EditText mWeight;
    private Button mRegisterButton;
    private Spinner mSchool;
    private Spinner mClass;
    private List<School> schools;


    private int check_age = 0;


    private String userId;
    private String schoolId;
    private String classId;
    private ArrayList<Class> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        userId = getIntent().getStringExtra("userID");
        initViews();
        requestSchool();
    }


    private void initViews() {

        mName = (EditText) findViewById(R.id.register_name);
        mSex = (RadioGroup) findViewById(R.id.register_sex_group);
        mMale = (RadioButton) findViewById(R.id.register_radio_male);
        mFemale = (RadioButton) findViewById(R.id.register_radio_female);
        mAge = (EditText) findViewById(R.id.register_age);
        mHeight = (EditText) findViewById(R.id.register_height);
        mWeight = (EditText) findViewById(R.id.register_weight);
        mSchool = (Spinner) findViewById(R.id.register_spinner);
        mClass = (Spinner) findViewById(R.id.register_spinner_class);
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
        String name = mName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }
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
            schoolId = schools.get(mSchool.getSelectedItemPosition()).getId();
            classId = classes.get(mClass.getSelectedItemPosition()).getClassId();
            OkHttp.get(AppUtil.ADDPERSONINFO + "userID=" + userId + "&name=" + name + "&sex=" + check_age + "&age=" + age +
                    "&height=" + height + "&weight=" + weight + "&schoolID=" + schoolId + "&classID=" + classId, new OkHttp.ResultCallBack() {
                @Override
                public void onError(String str, Exception e) {
                    Toast.makeText(FirstLoginActivity.this, "网络出错", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String str) {
                    boolean result = parseAddInfo(str);
                    if (result) {
                        Intent intent = new Intent(FirstLoginActivity.this, ContainerActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(FirstLoginActivity.this, "网络出错", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


    }

    private boolean parseAddInfo(String str) {
        try {
            JSONObject jb = new JSONObject(str);
            String reCode = jb.getString("reCode");
            if (reCode.equals("SUCCESS"))
                return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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
                completeSchoolSpinner();
                requestClass();
            }
        });
    }

    private void requestClass() {
        classes = new ArrayList<>();
        OkHttp.get(AppUtil.GETCLASS + "schoolID=" + schoolId, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {

            }

            @Override
            public void onResponse(String str) {
                parseClasses(str);
                completeClassSpinner();
            }
        });
    }

    private void completeClassSpinner() {
        ClassSpinnerAdapter classSpinnerAdapter = new ClassSpinnerAdapter();
        mClass.setAdapter(classSpinnerAdapter);
    }

    private void parseClasses(String str) {
        try {
            JSONObject js = new JSONObject(str);
            JSONArray ja = js.getJSONArray("classes");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                Class sc = new Class();
                sc.setClassId(jb.getString("classid"));
                sc.setClassName(jb.getString("className"));
                classes.add(sc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void completeSchoolSpinner() {
        SchoolSpinnerAdapter spinnerAdapter = new SchoolSpinnerAdapter();
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

    public class SchoolSpinnerAdapter extends BaseAdapter {

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

    public class ClassSpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return classes.size();
        }

        @Override
        public Object getItem(int position) {
            return classes.get(position);
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
                textView.setText(classes.get(position).getClassName());
            }
            return inflate;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void makeDialog() {

    }
}
