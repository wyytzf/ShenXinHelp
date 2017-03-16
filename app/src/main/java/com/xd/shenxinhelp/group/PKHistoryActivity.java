package com.xd.shenxinhelp.group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.PKHistoryAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.PKHistory;
import com.xd.shenxinhelp.model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/16.
 */

public class PKHistoryActivity extends AppCompatActivity {

    private ListView pkHistoryListView;
    private List<PKHistory> pkHistoryList = new ArrayList<>();
    private PKHistoryAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initViews();
        getAllPKRecords();
        initEvents();

    }

    private void initEvents() {
        pkHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PKHistoryActivity.this,LaunchPKActivity.class);
                intent.putExtra("teams",(Serializable) pkHistoryList.get(position).getTeams());
                intent.setType("PKHistoryActivity");
                startActivity(intent);
            }
        });
    }

    private void getAllPKRecords() {
        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();

                String urlget =  AppUtil.GetAllPKRecords+"?id=1&type=0";
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
        pkHistoryList.clear();
        try {
            JSONObject result = new JSONObject(jsonStr);
            String status = result.getString("reCode");
            if (status.equalsIgnoreCase("success")) {
                JSONArray pkRingsArray = result.getJSONArray("PKRings");
                Log.i("kmj","---length----" + pkRingsArray.length());
                for(int i=0; i < pkRingsArray.length();i++) {
                    PKHistory history = new PKHistory();
                    JSONObject jo = pkRingsArray.getJSONObject(i);
                    history.setWinTeamID(jo.getInt("winTeamID"));
                    history.setPkringID(jo.getInt("pkringID"));
                    history.setCridits(jo.getInt("cridits"));
                    history.setDate(jo.getString("date"));
                    history.setType(jo.getInt("type"));

                    List<List<Team>> twoTeams = new ArrayList<List<Team>>();
                    JSONArray teamsArray = jo.getJSONArray("teams");
                    for(int j=0; j < teamsArray.length();j++){
                        JSONArray array = teamsArray.getJSONArray(j);
                        List<Team> teams = new ArrayList<>();
                        for(int k=0;k < array.length();k++) {
                            JSONObject jot = array.getJSONObject(k);
                            Team team = new Team();
                            team.setAccount(jot.getString("account"));
                            team.setHeaderUrl(jot.getString("head_url"));
                            team.setHealthDegree(jot.getInt("health_degree"));
                            team.setSchoolName(jot.getString("schoolName"));
                            team.setClassName(jot.getString("className"));
                            teams.add(team);
                        }
                        twoTeams.add(teams);
                    }
                    history.setTeams(twoTeams);
                    pkHistoryList.add(history);
                }
                Log.i("kmj","-------" + pkHistoryList.size());
                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(PKHistoryActivity.this, "获取历史信息失败，请重试", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void initViews() {
        pkHistoryListView = (ListView)findViewById(R.id.pk_history_listView);
        adapter = new PKHistoryAdapter(PKHistoryActivity.this,pkHistoryList);
        pkHistoryListView.setAdapter(adapter);
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
