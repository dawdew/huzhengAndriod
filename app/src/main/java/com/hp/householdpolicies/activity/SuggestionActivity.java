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
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuggestionActivity extends BaseActivity implements Validator.ValidationListener{
    //姓名
    @BindView(R.id.edtName)
    @NotEmpty(messageResId = R.string.errorMessage)
    @Order(1)
    EditText edtName;
    //选择组
    @BindView(R.id.rg)
    RadioGroup rg;
    //内容
    @BindView(R.id.edtContent)
    @NotEmpty(messageResId = R.string.errorMessage)
    @Order(3)
    EditText edtContent;
    //联系方式
    @BindView(R.id.edtPhone)
    @NotEmpty(messageResId = R.string.errorMessage)
    @Order(4)
    EditText edtPhone;
    //提交
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    Validator validator;
    Boolean verify=false;
    /*
     * rbPraise:表扬
     * rbOpinion:意见
     * rbSuggest建议
     */
    private int RbId[]={R.id.rbPraise, R.id.rbOpinion, R.id.rbSuggest,R.id.rbComplain};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_suggestion);
        ButterKnife.bind(this);
        rg.check(RbId[0]);
        validator = new Validator(this);
        validator.setValidationListener(this);
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
        validator.validate();
        if(verify){
            HashMap<String, String> json_map = new HashMap<>();
            json_map.put("type",str);
            json_map.put("name",edtName.getText().toString());
            json_map.put("content",edtContent.getText().toString());
            json_map.put("phone",edtPhone.getText().toString());
            edtName.setText(null);
            edtContent.setText(null);
            edtPhone.setText(null);
            OkhttpUtil.okHttpPost(Api.advice, json_map, new ApiCallBack() {
                @Override
                public void onResponse(Object response) {
                    LemonBubble.showRight(SuggestionActivity.this, "提交成功！", 2000);
                }
            });
        }
    }

    @Override
    public void onValidationSucceeded() {
        verify=true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}
