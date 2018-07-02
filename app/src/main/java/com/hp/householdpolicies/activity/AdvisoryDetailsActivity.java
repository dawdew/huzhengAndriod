package com.hp.householdpolicies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hp.householdpolicies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvisoryDetailsActivity extends BaseActivity {
    //便签打印
    @BindView(R.id.llPrint)
    LinearLayout llPrint;
    //一年内
    @BindView(R.id.btnYear)
    Button btnYear;
    //一年以上
    @BindView(R.id.btnMoreYear)
    Button btnMoreYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_advisory_details);
        ButterKnife.bind(this);
        btnYear.setSelected(true);
    }

    @OnClick({R.id.btnYear, R.id.btnMoreYear})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnYear://一年内
                btnYear.setSelected(true);
                btnMoreYear.setSelected(false);
                break;
            case R.id.btnMoreYear://一年以上
                btnMoreYear.setSelected(true);
                btnYear.setSelected(false);
                break;
        }

    }
}
