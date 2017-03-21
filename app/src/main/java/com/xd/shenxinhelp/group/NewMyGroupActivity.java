package com.xd.shenxinhelp.group;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ConnectUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MMY on 2017/3/16.
 */

public class NewMyGroupActivity extends AppCompatActivity {
    EditText title,des;
    Button saveBtn;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            //dismissRequestDialog();
            switch (msg.what) {
                case 1: {
                    setResult(100,null);
                    finish();
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
        setContentView(R.layout.activity_new_mygroup);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        toolbar.setTitle("");

       // setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("建立圈子");
        title=(EditText)findViewById(R.id.new_group_title);
        des=(EditText)findViewById(R.id.new_group_des);
        saveBtn=(Button)findViewById(R.id.new_group_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"亲，输入圈子名",Toast.LENGTH_SHORT).show();
                }else if(des.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"亲，输入圈子简介",Toast.LENGTH_SHORT).show();
                }
                else {
                    save();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void save() {



        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();
                SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
                String userID = sp.getString("userid", "xiaoming");
                //String urlget = ConnectUtil.GetRingMember + "?ringID="+groupID+"&type="+type+"&top=5&userID=" + userID;

                String urlget =  AppUtil.CreateRing  + "?userID="+userID+"&desc="+ ConnectUtil.encodeParameters(des.getText().toString())+"&title="+ConnectUtil.encodeParameters(title.getText().toString());
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
}
