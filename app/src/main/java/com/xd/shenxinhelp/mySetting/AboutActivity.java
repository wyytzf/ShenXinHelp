package com.xd.shenxinhelp.mySetting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.xd.shenxinhelp.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        iv_icon = (ImageView) findViewById(R.id.about_icon);
        rectRoundBitmap();
    }

    private void rectRoundBitmap(){
        //得到资源文件的BitMap
        Bitmap image= BitmapFactory.decodeResource(getResources(),R.mipmap.default_head_image);
        //创建RoundedBitmapDrawable对象
        RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create(getResources(),image);
        //抗锯齿
        roundImg.setAntiAlias(true);
        //设置圆角半径
        roundImg.setCornerRadius(30);
        //设置显示图片
        iv_icon.setImageDrawable(roundImg);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
