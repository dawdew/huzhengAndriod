package com.hp.householdpolicies.activity.department;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.activity.BaseActivity;
import com.hp.householdpolicies.adapter.DepartmentsOneAdapter;
import com.hp.householdpolicies.adapter.DepartmentsSearchAdapter;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentsSearchActivity extends BaseActivity {
    //搜索框
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    //列表
    @BindView(R.id.list)
    RecyclerView list;
    private DepartmentsSearchAdapter adapter;
    private List<Map<String,String>> listContent = new ArrayList<>();
    Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_departments_search);
        ButterKnife.bind(this);
        mContext = this;
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DepartmentsSearchAdapter(listContent, mContext);
        adapter.setOnItemClickListener(new DepartmentsSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Map<String,String> item) {
//                Toast.makeText(mContext,"点击"+position,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,DepartmentsDetailActivity.class);
                intent.putExtra("address",item.get("address"));
                intent.putExtra("name",item.get("name"));
                intent.putExtra("tel",item.get("tel"));
                intent.putExtra("travel",item.get("travel"));
                intent.putExtra("latlng",item.get("latlng"));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        });
        list.setAdapter(adapter);
        Intent intent = getIntent();
//        AddData();
        search(intent.getStringExtra("name"));
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isNotBlank(edtSearch.getText().toString())){
                    search(edtSearch.getText().toString());
                }
            }
        });
    }

    private void AddData() {
        adapter.notifyDataSetChanged();
    }
    private void search(String name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name",name);
        OkhttpUtil.okHttpGet(Api.deptList, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                listContent = (List<Map<String, String>>) response;
                adapter.setData(listContent);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
