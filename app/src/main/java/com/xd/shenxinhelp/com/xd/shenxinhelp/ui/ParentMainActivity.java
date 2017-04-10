package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xd.shenxinhelp.R;

public class ParentMainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private ParentMainFragment parentMainFragment;
    private MySettingFragment mySettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);

        parentMainFragment = new ParentMainFragment();
        mySettingFragment = new MySettingFragment();

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.content, parentMainFragment);
        transaction.commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navi_first:
                        transaction.replace(R.id.fragment_container, parentMainFragment);
                        break;
                    case R.id.bottom_navi_third:
                        transaction.replace(R.id.fragment_container, mySettingFragment);
                        break;
                }
                transaction.commit();
                return false;
            }

        });
    }

}