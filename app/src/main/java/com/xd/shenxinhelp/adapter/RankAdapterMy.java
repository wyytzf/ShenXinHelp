package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.listener.ListItemClickListener;
import com.xd.shenxinhelp.model.Rank;
import com.xd.shenxinhelp.model.User;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.List;

/**
 * Created by MMY on 2017/2/14.
 */

public class RankAdapterMy extends MyBaseAdapter {
    protected Context context;
    private List<User> datas = null;
    private ImageLoaderInterface imageLoader;
    public RankAdapterMy(List<User> dataSet, Context context, ListItemClickListener listener) {
        this.context = context;
        this.datas = dataSet;
        setItemListener(listener);
        disableLoadMore();
        setIsenablePlaceHolder(false);
        imageLoader=new GlideImageLoader();
    }

    @Override
    protected int getDataCount() {
        return datas.size();
    }

    @Override
    protected int getItemType(int pos) {
        return 1;
    }

    @Override
    protected BaseViewHolder getItemViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_rank_detail, parent, false));
    }
    private class ChildViewHolder extends BaseViewHolder {

        ImageView img;
        TextView title;
        TextView grade;
        View container;

        ChildViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            title = (TextView) itemView.findViewById(R.id.title);
            grade = (TextView) itemView.findViewById(R.id.grade);
            container = itemView.findViewById(R.id.rank_list_item_detail);
        }

        @Override
        void setData(final int position) {
            final User single = datas.get(position);
            title.setText(single.getName());
            grade.setText("健康度"+single.getHealth_degree());
            imageLoader.displayImage(context, single.getPhotoUrl(), img);
//            Drawable dra = ImageUtils.getForunlogo(context, fid);
//            img.setImageDrawable(dra);
        }
    }
}
