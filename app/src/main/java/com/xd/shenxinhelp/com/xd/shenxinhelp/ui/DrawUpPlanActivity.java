package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.GlideImageLoader;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.DensityUtil;
import com.xd.shenxinhelp.netutils.OkHttp;
import com.youth.banner.loader.ImageLoaderInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawUpPlanActivity extends AppCompatActivity {

    private ListView today_plan_list_view;
    private ListView tomo_plan_list_view;
    private Button add_plan;
    private Button start_plan;
    private RelativeLayout add_plan_layout;
    private RelativeLayout start_plan_layout;
    private EditText share_content;
    private TextView stu_name;

    private JSONArray optional_plan_list;
    private JSONArray today_plan_list;
    private JSONArray tomo_plan_list;
    private Map<Integer, Boolean> state_map;
    private Map<Integer, Boolean> states;

    private ViewPager mPager;//页卡内容
    private List<ListView> listview_list;// Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    private ImageLoaderInterface imageLoader;
    private PopupWindow popupWindow;

    private String userID;
    private String userName;
    private String today;
    private String tomo;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_up_plan);

        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);

        userID = sp.getString("userid", "1");
        userName = sp.getString("name", "");

        today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        tomo = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        state_map = new HashMap<>();

        optional_plan_list = new JSONArray();
        today_plan_list = new JSONArray();
        tomo_plan_list = new JSONArray();
        initUI();

        InitTextView();
        InitViewPager();
        InitImageView();

        imageLoader = new GlideImageLoader();

        getTodayPlanList();
        getTomoPlanList();
        getOptionalPlanList();

    }

    private void initUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        stu_name = (TextView) findViewById(R.id.stu_name);
        stu_name.setText(userName+"你好，根据你所处的年级，");
        share_content = (EditText) findViewById(R.id.share_content);

        add_plan_layout = (RelativeLayout) findViewById(R.id.add_plan_layout);
        add_plan = (Button) findViewById(R.id.add_plan);
        add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optional_plan_list.length()==0)
                    Toast.makeText(DrawUpPlanActivity.this, "暂无更多锻炼项目", Toast.LENGTH_SHORT).show();
                else {
                    LayoutInflater layoutInflater = LayoutInflater.from(DrawUpPlanActivity.this);
                    View root = layoutInflater.inflate(R.layout.pop_up_window,null);
                    popupWindow = new PopupWindow(root,
                            DensityUtil.dip2px(DrawUpPlanActivity.this, 340),
                            DensityUtil.dip2px(DrawUpPlanActivity.this, 400));
                    // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
                    popupWindow.setFocusable(true);
                    // 实例化一个ColorDrawable颜色为半透明
                    //ColorDrawable dw = new ColorDrawable(0xb0000000);
                    findViewById(R.id.activity_draw_up_plan).setBackground(new ColorDrawable(0xb0000000));
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    // 设置popWindow的显示和消失动画
                    popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
                    // 在中间显示
                    popupWindow.showAtLocation(findViewById(R.id.app_bar), Gravity.CENTER, 0, 0);

                    //popWindow消失监听方法
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            findViewById(R.id.activity_draw_up_plan).setBackground(new BitmapDrawable());
                            state_map.clear();
                        }
                    });
                    Button add = (Button) root.findViewById(R.id.add);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String params = "?userID="+userID+"&date="+tomo+"&exerciseIDs=";
                            try {
                                for (int i : state_map.keySet()){
                                        params += ( optional_plan_list.getJSONObject(i).getString("id") + "-" );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OkHttp.get(AppUtil.CustomPlan + params, new OkHttp.ResultCallBack() {
                                @Override
                                public void onError(String str, Exception e) {
                                    Log.e("CustomPlan", str);
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(String str) {
                                    popupWindow.dismiss();
                                    getTomoPlanList();
                                }
                            });
                        }
                    });
                    ListView exe_list = (ListView) root.findViewById(R.id.exe_list);
                    exe_list.setAdapter(new ExeListAdapter(DrawUpPlanActivity.this));
                }
            }
        });

        start_plan_layout = (RelativeLayout) findViewById(R.id.start_plan_layout);
        start_plan = (Button) findViewById(R.id.start_plan);
        start_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = -1;
                if (today_plan_list.length()>0){
                    for (int i : states.keySet()){
                        if (states.get(i)){
                            position = i;
                            break;
                        }
                    }
                    if (position>-1){
                        if (!"".equals(share_content.getText().toString().trim())){
                            String plan_id=null;
                            try {
                                JSONObject temp = today_plan_list.getJSONObject(position);
                                plan_id = temp.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OkHttp.get(AppUtil.ShareToRing + "?planID="+plan_id+"&userID="+userID+"&content="+
                                    share_content.getText().toString().trim(), new OkHttp.ResultCallBack() {
                                @Override
                                public void onError(String str, Exception e) {
                                    Log.e("ShareToRing", str);
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(String str) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        String reCode = jsonObject.getString("reCode");
                                        if ("SUCCESS".equals(reCode)){
                                            Toast.makeText(DrawUpPlanActivity.this, "分享计划成功！", Toast.LENGTH_SHORT).show();
                                            getTodayPlanList();
                                        }
                                        else {
                                            Log.e("Fail", jsonObject.getString("message"));
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(DrawUpPlanActivity.this, "分享时说点什么吧", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(DrawUpPlanActivity.this, "想分享哪个计划呢？", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(DrawUpPlanActivity.this, "你昨天没制定计划哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getTodayPlanList(){
        OkHttp.get(AppUtil.GetPlanByDate + "?userID="+userID+"&date="+today, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getTodayPlanList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                analyze(str, "today");
            }
        });
    }

    private void getTomoPlanList(){
        OkHttp.get(AppUtil.GetPlanByDate + "?userID="+userID+"&date="+tomo, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getTomoPlanList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                analyze(str, "tomo");
            }
        });
    }

    private void getOptionalPlanList(){
        OkHttp.get(AppUtil.GetAllExercise + "?type=0", new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getOptionalPlanList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        optional_plan_list = jsonObject.getJSONArray("items");
                    }
                    else {
                        Log.e("Fail", jsonObject.getString("message"));
                        Toast.makeText(DrawUpPlanActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void analyze(String str, String flag){
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reCode = jsonObject.getString("reCode");
            if ("SUCCESS".equals(reCode)){
                switch (flag){
                    case "today":
                        today_plan_list = jsonObject.getJSONArray("plans");
                        today_plan_list_view.setAdapter(new PlanListAdapter(DrawUpPlanActivity.this, today_plan_list, "today"));
                        break;
                    case "tomo":
                        tomo_plan_list = jsonObject.getJSONArray("plans");
                        tomo_plan_list_view.setAdapter(new PlanListAdapter(DrawUpPlanActivity.this, tomo_plan_list, "tomo"));
                        break;
                }
            }
            else {
                Log.e("Fail", jsonObject.getString("message"));
                Toast.makeText(DrawUpPlanActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.today);
        t2 = (TextView) findViewById(R.id.tomorrow);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        listview_list =new ArrayList<ListView>();
        LayoutInflater layoutInflater = LayoutInflater.from(DrawUpPlanActivity.this);
        today_plan_list_view = (ListView)layoutInflater.inflate(R.layout.list_view,null);
        tomo_plan_list_view = (ListView)layoutInflater.inflate(R.layout.list_view,null);
        listview_list.add(today_plan_list_view);
        listview_list.add(tomo_plan_list_view);
        MyPagerAdapter myPagerAdapter  = new MyPagerAdapter();
        mPager.setAdapter(myPagerAdapter);
        mPager.setCurrentItem(0);
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / listview_list.size() - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
            switch (index){
                case 0:
                    add_plan_layout.setVisibility(View.GONE);
                    share_content.setVisibility(View.VISIBLE);
                    start_plan_layout.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            DensityUtil.dip2px(DrawUpPlanActivity.this, 220));
                    mPager.setLayoutParams(lp);
                    break;
                case 1:
                    add_plan_layout.setVisibility(View.VISIBLE);
                    share_content.setVisibility(View.GONE);
                    start_plan_layout.setVisibility(View.GONE);
                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            DensityUtil.dip2px(DrawUpPlanActivity.this, 310));
                    mPager.setLayoutParams(lp);
                    break;
            }
        }
    };

    /**
     * 页卡切换监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡0 -> 页卡1 偏移量
        int two = one * 2;// 页卡0 -> 页卡2 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) { //arg0是跳转的目标页卡
                case 0:
                    if (currIndex == 1) { //从页卡1跳到页卡0
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    add_plan_layout.setVisibility(View.GONE);
                    share_content.setVisibility(View.VISIBLE);
                    start_plan_layout.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            DensityUtil.dip2px(DrawUpPlanActivity.this, 220));
                    mPager.setLayoutParams(lp);
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    add_plan_layout.setVisibility(View.VISIBLE);
                    share_content.setVisibility(View.GONE);
                    start_plan_layout.setVisibility(View.GONE);
                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            DensityUtil.dip2px(DrawUpPlanActivity.this, 310));
                    mPager.setLayoutParams(lp);
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(200);//动画时长
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return listview_list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view= listview_list.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(listview_list.get(position));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class PlanListAdapter extends BaseAdapter{

        private Context context;
        private JSONArray jsonArray;
        private String type;

        PlanListAdapter(Context context, JSONArray jsonArray, String type){
            this.context = context;
            this.jsonArray = jsonArray;
            this.type = type;
            if ("today".equals(type)){
                states = new HashMap<>();
                for (int i=0; i<jsonArray.length(); i++){
                    states.put(i, false);
                }
            }
        }

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public JSONObject getItem(int position) {
            try {
                return jsonArray.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.list_view_draw_up_plan, null);
                viewHolder = new ViewHolder();
                viewHolder.plan = (TextView) convertView.findViewById(R.id.plan_number);
                viewHolder.second = (RelativeLayout) convertView.findViewById(R.id.second);
                viewHolder.picture1 = (ImageView) convertView.findViewById(R.id.picture1);
                viewHolder.picture2 = (ImageView) convertView.findViewById(R.id.picture2);
                viewHolder.name1 = (TextView) convertView.findViewById(R.id.name1);
                viewHolder.name2 = (TextView) convertView.findViewById(R.id.name2);
                viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_button);
                viewHolder.already_share = (TextView) convertView.findViewById(R.id.already_share);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(position);
                int isShare = jsonObject.getInt("isShare");
                JSONArray items = jsonObject.getJSONArray("items");
                viewHolder.plan.setText("计划"+(position+1));
                JSONObject item1 = items.getJSONObject(0);
                imageLoader.displayImage(DrawUpPlanActivity.this, item1.getString("imageUrl"), viewHolder.picture1);
                viewHolder.name1.setText(item1.getString("title"));
                if (items.length()>1){
                    JSONObject item2 = items.getJSONObject(1);
                    imageLoader.displayImage(DrawUpPlanActivity.this, item2.getString("imageUrl"), viewHolder.picture2);
                    viewHolder.name2.setText(item2.getString("title"));
                    viewHolder.second.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.second.setVisibility(View.GONE);
                }
                if ("today".equals(type)){
                    if (isShare==0){
                        viewHolder.already_share.setVisibility(View.GONE);
                        viewHolder.radioButton.setVisibility(View.VISIBLE);
                        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //重置，确保最多只有一项被选中
                                for (int i=0; i<jsonArray.length(); i++){
                                    states.put(i, false);
                                }
                                states.put(position, true);
                                PlanListAdapter.this.notifyDataSetChanged();
                            }
                        });
                        viewHolder.radioButton.setChecked(states.get(position));
                    }
                    else {
                        viewHolder.radioButton.setVisibility(View.GONE);
                        viewHolder.already_share.setVisibility(View.VISIBLE);
                    }
                }
                else if ("tomo".equals(type)){
                    viewHolder.radioButton.setVisibility(View.GONE);
                    viewHolder.already_share.setVisibility(View.GONE);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }

        private class ViewHolder{
            TextView plan;
            RelativeLayout second;
            ImageView picture1;
            ImageView picture2;
            TextView name1;
            TextView name2;
            RadioButton radioButton;
            TextView already_share;
        }
    }

    private class ExeListAdapter extends BaseAdapter{

        private Context context;

        ExeListAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return optional_plan_list.length();
        }

        @Override
        public JSONObject getItem(int position) {
            try {
                return optional_plan_list.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.list_view_exercise, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();
            try {
                final JSONObject jsonObject = optional_plan_list.getJSONObject(position);
                viewHolder.name.setText(jsonObject.getString("title"));
                imageLoader.displayImage(DrawUpPlanActivity.this, jsonObject.getString("reosurce_url"), viewHolder.picture);
                viewHolder.checkBox.setOnCheckedChangeListener(null);
                viewHolder.checkBox.setChecked(state_map.get(position)==null? false : true);
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            state_map.put(position, isChecked);
                            if (state_map.size()==3){
                                state_map.remove(position);
                                buttonView.setChecked(false);
                                Toast.makeText(DrawUpPlanActivity.this, "一次最多添加2个项目哦！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            state_map.remove(position);
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }

        private class ViewHolder{
            TextView name;
            ImageView picture;
            CheckBox checkBox;
        }
    }

}
