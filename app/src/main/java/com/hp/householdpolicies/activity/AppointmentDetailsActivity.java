package com.hp.householdpolicies.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.adapter.AppointmentDetailsAdapter;
import com.hp.householdpolicies.adapter.AppointmentDetailsDateAdapter;
import com.hp.householdpolicies.customView.SpinnerPopupWindown;
import com.hp.householdpolicies.model.AppDate;
import com.hp.householdpolicies.model.Article;
import com.hp.householdpolicies.model.Police;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @BindView(R.id.rg)
    public RadioGroup rg;
    private List<String> listBusiness = new ArrayList<String>();
    private List<String> listTime = new ArrayList<String>();
    private Set<String> userAppDay = new HashSet<String>();
    private Map<String,String> appDay;
    private Map<String,String> appDayNext;
    private List<Police> listPolice = new ArrayList<Police>();
    private Context mContext;
    private AppointmentDetailsAdapter adapter;
    /*
     *spIndext：0-选择业务
     *spIndext：1-选择时间
     */
    private int spIndext = 0;
    private Date day;
    private int checkedRadio=-1;
    private List<AppDate> listDate = new ArrayList<AppDate>();
    private AppointmentDetailsDateAdapter dateAdapter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        adapter = new AppointmentDetailsAdapter(listPolice, mContext);
        rvSalesman.setAdapter(adapter);
        rvDate.setHasFixedSize(true);
        rvDate.setLayoutManager(new GridLayoutManager(mContext, 7));
        dateAdapter = new AppointmentDetailsDateAdapter(listDate, mContext);
        rvDate.setAdapter(dateAdapter);
        adapter.setOnItemClickListener(new AppointmentDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击人员事件
//                Toast.makeText(mContext, "点击" + listPolice.get(position).getName() + position, Toast.LENGTH_SHORT).show();
                Police police = listPolice.get(position);
                userAppDay.clear();
                String id = police.getId();
                    for (String day : appDay.keySet()) {
                        String userid = appDay.get(day);
                        if (userid.equals(id)) {
                            userAppDay.add(day);
                        }
                    }
                    if(appDayNext!=null){
                        for (String day : appDayNext.keySet()) {
                            String userid = appDayNext.get(day);
                            if (userid.equals(id)) {
                                userAppDay.add(day);
                            }
                        }
                    }
                    ArrayList<AppDate> dest = new ArrayList<>();
                    for(AppDate ad:listDate){
                        dest.add(new AppDate(ad.getDateStr(),ad.getDay(),ad.getAvailable()));
                    }
                    for (AppDate ad : dest) {
                        if (ad.getAvailable()) {
                            String dateStr = ad.getDateStr();
                            if (!userAppDay.contains(dateStr)) {
                                ad.setAvailable(false);
                                ad.setDateStr("");
                                ad.setDay(null);
                            }
                        }
                    }
                    dateAdapter.setData(dest);
                    dateAdapter.notifyDataSetChanged();
                    RadioGroupUnVisible();
                    tvTime.setVisibility(View.VISIBLE);
                btnAffirm.setBackgroundResource(R.mipmap.ic_gray_bg);
                btnAffirm.setEnabled(false);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        dateAdapter.setOnItemClickListener(new AppointmentDetailsDateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,AppDate appDate) {
                TextView view1 = (TextView) view;
                String selectedDate = view1.getText().toString();
                String id = appDay.get(selectedDate);
                if(StringUtils.isBlank(id)){
                    id = appDayNext.get(selectedDate);
                }
                adapter.setSelected(id);
                adapter.notifyDataSetChanged();

                day = appDate.getDay();
                getAppointment(id,day);
                btnAffirm.setBackgroundResource(R.mipmap.ic_gray_bg);
                btnAffirm.setEnabled(false);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                checkedRadio =checkedId;
                btnAffirm.setBackgroundResource(R.mipmap.ic_blue_bg);
                btnAffirm.setEnabled(true);
//                Toast.makeText(mContext, "点击"  + checkedId, Toast.LENGTH_SHORT).show();
            }
        });
        AddData();
        hideBottomUIMenu();
    }

    @OnClick({R.id.llBack, R.id.llHomePage, R.id.tvBusiness, R.id.tvTime,R.id.btnReset})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.llBack://返回
                finish();
                break;
            case R.id.llHomePage://首页
                MyApp.clearActivity();
                break;
//            case R.id.tvBusiness://选择业务
//                spIndext = 0;
//                mSpinerPopWindow.setData(listBusiness);
//                showSpinWindow(tvBusiness);
//                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }

    private void AddData() {
//        listBusiness.add("业务一");
//        listBusiness.add("业务二");
//        listBusiness.add("业务三");
//        listBusiness.add("业务四");
//        listBusiness.add("业务五");
//        tvBusiness.setText(listBusiness.get(0));
//        mSpinerPopWindow.setData(listBusiness);
//        adapter.notifyDataSetChanged();
//        listTime.add("08:00~10:00");
//        listTime.add("10:00~12:00");
//        listTime.add("14:00~16:00");
//        listTime.add("16:00~18:00");
//        tvTime.setText(listTime.get(0));
        getStaff();
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

        //当天是最后一周的情况，算出下月的
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastday_of_month = calendar.get(calendar.DAY_OF_MONTH);
        if(day_of_month==lastday_of_month){
            day_of_month=1;
        }else {
            day_of_month++;
        }
        for (int i=0;i<day_of_week;i++){
            AppDate appDate = new AppDate();
            appDate.setDateStr("");
            appDate.setAvailable(false);
            listDate.add(appDate);
        }
        for (int i=0;i<7;i++){
            AppDate appDate = new AppDate();
            calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
            appDate.setDay(calendar.getTime());
            appDate.setDateStr(day_of_month.toString());
            appDate.setAvailable(true);
            if(day_of_month<lastday_of_month){
                day_of_month++;
            }else {
                day_of_month=1;
            }
                listDate.add(appDate);
        }
        for (int i=0;i<=7-day_of_week;i++){
            AppDate appDate = new AppDate();
            appDate.setDateStr("");
            appDate.setAvailable(false);
            listDate.add(appDate);
        }
        dateAdapter.notifyDataSetChanged();
    }
    void getStaff(){
        OkhttpUtil.okHttpGet(Api.yyStaff, null, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                Map<String, Object>  res= (Map<String, Object>) response;
                appDay = (Map<String, String>) res.get("appDay");
                if(res.containsKey("appDayNext")){
                    appDayNext = (Map<String, String>) res.get("appDayNext");
                }
                List<Map<String, String>> list = (List<Map<String, String>>) res.get("userList");
                for(int i=0;i<list.size();i++){
                    Map<String, String> m = list.get(i);
                    listPolice.add(new Police(m.get("id"),m.get("realName"),Api.downurl+m.get("photo"),m.get("pno"),i+1));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    void getAppointment(String id,Date date){
        HashMap<String, String> map = new HashMap<>();
        map.put("id",id);

        map.put("date",simpleDateFormat.format(date));
        OkhttpUtil.okHttpGet(Api.arrange, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                Map<String, Object>  res= (Map<String, Object>) response;
                RadioGroupClean();
                if(res.containsKey("1") && res.containsKey("2") && res.containsKey("3") && res.containsKey("4")){
                    RadioGroupUnVisible();
                    tvTime.setVisibility(View.VISIBLE);
                }else {
                    if(!res.containsKey("1")){
                        addRadioButton("08:00~10:00",1);
                    }
                    if(!res.containsKey("2")){
                        addRadioButton("10:00~12:00",2);
                    }
                    if(!res.containsKey("3")){
                        addRadioButton("14:00~16:00",3);
                    }
                    if(!res.containsKey("4")){
                        addRadioButton("16:00~18:00",4);
                    }
                    RadioGroupVisible();
                }
            }
        });
    }
    @SuppressLint("ResourceType")
    void addRadioButton(String text,Integer id){
        RadioButton radioButton = new RadioButton(AppointmentDetailsActivity.this);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,0,0,5);
        radioButton.setLayoutParams(layoutParams);
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);
        radioButton.setText(text);
        radioButton.setId(id);
        rg.addView(radioButton);
    }
    void RadioGroupVisible(){
        tvTime.setVisibility(View.GONE);
        rg.setVisibility(View.VISIBLE);
    }
    void RadioGroupUnVisible(){
        rg.setVisibility(View.GONE);
    }
    void RadioGroupClean(){
        rg.removeAllViews();
    }
    public void reset(){
        day=null;
        checkedRadio=-1;
        dateAdapter.setmPosition(-1);
        dateAdapter.setData(listDate);
        adapter.setSelected("");
        adapter.notifyDataSetChanged();
        RadioGroupClean();
        RadioGroupUnVisible();
        tvTime.setVisibility(View.VISIBLE);
        btnAffirm.setBackgroundResource(R.mipmap.ic_gray_bg);
        btnAffirm.setEnabled(false);
    }
    public void submit(View v){
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String name = intent.getStringExtra("name");
        HashMap<String, String> map = new HashMap<>();
        if(StringUtils.isNotBlank(adapter.getSelected()) && adapter.getSelectedPosition()!=-1){
            map.put("userId",adapter.getSelected());
            map.put("userName",listPolice.get(adapter.getSelectedPosition()).getName());
        }else {
            LemonBubble.showError(AppointmentDetailsActivity.this, "请选择办理人", 2000);
            return;
        }
        if(day!=null){
            map.put("atime",simpleDateFormat.format(day));
        }else {
            LemonBubble.showError(AppointmentDetailsActivity.this, "请选择办理日期", 2000);
            return;
        }
        if(checkedRadio!=-1){
            map.put("part",String.valueOf(checkedRadio));
        }else {
            LemonBubble.showError(AppointmentDetailsActivity.this, "请选择预约时间段", 2000);
            return;
        }
        map.put("customerName",name);
        map.put("customerPhone",phone);
        OkhttpUtil.okHttpPost(Api.postArrange, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                btnAffirm.setEnabled(false);
                LemonBubble.showRight(AppointmentDetailsActivity.this, "预约成功", 2000);
            }
        });
    }

}
