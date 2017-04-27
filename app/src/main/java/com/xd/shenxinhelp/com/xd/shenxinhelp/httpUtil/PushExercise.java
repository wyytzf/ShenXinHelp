package com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.netutils.OkHttp;
import com.youth.banner.loader.ImageLoaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by MCX on 2017/4/27.
 */

public class PushExercise {

    private Activity activity;
    private JSONArray exercise_list;
    private SharedPreferences sp;
    private String userID;
    private int first;
    private int second;
    private ImageLoaderInterface imageLoader;
    private String today;

    public PushExercise(Activity activity){
        this.activity = activity;
        sp = activity.getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");
        imageLoader = new GlideImageLoader();
        today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public void pushExercise(){
        getTodayPlanList();
    }

    private void getTodayPlanList(){
        OkHttp.get(AppUtil.GetPlanByDate + "?userID="+userID+"&date="+today, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getTodayPlanList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        if (jsonObject.getJSONArray("plans").length()==0){
                            getAllExercise();
                        }
                    }
                    else {
                        Log.e("Fail", jsonObject.getString("message"));
                        Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void creatDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("温馨提示");
        builder.setMessage("系统根据您所在的年级，专门为您推送了以下锻炼计划，是否制定该计划？");
        View root = activity.getLayoutInflater().inflate(R.layout.push_exercise, null);
        ImageView picture1 = (ImageView) root.findViewById(R.id.picture1);
        ImageView picture2 = (ImageView) root.findViewById(R.id.picture2);
        TextView name1 = (TextView) root.findViewById(R.id.name1);
        TextView name2 = (TextView) root.findViewById(R.id.name2);
        try {
            JSONObject jsonObject1 = exercise_list.getJSONObject(first);
            JSONObject jsonObject2 = exercise_list.getJSONObject(second);
            imageLoader.displayImage(activity, jsonObject1.getString("reosurce_url"), picture1);
            imageLoader.displayImage(activity, jsonObject2.getString("reosurce_url"), picture2);
            name1.setText(jsonObject1.getString("title"));
            name2.setText(jsonObject2.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.setView(root,0,0,0,0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                String params = "?userID="+userID+"&date="+today+"&exerciseIDs=";
                try {
                    params += ( exercise_list.getJSONObject(first).getString("id") + "-" +
                        exercise_list.getJSONObject(second).getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttp.get(AppUtil.CustomPlan + params, new OkHttp.ResultCallBack() {
                    @Override
                    public void onError(String str, Exception e) {
                        Log.e("CustomPlan", str);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String str) {
                        dialogInterface.dismiss();
                        Toast.makeText(activity, "制定计划成功！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getAllExercise(){
        OkHttp.get(AppUtil.GetAllExercise + "?type=0", new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getAllExercise", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        exercise_list = jsonObject.getJSONArray("items");
                        individualPush();
                        creatDialog();
                    }
                    else {
                        Log.e("Fail", jsonObject.getString("message"));
                        Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void individualPush(){
        if (exercise_list.length()>=4){
            if (sp.getString("className", "").startsWith("初")){
                int end = exercise_list.length()/2;
                Random random = new Random();
                first = random.nextInt(end-1);
                second = random.nextInt(end-1);
                while (second==first){
                    second = random.nextInt(end-1);
                }
            }
            else {
                int start = exercise_list.length()/2;
                Random random = new Random();
                first = random.nextInt(exercise_list.length()-1)
                        %(exercise_list.length()-start)+start;
                second = random.nextInt(exercise_list.length()-1)
                        %(exercise_list.length()-start)+start;
                while (second==first){
                    second = random.nextInt(exercise_list.length()-1)
                            %(exercise_list.length()-start)+start;
                }
            }
        }
        else if (exercise_list.length()>0){
            Toast.makeText(activity, "锻炼项目太少，无法推送", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(activity, "尚无锻炼项目", Toast.LENGTH_SHORT).show();
        }
    }

}
