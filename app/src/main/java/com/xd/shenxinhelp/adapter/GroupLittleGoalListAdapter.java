package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.LittleGoal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/2/15.
 */

public class GroupLittleGoalListAdapter extends BaseAdapter {

    private List<LittleGoal> commentVos = null;
    private LayoutInflater inflater;
    private Context context;
    ViewHolder holder = null;

    public GroupLittleGoalListAdapter(Context context, List<LittleGoal> commentVos) {
        inflater = LayoutInflater.from(context);
        if (commentVos != null) {
            this.commentVos = commentVos;
        } else {
            this.commentVos = new ArrayList<LittleGoal>();
        }
        this.context = context;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return commentVos.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return commentVos.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LittleGoal vo = (LittleGoal) commentVos.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_little_goal, null);
            holder.ItemComment = (TextView) convertView.findViewById(R.id.interaction_comment_content);
            holder.ItemName = (TextView) convertView.findViewById(R.id.interaction_comment_name);
            holder.ItemDate = (TextView) convertView.findViewById(R.id.interaction_comment_date);
            holder.ItemPhoto = (ImageView) convertView.findViewById(R.id.interaction_comment_photo);
//			holder.ItemClass = (TextView) convertView.findViewById(R.id.interaction_comment_class);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.ItemComment.setText(vo.getContent());
//        holder.ItemName.setText(vo.getName());
//
        return convertView;

    }



    public class ViewHolder {
        public TextView ItemName;
        //		public TextView ItemClass;
        public TextView ItemDate;
        public TextView ItemComment;
        public ImageView ItemPhoto;
    }

}
