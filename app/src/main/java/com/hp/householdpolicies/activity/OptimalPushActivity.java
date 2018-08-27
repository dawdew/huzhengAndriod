package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hp.householdpolicies.R;
import com.iflytek.cloud.SpeechSynthesizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class OptimalPushActivity extends BaseActivity {
//    继续
    @BindView(R.id.btnContinue)
    Button btnContinue;
    private SpeechSynthesizer mTts;
    private boolean isSpeaked=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_optimal_push);
        ButterKnife.bind(this);
        MyApp  application = (MyApp) getApplication();
        mTts = application.getmTts();
    }
    @OnClick({ R.id.btnContinue})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                Intent intent = new Intent(this,PersonalInformationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        mTts.startSpeaking("填写申请人信息即可由系统推送适合您的最优落户业务",null);
        super.onStart();
    }
}
