package com.xd.shenxinhelp.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private Button btnBack;
    private GroupLittleGoalListAdapter adapter;
    List<LittleGoal> goalList;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);
        btnBack=(Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView= (ListView)findViewById(R.id.lv_my_plan_list);
        initData();
        adapter = new GroupLittleGoalListAdapter(getApplicationContext(),
                goalList);
        listView.setAdapter(adapter);
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
