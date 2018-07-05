package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Article;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.editSearch)
    EditText editSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_advisory);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rlSearch,R.id.tv_birthr_egistration,R.id.tv_zxhk,R.id.tv_hkqy,R.id.tv_sxbg,R.id.tv_zjbf,R.id.tv_hfhk})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_birthr_egistration://出生登记
                Intent intentBirthrEgistration = new Intent(this, AdvisoryDetailsActivity.class);
                intentBirthrEgistration.putExtra("category","1");
                startActivity(intentBirthrEgistration);
                break;
            case R.id.tv_zxhk://注销户口
                Intent intent_zxhk = new Intent(this, AdvisoryDetailsActivity.class);
                intent_zxhk.putExtra("category","2");
                startActivity(intent_zxhk);
                break;
            case R.id.tv_hkqy://户口迁移
                Intent intent_hkqy = new Intent(this, AdvisoryDetailsActivity.class);
                intent_hkqy.putExtra("category","3");
                startActivity(intent_hkqy);
                break;
            case R.id.tv_sxbg://事项变更
                Intent intent_sxbg = new Intent(this, AdvisoryDetailsActivity.class);
                intent_sxbg.putExtra("category","4");
                startActivity(intent_sxbg);
                break;
            case R.id.tv_zjbf://证件补发
                Intent intent_zjbf = new Intent(this, AdvisoryDetailsActivity.class);
                intent_zjbf.putExtra("category","5");
                startActivity(intent_zjbf);
                break;
            case R.id.tv_hfhk://恢复户口
                Intent intent_hfhk = new Intent(this, AdvisoryDetailsActivity.class);
                intent_hfhk.putExtra("category","6");
                startActivity(intent_hfhk);
                break;
        }
    }
    @OnClick({R.id.imgSearch})
    void imgSearchClick(View view) {
        String searchString = editSearch.getText().toString();
        Intent intentSearch = new Intent(this, AdvisorySearchActivity.class);
        intentSearch.putExtra("keyword",searchString);
        startActivity(intentSearch);
    }
}
