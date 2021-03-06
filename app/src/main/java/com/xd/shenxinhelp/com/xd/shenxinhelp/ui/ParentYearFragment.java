package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.Student;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentYearFragment extends Fragment {

    private ArrayList<Student> stu_list;
    private String[] stu_names;
    private Calendar calendar;
    private SimpleDateFormat format;
    private String[] dates;
    private Map<String, Float> map_date_calories;
    private Map<String, Float> map_date_calories_same;

    private TextView date_txt;
    private ImageView left_arrow;
    private ImageView right_arrow;
    private Spinner spinner;
    private ProgressBar progressBar;
    private LineChart lineChart;
    private CheckBox same_class_check;
    private TextView total_heat;
    private TextView decrease_weight;
    private TextView no_data;
    private LinearLayout data_layout;

    private String buffer_userid;
    private String buffer_classid;
    private String buffer_begin_date;
    private String buffer_end_date;

    private final int READING = 0;
    private final int EXIST_DATA = 1;
    private final int NO_DATA = -1;

    public ParentYearFragment(ArrayList<Student> stu_list) {
        // Required empty public constructor
        this.stu_list = stu_list;
        stu_names = new String[stu_list.size()];
        int i=0;
        for (Student student : stu_list){
            stu_names[i] = student.getStudent_name();
            i++;
        }
        calendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_year, container, false);

        date_txt = (TextView) view.findViewById(R.id.day_date);

        dates = new String[7];
        map_date_calories = new TreeMap<>();
        map_date_calories_same = new TreeMap<>();
        dates[0] = format.format(calendar.getTime());
        map_date_calories.put(dates[0].substring(0, 7), 0f);
        map_date_calories_same.put(dates[0].substring(0, 7), 0f);
        for (int i=0; i<12; i++){
            calendar.add(Calendar.MONTH, -1);
            map_date_calories.put(format.format(calendar.getTime()).substring(0, 7), 0f);
            map_date_calories_same.put(format.format(calendar.getTime()).substring(0, 7), 0f);
        }
        calendar.add(Calendar.MONTH, 12);
        for (int i=1; i<7; i++){
            calendar.add(Calendar.MONTH, -2);
            dates[i] = format.format(calendar.getTime());
        }
        String end_date = dates[0];
        String begin_date = dates[6];
        date_txt.setText(begin_date.substring(0, 7)+"~"+end_date.substring(0, 7));

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        no_data = (TextView) view.findViewById(R.id.no_data);
        data_layout = (LinearLayout) view.findViewById(R.id.data_layout);

        lineChart = (LineChart) view.findViewById(R.id.line_chart);
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
        lineChart.setNoDataText("暂无数据");

        lineChart.getXAxis().setAxisMinimum(0);
        lineChart.getXAxis().setAxisMaximum(6);
        lineChart.getXAxis().setLabelCount(6);

        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.e("value",""+value);
                return dates[6-(int)value].substring(2, 7);
            }
        });

        viewShowOrGone(READING);

        total_heat = (TextView) view.findViewById(R.id.total_heat);
        decrease_weight = (TextView) view.findViewById(R.id.decrease_weight);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, stu_names));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewShowOrGone(READING);
                total_heat.setText("0千焦");
                decrease_weight.setText("≈减掉0公斤");
                buffer_classid = stu_list.get(position).getClass_id();
                getData(stu_list.get(position).getStudent_id(), null, null);
                same_class_check.setChecked(false);
                for (String key : map_date_calories.keySet()){
                    map_date_calories.put(key, 0f);
                    map_date_calories_same.put(key, 0f);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        left_arrow = (ImageView) view.findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewShowOrGone(READING);
                same_class_check.setChecked(false);
                total_heat.setText("0千焦");
                decrease_weight.setText("≈减掉0公斤");
                map_date_calories.clear();
                map_date_calories_same.clear();

                calendar.add(Calendar.MONTH, -1);
                dates[0] = format.format(calendar.getTime());
                map_date_calories.put(dates[0].substring(0, 7), 0f);
                map_date_calories_same.put(dates[0].substring(0, 7), 0f);
                for (int i=0; i<12; i++){
                    calendar.add(Calendar.MONTH, -1);
                    map_date_calories.put(format.format(calendar.getTime()).substring(0, 7), 0f);
                    map_date_calories_same.put(format.format(calendar.getTime()).substring(0, 7), 0f);
                }
                calendar.add(Calendar.MONTH, 12);
                for (int i=1; i<7; i++){
                    calendar.add(Calendar.MONTH, -2);
                    dates[i] = format.format(calendar.getTime());
                }
                String end_date = dates[0];
                String begin_date = dates[6];
                date_txt.setText(begin_date.substring(0, 7)+"~"+end_date.substring(0, 7));
                getData(null, begin_date.substring(0, 7), end_date);

            }
        });

        right_arrow = (ImageView) view.findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewShowOrGone(READING);
                same_class_check.setChecked(false);
                total_heat.setText("0千焦");
                decrease_weight.setText("≈减掉0公斤");
                map_date_calories.clear();
                map_date_calories_same.clear();

                calendar.add(Calendar.MONTH, 25);
                dates[0] = format.format(calendar.getTime());
                map_date_calories.put(dates[0].substring(0, 7), 0f);
                map_date_calories_same.put(dates[0].substring(0, 7), 0f);
                for (int i=0; i<12; i++){
                    calendar.add(Calendar.MONTH, -1);
                    map_date_calories.put(format.format(calendar.getTime()).substring(0, 7), 0f);
                    map_date_calories_same.put(format.format(calendar.getTime()).substring(0, 7), 0f);
                }
                calendar.add(Calendar.MONTH, 12);
                for (int i=1; i<7; i++){
                    calendar.add(Calendar.MONTH, -2);
                    dates[i] = format.format(calendar.getTime());
                }
                String end_date = dates[0];
                String begin_date = dates[6];
                date_txt.setText(begin_date.substring(0, 7)+"~"+end_date.substring(0, 7));
                getData(null, begin_date.substring(0, 7), end_date);

            }
        });

        same_class_check = (CheckBox) view.findViewById(R.id.same_class_check);
        same_class_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    OkHttp.get(AppUtil.GetSameClassYearConsumedCalories + "?userId="+buffer_userid +"&classId="
                                    +buffer_classid +"&begin_month=" +buffer_begin_date+"&end_month="+buffer_end_date,
                            new OkHttp.ResultCallBack() {
                                @Override
                                public void onError(String str, Exception e) {
                                    Log.e("same_class_check", str);
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(String str) {
                                    try {
                                        Log.e("year_same_class_check",str);
                                        JSONObject jsonObject = new JSONObject(str);
                                        String reCode = jsonObject.getString("reCode");
                                        if ("SUCCESS".equals(reCode)){
                                            JSONArray yearAverageCalories = jsonObject.getJSONArray("yearAverageCalories");
                                            drawLineChartSameClass(yearAverageCalories);
                                            /*if (yearAverageCalories.length()>0){
                                            }
                                            else {
                                                Toast.makeText(getActivity(), "同班同学当年无锻炼数据", Toast.LENGTH_SHORT).show();
                                            }*/
                                        }
                                        else {
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
                else {
                    if (lineChart.getData() != null &&
                            lineChart.getData().getDataSetCount() > 1) {
                        lineChart.getData().getDataSets().remove(1);
                        lineChart.getData().notifyDataChanged();
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                    }
                }
            }
        });

        buffer_classid = stu_list.get(0).getClass_id();
        getData(stu_list.get(0).getStudent_id(), begin_date.substring(0, 7), end_date);

        return view;
    }

    private void getData(String userId, String begin_date, String end_date){
        if (userId!=null){
            buffer_userid = userId;
        }
        if (begin_date!=null){
            buffer_begin_date = begin_date;
        }
        if (end_date!=null){
            buffer_end_date = end_date;
        }
        OkHttp.get(AppUtil.GetAChildConsumedCaloriesAYear + "?userId="+buffer_userid+"&begin_month="
                        +buffer_begin_date+"&end_month="+buffer_end_date,
                new OkHttp.ResultCallBack() {
                    @Override
                    public void onError(String str, Exception e) {
                        Log.e("getData", str);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String str) {
                        try {
                            Log.e("onResponse",str);
                            JSONObject jsonObject = new JSONObject(str);
                            String reCode = jsonObject.getString("reCode");
                            if ("SUCCESS".equals(reCode)){
                                total_heat.setText(jsonObject.getString("yearTotalCalories")+"千焦");
                                decrease_weight.setText("≈减掉"+jsonObject.getString("kilogram")+"公斤");
                                JSONArray differentMomentCalories = jsonObject.getJSONArray("differentMonthCalories");
                                if (differentMomentCalories.length()>0){
                                    drawLineChart(differentMomentCalories);
                                    viewShowOrGone(EXIST_DATA);
                                }
                                else {
                                    no_data.setText(spinner.getSelectedItem().toString()+"当年尚无锻炼数据");
                                    viewShowOrGone(NO_DATA);
                                }
                            }
                            else {
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

    private void drawLineChart(final JSONArray jsonArray){
        List<Entry> entryList = new ArrayList<>();
        try {
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map_date_calories.put(jsonObject.getString("month"), (float) jsonObject.getDouble("calories"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (String key : map_date_calories.keySet()){
            Log.e(key, map_date_calories.get(key)+"");
            entryList.add(new Entry( analyze(key), map_date_calories.get(key), null));
        }

        LineDataSet dataSet = new LineDataSet(entryList, "个人消耗热量");
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ""+(int)value;
            }
        });
        dataSet.enableDashedHighlightLine(10f, 5f, 0f);
        dataSet.setColor(Color.argb(255, 255, 166, 166));
        dataSet.setCircleColor(Color.argb(255, 255, 166, 166));
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setDrawFilled(true);
        dataSet.setFormLineWidth(1f);
        dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        dataSet.setFormSize(15.f);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet); // add the datasets
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void drawLineChartSameClass(final JSONArray jsonArray){
        List<Entry> entryList = new ArrayList<>();
        try {
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map_date_calories_same.put(jsonObject.getString("day"), (float) jsonObject.getDouble("average_calories"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (String key : map_date_calories_same.keySet()){
            Log.e(key, map_date_calories_same.get(key)+"");
            entryList.add(new Entry( analyze(key), map_date_calories_same.get(key), null));
        }

        LineDataSet dataSet = new LineDataSet(entryList, "同班平均消耗热量");
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ""+(int)value;
            }
        });
        dataSet.enableDashedHighlightLine(10f, 5f, 0f);
        dataSet.setColor(Color.argb(255, 255, 165, 0));
        dataSet.setCircleColor(Color.argb(255, 255, 165, 0));
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setDrawFilled(true);
        dataSet.setFormLineWidth(1f);
        dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        dataSet.setFormSize(15.f);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(Color.BLACK);
        }

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            lineChart.getData().getDataSets().add(dataSet);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        } else {
            Log.e("lineChart.getData()","lineChart.getData()==0");
        }

    }

    private float analyze(String date){
        for (int i=0; i<dates.length; i++){
            if (dates[i].substring(0,7).equals(date)){
                return 6-i;
            }
        }
        for (int i=0; i<dates.length-1; i++){
            if (dates[6-i].substring(0,7).compareTo(date)<0 && dates[5-i].substring(0,7).compareTo(date)>0){
                if (dates[6-i].substring(0,4).equals(date.substring(0,4))){
                    return i + 0.5f * ( Float.parseFloat(date.substring(5)) - Float.parseFloat(dates[6-i].substring(5,7)) );
                }
                else {
                    return i+1 - 0.5f * ( Float.parseFloat(dates[5-i].substring(5,7)) - Float.parseFloat(date.substring(5)) );
                }
            }
        }
        return 0;
    }

    private void viewShowOrGone(int state){
        switch (state){
            case READING:
                data_layout.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case EXIST_DATA:
                progressBar.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                data_layout.setVisibility(View.VISIBLE);
                break;
            case NO_DATA:
                progressBar.setVisibility(View.GONE);
                data_layout.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        calendar.add(Calendar.MONTH, 12);
    }
}
