package com.hp.householdpolicies.activity;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.customView.PopLogisticsInformation;
import com.hp.householdpolicies.model.LogisticsInformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InformationProgressActivity extends BaseActivity {
    //业务处理进度
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    //物流信息
    @BindView(R.id.tv_logistics_information)
    TextView tvLogisticsInformation;
    PopLogisticsInformation pop;
    Context mContext;
    private List<LogisticsInformation> listValue=new ArrayList<LogisticsInformation>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_information_progress);
        ButterKnife.bind(this);
        tvLogisticsInformation.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mContext=this;
        pop=new PopLogisticsInformation(mContext);
    }
    private void AddData(){
        listValue.add(new LogisticsInformation("物流信息1111111111","2018-06-20 19:27:30"));
        listValue.add(new LogisticsInformation("物流信息2222222222","2018-06-20 18:27:30"));
        listValue.add(new LogisticsInformation("物流信息3333333333","2018-06-20 17:27:30"));
        listValue.add(new LogisticsInformation("物流信息4444444444","2018-06-20 16:27:30"));
        listValue.add(new LogisticsInformation("物流信息5555555555","2018-06-20 15:27:30"));
        pop.setData(listValue,"1234567890123456");
    }

    @OnClick({R.id.tv_progress, R.id.tv_logistics_information})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_progress://进度
                tvProgress.setText("处理完成");
                tvProgress.setBackgroundResource(R.color.resolve_bg);
                tvLogisticsInformation.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_logistics_information://物流
                AddData();
                showLogisticsInformationWindow(tvLogisticsInformation);
                break;
        }
    }
    //物流信息窗口
    private void showLogisticsInformationWindow(View v) {
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAtLocation(v, Gravity.CENTER, 0, 0);

    }
}
