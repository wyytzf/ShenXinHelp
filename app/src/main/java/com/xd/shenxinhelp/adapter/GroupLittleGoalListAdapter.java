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
import com.xd.shenxinhelp.model.LittleGoal;
import com.xd.shenxinhelp.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/2/15.
 */

public class GroupLittleGoalListAdapter extends BaseAdapter {

//    private List<LittleGoal> commentVos = null;
    private List<Post> commentVos = null;
    private LayoutInflater inflater;
    private Context context;
    ViewHolder holder = null;
    private PlanAdapter adapter=null;

    public GroupLittleGoalListAdapter(Context context, List<Post> commentVos) {
        inflater = LayoutInflater.from(context);
        if (commentVos != null) {
            this.commentVos = commentVos;
        } else {
            this.commentVos = new ArrayList<Post>();
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
        Post vo = (Post) commentVos.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_little_goal, null);
            holder.ItemComment = (TextView) convertView.findViewById(R.id.interaction_comment_content);
            holder.ItemName = (TextView) convertView.findViewById(R.id.interaction_comment_name);
            holder.ItemDate = (TextView) convertView.findViewById(R.id.interaction_comment_date);
            holder.ItemPhoto = (ImageView) convertView.findViewById(R.id.interaction_comment_photo);
//			holder.ItemClass = (TextView) convertView.findViewById(R.id.interaction_comment_class);
            holder.ItemPlanGrideView = (GridView)convertView.findViewById(R.id.planGridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.ItemComment.setText(vo.getContent());
//        holder.ItemName.setText(vo.getName());
//
        holder.ItemComment.setText(vo.getContent());
        holder.ItemName.setText(vo.getName());
        holder.ItemDate.setText(vo.getDate());
        new GlideImageLoader().displayImage(context,vo.getHead_url(), holder.ItemPhoto);
        if(vo.getPlans().size() == 0){
            holder.ItemPlanGrideView.setVisibility(View.GONE);
        }else{
            holder.ItemPlanGrideView.setVisibility(View.VISIBLE);
        }
        adapter = new PlanAdapter(context,vo.getPlans());
        holder.ItemPlanGrideView.setAdapter(adapter);
        return convertView;

    }



    public class ViewHolder {
        public TextView ItemName;
        //		public TextView ItemClass;
        public TextView ItemDate;
        public TextView ItemComment;
        public ImageView ItemPhoto;
        public GridView ItemPlanGrideView;
    }

}
