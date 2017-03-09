package com.xd.shenxinhelp.group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.GroupLittleGoalListAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ConnectUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.HelpContent;
import com.xd.shenxinhelp.model.LittleGoal;
import com.xd.shenxinhelp.model.User;
import com.youth.banner.loader.ImageLoaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/2/14.
 */

public class GroupDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private GridView gridView;
    private List<User> userList;
    private Button btnBack, btnMore;
    private View headerView;
    private ListView listView;
    private GroupLittleGoalListAdapter adapter;
    private LinearLayout llRank;
    private List<LittleGoal> goalList;
    private PopupWindow pop;
    private SharedPreferences sp ;
    private String userID;
    private String groupID;
    private String type;
    private GridViewAdapter gridAdapter;
    private ImageView image1,image2,image3,image4,image5;
    private LinearLayout addView;
    private ImageLoaderInterface imageLoader;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            //dismissRequestDialog();
            switch (msg.what) {
                case 1: {

                    try {
                        JSONObject result = new JSONObject((String) msg.obj);
                        JSONArray array = result.getJSONArray("students");
                        JSONObject object;
                        userList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            User user=new User();
                            object = array.getJSONObject(i);

                            user.setName(object.getString("userid"));
                            user.setSex(object.getString("sex"));
                            user.setAge(object.getString("age"));
                            user.setHeight(object.getString("height"));
                            user.setCredits(object.getString("credits"));
                            user.setHealth_degree(object.getString("health_degree"));
                            user.setLevel(object.getString("level"));
                            user.setPhotoUrl(object.getString("head_url"));
                            user.setClass_id(object.getString("class_id"));
                            user.setSchool_id(object.getString("school_id"));
                            userList.add(user);

                        }
                        imageLoader = new GlideImageLoader();

                        if (userList == null||userList.size()==0) {
                            image1.setVisibility(View.INVISIBLE);
                            image2.setVisibility(View.INVISIBLE);
                            image3.setVisibility(View.INVISIBLE);
                            image4.setVisibility(View.INVISIBLE);
                            image5.setVisibility(View.INVISIBLE);
                        }
                        else if(userList.size()==1){
                            image1.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image1);
                            image2.setVisibility(View.INVISIBLE);
                            image3.setVisibility(View.INVISIBLE);
                            image4.setVisibility(View.INVISIBLE);
                            image5.setVisibility(View.INVISIBLE);
                        }
                        else if(userList.size()==2){
                            image1.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image1);
                            image2.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image2);
                            image3.setVisibility(View.INVISIBLE);
                            image4.setVisibility(View.INVISIBLE);
                            image5.setVisibility(View.INVISIBLE);
                        }
                        else if(userList.size()==3){
                            image1.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image1);
                            image2.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image2);
                            image3.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image3);
                            image4.setVisibility(View.INVISIBLE);
                            image5.setVisibility(View.INVISIBLE);
                        }
                        else if(userList.size()==4){
                            image1.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image1);
                            image2.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image2);
                            image3.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image3);
                            image4.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image4);
                            image5.setVisibility(View.INVISIBLE);
                        }
                        else if(userList.size()==5){
                            image1.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image1);
                            image2.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image2);
                            image3.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image3);
                            image4.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image4);
                            image5.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(getApplicationContext(), userList.get(0).getPhotoUrl(), image5);
                        }



//                        recyclerVie
                    } catch (JSONException e) {
                        Log.e("mmm", e.getMessage());
                    }
                }

                break;

                case -1: {

                    break;
                }
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.group_detail_toolbar_menu);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID=sp.getString("account", "");
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                return true;
//            }
//        });

        listView = (ListView) findViewById(R.id.lv_comment_list);
        headerView = (View) LayoutInflater.from(GroupDetailActivity.this).inflate(R.layout.activity_group_detail_header, null);
        gridView = (GridView) headerView.findViewById(R.id.grid);
//        btnBack = (Button) findViewById(R.id.btn_back);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        btnMore = (Button) findViewById(R.id.btn_more);
        image1=(ImageView)headerView.findViewById(R.id.ItemImage1);
        image2=(ImageView)headerView.findViewById(R.id.ItemImage2);
        image3=(ImageView)headerView.findViewById(R.id.ItemImage3);
        image4=(ImageView)headerView.findViewById(R.id.ItemImage4);
        image5=(ImageView)headerView.findViewById(R.id.ItemImage5);
        addView=(LinearLayout)headerView.findViewById(R.id.addImage);
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupDetailActivity.this, GroupMemberActivity.class);
                startActivity(intent);
            }
        });
//        LayoutInflater inflater = LayoutInflater.from(this);
//        // 引入窗口配置文件
//        View view = inflater.inflate(R.layout.popmenu, null);
//        Button myPlan = (Button) view.findViewById(R.id.my_plan);
//        Button makePlan = (Button) view.findViewById(R.id.make_plan);
//        Button makePk = (Button) view.findViewById(R.id.make_pk);
//        myPlan.setOnClickListener(this);
//        makePlan.setOnClickListener(this);
//        makePk.setOnClickListener(this);
//
//        // 创建PopupWindow对象
//        pop = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
//        pop.setBackgroundDrawable(new BitmapDrawable());
//        //设置点击窗口外边窗口消失
//        pop.setOutsideTouchable(true);
//        // 设置此参数获得焦点，否则无法点击
//        pop.setFocusable(true);
//        btnMore.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (pop.isShowing()) {
//                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
//                    pop.dismiss();
//                } else {
//                    // 显示窗口
//                    pop.showAsDropDown(v);
//                }
//
//            }
//        });

        llRank = (LinearLayout) headerView.findViewById(R.id.ll_rank);
        llRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupDetailActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });
        listView.addHeaderView(headerView);

        userList = new ArrayList<User>();
        goalList = new ArrayList<LittleGoal>();
        //setData();
        getGroupMember();
        //setGridView();
        initData();
        adapter = new GroupLittleGoalListAdapter(getApplicationContext(),
                goalList);
        listView.setAdapter(adapter);

    }

    void initData() {

        LittleGoal data1 = new LittleGoal();
        goalList.add(data1);
        goalList.add(data1);
        goalList.add(data1);
        goalList.add(data1);
        goalList.addAll(goalList);

    }
    public void getGroupMember() {


        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();

                //String urlget = ConnectUtil.GetRingMember + "?ringID="+groupID+"&type="+type+"&top=5&userID=" + userID;
                String urlget = ConnectUtil.GetRingMember + "?ringID=7&type=2&top=5";
                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        try {
                            JSONObject result = new JSONObject(jsonStr);
                            String status = result.getString("reCode");
                            if (status.equalsIgnoreCase("success")) {
                                message.obj = jsonStr;
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                message.what = -1;//失败
                                message.obj = "获取失败";
                                handler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            Log.e("mmm", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        message.what = -1;
                        message.obj = "获取数据失败";
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }
    /**
     * 设置数据
     */
    private void setData() {

        User item = new User();
        item.setName("深圳");
        userList.add(item);
        item = new User();
        item.setName("上海");
        userList.add(item);
        item = new User();
        item.setName("广州");
        userList.add(item);
        item = new User();
        item.setName("北京");
        userList.add(item);
        item = new User();
        item.setName("武汉");
        userList.add(item);

        //cityList.addAll(cityList);
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        int size = userList.size();
        int length = 55;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数
//        gridView.add
        gridAdapter = new GridViewAdapter(getApplicationContext(),
                userList);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
//        Intent intent;
//        switch (view.getId()) {
//            case R.id.my_plan:
//
//                if (pop.isShowing()) {
//                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
//                    pop.dismiss();
//                }
//                intent = new Intent(GroupDetailActivity.this, MyPlanActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.make_plan:
//
//                if (pop.isShowing()) {
//                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
//                    pop.dismiss();
//                }
//                intent = new Intent(GroupDetailActivity.this, MakePlanActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.make_pk:
//                Toast.makeText(getApplicationContext(), "3333", Toast.LENGTH_SHORT).show();
//                if (pop.isShowing()) {
//                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
//                    pop.dismiss();
//                }
//                break;
//        }

    }

    /**
     * GirdView 数据适配器
     */
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        List<User> list;

        public GridViewAdapter(Context _context, List<User> _list) {
            this.list = _list;
            this.context = _context;
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
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_grid_view, null);
            User city = list.get(position);
            return convertView;
        }
    }


    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.group_detail_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.my_little_plan) {
            Intent intent = new Intent(GroupDetailActivity.this, MyPlanActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.go_little_plan) {
            Intent intent = new Intent(GroupDetailActivity.this, MakePlanActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.go_pk) {
//            Intent intent = new Intent(GroupDetailActivity.this, );
//            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
