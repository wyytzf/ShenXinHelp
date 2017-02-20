package com.xd.shenxinhelp.group;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.View.MyGridDivider;
import com.xd.shenxinhelp.adapter.RankAdapterMy;
import com.xd.shenxinhelp.listener.ListItemClickListener;
import com.xd.shenxinhelp.model.Rank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMY on 2017/2/14.
 */

public class RankActivity  extends AppCompatActivity implements ListItemClickListener {
    private List<Rank> datas = null;
    private RankAdapterMy adapter = null;
    private SwipeRefreshLayout listview;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //设置可以滑出底栏
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(0,0,0, (int) getResources().getDimension(R.dimen.BottomBarHeight));
        listview=(SwipeRefreshLayout)findViewById(R.id.listview_get_resource);
        adapter = new RankAdapterMy(datas,RankActivity.this, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == 0) {
                    return 1;
                } else {
                    return 1;
                }
            }
        });
        listview.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        recyclerView.addItemDecoration(new MyGridDivider(1,
                ContextCompat.getColor(this, R.color.colorDivider)));
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        listview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview.setRefreshing(false);
            }
        });
        btnBack=(Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    void initData(){
        datas= new ArrayList<Rank>();
        Rank rank1= new Rank();
        rank1.setName("夏利i");
        rank1.setHealthValue("120");
        Rank rank2= new Rank();
        rank2.setName("夏利i");
        rank2.setHealthValue("120");
        datas.add(rank1);
        datas.add(rank2);
    }

    @Override
    public void onListItemClick(View v, int position) {



    }

}
