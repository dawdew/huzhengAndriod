package com.hp.householdpolicies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.StaffFacultyNewAdapter;
import com.hp.householdpolicies.model.StaffFaculty;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StaffFacultyNewActivity extends BaseActivity {
    //警员
    @BindView(R.id.BtnConstable)
    Button BtnConstable;
    //文员
    @BindView(R.id.BtnSecretary)
    Button BtnSecretary;
    //辅警
    @BindView(R.id.BtnAuxiliaryPolice)
    Button BtnAuxiliaryPolice;
    /*
     *indext：上次选择的菜单
     *indextSelect：当前选择的菜单
     */
    int indext = 0, indextSelect = 0;
    //    按钮数组
    Button btns[];
    Context mContext;
    @BindView(R.id.rvPersonnel)
    RecyclerView rvPersonnel;
    private List<Map> listPersonnel = new ArrayList<Map>();
    private StaffFacultyNewAdapter adapterPersonnel;
    private Map<String, List<Map>> userListMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_staff_faculty_new);
        ButterKnife.bind(this);
        mContext = this;
        btns = new Button[]{BtnConstable, BtnSecretary, BtnAuxiliaryPolice};
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btns[0].getLayoutParams();
        layoutParams.leftMargin = 0;//将默认的距离上方20dp，改为0，这样底部区域全被listview填满。
        btns[0].setLayoutParams(layoutParams);
        rvPersonnel.setHasFixedSize(true);
        rvPersonnel.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapterPersonnel = new StaffFacultyNewAdapter(listPersonnel, mContext);
        rvPersonnel.setAdapter(adapterPersonnel);
        AddData();
    }

    @OnClick({R.id.BtnConstable, R.id.BtnSecretary, R.id.BtnAuxiliaryPolice})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.BtnConstable://警员
                indextSelect = 0;
                AddData("police");
                break;
            case R.id.BtnSecretary://文员
                indextSelect = 1;
                AddData("clerk");
                break;
            case R.id.BtnAuxiliaryPolice://辅警
                indextSelect = 2;
                AddData("user");
                break;
        }
        MenuSelect();
    }

    //修改菜单显示状态，并修改内容显示
    void MenuSelect() {
        if (indextSelect != indext) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btns[indextSelect].getLayoutParams();
            layoutParams.leftMargin = 0;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            btns[indextSelect].setLayoutParams(layoutParams);
            int left = (int) this.getResources().getDimension(R.dimen.activity_staff_faculty_new_btn);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) btns[indext].getLayoutParams();
            layoutParams1.leftMargin = left;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            btns[indext].setLayoutParams(layoutParams1);
            indext = indextSelect;

        }
    }
    private void AddData(String str) {
        listPersonnel = userListMap.get(str);
        adapterPersonnel.setData(listPersonnel);
    }
    private void AddData() {
        OkhttpUtil.okHttpGet(Api.staff, null, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                userListMap = (Map<String, List<Map>>) response;
                listPersonnel = userListMap.get("police");
                adapterPersonnel.setData(listPersonnel);
            }
        });
    }
}
