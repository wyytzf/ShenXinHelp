package com.xd.shenxinhelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.Contact;
import com.xd.shenxinhelp.model.Plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class ContactAdapter extends BaseAdapter {

    private Context mContext;
    private List<Contact> list;
    private Map<String,Contact> map=new HashMap<>();//已经关联的账号
    private LayoutInflater mInflater;
    private ListBtnListener mListener;

    public ContactAdapter(Context mContext, List<Contact> list,ListBtnListener mListener) {
        this.mContext = mContext;
        this.list = list;
        this.mInflater = LayoutInflater.from(mContext);
        this.mListener=mListener;
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
            final Contact contact= list.get(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.contact_list_item, parent, false);
                holder.name=(TextView)convertView.findViewById(R.id.tv_contact_name);
                holder.tel=(TextView)convertView.findViewById(R.id.tv_contact_phone);
                holder.add = (Button) convertView.findViewById(R.id.contact_add_btn);
                //holder.image = (ImageView) convertView.findViewById(R.id.item_plan_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //new GlideImageLoader().displayImage(mContext,list.get(position).getImageUrl(),holder.image);
            holder.name.setText(list.get(position).getName());
            holder.tel.setText(list.get(position).getTel());
            if(contact.getIsAdded().equals("1")){
                holder.add.setText("已添加");
                holder.add.setTextColor(Color.parseColor("#595959"));
                holder.add.setBackgroundColor(Color.parseColor("#ffffff"));
                //holder.add.setFocusable(false);
                holder.add.setClickable(false);
            }else {
                holder.add.setOnClickListener(mListener);
            }
           // holder.add.setOnClickListener(new ListBtnListener(position,mContext));

            holder.add.setTag(position);
            return convertView;
    }

    class ViewHolder {
        private TextView name;
        private  TextView tel;
        private Button add;
        //private ImageView image;
    }

    public static abstract class ListBtnListener implements View.OnClickListener {

        int mPosition;
        Context mContext;

      /*  public ListBtnListener(int position, Context context) {
            this.mPosition = position;
            this.mContext = context;
        }*/

        @Override
        public void onClick(View v) {
           /* Toast.makeText(mContext, list.get(mPosition).getTel(), Toast.LENGTH_SHORT)
                    .show();
            Intent intent=new Intent();
            intent.putExtra("tel",list.get(mPosition).getTel());*/
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);


    }

}
