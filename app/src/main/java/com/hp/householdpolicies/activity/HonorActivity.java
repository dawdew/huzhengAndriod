package com.hp.householdpolicies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.DownloadAdapter;
import com.hp.householdpolicies.adapter.HonorAdapter;
import com.hp.householdpolicies.model.Download;
import com.hp.householdpolicies.model.Honor;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HonorActivity extends BaseActivity {
    //列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Honor> listHonor = new ArrayList<Honor>();
    private HonorAdapter adapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_honor);
        ButterKnife.bind(this);
        mContext=this;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new HonorAdapter(listHonor, mContext);
        recyclerView.setAdapter(adapter);
        AddData();
    }
    private void AddData(){
        OkhttpUtil.okHttpGet(Api.honorList, null, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                List<Map<String, String>> list = (List<Map<String, String>>) response;
                for(Map<String, String> m:list){
                    Honor honor = new Honor(true);
                    honor.setTitle(m.get("title"));
                    honor.setImage(Api.downurl+m.get("image"));
                    listHonor.add(honor);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
