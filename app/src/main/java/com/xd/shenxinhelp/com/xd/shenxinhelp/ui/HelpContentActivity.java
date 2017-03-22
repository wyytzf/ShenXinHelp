package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.HelpContent;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelpContentActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private HelpContentAdapter helpContentAdapter;
    private OrientationUtils orientationutils;
    private String buwei;
    private String type;
    private List<HelpContent> contentList;
    private String userID;


    private String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_content);
        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");
        Intent intent = getIntent();
        buwei = intent.getStringExtra("buwei");
        type = intent.getStringExtra("type");
        contentList = new ArrayList<HelpContent>();
        initView();
        if (type.equals("yan")) {
            HelpContent content1 = new HelpContent();
            content1.setWebUrl("http://192.168.0.21:8080/BodyMindHelper/webview/yan1.FLV");
            content1.setName("最新版眼保健操教学视频");
            content1.setReosurce_url("http://192.168.0.21:8080/BodyMindHelper/images/yan1_cover.png");
            content1.setDiffculty("1");
            content1.setHeat("");
            content1.setTotal_time("");
            HelpContent content2 = new HelpContent();
            content2.setWebUrl("http://192.168.0.21:8080/BodyMindHelper/webview/yan2.FLV");
            content2.setName("如何找准穴位 正确做眼保健操");
            content2.setReosurce_url("http://192.168.0.21:8080/BodyMindHelper/images/yan2_cover.png");
            content2.setDiffculty("1");
            content2.setHeat("");
            content2.setTotal_time("");
            contentList.add(content1);
            contentList.add(content2);
            helpContentAdapter.update(contentList);
        } else {
            Request();
        }


    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        helpContentAdapter = new HelpContentAdapter(contentList);
        recyclerView = (RecyclerView) findViewById(R.id.help_content_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(helpContentAdapter);
    }

    class HelpContentAdapter extends RecyclerView.Adapter<HelpContentAdapter.MyViewHolder> {
        List<HelpContent> helpContents;

        HelpContentAdapter(List<HelpContent> helpContents) {
            this.helpContents = helpContents;
        }

        public void update(List<HelpContent> helpContents) {
            this.helpContents = helpContents;
            this.notifyDataSetChanged();
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(HelpContentActivity.this).inflate(R.layout.item_help_content, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(inflate);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
//            Glide.with(HelpContentActivity.this).
//                    load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488273623&di=ea34c54ab63f18a6ae02b94d99d70b25&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.bz55.com%2Fuploads%2Fallimg%2F150318%2F140-15031PUR6.jpg").into(holder.imageView);
            HelpContent helpContent = helpContents.get(position);
            holder.textView.setText(helpContent.getName());
//            holder.standardGSYVideoPlayer.setUp(helpContent.getReosurce_url(), true, "");
            holder.totalTime.setText(helpContent.getTotal_time());
            holder.calorie.setText(helpContent.getHeat());
            float numStars = Float.parseFloat(helpContent.getDiffculty());
            holder.difficult.setRating(numStars);
            holder.standardGSYVideoPlayer.setUp(helpContent.getWebUrl(), true, "");
            holder.standardGSYVideoPlayer.setStandardVideoAllCallBack(new StandardVideoAllCallBack() {
                @Override
                public void onClickStartThumb(String s, Object... objects) {
                    Toast.makeText(HelpContentActivity.this, "1", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onClickBlank(String s, Object... objects) {

                }

                @Override
                public void onClickBlankFullscreen(String s, Object... objects) {

                }

                @Override
                public void onPrepared(String s, Object... objects) {

                }

                @Override
                public void onClickStartIcon(String s, Object... objects) {
                    Toast.makeText(HelpContentActivity.this, "开始锻炼", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onClickStartError(String s, Object... objects) {

                }

                @Override
                public void onClickStop(String s, Object... objects) {

                }

                @Override
                public void onClickStopFullscreen(String s, Object... objects) {

                }

                @Override
                public void onClickResume(String s, Object... objects) {

                }

                @Override
                public void onClickResumeFullscreen(String s, Object... objects) {

                }

                @Override
                public void onClickSeekbar(String s, Object... objects) {

                }

                @Override
                public void onClickSeekbarFullscreen(String s, Object... objects) {

                }

                @Override
                public void onAutoComplete(String s, Object... objects) {

                }

                @Override
                public void onEnterFullscreen(String s, Object... objects) {

                }

                @Override
                public void onQuitFullscreen(String s, Object... objects) {

                }

                @Override
                public void onQuitSmallWidget(String s, Object... objects) {

                }

                @Override
                public void onEnterSmallWidget(String s, Object... objects) {

                }

                @Override
                public void onTouchScreenSeekVolume(String s, Object... objects) {

                }

                @Override
                public void onTouchScreenSeekPosition(String s, Object... objects) {

                }

                @Override
                public void onTouchScreenSeekLight(String s, Object... objects) {

                }

                @Override
                public void onPlayError(String s, Object... objects) {

                }
            });
            ImageView imageview = new ImageView(HelpContentActivity.this);
            Glide.with(HelpContentActivity.this).load(helpContent.getReosurce_url()).into(imageview);
            holder.standardGSYVideoPlayer.setThumbImageView(imageview);
            if (type.equals("yan")) {
                holder.useless.setVisibility(View.GONE);
            }
        }


        @Override
        public int getItemCount() {
            return helpContents.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            //            ImageView imageView;
            StandardGSYVideoPlayer standardGSYVideoPlayer;
            TextView textView;
            TextView totalTime;
            TextView calorie;
            RatingBar difficult;
            View useless;

            public MyViewHolder(View itemView) {
                super(itemView);
                standardGSYVideoPlayer = (StandardGSYVideoPlayer) itemView.findViewById(R.id.item_imageview);
//                imageView = (ImageView) itemView.findViewById(R.id.item_imageview);
                textView = (TextView) itemView.findViewById(R.id.item_textview_time);
                totalTime = (TextView) itemView.findViewById(R.id.item_textview_time);
                calorie = (TextView) itemView.findViewById(R.id.item_textview_calorie);
                difficult = (RatingBar) itemView.findViewById(R.id.item_ratingbar_difficult);
                useless = itemView.findViewById(R.id.useless_container);
            }
        }
    }


//    private void submit() {
//        OkHttp.get(AppUtil.DOEXERCISE+"");
//    }


    private void Request() {
        OkHttp.get(AppUtil.GetExerciseItem + "buwei=" + buwei + "&userID=" + userID + "&type=" + type, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {

            }

            @Override
            public void onResponse(String str) {
                ParseResponse(str);
                helpContentAdapter.update(contentList);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        GSYVideoPlayer.releaseAllVideos();
        super.onPause();
    }
}
