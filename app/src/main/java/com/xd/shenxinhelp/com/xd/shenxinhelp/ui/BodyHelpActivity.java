package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.BodyItem;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class BodyHelpActivity extends AppCompatActivity {

    private ImageView func1_image;
    private ImageView func2_image;
    private ImageView func3_image;
    private ImageView func4_image;
    private TextView func1_text1;
    private TextView func1_text2;
    private TextView func1_text3;
    private TextView func1_text4;
    private View Liner1;
    private View Liner2;
    private View Liner3;
    private View Liner4;

    private View checkMore;


    private LineChart lineChart;

    private static final int TYPE = 0;

    private ArrayList<BodyItem> news_list;
    private String userID;

    private View news1;
    private ImageView news1_image;
    private TextView news1_text;

    private View news2;
    private ImageView news2_image;
    private TextView news2_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_help);

        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");


        initView();
        RequestRecommendar();
        RequestLineChart();

    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 启动制定计划activity
            }
        });


        func1_image = (ImageView) findViewById(R.id.content_BEHE_image1);
        func2_image = (ImageView) findViewById(R.id.content_BEHE_image2);
        func3_image = (ImageView) findViewById(R.id.content_BEHE_image3);
        func4_image = (ImageView) findViewById(R.id.content_BEHE_image4);
        Glide.with(this).load(R.mipmap.body_leg).into(func1_image);
        Glide.with(this).load(R.mipmap.body_abdomen).into(func2_image);
        Glide.with(this).load(R.mipmap.body_allbody).into(func3_image);
        Glide.with(this).load(R.mipmap.body_arm).into(func4_image);


        func1_text1 = (TextView) findViewById(R.id.content_BEHE_text1);
        func1_text2 = (TextView) findViewById(R.id.content_BEHE_text2);
        func1_text3 = (TextView) findViewById(R.id.content_BEHE_text3);
        func1_text4 = (TextView) findViewById(R.id.content_BEHE_text4);
        func1_text1.setText("腿部");
        func1_text2.setText("腰部");
        func1_text3.setText("手臂");
        func1_text4.setText("全身");

        Liner1 = findViewById(R.id.content_BEHE_liner1);
        Liner2 = findViewById(R.id.content_BEHE_liner2);
        Liner3 = findViewById(R.id.content_BEHE_liner3);
        Liner4 = findViewById(R.id.content_BEHE_liner4);


        Liner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, HelpContentActivity.class);
                intent.putExtra("buwei", "3");
                startActivity(intent);
            }
        });
        Liner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, HelpContentActivity.class);
                intent.putExtra("buwei", "2");
                startActivity(intent);
            }
        });
        Liner3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, HelpContentActivity.class);
                intent.putExtra("buwei", "1");
                startActivity(intent);
            }
        });
        Liner4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, HelpContentActivity.class);
                intent.putExtra("buwei", "4");
                startActivity(intent);
            }
        });

        lineChart = (LineChart) findViewById(R.id.linechart);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setScaleEnabled(false);
        Description description = new Description();
        description.setText("消耗热量");
        lineChart.setDescription(description);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Calendar calendar = Calendar.getInstance();
                int i = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                return (i + 1) + "月" + (int) value + "日";
            }
        });
        lineChart.setNoDataText("暂无数据");
        setData(7, 100);


        checkMore = findViewById(R.id.check_more);
        checkMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, LineChartActivity.class);
                startActivity(intent);
            }
        });

        news1 = findViewById(R.id.body_news_1);
        news1_image = (ImageView) findViewById(R.id.body_news1_image);
        news1_text = (TextView) findViewById(R.id.body_news1_text);

        news2 = findViewById(R.id.body_news_2);
        news2_image = (ImageView) findViewById(R.id.body_news2_image);
        news2_text = (TextView) findViewById(R.id.body_news2_text);


    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i + 1, val, null));
        }

        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "消耗热量");

//            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.argb(255, 255, 166, 166));
            set1.setCircleColor(Color.argb(255, 255, 166, 166));
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }

    ///  首页新闻
    private void RequestRecommendar() {
        OkHttp.get(AppUtil.GETEXERCISETOFOUR + "type=" + TYPE, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                // 已经是主线程了，直接操作

            }

            @Override
            public void onResponse(String str) {
                // 已经是主线程了，直接操作
                parseRecommendar(str);
                fillViews();
            }
        });

    }

    private void fillViews() {
    }


    // 统计图表
    private void RequestLineChart() {
        OkHttp.get(AppUtil.GETDOEXERCISEINFO + "userID=" + userID, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                // 已经是主线程了，直接操作

            }

            @Override
            public void onResponse(String str) {
                // 已经是主线程了，直接操作
                parseLineChart(str);
            }
        });
    }

    private void parseLineChart(String str) {

        try {
            JSONObject js = new JSONObject(str);
            JSONArray ja = js.getJSONArray("infos");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseRecommendar(String str) {
        news_list = new ArrayList<>();
        try {
            JSONObject js = new JSONObject(str);
            JSONArray ja = js.getJSONArray("exercisees");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                BodyItem bodyitem = new BodyItem();
                bodyitem.setId(jb.getString("id"));
                bodyitem.setBuwei(jb.getString("buwei"));
                bodyitem.setName(jb.getString("name"));
                bodyitem.setReosurce_url(jb.getString("reosurce_url"));
                bodyitem.setTotal_time(jb.getString("total_time"));
                bodyitem.setFee_cridits(jb.getString("fee_cridits"));
                bodyitem.setHeat(jb.getString("heat"));
                bodyitem.setGet_degree(jb.getString("get_degree"));
                bodyitem.setDiffculty(jb.getString("diffculty"));
                bodyitem.setWebUrl(jb.getString("webUrl"));
                news_list.add(bodyitem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
