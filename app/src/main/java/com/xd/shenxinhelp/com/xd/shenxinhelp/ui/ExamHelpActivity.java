package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Intent;
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
import android.widget.Toast;

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
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.News;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExamHelpActivity extends AppCompatActivity {
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


    private LineChart lineChart;

    private static final int TYPE = 3;

    private ArrayList<News> news_list;
    private String userID;

    private View news1;
    private ImageView news1_image;
    private TextView news1_text;

    private View news2;
    private ImageView news2_image;
    private TextView news2_text;


    private List<ImageView> image_list;
    private List<TextView> text_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        initViews();
        RequestRecommendar();
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
        fab.setVisibility(View.GONE);
        findViewById(R.id.linechart_container).setVisibility(View.GONE);

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

        func1_text1.setText("会考项目");
        func1_text2.setText("注意事项");

        Liner1 = findViewById(R.id.content_BEHE_liner1);
        Liner2 = findViewById(R.id.content_BEHE_liner2);

        Liner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExamHelpActivity.this, WebViewActivity.class);
                intent.putExtra("url", "file:///android_asset/huikao.html");
                intent.putExtra("title", "会考");
                intent.putExtra("image_url", "");
                startActivity(intent);
            }
        });
        Liner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExamHelpActivity.this, WebViewActivity.class);
                intent.putExtra("url", "file:///android_asset/kaishi.html");
                intent.putExtra("title", "考试");
                intent.putExtra("image_url", "");
                startActivity(intent);
            }
        });

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

        news1 = findViewById(R.id.body_news_1);
        news1_image = (ImageView) findViewById(R.id.body_news1_image);
        news1_text = (TextView) findViewById(R.id.body_news1_text);

        news2 = findViewById(R.id.body_news_2);
        news2_image = (ImageView) findViewById(R.id.body_news2_image);
        news2_text = (TextView) findViewById(R.id.body_news2_text);

        text_list = new ArrayList<>();
        image_list = new ArrayList<>();

        text_list.add(news1_text);
        text_list.add(news2_text);
        image_list.add(news1_image);
        image_list.add(news2_image);

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
                initListener();
            }
        });

    }

    private void initListener() {
        news1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExamHelpActivity.this, WebViewActivity.class);
                intent.putExtra("url", news_list.get(0).getWebUrl());
                intent.putExtra("title", news_list.get(0).getTitle());
                intent.putExtra("image_url", news_list.get(0).getImageUrl());
                startActivity(intent);
            }
        });

        news2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExamHelpActivity.this, WebViewActivity.class);
                intent.putExtra("url", news_list.get(1).getWebUrl());
                intent.putExtra("title", news_list.get(1).getTitle());
                intent.putExtra("image_url", news_list.get(1).getImageUrl());
                startActivity(intent);
            }
        });
    }

    private void fillViews() {

        for (int i = 0; i < news_list.size(); i++) {
            text_list.get(i).setText(news_list.get(i).getTitle());
            Glide.with(this).load(news_list.get(i).getImageUrl()).into(image_list.get(i));
        }

    }

    private void parseRecommendar(String str) {
        news_list = new ArrayList<>();
        try {
            JSONObject js = new JSONObject(str);
            JSONArray ja = js.getJSONArray("news");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                News news = new News();
                news.setTitle(jb.getString("title"));
                news.setImageUrl(jb.getString("imageUrl"));
                news.setWebUrl(jb.getString("webUrl"));
                news_list.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
