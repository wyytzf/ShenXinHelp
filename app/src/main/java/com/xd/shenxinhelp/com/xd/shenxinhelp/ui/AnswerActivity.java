package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xd.shenxinhelp.R;

public class AnswerActivity extends AppCompatActivity {


    private TextView content;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        content = (TextView) findViewById(R.id.content);
        radioGroup = (RadioGroup) findViewById(R.id.mRadio_group);
    }
}
