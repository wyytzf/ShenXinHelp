package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.DialogFactory;
import com.xd.shenxinhelp.netutils.OkHttp;
import com.youth.banner.loader.ImageLoaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RelationshipActivity extends AppCompatActivity {

    private Button add_relationship;
    private ListView relationship_list_view;

    private Map<String, Integer> type_name_id_map;
    private JSONArray myRelationships;
    private String userID;
    private String[] types;

    private Dialog mDialog=null;
    private ImageLoaderInterface imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);

        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");

        initUI();
        type_name_id_map = new HashMap<>();
        getTypeList();
    }

    private void initUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        add_relationship = (Button) findViewById(R.id.add_relationship);
        add_relationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (types.length>0){
                    creatDialog();
                }
                else {
                    getTypeList();
                    Toast.makeText(RelationshipActivity.this, "网络连接中断", Toast.LENGTH_SHORT).show();
                }
            }
        });

        relationship_list_view = (ListView) findViewById(R.id.relationship_list_view);
        relationship_list_view.setAdapter(new RelationshipAdapter());

        imageLoader = new GlideImageLoader();
    }

    private void getRelationshipList(){
        OkHttp.get(AppUtil.GetMyAllRelationship + "?studentID="+userID, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                dismissRequestDialog();
                Log.e("getRelationshipList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                analyzeRelationshipList(str);
            }
        });
    }

    private void analyzeRelationshipList(String str){
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reCode = jsonObject.getString("reCode");
            if ("SUCCESS".equals(reCode)){
                myRelationships = jsonObject.getJSONArray("myRelationships");
                relationship_list_view.setAdapter(new RelationshipAdapter(myRelationships));
                dismissRequestDialog();
            }
            else {
                dismissRequestDialog();
                Log.e("Fail", jsonObject.getString("message"));
            }
        }
        catch (Exception e){
            dismissRequestDialog();
            e.printStackTrace();
        }
    }

    private void getTypeList(){
        showRequestDialog();
        OkHttp.get(AppUtil.GetAllRelationshipType , new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                dismissRequestDialog();
                Log.e("getTypeList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                analyzeTypeList(str);
            }
        });
    }

    private void analyzeTypeList(String str){
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reCode = jsonObject.getString("reCode");
            if ("SUCCESS".equals(reCode)){
                JSONArray relationshipTypes = jsonObject.getJSONArray("relationshipTypes");
                for (int i=0; i<relationshipTypes.length(); i++){
                    JSONObject temp = relationshipTypes.getJSONObject(i);
                    type_name_id_map.put(temp.getString("relationshipName"), temp.getInt("relationshipid"));
                    types[i] = temp.getString("relationshipName");
                }
                getRelationshipList();
            }
            else {
                dismissRequestDialog();
                Log.e("Fail", jsonObject.getString("message"));
            }
        }
        catch (Exception e){
            dismissRequestDialog();
            e.printStackTrace();
        }
    }

    private void creatDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加亲密关系");
        builder.setMessage("输入亲密关系的账号和类型");
        View view = getLayoutInflater().inflate(R.layout.dialog_add_relationship, null);
        final EditText relationship_account = (EditText) view.findViewById(R.id.relationship_account);
        final Spinner relationship_type = (Spinner) view.findViewById(R.id.relationship_type);
        relationship_type.setAdapter(new ArrayAdapter<String>(RelationshipActivity.this,
                android.R.layout.simple_spinner_item, types));
        builder.setView(view,60,0,60,0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String account = relationship_account.getText().toString().trim();
                int typeID = type_name_id_map.get(relationship_type.getSelectedItem().toString());
                addRelationship(account, typeID);
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

    private void addRelationship(String parentAccount, int relationshipTypeID){
        OkHttp.get(AppUtil.AddCloseRelationship + "studentID="+userID+"&parentAccount="+parentAccount+
                        "&relationshipTypeID"+relationshipTypeID, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("addRelationship", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        Toast.makeText(RelationshipActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.e("Fail", jsonObject.getString("message"));
                        Toast.makeText(RelationshipActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RelationshipActivity.this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
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

    public void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(this);
        mDialog.show();
    }

    public void dismissRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private class RelationshipAdapter extends BaseAdapter{

        private JSONArray relations;

        RelationshipAdapter(){
            this.relations = new JSONArray();
        }

        RelationshipAdapter(JSONArray relations){
            this.relations = relations;
        }

        @Override
        public int getCount() {
            return relations.length();
        }

        @Override
        public JSONObject getItem(int position) {
            try {
                return relations.getJSONObject(position);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView==null){
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.list_view_relation, null);
                viewHolder.head = (ImageView) convertView.findViewById(R.id.list_view_relation_image);
                viewHolder.name = (TextView) convertView.findViewById(R.id.list_view_relation_name);
                viewHolder.type = (TextView) convertView.findViewById(R.id.list_view_relation_type);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            try {
                JSONObject jsonObject = relations.getJSONObject(position);
                imageLoader.displayImage(RelationshipActivity.this, jsonObject.getString("parentImage"), viewHolder.head);
                viewHolder.name.setText(jsonObject.getString("parentName"));
                viewHolder.type.setText("关系："+jsonObject.getString("relationshipType"));
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }

        private class ViewHolder{
            ImageView head;
            TextView name;
            TextView type;
        }
    }
}
