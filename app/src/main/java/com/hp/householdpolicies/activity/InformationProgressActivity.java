package com.hp.householdpolicies.activity;

import android.content.Context;
import android.content.Intent;
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

import net.lemonsoft.lemonbubble.LemonBubble;

import org.apache.commons.lang3.StringUtils;

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
    String emsNo;
    private List<LogisticsInformation> listValue=new ArrayList<LogisticsInformation>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_information_progress);
        ButterKnife.bind(this);
        tvLogisticsInformation.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mContext=this;
        pop=new PopLogisticsInformation(mContext);
        getData();
    }
    private void getEms(){
        HashMap<String, String> headMap = new HashMap<>();
        headMap.put("Version","ems_track_cn_1.0");
        headMap.put("authenticate",Api.emsauth);
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
                break;
            case R.id.tv_logistics_information://物流
                getEms();
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
    public void getData(){
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String idcard = intent.getStringExtra("idcard");
        HashMap<String, String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("idcard",idcard);
        OkhttpUtil.okHttpPost(Api.progress, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                Map<String,Object> resultMap = (Map<String,Object>) response;
                Double resultCode_d = (Double) resultMap.get("resultCode");
                int resultCode = resultCode_d.intValue();
                if(resultCode==0){
                    tvProgress.setText("未查询到记录");
                    tvProgress.setBackgroundResource(R.color.logistics_information_circle);
                }else if(resultCode==2){
                    tvProgress.setText("您所办理的业务正在处理中");
                }else {
                    tvProgress.setText("处理完成");
//                    String ems = (String) resultMap.get("ems");
//                    if(StringUtils.isNotBlank(ems)){
//                        emsNo =ems;
//                        tvLogisticsInformation.setVisibility(View.VISIBLE);
//                    }
                    tvProgress.setBackgroundResource(R.color.resolve_bg);
                }
            }
        });
    }
}
