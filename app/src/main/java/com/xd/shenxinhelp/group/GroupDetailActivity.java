package com.xd.shenxinhelp.group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.View.MyGridDivider;
import com.xd.shenxinhelp.adapter.GridViewAdapter;
import com.xd.shenxinhelp.adapter.GroupLittleGoalListAdapter;
import com.xd.shenxinhelp.adapter.GroupMemberAdapter;
import com.xd.shenxinhelp.adapter.MyBaseAdapter;
import com.xd.shenxinhelp.adapter.RankAdapterMy;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ConnectUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.listener.ListItemClickListener;
import com.xd.shenxinhelp.model.Group;
import com.xd.shenxinhelp.model.GroupDetail;
import com.xd.shenxinhelp.model.HelpContent;
import com.xd.shenxinhelp.model.LittleGoal;
import com.xd.shenxinhelp.model.Plan;
import com.xd.shenxinhelp.model.Post;
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

public class GroupDetailActivity extends AppCompatActivity implements View.OnClickListener,ListItemClickListener {
    private GridView gridView;
    private List<User> userList;
    private Button btnBack, btnMore;
    private View headerView;
    private ListView listView;
    private GroupLittleGoalListAdapter adapter;
    private LinearLayout llRank;
    private List<LittleGoal> goalList;
    private PopupWindow pop;
    private SharedPreferences sp;
    private String userID;
    private String groupID;
    private String type;
    private SwipeRefreshLayout memberlistview;
    private GridViewAdapter gridAdapter;
//    private ImageView image1, image2, image3, image4, image5;
//    private LinearLayout addView;
//    private ImageLoaderInterface imageLoader;
    private List<Post> postList = new ArrayList<Post>();
    private TextView noPosts;
    private GroupDetail groupDetail;
    private TextView des,name,owner;


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
                            User user = new User();
                            object = array.getJSONObject(i);

                            user.setUid(object.getString("userid"));
                            user.setName(object.getString("account"));
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
//                        imageLoader = new GlideImageLoader();
                        gridAdapter.notifyDataSetChanged();
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
        userID = sp.getString("userid", "xiaoming");
        groupDetail= (GroupDetail) getIntent().getSerializableExtra("detail");


        listView = (ListView) findViewById(R.id.lv_comment_list);
        headerView = (View) LayoutInflater.from(GroupDetailActivity.this).inflate(R.layout.activity_group_detail_header, null);

        des=(TextView)headerView.findViewById(R.id.group_des);
        name=(TextView)headerView.findViewById(R.id.group_name);
        owner=(TextView)headerView.findViewById(R.id.group_owner);

        des.setText("简介："+groupDetail.getDes());
        name.setText("圈子名："+groupDetail.getName());
        owner.setText("  ");

        llRank = (LinearLayout) headerView.findViewById(R.id.ll_rank);
        llRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userList==null||userList.size()==0){
                    Toast.makeText(getApplicationContext(),"圈子没有圈子成员",Toast.LENGTH_SHORT).show();


                }else {
                    Intent intent = new Intent(GroupDetailActivity.this, RankActivity.class);
                    intent.putExtra("detail",groupDetail);
                    startActivity(intent);
                }
            }
        });
        userList = new ArrayList<User>();
        goalList = new ArrayList<LittleGoal>();


        setData();
        RecyclerView recyclerView = (RecyclerView) headerView.findViewById(R.id.recycler_view_member);
        //设置可以滑出底栏
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(0,0,0, (int) getResources().getDimension(R.dimen.BottomBarHeight));
        memberlistview=(SwipeRefreshLayout)headerView.findViewById(R.id.listview_member);
        gridAdapter = new GridViewAdapter(userList,this, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (gridAdapter.getItemViewType(position) == 0) {
                    return 6;
                } else {
                    return 1;
                }
            }
        });
        memberlistview.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
//        recyclerView.addItemDecoration(new MyGridDivider(1,ContextCompat.getColor(this,R.color.colorDivider)));
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(gridAdapter);

        memberlistview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                memberlistview.setRefreshing(false);
            }
        });

        listView.addHeaderView(headerView);
        getGroupMember();


        //initData();
//        adapter = new GroupLittleGoalListAdapter(getApplicationContext(),
//                goalList);
//        listView.setAdapter(adapter);
        noPosts = (TextView)findViewById(R.id.no_posts);
        adapter = new GroupLittleGoalListAdapter(getApplicationContext(),
                postList);
        listView.setAdapter(adapter);
        getPosts();

    }

    private void getPosts() {
        new Thread() {
            @Override
            public void run() {
                final Message message = new Message();
                int type=Integer.parseInt(groupDetail.getType())+1;
                String urlget =  AppUtil.GetAllPosts  + "?type="+type+"&id="+groupDetail.getId()+"&userID="+userID;
                HttpUtil.get(getApplicationContext(), urlget, new ResponseHandler() {
                    @Override
                    public void onSuccess(byte[] response) {
                        String jsonStr = new String(response);
                        Log.i("kmj","-------" + jsonStr);
                        parseResponse(jsonStr);
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });
            }
        }.start();
    }

    private void parseResponse(String jsonStr) {
        try {
            JSONObject result = new JSONObject(jsonStr);
            String status = result.getString("reCode");
            if (status.equalsIgnoreCase("success")) {
                postList.clear();
                JSONArray postArray = result.getJSONArray("posts");
                for(int i=0; i < postArray.length();i++){
                    Post post = new Post();
                    JSONObject jo = postArray.getJSONObject(i);
                    post.setPostId(jo.getInt("postid"));
                    post.setName(jo.getString("name"));
                    post.setHead_url(jo.getString("head_url"));
                    post.setDate(jo.getString("date"));
                    post.setContent(jo.getString("content"));
                    post.setIsPraise(jo.getString("isPraise"));
                    post.setPraiseCount(jo.getInt("praiseCount"));
                    JSONArray planArray = jo.getJSONArray("plans");
                    List<Plan> plans = new ArrayList<Plan>();
                    for(int j=0;j<planArray.length();j++){
                        JSONObject jop = planArray.getJSONObject(j);
                        Plan plan = new Plan();
                        plan.setTitle(jop.getString("title"));
                        plan.setImageUrl(jop.getString("imageUrl"));
                        plans.add(plan);
                    }
                    post.setPlans(plans);
                    postList.add(post);
                }
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(GroupDetailActivity.this, "获取圈子动态失败，请重试", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
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
                String urlget =  AppUtil.GetRingMember+ "?ringID="+groupDetail.getId()+"&type="+groupDetail.getType()+"&top=5";
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
//        int size = userList.size();
//        Log.i("mmm","---------"+size);
//        int length = 55;
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//        int gridviewWidth = (int) (size * (length + 4) * density);
//        int itemWidth = (int) (length * density);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
//        gridView.setColumnWidth(itemWidth); // 设置列表项宽
//        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
//        gridView.setStretchMode(GridView.NO_STRETCH);
//        gridView.setNumColumns(size); // 设置列数量=列表集合数
//        gridView.add
//        gridAdapter = new GridViewAdapter(this,userList);
//        gridView.setAdapter(gridAdapter);
    }

    @Override
    public void onClick(View view) {
//

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
            String urlget =  AppUtil.GetAllPosts  + "?type=0&id="+groupDetail.getId()+"&userID="+userID;
            Bundle bundle = new Bundle();
            bundle.putString("url",urlget);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (itemId == R.id.go_little_plan) {
            Intent intent = new Intent(GroupDetailActivity.this, MakePlanActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.go_pk) {
            Intent intent = new Intent(GroupDetailActivity.this,LaunchPKActivity.class );
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void onListItemClick(View v, int position) {
        //openApi = datas.get(position);

        Intent intent = new Intent(GroupDetailActivity.this, GroupMemberActivity.class);
        intent.putExtra("detail",groupDetail);
        startActivityForResult(intent,0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 100:
                getGroupMember();
                break;
            //来自按钮1的请求，作相应业务处理
            case 2:
                //来自按钮2的请求，作相应业务处理
        }
    }
}
