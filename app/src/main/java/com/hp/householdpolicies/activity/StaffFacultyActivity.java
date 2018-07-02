package com.hp.householdpolicies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.StaffFacultyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffFacultyActivity extends BaseActivity {
    //组长照片
    @BindView(R.id.img_group_leader)
    ImageView imgGroupLeader;
    //组长姓名
    @BindView(R.id.tv_group_leader)
    TextView tvGroupLeader;
    //副组长照片
    @BindView(R.id.img_pendragon)
    ImageView imgPendragon;
    //副组长姓名
    @BindView(R.id.tv_pendragon)
    TextView tvPendragon;
    //值班人员列表
    @BindView(R.id.rvPersonnel)
    RecyclerView rvPersonnel;
    //主任照片
    @BindView(R.id.img_director)
    ImageView imgDirector;
    //主任姓名
    @BindView(R.id.tv_director)
    TextView tvDirector;
    //副主任照片
    @BindView(R.id.img_vice_director)
    ImageView imgViceDirector;
    //副主任姓名
    @BindView(R.id.tv_vice_director)
    TextView tvViceDirector;
    //值班领导列表
    @BindView(R.id.rvLeader)
    RecyclerView rvLeader;
    //值班人员数据
    private List<String> listPersonnel = new ArrayList<String>();
    private StaffFacultyAdapter adapterPersonnel;
    //值班领导数据
    private List<String> listLeader = new ArrayList<String>();
    private StaffFacultyAdapter adapterLeader;
    Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_staff_faculty);
        ButterKnife.bind(this);
        mContext = this;
        rvPersonnel.setHasFixedSize(true);
        rvPersonnel.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapterPersonnel = new StaffFacultyAdapter(listPersonnel, mContext);
        rvPersonnel.setAdapter(adapterPersonnel);
        rvLeader.setHasFixedSize(true);
        rvLeader.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapterLeader = new StaffFacultyAdapter(listLeader, mContext);
        rvLeader.setAdapter(adapterLeader);
        AddData();
    }

    private void AddData() {
        for (int i = 0; i < 12; i++) {
            listPersonnel.add (i+ "");
            listLeader.add(i+ "");
        }
        adapterPersonnel.setData(listPersonnel);
        adapterLeader.setData(listLeader);
    }
}
