package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.listener.ListItemClickListener;
import com.xd.shenxinhelp.model.Post;
import com.xd.shenxinhelp.model.User;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/3/14.
 */

public class GridViewAdapter extends MyBaseAdapter {
    protected Context context;
    private List<User> datas = null;
    private ImageLoaderInterface imageLoader;
    public GridViewAdapter(List<User> dataSet, Context context, ListItemClickListener listener) {
        this.context = context;
        this.datas = dataSet;
        setItemListener(listener);
        disableLoadMore();
        setIsenablePlaceHolder(false);
        imageLoader=new GlideImageLoader();
    }

    @Override
    protected int getDataCount() {
        return datas.size()+1;
    }

    @Override
    protected int getItemType(int pos) {
        return 1;
    }

    @Override
    protected MyBaseAdapter.BaseViewHolder getItemViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_view, parent, false));
    }
    private class ChildViewHolder extends MyBaseAdapter.BaseViewHolder {

        ImageView imageView;
        View container;

        ChildViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.ItemImage);
            container = itemView.findViewById(R.id.member_item);
        }

        @Override
        void setData(final int position) {
            if (position==datas.size()){
                imageView.setImageResource(R.drawable.timg);;
            }
            else {
                final User single = datas.get(position);
                imageLoader.displayImage(context, single.getPhotoUrl(), imageView);
            }
        }
    }

}
