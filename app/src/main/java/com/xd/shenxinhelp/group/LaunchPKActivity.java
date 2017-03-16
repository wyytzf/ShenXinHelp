package com.xd.shenxinhelp.group;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.TeamAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.Item;
import com.xd.shenxinhelp.model.Plan;
import com.xd.shenxinhelp.model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/16.
 */

public class LaunchPKActivity extends AppCompatActivity {

    private TextView  challengerHeader,talkerHeader;
    private ListView  challengerListView,talkerListView;
    private List<Team> teamList = new ArrayList<>();
    private List<Team> winerList = new ArrayList<>();
    private List<Team> loserList = new ArrayList<>();
    private TeamAdapter winerAdapter,loserAdapter;
    private String otherRingTitle="";
    private LinearLayout headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_pk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initViews();
        getPKInformation();

    }

    private void getPKInformation() {
        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();

                String urlget =  AppUtil.CreatePK+"?type=2&ringID=7&initatorID=1&schoolID=7";
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
        teamList.clear();
        winerList.clear();
        loserList.clear();
        try {
            JSONObject result = new JSONObject(jsonStr);
            String status = result.getString("reCode");
            if (status.equalsIgnoreCase("success")) {
                teamList.clear();
                otherRingTitle = result.getString("otherRingTitle");
                JSONArray teamArray = result.getJSONArray("teams");
                for(int i=0; i < teamArray.length();i++) {
                    Team team = new Team();
                    JSONObject jo = teamArray.getJSONObject(i);
                    team.setAccount(jo.getString("account"));
                    team.setHeaderUrl(jo.getString("head_url"));
                    team.setHealthDegree(jo.getInt("health_degree"));
                    team.setSchoolName(jo.getString("schoolName"));
                    team.setClassName(jo.getString("className"));
                    teamList.add(team);
                }
                if(teamList.size()!=0){
                    headers.setVisibility(View.VISIBLE);
                    for(Team team : teamList) {
                        if (team.getSchoolName().equals(teamList.get(0).getSchoolName())) {
                            winerList.add(team);
                        } else {
                            loserList.add(team);
                        }
                    }
                }else{
                    headers.setVisibility(View.GONE);
                }
                winerAdapter.notifyDataSetChanged();
                loserAdapter.notifyDataSetChanged();
                challengerHeader.setText(winerList.get(0).getSchoolName());
                talkerHeader.setText(loserList.get(0).getSchoolName());

            }else{
                headers.setVisibility(View.GONE);
                Toast.makeText(LaunchPKActivity.this, "今天已经PK次数已经用完，期待明天继续PK", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void initViews() {
        headers = (LinearLayout)findViewById(R.id.ll_two_listview_header);
        challengerHeader = (TextView)findViewById(R.id.challenger_listView_header);
        talkerHeader = (TextView)findViewById(R.id.talker_listView_header);
        challengerListView = (ListView)findViewById(R.id.challenger_listView);
        talkerListView = (ListView)findViewById(R.id.taker_listView);
        winerAdapter = new TeamAdapter(LaunchPKActivity.this,winerList);
        loserAdapter = new TeamAdapter(LaunchPKActivity.this,loserList);
        challengerListView.setAdapter(winerAdapter);
        talkerListView.setAdapter(loserAdapter);

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
