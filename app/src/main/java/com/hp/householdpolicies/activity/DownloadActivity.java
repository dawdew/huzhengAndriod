package com.hp.householdpolicies.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.DownloadAdapter;
import com.hp.householdpolicies.adapter.OptimalPushResultsAdapter;
import com.hp.householdpolicies.customView.DownloadPopupWindown;
import com.hp.householdpolicies.model.Download;
import com.hp.householdpolicies.model.OptimalPushResult;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadActivity extends BaseActivity {
    //出生登记
    @BindView(R.id.btn_birthr_egistration)
    Button btnBirthrEgistration;
    ///注销户口
    @BindView(R.id.btn_cancel_account)
    Button btnCancelAccount;
    //户口迁移
    @BindView(R.id.btn_migration_account)
    Button btnMigrationAccount;
    //事项变更
    @BindView(R.id.btn_things_change)
    Button btnThingsChange;
    //证件补发
    @BindView(R.id.btn_certificates_reissue)
    Button btnCertificatesReissue;
    //恢复户口
    @BindView(R.id.btn_recover_account)
    Button btnRecoverAccount;
    /*
     *indext：上次选择的菜单
     *indextSelect：当前选择的菜单
     */
    int indext = 0, indextSelect = 0;
    //    按钮数组
    Button btns[];
    //列表
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private List<Download> listDownload = new ArrayList<Download>();
    Context mContext;
    private DownloadAdapter adapter;
    private DownloadPopupWindown popupWindown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_download);
        ButterKnife.bind(this);
        mContext=this;
        btns = new Button[]{btnBirthrEgistration, btnCancelAccount, btnMigrationAccount, btnThingsChange, btnCertificatesReissue, btnRecoverAccount};
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btns[0].getLayoutParams();
        layoutParams.topMargin = 0;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
        btns[0].setLayoutParams(layoutParams);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DownloadAdapter(listDownload, mContext);
        recyclerView.setAdapter(adapter);
        popupWindown=new DownloadPopupWindown(mContext);
//        adapter.OnItemClickListener(this);
        adapter.setOnItemClickListener(new DownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onDownloadClick(View view,int position,String url) {
                showLogisticsInformationWindow(view,url);

            }
        });
        AddData();
    }

    @OnClick({R.id.btn_birthr_egistration, R.id.btn_cancel_account, R.id.btn_migration_account, R.id.btn_things_change, R.id.btn_certificates_reissue, R.id.btn_recover_account})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_birthr_egistration://出生登记
                indextSelect = 0;
                break;
            case R.id.btn_cancel_account://注销户口
                indextSelect = 1;
                break;
            case R.id.btn_migration_account://户口迁移
                indextSelect = 2;
                break;
            case R.id.btn_things_change://事项变更
                indextSelect = 3;
                break;
            case R.id.btn_certificates_reissue://证件补发
                indextSelect = 4;
                break;
            case R.id.btn_recover_account://恢复户口
                indextSelect = 5;
                break;

        }
        MenuSelect();
        AddData();
    }

    //修改菜单显示状态，并修改内容显示
    void MenuSelect() {
        if (indextSelect != indext) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btns[indextSelect].getLayoutParams();
            layoutParams.topMargin = 0;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            btns[indextSelect].setLayoutParams(layoutParams);
            int top = (int) this.getResources().getDimension(R.dimen.download_btn_margin);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) btns[indext].getLayoutParams();
            layoutParams1.topMargin = top;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            btns[indext].setLayoutParams(layoutParams1);
            indext = indextSelect;
        }
    }

    private void AddData() {
        adapter.clear();
        HashMap<String, String> json_map = new HashMap<>();
        json_map.put("category",String.valueOf(indextSelect+1));
        OkhttpUtil.okHttpGet(Api.downloadList, json_map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                List<Map<String, String>> list = (List<Map<String, String>>) response;
                for(Map<String, String> m:list){
                    listDownload.add(new Download(m.get("title"),m.get("url")));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    //扫一扫下载窗口
    private void showLogisticsInformationWindow(View v,String url) {
        popupWindown.setData(Api.baseFile+url);
        popupWindown.setTouchable(true);
        popupWindown.setOutsideTouchable(true);
        popupWindown.setFocusable(true);
        popupWindown.setBackgroundDrawable(new BitmapDrawable());
        popupWindown.showAtLocation(v, Gravity.CENTER, 0, 0);

    }


}
