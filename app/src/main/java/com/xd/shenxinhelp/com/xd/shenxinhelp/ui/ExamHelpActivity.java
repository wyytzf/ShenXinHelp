package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xd.shenxinhelp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ExamHelpActivity extends AppCompatActivity {
    private ImageView func1_image;
    private ImageView func2_image;
    private ImageView func3_image;
    private ImageView func4_image;
    private TextView func1_text1;
    private TextView func1_text2;
    private TextView func1_text3;
    private TextView func1_text4;

    private LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        initViews();
    }

    private void initViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        func1_image = (ImageView) findViewById(R.id.content_BEHE_image1);
        func2_image = (ImageView) findViewById(R.id.content_BEHE_image2);
        func3_image = (ImageView) findViewById(R.id.content_BEHE_image3);
        func4_image = (ImageView) findViewById(R.id.content_BEHE_image4);
        View parent = (View) func3_image.getParent();
        parent.setVisibility(View.GONE);
        parent = (View) func4_image.getParent();
        parent.setVisibility(View.GONE);
        Glide.with(this).load(R.mipmap.exam_huikao).into(func1_image);
        Glide.with(this).load(R.mipmap.exam_kaoshi).into(func2_image);


        func1_text1 = (TextView) findViewById(R.id.content_BEHE_text1);
        func1_text2 = (TextView) findViewById(R.id.content_BEHE_text2);

        func1_text1.setText("会考");
        func1_text2.setText("考试");


        lineChart = (LineChart) findViewById(R.id.linechart);
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
}
