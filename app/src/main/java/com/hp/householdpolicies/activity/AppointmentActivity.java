package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hp.householdpolicies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentActivity extends BaseActivity {
    //出生登记
    @BindView(R.id.tv_birthr_egistration)
    TextView tvBirthrEgistration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_appointment);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_birthr_egistration})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_birthr_egistration:
                Intent intentBirthrEgistration=new Intent(this,AppointmentDetailsActivity.class);
                startActivity(intentBirthrEgistration);
                break;
        }
    }
}
