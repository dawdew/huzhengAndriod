package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hp.householdpolicies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvisoryActivity extends BaseActivity {
    //搜索框
    @BindView(R.id.rlSearch)
    RelativeLayout rlSearch;
    //出生登记
    @BindView(R.id.tv_birthr_egistration)
    TextView tvBirthrEgistration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_advisory);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rlSearch,R.id.tv_birthr_egistration})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.rlSearch://搜索框
                Intent intentSearch = new Intent(this, AdvisorySearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.tv_birthr_egistration://出生登记
                Intent intentBirthrEgistration = new Intent(this, AdvisoryDetailsActivity.class);
                intentBirthrEgistration.putExtra("category","1");
                startActivity(intentBirthrEgistration);
                break;

        }
    }
}
