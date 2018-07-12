package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.AppointmentDetailsAdapter;
import com.hp.householdpolicies.adapter.AppointmentDetailsDateAdapter;
import com.hp.householdpolicies.customView.SpinnerPopupWindown;
import com.hp.householdpolicies.model.AppDate;
import com.hp.householdpolicies.model.Salesman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentDetailsActivity extends Activity implements SpinnerPopupWindown.IOnItemSelectListener {
    //返回
    @BindView(R.id.llBack)
    LinearLayout llBack;
    //首页
    @BindView(R.id.llHomePage)
    LinearLayout llHomePage;
    //重置
    @BindView(R.id.btnReset)
    Button btnReset;
    //确认预约
    @BindView(R.id.btnAffirm)
    Button btnAffirm;
    //选择业务
    @BindView(R.id.tvBusiness)
    TextView tvBusiness;
    SpinnerPopupWindown mSpinerPopWindow;
    //人员
    @BindView(R.id.rvSalesman)
    RecyclerView rvSalesman;
    //预约时间
    @BindView(R.id.tvTime)
    TextView tvTime;
    //预约日期
    @BindView(R.id.rvDate)
    RecyclerView rvDate;
    private List<String> listBusiness = new ArrayList<String>();
    private List<String> listTime = new ArrayList<String>();
    private List<Salesman> listSaleman = new ArrayList<Salesman>();
    private Context mContext;
    private AppointmentDetailsAdapter adapter;
    /*
     *spIndext：0-选择业务
     *spIndext：1-选择时间
     */
    private int spIndext = 0;
    private TextView times[];
    private int timesID[];
    private List<AppDate> listDate = new ArrayList<AppDate>();
    private AppointmentDetailsDateAdapter dateAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        ButterKnife.bind(this);
        mContext = this;
        mSpinerPopWindow = new SpinnerPopupWindown(this, listBusiness);
        mSpinerPopWindow.setItemListener(this);
        rvSalesman.setHasFixedSize(true);
        rvSalesman.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter = new AppointmentDetailsAdapter(listSaleman, mContext);
        rvSalesman.setAdapter(adapter);
        rvDate.setHasFixedSize(true);
        rvDate.setLayoutManager(new GridLayoutManager(mContext, 7));
        dateAdapter = new AppointmentDetailsDateAdapter(listDate, mContext);
        rvDate.setAdapter(dateAdapter);
        adapter.setOnItemClickListener(new AppointmentDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击人员事件
                Toast.makeText(mContext, "点击" + listSaleman.get(position).getName() + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        dateAdapter.setOnItemClickListener(new AppointmentDetailsDateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView view1 = (TextView) view;
                Toast.makeText(mContext, "点击"  + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        AddData();
        hideBottomUIMenu();
    }

    @OnClick({R.id.llBack, R.id.llHomePage, R.id.tvBusiness, R.id.tvTime})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.llBack://返回
                finish();
                break;
            case R.id.llHomePage://首页
                MyApp.clearActivity();
                break;
            case R.id.tvBusiness://选择业务
                spIndext = 0;
                mSpinerPopWindow.setData(listBusiness);
                showSpinWindow(tvBusiness);
                break;
            case R.id.tvTime://预约时间
                spIndext = 1;
                mSpinerPopWindow.setData(listTime);
                showSpinWindow(tvTime);
                break;
        }
    }

    private void AddData() {
        listBusiness.add("业务一");
        listBusiness.add("业务二");
        listBusiness.add("业务三");
        listBusiness.add("业务四");
        listBusiness.add("业务五");
        tvBusiness.setText(listBusiness.get(0));
        mSpinerPopWindow.setData(listBusiness);
        listSaleman.add(new Salesman("曹天瑞", "12000abC"));
        listSaleman.add(new Salesman("曹天瑞", "12000abC"));
        listSaleman.add(new Salesman("曹天瑞", "12000abC"));
        listSaleman.add(new Salesman("曹天瑞", "12000abC"));
        listSaleman.add(new Salesman("曹天瑞", "12000abC"));
        listSaleman.add(new Salesman("曹天瑞", "12000abC"));
        adapter.notifyDataSetChanged();
        listTime.add("08:00~10:00");
        listTime.add("10:00~12:00");
        listTime.add("14:00~16:00");
        listTime.add("16:00~18:00");
        tvTime.setText(listTime.get(0));
        getDate();
    }

    //下拉选择窗口显示
    private void showSpinWindow(View v) {
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v, 0, 10);
        mSpinerPopWindow.setFocusable(true);
        mSpinerPopWindow.update();

    }

    @Override
    public void onItemClick(int pos) {
        switch (spIndext) {
            case 0:
                tvBusiness.setText(listBusiness.get(pos));
                break;
            case 1:
                tvTime.setText(listTime.get(pos));
                break;
        }

    }
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    void getDate(){
        Calendar calendar = Calendar.getInstance();
        Integer day_of_week = calendar.get(calendar.DAY_OF_WEEK);
        Integer day_of_month = calendar.get(calendar.DAY_OF_MONTH);
        for (int i=0;i<day_of_week-1;i++){
            AppDate appDate = new AppDate();
            appDate.setDateStr("");
            appDate.setAvailable(false);
            listDate.add(appDate);
        }
        for (int i=0;i<=14-day_of_week;i++){
            AppDate appDate = new AppDate();
            appDate.setDateStr(day_of_month.toString());
            appDate.setAvailable(true);
            day_of_month++;
                listDate.add(appDate);
        }
        dateAdapter.notifyDataSetChanged();
    }
}
