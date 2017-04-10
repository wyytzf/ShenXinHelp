package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.xd.shenxinhelp.R;

public class TeacherMainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    private TeacherMainFragment teacherMainFragment;
    private TeacherRankFragment teacherRankFragment;
    private MySettingFragment mySettingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        teacherMainFragment = new TeacherMainFragment();
        teacherRankFragment = new TeacherRankFragment();
        mySettingFragment = new MySettingFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, teacherMainFragment);
        transaction.commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.bottom_navi_first:
                        transaction.replace(R.id.content, teacherMainFragment);
                        break;
                    case R.id.bottom_navi_second:
                        transaction.replace(R.id.content, teacherRankFragment);
                        break;
                    case R.id.bottom_navi_third:
                        transaction.replace(R.id.content, mySettingFragment);
                        break;
                }
                transaction.commit();
                return false;
            }

        });
    }
}
