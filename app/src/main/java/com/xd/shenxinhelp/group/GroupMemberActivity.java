package com.xd.shenxinhelp.group;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.View.MyGridDivider;
import com.xd.shenxinhelp.adapter.GroupMemberAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ConnectUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.com.xd.shenxinhelp.ui.AdressBookActivity;
import com.xd.shenxinhelp.listener.ListItemClickListener;
import com.xd.shenxinhelp.model.GroupDetail;
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
    private GroupDetail groupDetail;
    private EditText editText;
    private Button button;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private String tag="";
    private LayoutInflater mLayoutInflater;
    private View view;
    private EditText et_friend_account;
    private Button bt_contact;
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
                            user.setName(object.getString("name"));
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
//                        if (datas==null||datas.size()==0){
//                            testData();
//                        }
                        //imageLoader = new GlideImageLoader();
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("mmm", e.getMessage());
                    }
                }

                break;
                case 2:
                    getGroupMember();
                    tag="1";
                    break;
                case -1: {

                    break;
                }
                case -2: {
                    String msssg=(String)msg.obj;
                    Toast.makeText(getApplicationContext(),msssg,Toast.LENGTH_SHORT).show();
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
        mLayoutInflater=LayoutInflater.from(this);
        view=mLayoutInflater.inflate(R.layout.add_into_group, null);
        et_friend_account = (EditText)view.findViewById(R.id.friend_account);
        bt_contact = (Button)view.findViewById(R.id.firend_address_book);

        datas= new ArrayList<User>();
        groupDetail= (GroupDetail) getIntent().getSerializableExtra("detail");
        bt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), GroupAdressBookActivity.class);
                intent.putExtra("detail",groupDetail);
                startActivityForResult(intent,2);
            }
        });

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
        editText = new EditText(this);
        button = new Button(this);
    }
    public void getGroupMember() {


        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();

                //String urlget = ConnectUtil.GetRingMember + "?ringID="+groupID+"&type="+type+"&top=5&userID=" + userID;
                String urlget =  AppUtil.GetRingMember  + "?ringID="+groupDetail.getId()+"&type="+groupDetail.getType()+"&top=100";
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
            if (tag.equals("1")){
                setResult(100);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onListItemClick(View v, int position) {
        //openApi = datas.get(position);
        if (position==datas.size()){
            creatDialog();
           // Toast.makeText(this,"tianjia",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==153){
            String tel=data.getStringExtra("tel");
            et_friend_account.setText(tel);
        }
    }

    private void creatDialog(){
        if (builder!=null || alertDialog!=null){

            alertDialog.show();
        }
        else{
            builder = new AlertDialog.Builder(this);
            builder.setTitle("添加小伙伴进圈子");
            builder.setMessage("输入小伙伴的账号");
            //builder.setView(editText,60,0,60,0);
            builder.setView(view);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    /*if (!editText.getText().toString().trim().equals("")){
                        save(editText.getText().toString());
                        dialogInterface.dismiss();
                    }*/
                    if (!et_friend_account.getText().toString().trim().equals("")){
                        save(et_friend_account.getText().toString());
                        dialogInterface.dismiss();
                    }

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
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
    public void save(final String otherID) {

        tag="";

        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();
                SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
                String userID = sp.getString("userid", "xiaoming");
                //String urlget = ConnectUtil.GetRingMember + "?ringID="+groupID+"&type="+type+"&top=5&userID=" + userID;

                String urlget =  AppUtil.AddPersonToRing  + "?ringID="+groupDetail.getId()+"&otherAccount="+ConnectUtil.encodeParameters(otherID);
                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);
                            String status = result.getString("reCode");
                            if (status.equalsIgnoreCase("success")) {
                                message.obj = jsonStr;
                                message.what = 2;
                                handler.sendMessage(message);
                            } else {
                                message.what = -2;//失败
                                message.obj = result.getString("message");
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
}
