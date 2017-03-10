package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYPreViewManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ConnectUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.HelpContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class HelpContentActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private HelpContentAdapter helpContentAdapter;
    private OrientationUtils orientationutils;
    private String buwei;
    private List<HelpContent> contentList;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            //dismissRequestDialog();
            switch (msg.what) {
                case 1: {

                    try {
                        JSONObject result = new JSONObject((String) msg.obj);
                        JSONArray array = result.getJSONArray("exercisees");
                        JSONObject object;
                        contentList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            HelpContent helpContent=new HelpContent();
                            object = array.getJSONObject(i);
                            helpContent.setId(object.getString("id"));
                            helpContent.setBuwei(object.getString("buwei"));
                            helpContent.setName(object.getString("name"));
                            helpContent.setReosurce_url(object.getString("reosurce_url"));
                            helpContent.setTotal_time(object.getString("total_time"));
                            helpContent.setFee_cridits(object.getString("fee_cridits"));
                            helpContent.setHeat(object.getString("heat"));
                            helpContent.setGet_degree(object.getString("get_degree"));
                            helpContent.setDiffculty(object.getString("diffculty"));
                            helpContent.setWebUrl(object.getString("webUrl"));
                            helpContent.setHasBuy(object.getString("hasBuy"));
                            contentList.add(helpContent);

                        }
                        if (contentList == null||contentList.size()==0) {
                            contentList.clear();
                            for (int i = 0; i < 5; i++) {
                                HelpContent helpContent=new HelpContent();
                                helpContent.setId(i+"");
                                helpContent.setBuwei("手臂");
                                helpContent.setName("燃烧吧，手臂");
                                helpContent.setReosurce_url("http://baobab.wdjcdn.com/14564977406580.mp4");
                                helpContent.setTotal_time("5分钟");
                                helpContent.setFee_cridits("0");
                                helpContent.setHeat("100卡");
                                helpContent.setGet_degree("3");
                                helpContent.setDiffculty("3");
                                helpContent.setWebUrl("");
                                helpContent.setHasBuy("1");
                                contentList.add(helpContent);
                            }
                        }
                        helpContentAdapter.notifyDataSetChanged();

//                        recyclerVie
                    } catch (JSONException e) {
                        Log.e("mmm", e.getMessage());
                    }
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
        setContentView(R.layout.activity_help_content);
        Intent intent = getIntent();
        buwei=intent.getStringExtra("buwei");
        contentList = new ArrayList<HelpContent>();
        getHelpContent();
        initView();

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
        HelpContentAdapter(List<HelpContent> helpContents){
            this.helpContents= helpContents;
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
            HelpContent helpContent=helpContents.get(position);
            holder.textView.setText(helpContent.getName());
            holder.standardGSYVideoPlayer.setUp(helpContent.getReosurce_url(), true, "");
            holder.totalTime.setText(helpContent.getTotal_time());
            holder.calorie.setText(helpContent.getHeat());
            float numStars= Float.parseFloat(helpContent.getDiffculty());
            holder.difficult.setRating(numStars);
            //holder.standardGSYVideoPlayer.setUp("http://baobab.wdjcdn.com/14564977406580.mp4", true, "");

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
            public MyViewHolder(View itemView) {
                super(itemView);
                standardGSYVideoPlayer = (StandardGSYVideoPlayer) itemView.findViewById(R.id.item_imageview);
//                imageView = (ImageView) itemView.findViewById(R.id.item_imageview);
                textView = (TextView) itemView.findViewById(R.id.item_textview_time);
                totalTime =(TextView) itemView.findViewById(R.id.item_textview_time);
                calorie =(TextView) itemView.findViewById(R.id.item_textview_calorie);
                difficult=(RatingBar)itemView.findViewById(R.id.item_ratingbar_difficult);
            }
        }
    }
    public void getHelpContent() {


        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();
                String urlget = AppUtil.GetExerciseItem + "?type=1&buwei=" + buwei;

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
