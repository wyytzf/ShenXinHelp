package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.commons.SMSSDK;
import com.xd.shenxinhelp.R;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private TextView summary;
    private ImageView imageView;

    private final int SPLASH_DISPLAY = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        cn.smssdk.SMSSDK.initSDK(this,"1d8b895cb822c","4d7ebc8a426429f2687163d5d5d54614");


//        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//        startActivity(intent);
        textView = (TextView) findViewById(R.id.guide_text);
        imageView = (ImageView) findViewById(R.id.guide_image);
        summary = (TextView) findViewById(R.id.summary);
        rectRoundBitmap();
        Typeface typeface = Typeface.createFromAsset(getAssets(), "manyu.ttf");
        textView.setTypeface(typeface);
        summary.setTypeface(typeface);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY);
    }

    private void rectRoundBitmap() {
        //得到资源文件的BitMap
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.default_head_image);
        //创建RoundedBitmapDrawable对象
        RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create(getResources(), image);
        //抗锯齿
        roundImg.setAntiAlias(true);
        //设置圆角半径
        roundImg.setCornerRadius(30);
        //设置显示图片
        imageView.setImageDrawable(roundImg);
    }
}
