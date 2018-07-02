package com.hp.householdpolicies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Download;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuggestionActivity extends BaseActivity{
    //姓名
    @BindView(R.id.edtName)
    EditText edtName;
    //选择组
    @BindView(R.id.rg)
    RadioGroup rg;
    //内容
    @BindView(R.id.edtContent)
    EditText edtContent;
    //联系方式
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    //提交
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    /*
     * rbPraise:表扬
     * rbOpinion:意见
     * rbSuggest建议
     */
    private int RbId[]={R.id.rbPraise,R.id.rbOpinion,R.id.rbSuggest};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_suggestion);
        ButterKnife.bind(this);
        rg.check(RbId[0]);
    }
    @OnClick({R.id.btnSubmit})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit://提交
                // 获取单选框选择的值
                int radioButtonId = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) SuggestionActivity.this.findViewById(radioButtonId);
               String str = rb.getText().toString();
                Log.e("ceshi","选择："+str);
                submit(str);
                break;
        }

        }
    private void submit(String str){
        HashMap<String, String> json_map = new HashMap<>();
        json_map.put("type",str);
        json_map.put("name",edtName.getText().toString());
        json_map.put("content",edtContent.getText().toString());
        json_map.put("phone",edtPhone.getText().toString());
        OkhttpUtil.okHttpPost(Api.advice, json_map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {

            }
        });
    }
}
