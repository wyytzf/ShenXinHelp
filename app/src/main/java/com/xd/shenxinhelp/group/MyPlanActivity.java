package com.xd.shenxinhelp.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.GroupLittleGoalListAdapter;
import com.xd.shenxinhelp.model.LittleGoal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/2/17.
 */

public class MyPlanActivity extends AppCompatActivity {

    private GroupLittleGoalListAdapter adapter;
    List<LittleGoal> goalList;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);


        listView= (ListView)findViewById(R.id.lv_my_plan_list);
        initData();
//        adapter = new GroupLittleGoalListAdapter(getApplicationContext(),
//                goalList);
//        listView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    void initData(){
        goalList = new ArrayList<LittleGoal>();
        LittleGoal data1= new LittleGoal();
        goalList.add(data1);
        goalList.add(data1);
        goalList.add(data1);
        goalList.add(data1);
        goalList.addAll(goalList);

    }
}
