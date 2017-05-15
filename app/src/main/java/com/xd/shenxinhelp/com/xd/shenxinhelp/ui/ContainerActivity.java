package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.service.LongRunningService;

public class ContainerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MainPagerFragment mainPagerFragment;
    private GroupFragment groupFragment;
    private MySettingFragment mySettingFragment;
    private Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        initView();
        setDefaultFragment();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }

        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        String huyan_on = sp.getString("huyan_on", "1");
        if (huyan_on.equals("1")) {
            serviceIntent = new Intent(this, LongRunningService.class);
            startService(serviceIntent);
        }

    }

    private void initView() {

        mainPagerFragment = new MainPagerFragment();
        groupFragment = new GroupFragment();
        mySettingFragment = new MySettingFragment();


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navi_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.bottom_navi_first:
                        transaction.replace(R.id.fragment_container, mainPagerFragment);
                        break;
                    case R.id.bottom_navi_second:
                        transaction.replace(R.id.fragment_container, groupFragment);
                        break;
                    case R.id.bottom_navi_third:
                        transaction.replace(R.id.fragment_container, mySettingFragment);
                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, mainPagerFragment);
        transaction.commit();
    }


    @Override
    protected void onDestroy() {
        if (serviceIntent != null)
            stopService(serviceIntent);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
    }
}
