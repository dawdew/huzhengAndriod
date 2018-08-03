package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import com.hp.householdpolicies.activity.department.DepartmentsOneActivity;
import com.hp.householdpolicies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SynopsisActivity extends BaseActivity {
    //大厅受理范围
    @BindView(R.id.ll_accept_range)
    LinearLayout llAcceptRange;
    //荣誉展示
    @BindView(R.id.ll_honor)
    LinearLayout llHonor;
    //人员介绍
    @BindView(R.id.ll_staff_faculty)
    LinearLayout llStaffFaculty;
    //相关部门信息
    @BindView(R.id.ll_departments)
    LinearLayout llDepartments;
    //synopsis_tv
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_synopsis);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_accept_range,R.id.ll_honor,R.id.ll_staff_faculty,R.id.ll_departments})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_accept_range://大厅受理范围
                Intent intentAcceptRange=new Intent(this,AcceptRangeActivity.class);
                startActivity(intentAcceptRange);
                break;
            case R.id.ll_honor://荣誉展示
                Intent intentHonor=new Intent(this,HonorActivity.class);
                startActivity(intentHonor);
                break;
            case R.id.ll_staff_faculty://人员介绍
                Intent intentStaffFaculty=new Intent(this,StaffFacultyNewActivity.class);
                startActivity(intentStaffFaculty);
                break;
            case R.id.ll_departments://相关部门信息
                Intent intentDepartments=new Intent(this,DepartmentsOneActivity.class);
                startActivity(intentDepartments);
                break;

        }

    }
}
