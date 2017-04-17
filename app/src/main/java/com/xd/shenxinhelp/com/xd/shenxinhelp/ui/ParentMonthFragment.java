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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentMonthFragment extends Fragment {

    private ArrayList<Student> stu_list;
    private String[] stu_names;
    private Calendar calendar;
    private SimpleDateFormat format;

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
    private String buffer_begin_date;
    private String buffer_end_date;

    private final int READING = 0;
    private final int EXIST_DATA = 1;
    private final int NO_DATA = -1;

    public ParentMonthFragment(ArrayList<Student> stu_list) {
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
        View view = inflater.inflate(R.layout.fragment_parent_month, container, false);

        date_txt = (TextView) view.findViewById(R.id.day_date);
        String end_date = format.format(calendar.getTime());
        calendar.add(Calendar.DATE, -29);
        String begin_date = format.format(calendar.getTime());
        date_txt.setText(begin_date+"~"+end_date);

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
                getData(stu_list.get(position).getStudent_id(), null, null);
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
                total_heat.setText("0千焦");
                decrease_weight.setText("≈减掉0公斤");
                calendar.add(Calendar.DATE, -1);
                String end_date = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, -29);
                String begin_date = format.format(calendar.getTime());
                date_txt.setText(begin_date+"~"+end_date);
                getData(null, begin_date, end_date);

            }
        });

        right_arrow = (ImageView) view.findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewShowOrGone(READING);
                total_heat.setText("0千焦");
                decrease_weight.setText("≈减掉0公斤");
                calendar.add(Calendar.DATE, 59);
                String end_date = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, -29);
                String begin_date = format.format(calendar.getTime());
                date_txt.setText(begin_date+"~"+end_date);
                getData(null, begin_date, end_date);

            }
        });

        same_class_check = (CheckBox) view.findViewById(R.id.same_class_check);
        same_class_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        getData(stu_list.get(0).getStudent_id(), begin_date, end_date);

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
        OkHttp.get(AppUtil.GetAChildConsumedCaloriesAMonth + "?userId="+buffer_userid+"&begin_date="
                        +buffer_begin_date+"&end_date="+buffer_end_date,
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
                                total_heat.setText(jsonObject.getString("monthTotalCalories")+"千焦");
                                decrease_weight.setText("≈减掉"+jsonObject.getString("kilogram")+"公斤");
                                JSONArray differentMomentCalories = jsonObject.getJSONArray("differentDayCalories");
                                if (differentMomentCalories.length()>0){
                                    drawLineChart(differentMomentCalories);
                                    viewShowOrGone(EXIST_DATA);
                                }
                                else {
                                    no_data.setText(spinner.getSelectedItem().toString()+"当天尚无锻炼数据");
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
        final String[] xAxis = new String[jsonArray.length()];
        try {
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                entryList.add(new Entry(i, (float) jsonObject.getDouble("calories"), null));
                xAxis[i] = jsonObject.getString("day").substring(5);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        lineChart.getXAxis().setAxisMinimum(0);
        lineChart.getXAxis().setAxisMaximum(jsonArray.length()-1);
        lineChart.getXAxis().setLabelCount(jsonArray.length()-1);

        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.e("value",""+value);
                if (jsonArray.length()==2 && value==0.5)
                    return "";
                if ( 0<=value && value<jsonArray.length())
                    return xAxis[(int)value];
                else
                    return "";
            }
        });

        LineDataSet dataSet = new LineDataSet(entryList, "消耗热量");
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
        calendar.add(Calendar.DATE, 29);
    }
}
