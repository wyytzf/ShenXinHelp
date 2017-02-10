package com.xd.shenxinhelp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyuyang on 17-1-25.
 */
public class MainPagerAdapter extends PagerAdapter {

    private Context context;
    private List<ImageView> imageViewslsit;
    private List<NewsImageItem> newsImageItemslist;

    public MainPagerAdapter(Context context,List<NewsImageItem> imageViews) {
        this.context = context;
        if (imageViews == null || imageViews.size() == 0) {
            newsImageItemslist = new ArrayList<>();
        } else {
            newsImageItemslist = imageViews;
        }
        imageViewslsit = new ArrayList<ImageView>();
        for (int i = 0; i < newsImageItemslist.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageViewslsit.add(imageView);
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewslsit.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViewslsit.get(position);
        Glide.with(context).load(newsImageItemslist.get(position).getImageUrl()).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return imageViewslsit.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
