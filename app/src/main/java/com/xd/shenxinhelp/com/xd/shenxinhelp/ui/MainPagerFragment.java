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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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


    // 首页轮播图 url
    String[] urls = {"http://img3.imgtn.bdimg.com/it/u=1340784719,3145976582&fm=21&gp=0.jpg",
            "http://mvimg2.meitudata.com/569b0bb20a8af8855.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/7aec54e736d12f2e42db1cf54fc2d5628435684d.jpg"};
    String[] t = {"title1", "title2", "title3"};
    List<String> images;
    List<String> titles;

    // 首页推荐阅读 url
    String[] urls_tuijian = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488681751&di=22b1bf638088da03ed1dadde06b859a2&imgtype=jpg&er=1&src=http%3A%2F%2Fimages.quanjing.com%2Faig001%2Fhigh%2Faig-ai12485.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488087282919&di=8679255ded16de4b0d438c53839bacce&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20131122%2FImg390611560.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488682132&di=c860ca4b3376ae088b9bd50c21888b7b&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.kaoder.com%2Fupload%2Fattach%2F000%2F578%2F578588.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488087513814&di=711389a437bdc13a634099b07037dd13&imgtype=0&src=http%3A%2F%2Fimg.qhdxw.com%2Fhealth%2Fuploads%2Fallimg%2F20161217%2F4701.jpg"};
    String[] title_tuijian = {"沉迷电子设备成青少年近视“主犯”", "不戒垃圾食品身材照样好？少年看这里",
            "化解青少年“成长烦恼”", "青少年追求“完美身材”有危害？"};


    Activity activity;
    View root;

    private ImageView news_image_1;
    private ImageView news_image_2;
    private ImageView news_image_3;
    private ImageView news_image_4;
    private TextView news_text_1;
    private TextView news_text_2;
    private TextView news_text_3;
    private TextView news_text_4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mainpage, container, false);
        activity = getActivity();
        initViews();
        initListener();
        return root;
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


        news_image_1 = (ImageView) root.findViewById(R.id.news1_image);
        news_image_2 = (ImageView) root.findViewById(R.id.news2_image);
        news_image_3 = (ImageView) root.findViewById(R.id.news3_image);
        news_image_4 = (ImageView) root.findViewById(R.id.news4_image);
        Glide.with(activity).load(urls_tuijian[0]).into(news_image_1);
        Glide.with(activity).load(urls_tuijian[1]).into(news_image_2);
        Glide.with(activity).load(urls_tuijian[2]).into(news_image_3);
        Glide.with(activity).load(urls_tuijian[3]).into(news_image_4);

        news_text_1 = (TextView) root.findViewById(R.id.news1_text);
        news_text_2 = (TextView) root.findViewById(R.id.news2_text);
        news_text_3 = (TextView) root.findViewById(R.id.news3_text);
        news_text_4 = (TextView) root.findViewById(R.id.news4_text);
        news_text_1.setText(title_tuijian[0]);
        news_text_2.setText(title_tuijian[1]);
        news_text_3.setText(title_tuijian[2]);
        news_text_4.setText(title_tuijian[3]);


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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        banner.stopAutoPlay();
    }
}
