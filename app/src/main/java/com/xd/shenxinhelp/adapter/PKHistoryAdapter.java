package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.GroupDetail;
import com.xd.shenxinhelp.model.PKHistory;
import com.xd.shenxinhelp.model.ParticipateTeam;
import com.xd.shenxinhelp.model.Plan;
import com.xd.shenxinhelp.model.Team;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/16.
 */

public class PKHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<PKHistory> list;
    private LayoutInflater mInflater;
    private GroupDetail detail;
    private SharedPreferences sp;
    private String account;

    public PKHistoryAdapter(Context mContext, List<PKHistory> list, GroupDetail detail) {
        this.mContext = mContext;
        this.list = list;
        this.detail = detail;
        this.mInflater = LayoutInflater.from(mContext);
        this.sp = mContext.getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        this.account = sp.getString("account","");
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
        PKHistory history = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.pkhistory_list_item, parent, false);
            holder.takerName = (TextView) convertView.findViewById(R.id.pk_history_taker_name);
            holder.cridits = (TextView) convertView.findViewById(R.id.pk_history_cridits);
            holder.result = (TextView) convertView.findViewById(R.id.pk_history_result);
            holder.date = (TextView) convertView.findViewById(R.id.pk__history_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(detail.getType().equals("0")){
            if(history.getWinTeamID() == Integer.parseInt(history.getParticipateTeam().get(0).getTeamId())) {
                List<Team> students = history.getParticipateTeam().get(0).getStudents();
                int i = 0;
                for (i = 0; i < students.size(); i++) {
                    if (students.get(i).getAccount().equals(account)) {
                        holder.takerName.setText(history.getParticipateTeam().get(1).getTitle());
                        holder.result.setText("赢");
                        holder.result.setTextColor(mContext.getResources().getColor(R.color.red_light));
                        break;
                    }
                }
                if (i == 3) {
                    holder.takerName.setText(history.getParticipateTeam().get(0).getTitle());
                    holder.result.setText("败");
                    holder.result.setTextColor(mContext.getResources().getColor(R.color.green_light));
                }
            }else{
                List<Team> students = history.getParticipateTeam().get(0).getStudents();
                int i = 0;
                for (i = 0; i < students.size(); i++) {
                    if (students.get(i).getAccount().equals(account)) {
                        holder.takerName.setText(history.getParticipateTeam().get(1).getTitle());
                        holder.result.setText("败");
                        holder.result.setTextColor(mContext.getResources().getColor(R.color.green_light));
                        break;
                    }
                }
                if (i == 3) {
                    holder.takerName.setText(history.getParticipateTeam().get(0).getTitle());
                    holder.result.setText("赢");
                    holder.result.setTextColor(mContext.getResources().getColor(R.color.red_light));
                }
            }
        }else{
            if(history.getParticipateTeam().get(0).getTitle().equals(detail.getName())){
                holder.takerName.setText(history.getParticipateTeam().get(1).getTitle());
            }else{
                holder.takerName.setText(history.getParticipateTeam().get(0).getTitle());
            }
            if(history.getWinTeamID() == Integer.parseInt(history.getParticipateTeam().get(0).getTeamId())) {

                if(history.getParticipateTeam().get(0).getTitle().equals(detail.getName())){
                    holder.result.setText("赢");
                    holder.result.setTextColor(mContext.getResources().getColor(R.color.red_light));
                }else{
                    holder.result.setText("败");
                    holder.result.setTextColor(mContext.getResources().getColor(R.color.green_light));
                }
            }else{
                if(history.getParticipateTeam().get(0).getTitle().equals(detail.getName())){
                    holder.result.setText("败");
                    holder.result.setTextColor(mContext.getResources().getColor(R.color.green_light));
                }else{
                    holder.result.setText("赢");
                    holder.result.setTextColor(mContext.getResources().getColor(R.color.red_light));
                }
            }

        }



        holder.date.setText(list.get(position).getDate());

        return convertView;
    }



    class ViewHolder {
        private TextView takerName;
        private TextView date;
        private TextView cridits;
        private TextView result;
    }
}
