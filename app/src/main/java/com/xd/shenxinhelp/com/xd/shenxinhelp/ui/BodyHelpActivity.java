package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.netutils.OkHttp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_help);

        initView();

        Request();

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
                startActivity(intent);
            }
        });
        Liner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, HelpContentActivity.class);
                startActivity(intent);
            }
        });
        Liner3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, HelpContentActivity.class);
                startActivity(intent);
            }
        });
        Liner4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyHelpActivity.this, HelpContentActivity.class);
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
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i + 1, val, getResources().getDrawable(R.drawable.ic_assignment_black_24dp)));
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

    private void Request() {
        OkHttp.get("http://www.baidu.com", new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                // 已经是主线程了，直接操作
                Log.i("wyy", str);
            }

            @Override
            public void onResponse(String str) {
                // 已经是主线程了，直接操作
                Log.i("wyy", str);
            }
        });

    }


}
