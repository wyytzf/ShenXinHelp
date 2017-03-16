package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.Plan;
import com.xd.shenxinhelp.model.Team;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/16.
 */

public class TeamAdapter extends BaseAdapter{

    private Context mContext;
    private List<Team> list;
    private LayoutInflater mInflater;


    public TeamAdapter(Context mContext, List<Team> list) {
        this.mContext = mContext;
        this.list = list;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<Team> list){
        this.list = list;
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
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.team_list_iteam, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.pk_headerUrl);
            holder.account = (TextView)convertView.findViewById(R.id.pk_account);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        new GlideImageLoader().displayImage(mContext,list.get(position).getHeaderUrl(),holder.image);
        holder.account.setText(list.get(position).getAccount());
        return convertView;
    }

    class ViewHolder {
        private ImageView image;
        private TextView account;
    }
}
