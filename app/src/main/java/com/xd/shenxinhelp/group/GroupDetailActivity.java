package com.xd.shenxinhelp.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.GroupLittleGoalListAdapter;
import com.xd.shenxinhelp.model.LittleGoal;
import com.xd.shenxinhelp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/2/14.
 */

public class GroupDetailActivity extends AppCompatActivity implements View.OnClickListener {
    GridView gridView;
    List<User> cityList;
    private Button btnBack, btnMore;
    private View headerView;
    private ListView listView;
    private GroupLittleGoalListAdapter adapter;
    private LinearLayout llRank;
    List<LittleGoal> goalList;
    PopupWindow pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.group_detail_toolbar_menu);
        setSupportActionBar(toolbar);
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
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnMore = (Button) findViewById(R.id.btn_more);

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
        setData();
        setGridView();
        initData();
        adapter = new GroupLittleGoalListAdapter(getApplicationContext(),
                goalList);
        listView.setAdapter(adapter);

    }

    void initData() {
        goalList = new ArrayList<LittleGoal>();
        LittleGoal data1 = new LittleGoal();
        goalList.add(data1);
        goalList.add(data1);
        goalList.add(data1);
        goalList.add(data1);
        goalList.addAll(goalList);

    }

    /**
     * 设置数据
     */
    private void setData() {
        cityList = new ArrayList<User>();
        User item = new User();
        item.setName("深圳");
        cityList.add(item);
        item = new User();
        item.setName("上海");
        cityList.add(item);
        item = new User();
        item.setName("广州");
        cityList.add(item);
        item = new User();
        item.setName("北京");
        cityList.add(item);
        item = new User();
        item.setName("武汉");
        cityList.add(item);
        item = new User();
        item.setName("孝感");
        cityList.add(item);
        //cityList.addAll(cityList);
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        int size = cityList.size();
        int length = 55;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),
                cityList);
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
