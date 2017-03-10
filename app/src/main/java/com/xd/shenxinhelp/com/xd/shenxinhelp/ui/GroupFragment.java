package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.MainGroupAdapter;
import com.xd.shenxinhelp.group.GroupDetailActivity;
import com.xd.shenxinhelp.model.Group;
import com.xd.shenxinhelp.model.GroupDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View root;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected SwipeRefreshLayout listview;
    private RecyclerView recyclerView;
    Activity activity;
    private OnFragmentInteractionListener mListener;
    MainGroupAdapter adapter;
    List<Group> datas;
    private ExpandableListView exListView;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_group, container, false);
        activity = getActivity();
        initData();
        initView();

        return root;
    }


    public void initView() {

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_group) {
                    Toast.makeText(activity, "1", Toast.LENGTH_LONG).show();
                }
                if (item.getItemId() == R.id.creat_group) {
                    Toast.makeText(activity, "2", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


        exListView = (ExpandableListView) root.findViewById(R.id.group_refresh_listview);
        adapter = new MainGroupAdapter(activity, datas);
        exListView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            exListView.expandGroup(i);
        }

//        exListView.setOnGroupClickListener(new MyOnGroupClickListener());
        exListView.setOnChildClickListener(new MyOnChildClickListener());

        exListView.setGroupIndicator(null);
        exListView.setDivider(null);

    }

    public void initData() {
        datas = new ArrayList<Group>();

        Group data1 = new Group();
        data1.setName("学校");
        GroupDetail datadetail1 = new GroupDetail();
        datadetail1.setName("学校");
        datadetail1.setDes("一个很good的学校");
        List<GroupDetail> list1 = new ArrayList<GroupDetail>();
        list1.add(datadetail1);
        data1.setGroupList(list1);
        datas.add(data1);
        Group data2 = new Group();
        data2.setName("班级");
        GroupDetail datadetail2 = new GroupDetail();
        datadetail2.setName("班级");
        datadetail2.setDes("一个很good的班级一个很good的班级一个很good的班级一个很good的班级一个很good的班级一个很good的班级一个很good的班级一个很good的班级");
        List<GroupDetail> list2 = new ArrayList<GroupDetail>();
        list2.add(datadetail2);
        data2.setGroupList(list2);
        datas.add(data2);

        Group data3 = new Group();
        data3.setName("圈子");
        GroupDetail datadetail3 = new GroupDetail();
        datadetail3.setName("圈子1");
        datadetail3.setDes("一个很good的圈子");
        GroupDetail datadetail4 = new GroupDetail();
        datadetail4.setName("圈子2");
        datadetail4.setDes("一个很bad的圈子");
        List<GroupDetail> list3 = new ArrayList<GroupDetail>();
        list3.add(datadetail3);
        list3.add(datadetail4);
        data3.setGroupList(list3);
        datas.add(data3);
    }
//    public class MyOnGroupClickListener implements OnGroupClickListener {
//
//        public boolean onGroupClick(ExpandableListView parent, final View v,
//                                    final int groupPosition, long id)
//        {
//            System.out.println("222111111111111111");
//            Log.i("11","rrrrrrrrrrrrrrr");
//            exListView.expandGroup(groupPosition);
//            exListView.setOnChildClickListener(new MyOnChildClickListener());
//            return true;
//        }
//    }


    public class MyOnChildClickListener implements OnChildClickListener {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            Intent intent = new Intent(activity, GroupDetailActivity.class);
            startActivity(intent);
            return true;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_group, menu);
    }


}
