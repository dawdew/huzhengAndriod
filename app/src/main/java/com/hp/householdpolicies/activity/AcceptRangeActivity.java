package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.utils.MsynthesizerListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;

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
    private SpeechSynthesizer mTts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_accept_range);
        ButterKnife.bind(this);
        MyApp myApp = (MyApp) getApplication();
        mTts = myApp.getmTts();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mTts.startSpeaking("请点击图片进入详情", null);
    }
    @OnClick({R.id.RLManagementCenter,R.id.RLLobby,R.id.RLIDCards,R.id.RLWould})
    void ViewClick(View view) {
        String type="";
        String location = "";
        switch (view.getId()) {
            case R.id.RLManagementCenter://公安南开分局人口管理中心
                type="公安南开分局人口管理中心";
                location = "中心整体情况";
                break;
            case R.id.RLLobby://大厅
                type="大厅";
                location = "户口受理厅";
                break;
            case R.id.RLIDCards://身份证
                type="身份证";
                location = "身份证受理厅";
                break;
            case R.id.RLWould://户政
                type="户政";
                location = "户口申报接待室";
                break;
        }
        final String type2 = type;
        mTts.startSpeaking("为您介绍" + location, new MsynthesizerListener() {
            @Override
            public void onCompleted(SpeechError speechError) {
                Intent intent = new Intent(AcceptRangeActivity.this, AcceptRangeTaskNumberActivity.class);
                intent.putExtra("type", type2);
                startActivity(intent);
            }
        });
    }
}