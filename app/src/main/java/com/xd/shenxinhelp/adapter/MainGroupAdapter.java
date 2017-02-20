package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.Group;
import com.xd.shenxinhelp.model.GroupDetail;

import java.util.List;

/**
 * @author MMY
 * @since 2016/12/22.
 */

public class MainGroupAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<Group> datas;
    public MainGroupAdapter(Context context,List<Group> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public int getGroupCount() {
        return datas.size() ;
    }

    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).getGroupList().size();
    }

    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).getGroupList()
                .get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        Group termRecordVo = datas
                .get(groupPosition);
        GrupViewHolder holder = null;
        holder = new GrupViewHolder();
        convertView = inflater.inflate(R.layout.item_main_group, null);
        holder.type = (TextView) convertView.findViewById(R.id.type);
        Group recordVo = datas.get(groupPosition);
        holder.type.setText(recordVo.getName());
        convertView.setTag(holder);


        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        ChildViewHolder holder = new ChildViewHolder();
        convertView = inflater.inflate(
                R.layout.item_main_group_detail, null);
        holder.img = (ImageView) convertView.findViewById(R.id.img);
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.des = (TextView) convertView.findViewById(R.id.des);


        GroupDetail recordVo = datas.get(groupPosition).getGroupList().get(childPosition);
        holder.title.setText(recordVo.getName());
        holder.des.setText(recordVo.getDes());
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class GrupViewHolder {
        public TextView type;

    }
    public static class ChildViewHolder {
        ImageView img;
        TextView title;
        TextView des;
    }


}
