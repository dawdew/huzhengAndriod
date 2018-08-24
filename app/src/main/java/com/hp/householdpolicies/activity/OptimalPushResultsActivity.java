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

import com.google.gson.Gson;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.OptimalPushResultsAdapter;
import com.hp.householdpolicies.customView.RecyclerViewDivider;
import com.hp.householdpolicies.model.OptimalPushResult;
import com.hp.householdpolicies.model.PersonInfo;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.BeanUtil;
import com.hp.householdpolicies.utils.CallBackUtil;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.io.Serializable;
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
                Intent intent = new Intent(OptimalPushResultsActivity.this, AdvisoryDetailsActivity.class);
                intent.putExtra("title",opr.getName());
                intent.putExtra("content",opr.getContent());
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
    }

    @Override
    protected void onStart() {
        AddData();
        super.onStart();
    }

    private void AddData(){
        Intent intent = getIntent();
        PersonInfo info = (PersonInfo)intent.getSerializableExtra("info");
        HashMap<String, String> json_map = new HashMap<>();
        json_map.put("age",info.getAge().toString());

        json_map.put("marriage",BeanUtil.toString(info.getMarriage()));
        json_map.put("sex",BeanUtil.toString(info.getSex()));
        json_map.put("special",BeanUtil.toString(info.getSpecial()));
        json_map.put("marriageParent",BeanUtil.toString(info.getMarriageParent()));

        json_map.put("housePrice_a",BeanUtil.BooleanToString(info.getHousePrice_a()));
        json_map.put("housePrice_b",BeanUtil.BooleanToString(info.getHousePrice_b()));
        json_map.put("house",BeanUtil.BooleanToString(info.getHouse()));
        json_map.put("adopt",BeanUtil.BooleanToString(info.getAdopt()));
        json_map.put("marriageThree",BeanUtil.BooleanToString(info.getMarriageThree()));
        json_map.put("liveFiveYear",BeanUtil.BooleanToString(info.getLiveFiveYear()));
        json_map.put("houseLoan",BeanUtil.BooleanToString(info.getHouseLoan()));
        json_map.put("childrensHousehold",BeanUtil.BooleanToString(info.getChildrensHousehold()));
        json_map.put("adopterHasChild",BeanUtil.BooleanToString(info.getAdopterHasChild()));
        json_map.put("adoptAge30",BeanUtil.BooleanToString(info.getAdoptAge30()));
        json_map.put("adoptCard",BeanUtil.BooleanToString(info.getAdoptCard()));
        json_map.put("householdTj",BeanUtil.BooleanToString(info.getHouseholdTj()));
        json_map.put("marriageParentThenLessThan18",BeanUtil.BooleanToString(info.getMarriageParentThenLessThan18()));
        json_map.put("marriageParentThree",BeanUtil.BooleanToString(info.getMarriageParentThree()));
        OkhttpUtil.okHttpPost(Api.optimalPush, json_map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                List<Map<String, String>> list = (List<Map<String, String>>) response;
                listOptimalPushResult.clear();
                for(Map<String, String> m:list){
                    listOptimalPushResult.add(new OptimalPushResult(m.get("title"),m.get("id"),m.get("content")));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
