package com.hp.householdpolicies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.AdvisorySearchAdapter;
import com.hp.householdpolicies.adapter.OptimalPushResultsAdapter;
import com.hp.householdpolicies.model.AdvisorySearch;
import com.hp.householdpolicies.model.OptimalPushResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvisorySearchActivity extends BaseActivity {
    //搜索图片
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    //搜索录入框
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    //列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AdvisorySearchAdapter adapter;
    private List<AdvisorySearch> listAdvisorySearch=new ArrayList<AdvisorySearch>();
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_advisory_search);
        ButterKnife.bind(this);
        mContext = this;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new AdvisorySearchAdapter(listAdvisorySearch, mContext, false);
        recyclerView.setAdapter(adapter);
        AddData();
    }
    private void AddData(){
        listAdvisorySearch.add(new AdvisorySearch("测试1111111",true));
        listAdvisorySearch.add(new AdvisorySearch("测试2222222",false));
        listAdvisorySearch.add(new AdvisorySearch("测试3333333",false));
        listAdvisorySearch.add(new AdvisorySearch("测试4444444",false));
        listAdvisorySearch.add(new AdvisorySearch("测试5555555",false));
        listAdvisorySearch.add(new AdvisorySearch("测试6666666",false));
        listAdvisorySearch.add(new AdvisorySearch("测试7777777",false));
        listAdvisorySearch.add(new AdvisorySearch("测试8888888",false));
        adapter.notifyDataSetChanged();
    }
}
