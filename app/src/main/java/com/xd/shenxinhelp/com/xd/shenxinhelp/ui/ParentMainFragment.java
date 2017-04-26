package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.Student;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentMainFragment extends Fragment {

    private String userID;
    private ArrayList<Student> stu_list;

    private ViewPager mPager;//页卡内容
    private List<ListView> listview_list;// Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2,t3,t4;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private List<Fragment> fragmentList;// Tab页面列表

    private LinearLayout main_content;
    private TextView no_child;

    public ParentMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_main, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");
        stu_list = new ArrayList<>();

        t1 = (TextView) view.findViewById(R.id.parent_main_day);
        t2 = (TextView) view.findViewById(R.id.parent_main_week);
        t3 = (TextView) view.findViewById(R.id.parent_main_month);
        t4 = (TextView) view.findViewById(R.id.parent_main_year);
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        cursor = (ImageView) view.findViewById(R.id.cursor);

        main_content = (LinearLayout) view.findViewById(R.id.main_content);
        no_child = (TextView) view.findViewById(R.id.no_child);

        getMyChild();

        return view;
    }

    private void getMyChild(){
        OkHttp.get(AppUtil.GetMyChild + "?parentID="+userID, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("getMyChild", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                try {
                    Log.e("str",str);
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        JSONArray jsonArray = jsonObject.getJSONArray("children");
                        if (jsonArray.length()>0){
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject temp = jsonArray.getJSONObject(i);
                                Student student = new Student();
                                student.setClass_id(temp.getString("classid"));
                                student.setStudent_id(temp.getString("studentid"));
                                student.setStudent_name(temp.getString("studentName"));
                                stu_list.add(student);
                            }
                            no_child.setVisibility(View.GONE);
                            main_content.setVisibility(View.VISIBLE);
                            InitTextView();
                            InitViewPager();
                            InitImageView();
                        }
                        else {
                            main_content.setVisibility(View.GONE);
                            no_child.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        main_content.setVisibility(View.GONE);
                        no_child.setVisibility(View.GONE);
                        Log.e("Fail", jsonObject.getString("message"));
                        Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        t4.setOnClickListener(new MyOnClickListener(3));
    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        fragmentList =new ArrayList<Fragment>();
        fragmentList.add(new ParentDayFragment(stu_list));
        fragmentList.add(new ParentWeekFragment(stu_list));
        fragmentList.add(new ParentMonthFragment(stu_list));
        fragmentList.add(new ParentYearFragment(stu_list));
        FragAdapter fragAdapter = new FragAdapter(getFragmentManager(), fragmentList);
        mPager.setAdapter(fragAdapter);
        mPager.setCurrentItem(0);
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / fragmentList.size() - bmpW) / 2;// 计算偏移量
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
        }
    }

    /**
     * 页卡切换监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡0 -> 页卡1 偏移量
        int two = one * 2;// 页卡0 -> 页卡2 偏移量
        int three = one * 3;//页卡0 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) { //arg0是跳转的目标页卡
                case 0:
                    if (currIndex == 1) { //从页卡1跳到页卡0
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    }
                    break;
                case 3:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
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

    private class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        FragAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragments=fragmentList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
