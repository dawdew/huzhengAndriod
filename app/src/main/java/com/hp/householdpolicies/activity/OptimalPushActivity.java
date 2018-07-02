package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hp.householdpolicies.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_optimal_push);
        ButterKnife.bind(this);
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
}
