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
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.HelpContent;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ExamHelpItemActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    private String title;
    private String buwei;
    private String type;
    private String userID;
    //    String[] item_image = {};
    String[] item_text = {"1000米跑/800米跑", "立定跳远", "50米跑", "前掷实心球", "篮球", "排球", "足球"};
    private ExamItemAdapter examItemAdapter;

    List<HelpContent> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_help_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
//
        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        buwei = intent.getStringExtra("buwei");
        type = intent.getStringExtra("type");
        getSupportActionBar().setTitle(title);
        lists = new ArrayList<HelpContent>();


        examItemAdapter = new ExamItemAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.exam_item_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(examItemAdapter);
        examItemAdapter.setOnItemClickListener(new OnMyItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(ExamHelpItemActivity.this, WebViewActivity.class);
                intent.putExtra("url", lists.get(position).getWebUrl());
                intent.putExtra("title", lists.get(position).getName());
                intent.putExtra("image_url", lists.get(position).getReosurce_url());
                startActivity(intent);
            }
        });
        GoRequest();
    }

    private void GoRequest() {
        OkHttp.get(AppUtil.GetExerciseItem + "buwei=" + buwei + "&userID=" + userID + "&type=" + type, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {

            }

            @Override
            public void onResponse(String str) {
                ParseResponse(str);
//                helpContentAdapter.update(contentList);
                examItemAdapter.notifyDataSetChanged();
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
                lists.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class ExamItemAdapter extends RecyclerView.Adapter<ExamItemAdapter.ItemViewHodler> {

        OnMyItemClickListener OnMyItemClickListener = null;

        public void setOnItemClickListener(OnMyItemClickListener onMyItemClickListener) {
            this.OnMyItemClickListener = onMyItemClickListener;
        }

        @Override
        public ItemViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(ExamHelpItemActivity.this).inflate(R.layout.item_examhelp, parent, false);
            ItemViewHodler viewHodler = new ItemViewHodler(inflate);
            return viewHodler;
        }

        @Override
        public void onBindViewHolder(ItemViewHodler holder, final int position) {
            holder.imageView.setText((position + 1) + "");
            holder.textView.setText(lists.get(position).getName());
            if (OnMyItemClickListener != null) {
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnMyItemClickListener.OnItemClick(v, position);
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return lists.size();
        }

        class ItemViewHodler extends RecyclerView.ViewHolder {

            TextView imageView;
            TextView textView;
            View container;

            public ItemViewHodler(View itemView) {
                super(itemView);
                imageView = (TextView) itemView.findViewById(R.id.item_imageview);
                textView = (TextView) itemView.findViewById(R.id.item_textview);
                container = itemView.findViewById(R.id.item_container);
            }
        }
    }


//    class HelpContent2Adapter extends RecyclerView.Adapter<MyViewHolder> {
//
//        OnMyItemClickListener OnMyItemClickListener = null;
//
//
//
//        public void setOnItemClickListener(OnMyItemClickListener onMyItemClickListener) {
//            this.OnMyItemClickListener = onMyItemClickListener;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return null;
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//
//        }
//
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            ImageView imageview;
//            TextView textview;
//
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                imageview = (ImageView) itemView.findViewById(R.id.item_imageview);
//                textview = (TextView) itemView.findViewById(R.id.item_textview);
//            }
//        }
//    }

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
