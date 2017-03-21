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
import com.xd.shenxinhelp.model.User;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.List;

/**
 * Created by MMY on 2016/12/28.
 */

public class GroupMemberAdapter extends MyBaseAdapter{
    protected Context context;
    private List<User> datas = null;
    private ImageLoaderInterface imageLoader;
    public GroupMemberAdapter(List<User> dataSet, Context context, ListItemClickListener listener) {
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
    protected BaseViewHolder getItemViewHolder(ViewGroup parent, int viewType) {
        return new GroupMemberAdapter.ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_member, parent, false));
    }
    private class ChildViewHolder extends BaseViewHolder {

        TextView title;
        TextView des;
        ImageView imageView;
        View container;

        ChildViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            des = (TextView) itemView.findViewById(R.id.des);
            imageView=(ImageView)itemView.findViewById(R.id.ItemImage);
            container = itemView.findViewById(R.id.group_member_item);
        }

        @Override
        void setData(final int position) {
            if(position==datas.size()){
                title.setText("添加");
                des.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
                return;
            }
            final User single = datas.get(position);
            title.setText(single.getName());
            des.setVisibility(View.VISIBLE);
            des.setText("健康度："+single.getHealth_degree());
            imageLoader.displayImage(context, single.getPhotoUrl(), imageView);
        }
    }
}
