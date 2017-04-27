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

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.ui.DrawUpPlanActivity;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by MCX on 2017/4/27.
 */

public class PushExercise {

    private Activity activity;
    private JSONArray exercise_list;
    private SharedPreferences sp;

    private PushExercise(Activity activity){
        this.activity = activity;
        sp = activity.getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
    }

    private void creatDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("温馨提示");
        builder.setMessage("系统根据您所在的年级，专门为您推送了以下锻炼计划，是否制定该计划？");
        View root = activity.getLayoutInflater().inflate(R.layout.push_exercise, null);
        ImageView picture1 = (ImageView) root.findViewById(R.id.picture1);
        TextView name1 = (TextView) root.findViewById(R.id.name1);
        ImageView picture2 = (ImageView) root.findViewById(R.id.picture2);
        TextView name2 = (TextView) root.findViewById(R.id.name2);
        builder.setView(root,0,0,0,0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
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
        if (exercise_list.length()>0){
            sp.getString("className", "");
        }
        else {
            //Toast.makeText(activity, "尚无锻炼项目", Toast.LENGTH_SHORT).show();
        }
    }

}
