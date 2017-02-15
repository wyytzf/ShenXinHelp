package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainPagerFragment extends Fragment {
    //主容器，刷新控件
    private SwipeRefreshLayout swipeRefreshLayout;
    // 轮播图
    private Banner banner;

    //身材帮，视力帮，心理帮，体考帮
    private View grid_body;
    private View grid_eye;
    private View grid_heart;
    private View grid_exam;

    String[] urls = {"http://img3.imgtn.bdimg.com/it/u=1340784719,3145976582&fm=21&gp=0.jpg",
            "http://mvimg2.meitudata.com/569b0bb20a8af8855.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/7aec54e736d12f2e42db1cf54fc2d5628435684d.jpg"};
    String[] t = {"title1", "title2", "title3"};
    List<String> images;
    List<String> titles;

    Activity activity;
    View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mainpage, container, false);
        activity = getActivity();
        initViews();
        initListener();
        return root;
    }


    private void initListener() {
        // 刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // 轮播图点击监听
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        // 身材帮点击监听
        grid_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BodyHelpActivity.class);
                startActivity(intent);
            }
        });
        // 视力帮点击监听
        grid_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EyeHelpActivity.class);
                startActivity(intent);
            }
        });
        // 心理帮点击监听
        grid_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MentalityActivity.class);
                startActivity(intent);
            }
        });
        // 体考帮点击监听
        grid_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ExamHelpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {

        images = new ArrayList<>();
        titles = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            images.add(urls[i]);
            titles.add(t[i]);
        }

        banner = (Banner) root.findViewById(R.id.banner);

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(6000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.startAutoPlay();

        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefreshlayout);

        grid_body = root.findViewById(R.id.grid1);
        grid_eye = root.findViewById(R.id.grid2);
        grid_heart = root.findViewById(R.id.grid3);
        grid_exam = root.findViewById(R.id.grid4);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        banner.stopAutoPlay();
    }
}
