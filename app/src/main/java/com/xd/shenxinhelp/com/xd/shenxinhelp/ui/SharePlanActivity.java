package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.xd.shenxinhelp.R;

public class SharePlanActivity extends AppCompatActivity {

    private TextView stu_name;
    private EditText share_content;

    private String userID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_plan);

        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);

        userID = sp.getString("userid", "1");
        userName = sp.getString("name", "");

        initUI();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        stu_name = (TextView) findViewById(R.id.stu_name);
        stu_name.setText(userName+"您好，根据您所处的年级，");
        share_content = (EditText) findViewById(R.id.share_content);
    }
}
