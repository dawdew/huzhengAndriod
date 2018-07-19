package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Article;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import org.xutils.ImageManager;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AcceptRangeTaskNumberActivity extends BaseActivity {
    @BindView(R.id.imgBtnLeft)
    ImageButton imgBtnLeft;//左侧按钮
    @BindView(R.id.imgBtnRight)
    ImageButton imgBtnRight;//右侧按钮
    @BindView(R.id.tvDescribe)
    TextView tvDescribe;//描述
    @BindView(R.id.tvTitle)
    TextView tvTitle;//标题
    @BindView(R.id.img)
    ImageView img;//图片
    ImageManager image = x.image();
    private List<Map<String, String>> hallList;
    private Integer index =-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_accept_range_task_number);
        ButterKnife.bind(this);
        AddData();
    }
    private void AddData(){
        Intent intent = getIntent();
        HashMap<String, String> map = new HashMap<>();
        map.put("type",intent.getStringExtra("type"));
        OkhttpUtil.okHttpGet(Api.hall, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                hallList = (List<Map<String, String>>) response;
                if(hallList.size()>0){
                    Map<String, String> map = hallList.get(0);
                    tvTitle.setText(map.get("title"));
                    tvDescribe.setText(map.get("desc"));
                    image.bind(img,Api.downurl+map.get("src"));
                    index=0;
                }
            }
        });
    }
    @OnClick({R.id.imgBtnLeft,R.id.imgBtnRight})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnLeft:
                if(index>0){
                    --index;
                    Map<String, String> map = hallList.get(index);
                    tvTitle.setText(map.get("title"));
                    tvDescribe.setText(map.get("desc"));
                    image.bind(img,Api.downurl+map.get("src"));
                }
                break;
            case R.id.imgBtnRight:
                if(index<hallList.size()-1){
                    index++;
                    Map<String, String> map = hallList.get(index);
                    tvTitle.setText(map.get("title"));
                    tvDescribe.setText(map.get("desc"));
                    image.bind(img,Api.downurl+map.get("src"));
                }
                break;
        }
    }
}
