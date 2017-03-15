package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private View news1;
    private View news2;
    private View news3;
    private View news4;


    private ImageView news1_image, news2_image, news3_image, news4_image;
    private TextView news1_text, news2_text, news3_text, news4_text;
    String[] urls = {"http://img3.imgtn.bdimg.com/it/u=1340784719,3145976582&fm=21&gp=0.jpg",
            "http://mvimg2.meitudata.com/569b0bb20a8af8855.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/7aec54e736d12f2e42db1cf54fc2d5628435684d.jpg"};
    String[] t = {"title1", "title2", "title3"};
    List<String> images2;
    List<String> titles2;
    List<String> images;
    List<String> titles;
    List<String> weburls2;
    List<String> weburls;
    private ImageLoaderInterface imageLoader;
    Activity activity;
    View root;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            //dismissRequestDialog();
            switch (msg.what) {
                case 3: {

                    try {
                        JSONObject result = new JSONObject((String) msg.obj);
                        JSONArray array = result.getJSONArray("images");
                        JSONObject object;
                        images = new ArrayList<>();
                        titles = new ArrayList<>();
                        weburls = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            object = array.getJSONObject(i);
                            String title = object.getString("title");
                            String image = object.getString("imageUrl");
                            String weburl = object.getString("webUrl");
                            images.add(image);
                            titles.add(title);
                            weburls.add(weburl);
                        }
                        if (images == null) {
                            images.clear();
                            titles.clear();
                            for (int i = 0; i < urls.length; i++) {
                                images.add(urls[i]);
                                titles.add(t[i]);
                            }
                        }
                        initTop();
                        getHomePageImages("4");
                    } catch (JSONException e) {
                        Log.e("mmm", e.getMessage());
                    }
                }

                break;
                case 4: {
                    try {
                        JSONObject result = new JSONObject((String) msg.obj);
                        JSONArray array = result.getJSONArray("images");
                        JSONObject object;
                        images2 = new ArrayList<>();
                        titles2 = new ArrayList<>();
                        weburls2 = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            object = array.getJSONObject(i);
                            String title = object.getString("title");
                            String image = object.getString("imageUrl");
                            String url = object.getString("webUrl");
                            images2.add(image);
                            titles2.add(title);
                            weburls2.add(url);
                        }
                        System.out.println("--------------" + images2.get(0));
                        imageLoader = new GlideImageLoader();
                        imageLoader.displayImage(activity, images2.get(0), news1_image);
                        imageLoader.displayImage(activity, images2.get(1), news2_image);
                        imageLoader.displayImage(activity, images2.get(2), news3_image);
                        imageLoader.displayImage(activity, images2.get(3), news4_image);
                        news1_text.setText(titles2.get(0));
                        news2_text.setText(titles2.get(1));
                        news3_text.setText(titles2.get(2));
                        news4_text.setText(titles2.get(3));
                    } catch (JSONException e) {
                        Log.e("mmm", e.getMessage());
                    }
                    break;
                }
                case -1: {

                    break;
                }
                case -2: {

                    break;
                }
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mainpage, container, false);
        activity = getActivity();
        getHomePageImages("3");
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

    public void getHomePageImages(final String typeStr) {


        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();
                String urlget = AppUtil.GetHomePageImages + "?type=" + typeStr;

                HttpUtil.get(activity, urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);
                            String status = result.getString("reCode");
                            if (status.equalsIgnoreCase("success")) {
                                message.obj = jsonStr;
                                message.what = Integer.parseInt(typeStr);
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

    public void initTop() {
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

        // 轮播图点击监听
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                switch (position) {
                    case 1:
                        intent.putExtra("url", weburls.get(0));
                        intent.putExtra("title", titles.get(0));
                        intent.putExtra("image_url", images.get(0));
                        break;
                    case 2:
                        intent.putExtra("url", weburls.get(0));
                        intent.putExtra("title", titles.get(1));
                        intent.putExtra("image_url", images.get(1));
                        break;
                    case 3:
                        intent.putExtra("url", weburls.get(0));
                        intent.putExtra("title", titles.get(2));
                        intent.putExtra("image_url", images.get(2));
                        break;
                }
                startActivity(intent);
            }
        });
    }

    private void initViews() {

//        images = new ArrayList<>();
//        titles = new ArrayList<>();
//        for (int i = 0; i < urls.length; i++) {
//            images.add(urls[i]);
//            titles.add(t[i]);
//        }


        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefreshlayout);

        grid_body = root.findViewById(R.id.grid1);
        grid_eye = root.findViewById(R.id.grid2);
        grid_heart = root.findViewById(R.id.grid3);
        grid_exam = root.findViewById(R.id.grid4);

        news1_image = (ImageView) root.findViewById(R.id.news1_image);
        news2_image = (ImageView) root.findViewById(R.id.news2_image);
        news3_image = (ImageView) root.findViewById(R.id.news3_image);
        news4_image = (ImageView) root.findViewById(R.id.news4_image);

        news1_text = (TextView) root.findViewById(R.id.news1_text);
        news2_text = (TextView) root.findViewById(R.id.news2_text);
        news3_text = (TextView) root.findViewById(R.id.news3_text);
        news4_text = (TextView) root.findViewById(R.id.news4_text);


        news1 = root.findViewById(R.id.news_1);
        news2 = root.findViewById(R.id.news_2);
        news3 = root.findViewById(R.id.news_3);
        news4 = root.findViewById(R.id.news_4);
        news1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("url", weburls2.get(0));
                intent.putExtra("title", titles2.get(0));
                intent.putExtra("image_url", images2.get(0));
                startActivity(intent);
            }
        });
        news2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("url", weburls2.get(1));
                intent.putExtra("title", titles2.get(1));
                intent.putExtra("image_url", images2.get(1));
                startActivity(intent);
            }
        });
        news3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("url", weburls2.get(2));
                intent.putExtra("title", titles2.get(2));
                intent.putExtra("image_url", images2.get(2));
                startActivity(intent);
            }
        });
        news4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("url", weburls2.get(3));
                intent.putExtra("title", titles2.get(3));
                intent.putExtra("image_url", images2.get(3));
                startActivity(intent);
            }
        });


    }

    @Override
    public void onDestroyView() {
        if (banner != null)
            banner.stopAutoPlay();
        super.onDestroyView();

    }
}
