package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYPreViewManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xd.shenxinhelp.R;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class HelpContentActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private HelpContentAdapter helpContentAdapter;
    private OrientationUtils orientationutils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_content);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);


        helpContentAdapter = new HelpContentAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.help_content_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(helpContentAdapter);
    }

    class HelpContentAdapter extends RecyclerView.Adapter<HelpContentAdapter.MyViewHolder> {

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
            holder.textView.setText(position + "");
            holder.standardGSYVideoPlayer.setUp("http://baobab.wdjcdn.com/14564977406580.mp4", true, "");

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            //            ImageView imageView;
            StandardGSYVideoPlayer standardGSYVideoPlayer;
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                standardGSYVideoPlayer = (StandardGSYVideoPlayer) itemView.findViewById(R.id.item_imageview);
//                imageView = (ImageView) itemView.findViewById(R.id.item_imageview);
                textView = (TextView) itemView.findViewById(R.id.item_textview_time);
            }
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
}
