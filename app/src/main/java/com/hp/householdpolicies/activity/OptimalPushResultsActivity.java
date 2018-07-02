package com.hp.householdpolicies.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.OptimalPushResultsAdapter;
import com.hp.householdpolicies.customView.RecyclerViewDivider;
import com.hp.householdpolicies.model.OptimalPushResult;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.CallBackUtil;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptimalPushResultsActivity extends BaseActivity {
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private OptimalPushResultsAdapter adapter;
    private List<OptimalPushResult> listOptimalPushResult=new ArrayList<OptimalPushResult>();
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_optimal_push_results);
        ButterKnife.bind(this);
        mContext = this;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new OptimalPushResultsAdapter(listOptimalPushResult, mContext, false);
        OptimalPushResultsAdapter.setOnItemClickListener(new OptimalPushResultsAdapter.OnItemClickListener(){


            @Override
            public void onItemClick(View view, OptimalPushResult opr) {
                System.out.println(opr.getId());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDel(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        AddData();
        super.onStart();
    }

    private void AddData(){
        Intent intent = getIntent();
        HashMap<String, String> json_map = new HashMap<>();
        json_map.put("sex",intent.getStringExtra("sex"));
        json_map.put("age",intent.getStringExtra("age"));
        json_map.put("education",intent.getStringExtra("education"));
        json_map.put("address",intent.getStringExtra("address"));
        json_map.put("marriage",intent.getStringExtra("marriage"));
        json_map.put("children",intent.getStringExtra("children"));
        json_map.put("houseProperty",intent.getStringExtra("houseProperty"));
        OkhttpUtil.okHttpGet(Api.optimalPush, json_map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                List<Map<String, String>> list = (List<Map<String, String>>) response;
                for(Map<String, String> m:list){
                    listOptimalPushResult.add(new OptimalPushResult(m.get("id"),m.get("title"),false));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
