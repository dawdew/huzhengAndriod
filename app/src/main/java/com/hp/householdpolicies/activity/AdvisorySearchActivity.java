package com.hp.householdpolicies.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.AdvisorySearchAdapter;
import com.hp.householdpolicies.adapter.ArticleAdapter;
import com.hp.householdpolicies.adapter.OptimalPushResultsAdapter;
import com.hp.householdpolicies.model.AdvisorySearch;
import com.hp.householdpolicies.model.Honor;
import com.hp.householdpolicies.model.OptimalPushResult;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvisorySearchActivity extends BaseActivity {
    //搜索图片
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    //搜索录入框
    @BindView(R.id.editSearch)
    EditText editSearch;
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
        adapter.setOnItemClickListener(new AdvisorySearchAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(View view, int position) {
                AdvisorySearch advisorySearch = listAdvisorySearch.get(position);
                Intent intent = new Intent(AdvisorySearchActivity.this, AdvisoryDetailsActivity.class);
                intent.putExtra("title",advisorySearch.getName());
                intent.putExtra("cname",advisorySearch.getCname());
                intent.putExtra("content",advisorySearch.getContent());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDel(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword");
        AddData(keyword);
    }
    private void AddData(String keyword){
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword",keyword);
        OkhttpUtil.okHttpGet(Api.search, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                if(response!=null){
                    List<Map<String, String>> list = (List<Map<String, String>>) response;
                    for(Map<String, String> m:list){
                        AdvisorySearch as = new AdvisorySearch(m.get("title"), false);
                        as.setContent(m.get("content"));
                        as.setCname(m.get("cname"));
                        listAdvisorySearch.add(as);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    @OnClick({R.id.imgSearch})
    void imgSearchClick(View view) {
        String sss = editSearch.getText().toString();
        adapter.clear();
        AddData(sss);
    }
}
