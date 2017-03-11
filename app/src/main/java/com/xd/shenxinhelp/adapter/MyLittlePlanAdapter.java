package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.Plan;
import com.xd.shenxinhelp.model.Post;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/12.
 */

public class MyLittlePlanAdapter extends BaseAdapter {

    private List<Plan> plans = null;
    private LayoutInflater inflater;
    private Context context;
    MyLittlePlanAdapter.ViewHolder holder = null;

    public MyLittlePlanAdapter(List<Plan> plans, Context context) {
        this.plans = plans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return plans.size();
    }

    @Override
    public Object getItem(int position) {
        return plans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Plan plan = (Plan) plans.get(position);

        if (convertView == null) {
            holder = new MyLittlePlanAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_little_goal, null);
            //...
            convertView.setTag(holder);
        } else {
            holder = (MyLittlePlanAdapter.ViewHolder) convertView.getTag();
        }
        //...
        return convertView;
    }


    public class ViewHolder {
       //...
    }
}
