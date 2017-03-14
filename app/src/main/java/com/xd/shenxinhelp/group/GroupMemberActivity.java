package com.xd.shenxinhelp.group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.View.MyGridDivider;
import com.xd.shenxinhelp.adapter.GroupMemberAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ConnectUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.listener.ListItemClickListener;
import com.xd.shenxinhelp.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/3/8.
 */

public class GroupMemberActivity extends AppCompatActivity implements ListItemClickListener {
    private List<User> datas = null;
    private GroupMemberAdapter adapter = null;
    private SwipeRefreshLayout listview;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            //dismissRequestDialog();
            switch (msg.what) {
                case 1: {

                    try {
                        JSONObject result = new JSONObject((String) msg.obj);
                        JSONArray array = result.getJSONArray("students");
                        JSONObject object;
                        datas.clear();

                        for (int i = 0; i < array.length(); i++) {
                            User user=new User();
                            object = array.getJSONObject(i);
                            user.setName(object.getString("account"));
                            user.setUid(object.getString("userid"));
                            user.setSex(object.getString("sex"));
                            user.setAge(object.getString("age"));
                            user.setHeight(object.getString("height"));
                            user.setCredits(object.getString("credits"));
                            user.setHealth_degree(object.getString("health_degree"));
                            user.setLevel(object.getString("level"));
                            user.setPhotoUrl(object.getString("head_url"));
                            user.setClass_id(object.getString("class_id"));
                            user.setSchool_id(object.getString("school_id"));
                            datas.add(user);

                        }
                        if (datas==null||datas.size()==0){
                            testData();
                        }
                        //imageLoader = new GlideImageLoader();
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("mmm", e.getMessage());
                    }
                }

                break;

                case -1: {

                    break;
                }
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        datas= new ArrayList<User>();
        getGroupMember();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_group_member);
        //设置可以滑出底栏
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(0,0,0, (int) getResources().getDimension(R.dimen.BottomBarHeight));
        listview=(SwipeRefreshLayout)findViewById(R.id.listview_group_member);
        adapter = new GroupMemberAdapter(datas,GroupMemberActivity.this, GroupMemberActivity.this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == 0) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        listview.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        recyclerView.addItemDecoration(new MyGridDivider(1,
                ContextCompat.getColor(this,R.color.colorDivider)));
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        listview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview.setRefreshing(false);
            }
        });
    }
    public void getGroupMember() {


        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();

                //String urlget = ConnectUtil.GetRingMember + "?ringID="+groupID+"&type="+type+"&top=5&userID=" + userID;
                String urlget =  AppUtil.GetRingMember  + "?ringID=7&type=2&top=15";
                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);
                            String status = result.getString("reCode");
                            if (status.equalsIgnoreCase("success")) {
                                message.obj = jsonStr;
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                message.what = -1;//失败
                                message.obj = "获取失败";
                                handler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            Log.e("mmm", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        message.what = -1;
                        message.obj = "获取数据失败";
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onListItemClick(View v, int position) {
        //openApi = datas.get(position);
        if (position==datas.size()){
            Toast.makeText(this,"tianjia",Toast.LENGTH_SHORT).show();
        }

    }
    private void testData() {

        User item = new User();
        item.setName("深圳");
        datas.add(item);
        item = new User();
        item.setName("上海");
        datas.add(item);
        item = new User();
        item.setName("广州");
        datas.add(item);
        item = new User();
        item.setName("北京");
        datas.add(item);
        item = new User();
        item.setName("武汉");
        datas.add(item);

        //cityList.addAll(cityList);
    }
}
