package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xd.shenxinhelp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentMonthFragment extends Fragment {


    public ParentMonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_month, container, false);
    }

}
