package com.xd.shenxinhelp.mySetting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Feedback extends AppCompatActivity {

    private EditText et_feedback_content;
    private Button bt_submit;
    private Dialog mDialog=null;
    private SharedPreferences sp;
    private String userID;
    private String content;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:{
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    Feedback.this.finish();
                    break;
                }
                case 0:{
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                }
                case -1:{
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                }

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
        initData();
    }

    void initData(){
        sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID=sp.getString("account", "");
        content=et_feedback_content.getText().toString();
    }

    void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_feedback);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        et_feedback_content = (EditText)findViewById(R.id.feefback_content);
        bt_submit = (Button) findViewById(R.id.feedback_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!judge())
                    return;
                else
                    submit();
            }
        });
    }


    boolean judge(){
        if(et_feedback_content.getText().toString().equals("")){
            Toast.makeText(this,"请输入您的反馈意见",Toast.LENGTH_LONG).show();
            return false;
        }else
            return true;
    }

    void submit(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                final Message message= new Message();
                String url= AppUtil.Feedback + "?userID="+userID+"&content="+content;
                HttpUtil.get(getApplicationContext(), url, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);
                            String status = result.getString("reCode");
                            if (status.equalsIgnoreCase("SUCCESS")) {
                                message.what = 1;
                                message.obj = "提交成功";
                                handler.sendMessage(message);
                            } else {
                                message.what = -1;
                                message.obj = "提交失败";
                                handler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            Log.e("www", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        message.what = 0;
                        message.obj = "提交失败，请重试";
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
    /*public void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(Feedback.this, "正在提交...");
        mDialog.show();
    }

    public void dismissRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }*/



}
