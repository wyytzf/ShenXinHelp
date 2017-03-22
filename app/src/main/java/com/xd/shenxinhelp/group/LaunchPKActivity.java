package com.xd.shenxinhelp.group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.xd.shenxinhelp.model.GroupDetail;
import com.xd.shenxinhelp.model.Item;
import com.xd.shenxinhelp.model.PKHistory;
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
    private Intent previousIntent;
    private PKHistory history;
    private GroupDetail detail;
    private SharedPreferences sp;
    private String schoolID;
    private LinearLayout ll_ring_title;
    private TextView ringTitle;
    private int memberCount=0;
    private TextView titleView;
    private String winId = "";
    private TextView total1,total2;
    private int totalDegree1,totalDegree2;
    private String account;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_pk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initViews();
        initDatas();

    }

    private void initDatas() {
        sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        schoolID = sp.getString("school_id", "");
        account = sp.getString("account","");
        previousIntent = getIntent();
        detail = (GroupDetail) previousIntent.getSerializableExtra("groupDetail");
        winerAdapter = new TeamAdapter(LaunchPKActivity.this,winerList,detail.getType());
        loserAdapter = new TeamAdapter(LaunchPKActivity.this,loserList,detail.getType());
        challengerListView.setAdapter(winerAdapter);
        talkerListView.setAdapter(loserAdapter);

        if(previousIntent.getType() !=null){
            if(previousIntent.getType().equals("PKHistoryActivity")){
                history = (PKHistory)previousIntent.getSerializableExtra("history");
                titleView.setText("PK历史记录详情");
                totalDegree1=0;
                totalDegree2=0;
                headers.setVisibility(View.VISIBLE);
                challengerListView.setVisibility(View.VISIBLE);
                talkerListView.setVisibility(View.VISIBLE);
                winerList.clear();
                loserList.clear();
                if(history.getWinTeamID() == Integer.parseInt(history.getParticipateTeam().get(0).getTeamId())) {
                    winerList = history.getParticipateTeam().get(0).getStudents();
                    loserList = history.getParticipateTeam().get(1).getStudents();
                    challengerHeader.setText(history.getParticipateTeam().get(0).getTitle());
                    talkerHeader.setText(history.getParticipateTeam().get(1).getTitle());
                }else{
                    winerList = history.getParticipateTeam().get(1).getStudents();
                    loserList = history.getParticipateTeam().get(0).getStudents();
                    challengerHeader.setText(history.getParticipateTeam().get(1).getTitle());
                    talkerHeader.setText(history.getParticipateTeam().get(0).getTitle());
                }
                for(Team team : winerList){
                    totalDegree1 += team.getHealthDegree();
                }
                for(Team team : loserList){
                    totalDegree2 += team.getHealthDegree();
                }
                total1.setText("赢("+(String.valueOf(totalDegree1))+")");
                total2.setText("输("+(String.valueOf(totalDegree2))+")");
                winerAdapter.setList(winerList);
                loserAdapter.setList(loserList);
                winerAdapter.notifyDataSetChanged();
                loserAdapter.notifyDataSetChanged();

            }else{
                titleView.setText("发起PK");
                getPKInformation();
            }
        }
    }

    private boolean isInFirstTeam() {
        boolean result = false;
        List<Team> students = history.getParticipateTeam().get(0).getStudents();
        int i = 0;
        for (i = 0; i < students.size(); i++) {
            if (students.get(i).getAccount().equals(account)) {
                result = true;
                break;
            }
        }
        if (i == 3) {
            result = false;
        }
        return result;
    }

    private void getPKInformation() {

            new Thread() {
                @Override
                public void run() {
                    final Message message = new Message();
                    String urlget = "";
                    if (detail.getType().equals("1")) {
                        urlget = AppUtil.CreatePK + "?type=" + detail.getType() + "&ringID=" + detail.getId() + "&initatorID=" + detail.getId() + "&title="+detail.getName()+"&schoolID=" + schoolID;
                    } else {
                        urlget = AppUtil.CreatePK + "?type=" + detail.getType() + "&ringID=" + detail.getId() + "&initatorID=" + detail.getId() + "&title="+detail.getName()+"&schoolID=null";
                    }
                    Log.i("kmj", "-------" + urlget);
                    HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                        @Override
                        public void onSuccess(byte[] response) {
                            String jsonStr = new String(response);
                            Log.i("kmj", "-------" + jsonStr);
                            parseResponse(jsonStr);
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            Log.i("kmj", e.getMessage());
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
                winId = result.getString("winRingID");

                JSONArray teamArray = result.getJSONArray("teams");
                for(int i=0; i < teamArray.length();i++) {
                    Team team = new Team();
                    JSONObject jo = teamArray.getJSONObject(i);
                    team.setAccount(jo.getString("name"));
                    team.setHeaderUrl(jo.getString("head_url"));
                    team.setHealthDegree(jo.getInt("health_degree"));
                    team.setSchoolName(jo.getString("schoolName"));
                    team.setClassName(jo.getString("className"));
                    teamList.add(team);
                }
                if(teamList.size()!=0){
                    headers.setVisibility(View.VISIBLE);
                    challengerListView.setVisibility(View.VISIBLE);
                    talkerListView.setVisibility(View.VISIBLE);
                    totalDegree1 = 0;
                    totalDegree2 = 0;
                    for(int j=0;j < 3;j++){
                        winerList.add(teamList.get(j));
                        totalDegree1 += teamList.get(j).getHealthDegree();
                    }
                    for(int j=3;j < teamList.size();j++){
                        loserList.add(teamList.get(j));
                        totalDegree2 += teamList.get(j).getHealthDegree();
                    }
                    if(detail.getType().equals("0")){
                        if(winId.equals(detail.getId())){
                            challengerHeader.setText(detail.getName());
                            talkerHeader.setText(otherRingTitle);
                        }else{
                            challengerHeader.setText(otherRingTitle);
                            talkerHeader.setText(detail.getName());
                        }
                    }else if(detail.getType().equals("1")){
                        challengerHeader.setText(winerList.get(0).getClassName());
                        talkerHeader.setText(loserList.get(0).getClassName());
                    }else{ //学校
                        challengerHeader.setText(winerList.get(0).getSchoolName());
                        talkerHeader.setText(loserList.get(0).getSchoolName());
                    }
                    total1.setText("赢("+(String.valueOf(totalDegree1))+")");
                    total2.setText("输("+(String.valueOf(totalDegree2))+")");
                    winerAdapter.notifyDataSetChanged();
                    loserAdapter.notifyDataSetChanged();

                }else{
                    headers.setVisibility(View.GONE);
                    challengerListView.setVisibility(View.GONE);
                    talkerListView.setVisibility(View.GONE);
                    Toast.makeText(LaunchPKActivity.this, "对不起，暂无数据", Toast.LENGTH_LONG).show();
                }


            }else{
                String message = result.getString("message");
                headers.setVisibility(View.GONE);
                challengerListView.setVisibility(View.GONE);
                talkerListView.setVisibility(View.GONE);
                Toast.makeText(LaunchPKActivity.this, message, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void initViews() {
        titleView = (TextView)findViewById(R.id.pk_title);
        headers = (LinearLayout)findViewById(R.id.ll_two_listview_header);
        challengerHeader = (TextView)findViewById(R.id.challenger_listView_header);
        talkerHeader = (TextView)findViewById(R.id.talker_listView_header);
        challengerListView = (ListView)findViewById(R.id.challenger_listView);
        talkerListView = (ListView)findViewById(R.id.taker_listView);
        ll_ring_title = (LinearLayout)findViewById(R.id.ll_self_ring_title);
        ringTitle = (TextView)findViewById(R.id.self_ring_title);
        total1 = (TextView)findViewById(R.id.total_health_degree_1);
        total2 = (TextView)findViewById(R.id.total_health_degree_2);
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
