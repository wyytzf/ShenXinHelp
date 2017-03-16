package com.xd.shenxinhelp.group;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.GroupLittleGoalListAdapter;
import com.xd.shenxinhelp.adapter.MyLittlePlanAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.Item;
import com.xd.shenxinhelp.model.LittleGoal;
import com.xd.shenxinhelp.model.Plan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/2/17.
 */

public class MyPlanActivity extends AppCompatActivity {

    private GroupLittleGoalListAdapter adapter;
    List<LittleGoal> goalList;
    private ListView listView;
    private List<Plan> planList;
    private MyLittlePlanAdapter planAdapter;

    String urlget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);


        listView= (ListView)findViewById(R.id.lv_my_plan_list);
        urlget    = getIntent().getExtras().getString("url");
        Log.i("mmm",urlget);
//        getMyPlans();
//        initData();
//        adapter = new GroupLittleGoalListAdapter(getApplicationContext(),
//                goalList);
//        listView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        planAdapter = new MyLittlePlanAdapter(planList,MyPlanActivity.this);
//        listView.setAdapter(planAdapter);
//        getMyPlans();

    }

    private void getMyPlans() {
        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();

//                String urlget =  AppUtil.GetPlanByDate;
                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        Log.i("kmj","-------" + jsonStr);
                        parseResponse(jsonStr);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.i("kmj",e.getMessage());
                    }
                });
            }
        }.start();
    }


    private void parseResponse(String jsonStr) {
        try {
            JSONObject result = new JSONObject(jsonStr);
            String status = result.getString("reCode");
            if (status.equalsIgnoreCase("success")) {
                planList.clear();
                JSONArray planArray = result.getJSONArray("plans");
                for(int i=0; i < planArray.length();i++){
                    Plan plan = new Plan();
                    JSONObject jo = planArray.getJSONObject(i);
                    plan.setPlanId(jo.getString("id"));
                    plan.setDate(jo.getString("date"));

                    JSONArray itemsArray = jo.getJSONArray("items");
                    List<Item> items = new ArrayList<Item>();
                    for(int j=0;j<itemsArray.length();j++){
                        JSONObject joi = itemsArray.getJSONObject(j);
                        Item item = new Item();
                        item.setId(joi.getString("id"));
                        item.setImageUrl(joi.getString("imageUrl"));
                        item.setTitle(joi.getString("title"));
                        item.setHeat(joi.getString("heat"));
                        item.setType(joi.getString("type"));
                        items.add(item);
                    }
                    plan.setItems(items);
                    planList.add(plan);
                }
                planAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(MyPlanActivity.this, "获取我的小计划失败，请重试", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
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
