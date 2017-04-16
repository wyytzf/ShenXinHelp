package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.model.Student;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentWeekFragment extends Fragment {

    private ArrayList<Student> stu_list;

    public ParentWeekFragment(ArrayList<Student> stu_list) {
        // Required empty public constructor
        this.stu_list = stu_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_week, container, false);
    }

}
