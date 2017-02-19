package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.netutils.OkHttp;

public class BodyHelpActivity extends AppCompatActivity {

    private ImageView func1_image;
    private ImageView func2_image;
    private ImageView func3_image;
    private ImageView func4_image;
    private TextView func1_text1;
    private TextView func1_text2;
    private TextView func1_text3;
    private TextView func1_text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_help);


        initView();


        Request();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 启动制定计划activity
            }
        });


        func1_image = (ImageView) findViewById(R.id.content_BEHE_image1);
        func2_image = (ImageView) findViewById(R.id.content_BEHE_image2);
        func3_image = (ImageView) findViewById(R.id.content_BEHE_image3);
        func4_image = (ImageView) findViewById(R.id.content_BEHE_image4);
        Glide.with(this).load(R.mipmap.body_leg).into(func1_image);
        Glide.with(this).load(R.mipmap.body_abdomen).into(func2_image);
        Glide.with(this).load(R.mipmap.body_allbody).into(func3_image);
        Glide.with(this).load(R.mipmap.body_arm).into(func4_image);


        func1_text1 = (TextView) findViewById(R.id.content_BEHE_text1);
        func1_text2 = (TextView) findViewById(R.id.content_BEHE_text2);
        func1_text3 = (TextView) findViewById(R.id.content_BEHE_text3);
        func1_text4 = (TextView) findViewById(R.id.content_BEHE_text4);
        func1_text1.setText("腿部");
        func1_text2.setText("腰部");
        func1_text3.setText("手臂");
        func1_text4.setText("全身");
    }

    private void Request() {
        OkHttp.get("http://www.baidu.com", new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                // 已经是主线程了，直接操作
            }

            @Override
            public void onResponse(String str) {
                // 已经是主线程了，直接操作
            }
        });
    }
}
