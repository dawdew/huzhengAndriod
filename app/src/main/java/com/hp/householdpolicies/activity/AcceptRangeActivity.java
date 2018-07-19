package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.hp.householdpolicies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AcceptRangeActivity extends BaseActivity {
    //公安南开分局人口管理中心
    @BindView(R.id.RLManagementCenter)
    RelativeLayout RLManagementCenter;
    //大厅
    @BindView(R.id.RLLobby)
    RelativeLayout RLLobby;
    //身份证
    @BindView(R.id.RLIDCards)
    RelativeLayout RLIDCards;
    //户限
    @BindView(R.id.RLWould)
    RelativeLayout RLWould;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_accept_range);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.RLManagementCenter,R.id.RLLobby,R.id.RLIDCards,R.id.RLWould})
    void ViewClick(View view) {
        String type="";
        switch (view.getId()) {
            case R.id.RLManagementCenter://公安南开分局人口管理中心
                type="公安南开分局人口管理中心";
                break;
            case R.id.RLLobby://大厅
                type="大厅";
                break;
            case R.id.RLIDCards://身份证
                type="身份证";
                break;
            case R.id.RLWould://户政
                type="户政";
                break;

        }
        Intent intent=new Intent(this,AcceptRangeTaskNumberActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}