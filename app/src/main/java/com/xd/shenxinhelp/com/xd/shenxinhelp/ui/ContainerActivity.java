package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xd.shenxinhelp.R;

public class ContainerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MainPagerFragment mainPagerFragment;
    private GroupFragment groupFragment;
    private MySettingFragment mySettingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        initView();
        setDefaultFragment();
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

}
