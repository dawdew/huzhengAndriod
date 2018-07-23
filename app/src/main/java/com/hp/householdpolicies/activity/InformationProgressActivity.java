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
import com.hp.householdpolicies.model.Article;
import com.hp.householdpolicies.model.LogisticsInformation;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.CallBackUtil;
import com.hp.householdpolicies.utils.JsonParser;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
        HashMap<String, String> headMap = new HashMap<>();
        headMap.put("Version","ems_track_cn_1.0");
        headMap.put("authenticate",Api.emsauth);
        final String emsNo="123";
        OkhttpUtil.okHttpGet(Api.ems+emsNo, null,headMap, new CallBackUtil() {
            @Override
            public Object onParseResponse(Call call, Response response) {
                if(response.code()!=200){
                    return null;
                }
                ResponseBody body = response.body();
                String ddd = null;
                try {
                    ddd = body.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap result = JsonParser.fromJson(ddd, HashMap.class);
                return result.get("traces");
            }

            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(Object response) {
                List<Map<String,String>> traces = (List) response;
                for(Map<String,String> m:traces){
                    listValue.add(new LogisticsInformation(m.get("remark"),m.get("acceptTime")));
                }
                pop.setData(listValue,emsNo);
            }
        });
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
