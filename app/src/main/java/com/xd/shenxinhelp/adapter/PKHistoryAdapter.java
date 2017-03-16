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
import com.xd.shenxinhelp.model.PKHistory;
import com.xd.shenxinhelp.model.Plan;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/16.
 */

public class PKHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<PKHistory> list;
    private LayoutInflater mInflater;

    public PKHistoryAdapter(Context mContext, List<PKHistory> list) {
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
            convertView = mInflater.inflate(R.layout.pkhistory_list_item, parent, false);
            holder.type = (TextView) convertView.findViewById(R.id.pk_history_type);
            holder.date = (TextView) convertView.findViewById(R.id.pk__history_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(list.get(position).getType()==0){
            holder.type.setText("自定义圈");
        }else if(list.get(position).getType()==1){
            holder.type.setText("班级");
        }else{
            holder.type.setText("学校");
        }

//        holder.date.setText(list.get(position).getDate());
        holder.date.setText("2017-03-07");
        return convertView;
    }


    class ViewHolder {
        private TextView type;
        private TextView date;
    }
}
