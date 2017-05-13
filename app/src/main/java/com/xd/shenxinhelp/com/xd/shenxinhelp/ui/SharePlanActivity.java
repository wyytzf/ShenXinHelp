package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.netutils.OkHttp;
import com.youth.banner.loader.ImageLoaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SharePlanActivity extends AppCompatActivity {

    private TextView stu_name;
    private ListView plan_list;
    private ListView group_list;
    private EditText share_content;
    private Button start_plan;

    private JSONArray plan_array;
    private JSONArray group_array;
    private Map<Integer, Boolean> states;
    private Map<Integer, Boolean> group_check;
    private String userID;
    private String userName;
    private String class_id;
    private String className;
    private String school_id;
    private String schoolName;

    private ImageLoaderInterface imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_plan);

        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");
        userName = sp.getString("name", "");
        class_id = sp.getString("class_id", "");
        className = sp.getString("className", "");
        school_id = sp.getString("school_id", "");
        schoolName = sp.getString("schoolName", "");

        imageLoader = new GlideImageLoader();

        initUI();
        getPlanList();
        getGroupList();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        stu_name = (TextView) findViewById(R.id.stu_name);
        stu_name.setText(userName+"您好，根据您所处的年级，");
        share_content = (EditText) findViewById(R.id.share_content);

        plan_list = (ListView) findViewById(R.id.plan_list);
        group_list = (ListView) findViewById(R.id.group_list);

        start_plan = (Button) findViewById(R.id.start_plan);
        start_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = -1;
                if (plan_array.length()>0){
                    for (int i : states.keySet()){
                        if (states.get(i)){
                            position = i;
                            break;
                        }
                    }
                    if (position>-1){
                        boolean exist = false;
                        String params = "";
                        if (group_check.get(0)){
                            params+=("&schoolID="+school_id);
                            exist = true;
                        }
                        if (group_check.get(1)){
                            params+=("&classID="+class_id);
                            exist = true;
                        }
                        params+="&ringsID=";
                        for (int i : group_check.keySet()){
                            if (i==0 || i==1)
                                continue;
                            if (group_check.get(i)){
                                try {
                                    params+=(group_array.getJSONObject(i-2).getString("ringid")+"-");
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                finally {
                                    exist = true;
                                }
                            }
                        }
                        if (exist){
                            if (!"".equals(share_content.getText().toString().trim())){
                                String plan_id=null;
                                try {
                                    JSONObject temp = plan_array.getJSONObject(position);
                                    plan_id = temp.getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("params",params);
                                OkHttp.get(AppUtil.ShareToRing + "?planID="+plan_id+"&userID="+userID+"&content="+
                                        share_content.getText().toString().trim()+params, new OkHttp.ResultCallBack() {
                                    @Override
                                    public void onError(String str, Exception e) {
                                        Log.e("ShareToRing", str);
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(String str) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(str);
                                            String reCode = jsonObject.getString("reCode");
                                            Log.e("share",reCode);
                                            if ("SUCCESS".equals(reCode)){
                                                Toast.makeText(SharePlanActivity.this, "分享计划成功！", Toast.LENGTH_SHORT).show();
                                                getPlanList();
                                            }
                                            else {
                                                Log.e("Fail", jsonObject.getString("message"));
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(SharePlanActivity.this, "分享时说点什么吧", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SharePlanActivity.this, "想分享到哪个圈子呢?", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SharePlanActivity.this, "想分享哪个计划呢？", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SharePlanActivity.this, "你还没制定计划哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getPlanList(){
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        OkHttp.get(AppUtil.GetPlanByDate + "?userID="+userID+"&date="+today, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getPlanList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        plan_array = jsonObject.getJSONArray("plans");
                        plan_list.setAdapter(new PlanListAdapter());
                    }
                    else {
                        Log.e("Fail", jsonObject.getString("message"));
                        Toast.makeText(SharePlanActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getGroupList(){
        OkHttp.get(AppUtil.GetAllMyRing + "?userID="+userID, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getGroupList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        group_array = jsonObject.getJSONArray("rings");
                        Log.e("group_array",group_array.toString());
                        group_list.setAdapter(new GroupListAdapter());
                    }
                    else {
                        Log.e("Fail", jsonObject.getString("message"));
                        Toast.makeText(SharePlanActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private class PlanListAdapter extends BaseAdapter {

        PlanListAdapter(){
            states = new HashMap<>();
            for (int i=0; i<plan_array.length(); i++){
                states.put(i, false);
            }
        }

        @Override
        public int getCount() {
            return plan_array.length();
        }

        @Override
        public JSONObject getItem(int position) {
            try {
                return plan_array.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.list_view_draw_up_plan, null);
                viewHolder = new ViewHolder();
                viewHolder.plan = (TextView) convertView.findViewById(R.id.plan_number);
                viewHolder.second = (RelativeLayout) convertView.findViewById(R.id.second);
                viewHolder.picture1 = (ImageView) convertView.findViewById(R.id.picture1);
                viewHolder.picture2 = (ImageView) convertView.findViewById(R.id.picture2);
                viewHolder.name1 = (TextView) convertView.findViewById(R.id.name1);
                viewHolder.name2 = (TextView) convertView.findViewById(R.id.name2);
                viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_button);
                viewHolder.already_share = (TextView) convertView.findViewById(R.id.already_share);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();
            try {
                JSONObject jsonObject = plan_array.getJSONObject(position);
                int isShare = jsonObject.getInt("isShare");
                JSONArray items = jsonObject.getJSONArray("items");
                viewHolder.plan.setText("计划"+(position+1));
                JSONObject item1 = items.getJSONObject(0);
                imageLoader.displayImage(SharePlanActivity.this, item1.getString("imageUrl"), viewHolder.picture1);
                viewHolder.name1.setText(item1.getString("title"));
                if (items.length()>1){
                    JSONObject item2 = items.getJSONObject(1);
                    imageLoader.displayImage(SharePlanActivity.this, item2.getString("imageUrl"), viewHolder.picture2);
                    viewHolder.name2.setText(item2.getString("title"));
                    viewHolder.second.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.second.setVisibility(View.GONE);
                }
                if (isShare==0){
                    viewHolder.already_share.setVisibility(View.GONE);
                    viewHolder.radioButton.setVisibility(View.VISIBLE);
                    viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //重置，确保最多只有一项被选中
                            for (int i=0; i<plan_array.length(); i++){
                                states.put(i, false);
                            }
                            states.put(position, true);
                            PlanListAdapter.this.notifyDataSetChanged();
                        }
                    });
                    viewHolder.radioButton.setChecked(states.get(position));
                }
                else {
                    viewHolder.radioButton.setVisibility(View.GONE);
                    viewHolder.already_share.setVisibility(View.VISIBLE);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }

        private class ViewHolder{
            TextView plan;
            RelativeLayout second;
            ImageView picture1;
            ImageView picture2;
            TextView name1;
            TextView name2;
            RadioButton radioButton;
            TextView already_share;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class GroupListAdapter extends BaseAdapter{

        GroupListAdapter(){
            group_check = new HashMap<>();
            for (int i=0; i<group_array.length()+2; i++){
                group_check.put(i, false);
            }
        }

        @Override
        public int getCount() {
            return group_array.length()+2;
        }

        @Override
        public JSONObject getItem(int position) {
            if (position > 1){
                try {
                    return group_array.getJSONObject(position-2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                return null;
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.list_view_group, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();
            try {
                if (position==0){
                    viewHolder.name.setText(schoolName);
                }
                else if (position==1){
                    viewHolder.name.setText(className);
                }
                else {
                    viewHolder.name.setText(group_array.getJSONObject(position-2).getString("title"));
                }
                viewHolder.checkBox.setOnCheckedChangeListener(null);
                viewHolder.checkBox.setChecked(group_check.get(position));
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        group_check.put(position, isChecked);
                        GroupListAdapter.this.notifyDataSetChanged();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }

        private class ViewHolder{
            TextView name;
            CheckBox checkBox;
        }
    }

}
