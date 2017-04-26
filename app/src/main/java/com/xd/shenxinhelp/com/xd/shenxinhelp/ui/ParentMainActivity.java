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

    private ParentMainFragment parentMainFragment;
    private MySettingFragment mySettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);

        parentMainFragment = new ParentMainFragment();
        mySettingFragment = new MySettingFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, parentMainFragment);
        transaction.commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.bottom_navi_first:
                        parentMainFragment = new ParentMainFragment();
                        transaction.replace(R.id.content, parentMainFragment);
                        break;
                    case R.id.bottom_navi_third:
                        mySettingFragment = new MySettingFragment();
                        transaction.replace(R.id.content, mySettingFragment);
                        break;
                }
                transaction.commit();
                return true;
            }

        });
    }

}
