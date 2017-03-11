package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.Plan;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/9.
 */

public class PlanAdapter extends BaseAdapter{

    private Context mContext;
    private List<Plan> list;
    private LayoutInflater mInflater;

    public PlanAdapter(Context mContext, List<Plan> list) {
        this.mContext = mContext;
        this.list = list;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.plan_list_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.item_plan_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        new GlideImageLoader().displayImage(mContext,list.get(position).getImageUrl(),holder.image);
        return convertView;
    }

    class ViewHolder {
        private ImageView image;
    }
}
