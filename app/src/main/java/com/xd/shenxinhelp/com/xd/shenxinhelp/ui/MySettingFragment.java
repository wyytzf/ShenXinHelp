package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.mySetting.AboutActivity;
import com.xd.shenxinhelp.mySetting.Feedback;


public class MySettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private SharedPreferences sp;
    private ImageView iv_headphoto;
    private TextView tv_userName;
    private TextView tv_userSchool;
    private TextView tv_userClass;
    private TextView tv_level;
    private TextView tv_healthDegree;
    private TextView tv_credit;
    private RelativeLayout rl_feedback;
    private RelativeLayout rl_exit;
    private RelativeLayout rl_about;

    private OnFragmentInteractionListener mListener;

    public MySettingFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_setting, container, false);
        initView();
        initData();

        rl_feedback = (RelativeLayout) view.findViewById(R.id.mysetting_feefback);
        rl_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Feedback.class);
                startActivity(intent);
            }
        });

        rl_about = (RelativeLayout) view.findViewById(R.id.mysetting_about);
        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        rl_exit = (RelativeLayout) view.findViewById(R.id.mysetting_exit);
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    void initView() {
        iv_headphoto = (ImageView) view.findViewById(R.id.user_image);
        tv_userName = (TextView) view.findViewById(R.id.user_name);
        tv_userSchool = (TextView) view.findViewById(R.id.user_school);
        tv_userClass = (TextView) view.findViewById(R.id.user_class);
        tv_level = (TextView) view.findViewById(R.id.user_level);
        tv_healthDegree = (TextView) view.findViewById(R.id.user_health_degree);
        tv_credit = (TextView) view.findViewById(R.id.user_credits);
    }

    void initData() {
        sp = getActivity().getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        tv_userName.setText(sp.getString("name", ""));
        tv_userSchool.setText(sp.getString("schoolName", "西电附中"));
        tv_userClass.setText(sp.getString("className", "高二一班"));
        tv_level.setText(sp.getString("level", "10"));
        tv_healthDegree.setText(sp.getString("health_degree", "100"));
        tv_credit.setText(sp.getString("credits", "500"));
        if (!sp.getString("head_url", "").equals("")) {
            Glide.with(this).load(sp.getString("head_url", "")).centerCrop().placeholder(R.mipmap.default_head_image).into(iv_headphoto);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
