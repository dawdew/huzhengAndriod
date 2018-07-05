package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hp.householdpolicies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class BaseActivity extends Activity {
    //填充区域
//    @BindView(R.id.ly_content)
    LinearLayout lyContent;
    // 内容区域的布局
    private View contentView;
    //返回
    LinearLayout llBack;
    //首页
    LinearLayout llHomePage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        lyContent=(LinearLayout) findViewById(R.id.ly_content);
        llBack=(LinearLayout)findViewById(R.id.llBack);
        llHomePage=(LinearLayout)findViewById(R.id.llHomePage);
        MyApp.addActivity(this);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackListener();
            }
        });
        llHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHomeListener();
            }
        });

    }

    /***
     * 设置内容区域
     *
     * @param resId
     *  资源文件ID
     */
    public void setContentLayout(int resId) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(resId, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
//        contentView.setBackgroundDrawable(null);
        if (null != lyContent) {
            lyContent.addView(contentView);
        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.removeActivity(this);
    }
    /*
    返回触发点击事件
     */
    public void setBackListener() {
        finish();
    }
    /*
    首页触发点击事件
     */
    public void setHomeListener(){
        MyApp.clearActivity();
    }
}