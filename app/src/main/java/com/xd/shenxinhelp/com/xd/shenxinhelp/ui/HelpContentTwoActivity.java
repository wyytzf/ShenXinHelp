package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.HelpContent;
import com.xd.shenxinhelp.model.HelpContent2;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class HelpContentTwoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HelpContent2Adapter helpContentAdapter;
    private String buwei;
    private String type;
    private List<HelpContent> contentList;
    private String userID;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpcontenttwo);
//        contentList = new ArrayList<HelpContent2>();
//        HelpContent2 h1 = new HelpContent2();
//        h1.setTitle("有趣的心理小测验～");
//        h1.setImage_url("file:///android_asset/xinlijiance1.jpeg");
//        h1.setWeb_url("file:///android_asset/xinlijiance1.html");
//
//        HelpContent2 h2 = new HelpContent2();
//        h2.setTitle("笑容看你的新机有多重！");
//        h2.setImage_url("file:///android_asset/xinlijiance2.jpeg");
//        h2.setWeb_url("file:///android_asset/xinlijiance2.html");
//
//        contentList.add(h1);
//        contentList.add(h2);
        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");
        Intent intent = getIntent();
        buwei = intent.getStringExtra("buwei");
        type = intent.getStringExtra("type");
        title = intent.getStringExtra("title");
        contentList = new ArrayList<HelpContent>();
        initView();
        getRequest();
    }

    private void getRequest() {
        OkHttp.get(AppUtil.GetExerciseItem + "buwei=" + buwei + "&userID=" + userID + "&type=" + type, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {

            }

            @Override
            public void onResponse(String str) {
                ParseResponse(str);
                helpContentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void ParseResponse(String str) {
        try {
            JSONObject js = new JSONObject(str);
            JSONArray ja = js.getJSONArray("exercisees");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                HelpContent content = new HelpContent();
                content.setId(jb.getString("id"));
                content.setBuwei(jb.getString("buwei"));
                content.setName(jb.getString("title"));
                content.setReosurce_url(jb.getString("reosurce_url"));
                content.setTotal_time(jb.getString("total_time"));
                content.setFee_cridits(jb.getString("fee_cridits"));
                content.setHeat(jb.getString("heat"));
                content.setGet_degree(jb.getString("get_degree"));
                content.setDiffculty(jb.getString("diffculty"));
                content.setWebUrl(jb.getString("webUrl"));
                content.setHasBuy(jb.getString("hasBuy"));
                contentList.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle(title);

        helpContentAdapter = new HelpContent2Adapter();
        recyclerView = (RecyclerView) findViewById(R.id.help_content_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(helpContentAdapter);
        helpContentAdapter.setOnItemClickListener(new OnMyItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(HelpContentTwoActivity.this, WebViewActivity.class);
                intent.putExtra("url", contentList.get(position).getWebUrl());
                intent.putExtra("title", contentList.get(position).getName());
                intent.putExtra("image_url", contentList.get(position).getReosurce_url());
                startActivity(intent);
            }
        });

    }


    class HelpContent2Adapter extends RecyclerView.Adapter<HelpContent2Adapter.MyViewHolder> {

        OnMyItemClickListener OnMyItemClickListener = null;


        @Override
        public HelpContent2Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(HelpContentTwoActivity.this).inflate(R.layout.item_helpcontent2, parent, false);
            HelpContent2Adapter.MyViewHolder viewHolder = new HelpContent2Adapter.MyViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(HelpContent2Adapter.MyViewHolder holder, final int position) {
            HelpContent helpContent = contentList.get(position);
            holder.textview.setText(helpContent.getName());
            Glide.with(HelpContentTwoActivity.this).load(helpContent.getReosurce_url()).into(holder.imageview);
            if (OnMyItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnMyItemClickListener.OnItemClick(v, position);
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return contentList.size();
        }

        public void setOnItemClickListener(OnMyItemClickListener onMyItemClickListener) {
            this.OnMyItemClickListener = onMyItemClickListener;
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageview;
            TextView textview;

            public MyViewHolder(View itemView) {
                super(itemView);
                imageview = (ImageView) itemView.findViewById(R.id.item_imageview);
                textview = (TextView) itemView.findViewById(R.id.item_textview);
            }
        }
    }

    public interface OnMyItemClickListener {
        void OnItemClick(View view, int position);
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
