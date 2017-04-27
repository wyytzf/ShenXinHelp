package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.Class;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherRankFragment extends Fragment {

    private View view;
    private String teacherid;
    private SharedPreferences sp;
    private ArrayList<HashMap<String,String>> topStudents;
    private ArrayList<HashMap<String,String>> lastStudents;
    private ArrayList<HashMap<String,String>> upTopStudents;
    private TextView most_first,most_second,most_third;
    private TextView last_first,last_second,last_third;
    private TextView up_first,up_second,up_third;
    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                TextView[] textViews=new TextView[3];
                textViews[0]=most_first;
                textViews[1]=most_second;
                textViews[2]=most_third;
                if(topStudents.size()>0){
                    for (int i=0;i<topStudents.size();i++){
                        int j=i+1;
                        textViews[i].setText("Top"+j+"   "+topStudents.get(i).get("studentName"));
                    }
                }
               /* most_first.setText("Top1   "+topStudents.get(0).get("studentName"));
                most_second.setText("Top2   "+topStudents.get(1).get("studentName"));
                most_third.setText("Top3   "+topStudents.get(2).get("studentName"));*/
            }
            if(msg.what==2){
                TextView[] textViews=new TextView[3];
                textViews[0]=last_first;
                textViews[1]=last_second;
                textViews[2]=last_third;
                if(lastStudents.size()>0){
                    for (int i=0;i<lastStudents.size();i++){
                        int j=i+1;
                        textViews[i].setText("Top"+j+"   "+lastStudents.get(i).get("studentName"));
                    }
                }
            }

            if(msg.what==3){
                TextView[] textViews=new TextView[3];
                textViews[0]=up_first;
                textViews[1]=up_second;
                textViews[2]=up_third;
                if(upTopStudents.size()>0){
                    for (int i=0;i<upTopStudents.size();i++){
                        int j=i+1;
                        textViews[i].setText("Top"+j+"   "+upTopStudents.get(i).get("studentName"));
                    }
                }
            }

            if(msg.what==0){
                Toast.makeText(getContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
            }else{

            }
                //Toast.makeText(getContext(), "没有获取到当前排名", Toast.LENGTH_LONG).show();

        }
    };

    public TeacherRankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_teacher_rank, container, false);
        lastStudents=new ArrayList<HashMap<String, String>>();
        initView();
        initData();
        return view;
    }

    void initView(){
        most_first=(TextView)view.findViewById(R.id.most_first_student);
        most_second= (TextView) view.findViewById(R.id.most_second_student);
        most_third = (TextView) view.findViewById(R.id.most_third_student);
        last_first=(TextView)view.findViewById(R.id.last_first_student);
        last_second = (TextView) view.findViewById(R.id.last_second_student);
        last_third = (TextView) view.findViewById(R.id.last_third_student);
        up_first = (TextView) view.findViewById(R.id.first_up_student);
        up_second = (TextView) view.findViewById(R.id.second_up_student);
        up_third = (TextView) view.findViewById(R.id.third_up_student);
    }

    void initData(){
        sp=getActivity().getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        teacherid=sp.getString("userid","account");
        Log.i("www",teacherid);
        getTopThreeStudent();
        getLastThreeStudent();
        getUpTopThreeStudent();
    }

    private void getTopThreeStudent(){
        {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    final Message message = new Message();
                    String url = AppUtil.GetTopThreeStudent+ "?teacherID=" + teacherid;
                    HttpUtil.get(getContext(), url, new ResponseHandler() {
                        @Override
                        public void onSuccess(byte[] response) {
                            String jsonStr = new String(response);
                            try {
                                JSONObject result = new JSONObject(jsonStr);
                                String status = result.getString("reCode");
                                if (status.equalsIgnoreCase("SUCCESS")) {
                                    JSONArray ja = result.getJSONArray("students");
                                    topStudents=new ArrayList<HashMap<String, String>>();
                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject jb = ja.getJSONObject(i);
                                        HashMap map= new HashMap();
                                        map.put("studentid",jb.getString("studentid"));
                                        map.put("studentName",jb.getString("studentName"));
                                        topStudents.add(map);
                                    }
                                    message.what = 1;
                                    message.obj = topStudents;
                                    handler.sendMessage(message);
                                } else {
                                    message.what = -1;
                                    message.obj = "获取失败";
                                    handler.sendMessage(message);
                                }
                            } catch (JSONException e) {
                                Log.e("www", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            message.what = 0;
                            message.obj = "获取失败，请重试";
                            handler.sendMessage(message);
                        }
                    });
                }
            }.start();
        }
    }

    private void getLastThreeStudent(){
        {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    final Message message = new Message();
                    String url = AppUtil.GetLastThreeStudent+ "?teacherID=" + teacherid;
                    HttpUtil.get(getContext(), url, new ResponseHandler() {
                        @Override
                        public void onSuccess(byte[] response) {
                            String jsonStr = new String(response);
                            try {
                                JSONObject result = new JSONObject(jsonStr);
                                String status = result.getString("reCode");
                                if (status.equalsIgnoreCase("SUCCESS")) {
                                    JSONArray ja = result.getJSONArray("students");
                                    lastStudents=new ArrayList<HashMap<String, String>>();
                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject jb = ja.getJSONObject(i);
                                        HashMap map= new HashMap();
                                        map.put("studentid",jb.getString("studentid"));
                                        map.put("studentName",jb.getString("studentName"));
                                        lastStudents.add(map);
                                    }
                                    message.what = 2;
                                    message.obj = lastStudents;
                                    handler.sendMessage(message);
                                } else {
                                    message.what = -1;
                                    message.obj = "获取失败";
                                    handler.sendMessage(message);
                                }
                            } catch (JSONException e) {
                                Log.e("www", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                        }
                    });
                }
            }.start();
        }
    }

    private void getUpTopThreeStudent(){
        {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    final Message message = new Message();
                    String url = AppUtil.GetUpTopThreeStudent+ "?teacherID=" + teacherid;
                    HttpUtil.get(getContext(), url, new ResponseHandler() {
                        @Override
                        public void onSuccess(byte[] response) {
                            String jsonStr = new String(response);
                            try {
                                JSONObject result = new JSONObject(jsonStr);
                                String status = result.getString("reCode");
                                if (status.equalsIgnoreCase("SUCCESS")) {
                                    JSONArray ja = result.getJSONArray("students");
                                    upTopStudents=new ArrayList<HashMap<String, String>>();
                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject jb = ja.getJSONObject(i);
                                        HashMap map= new HashMap();
                                        map.put("studentid",jb.getString("studentid"));
                                        map.put("studentName",jb.getString("studentName"));
                                        upTopStudents.add(map);
                                    }
                                    message.what = 3;
                                    message.obj = upTopStudents;
                                    handler.sendMessage(message);
                                } else {
                                    message.what = -1;
                                    message.obj = "获取失败";
                                    handler.sendMessage(message);
                                }
                            } catch (JSONException e) {
                                Log.e("www", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                        }
                    });
                }
            }.start();
        }
    }


}
