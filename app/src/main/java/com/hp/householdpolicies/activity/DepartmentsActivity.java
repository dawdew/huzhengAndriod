package com.hp.householdpolicies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.Honor;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DepartmentsActivity extends BaseActivity {
    //地址
    @BindView(R.id.tv_address)
    TextView tvAddress;
    //联系电话
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    //交通出行
    @BindView(R.id.tv_transportation)
    TextView tvTransportation;
    //地图
    @BindView(R.id.map)
    MapView mapView;
    //部门1
    @BindView(R.id.btnDepartment1)
    Button btnDepartment1;
    //部门2
    @BindView(R.id.btnDepartment2)
    Button btnDepartment2;
    //部门3
    @BindView(R.id.btnDepartment3)
    Button btnDepartment3;
    //部门4
    @BindView(R.id.btnDepartment4)
    Button btnDepartment4;
    /*
     *indext：上次选择的菜单
     *indextSelect：当前选择的菜单
     */
    int indext = 0, indextSelect = 0;
    //    按钮数组
    Button btns[];
    private AMap mAMap;
    private List<Map<String,String>> deptList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_departments);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        btns = new Button[]{btnDepartment1, btnDepartment2, btnDepartment3, btnDepartment4};
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btns[0].getLayoutParams();
        layoutParams.topMargin = 0;//将默认的距离上方20dp，改为0，这样底部区域全被listview填满。
        btns[0].setLayoutParams(layoutParams);
        init();

    }

    void init() {
//        Log.e("测试",(mAMap == null)+"");
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.getUiSettings().setLogoBottomMargin(-200);
            mAMap.getUiSettings().setLogoLeftMargin(-200);
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
//            setUpMap();
        }
    }
    @OnClick({R.id.btnDepartment1, R.id.btnDepartment2, R.id.btnDepartment3, R.id.btnDepartment4})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnDepartment1://部门1
                indextSelect = 0;
                break;
            case R.id.btnDepartment2://部门2
                indextSelect = 1;
                break;
            case R.id.btnDepartment3://部门3
                indextSelect = 2;
                break;
            case R.id.btnDepartment4://部门4
                indextSelect = 3;
                break;
        }
        MenuSelect();
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
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }
    private void getData(){
        OkhttpUtil.okHttpGet(Api.deptList, null, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                deptList  = (List<Map<String, String>>) response;
                Map<String, String> deptMap = deptList.get(0);
                Map<String, String> deptMap1 = deptList.get(1);
                Map<String, String> deptMap2 = deptList.get(2);
                Map<String, String> deptMap3 = deptList.get(3);
                btnDepartment1.setText(deptMap.get("name"));
                btnDepartment2.setText(deptMap1.get("name"));
                btnDepartment3.setText(deptMap2.get("name"));
                btnDepartment4.setText(deptMap3.get("name"));
                tvAddress.setText(deptMap.get("address"));
                tvPhone.setText(deptMap.get("tel"));
                tvTransportation.setText(deptMap.get("travel"));

            }
        });
    }
    public void change(View v){

    }
}
