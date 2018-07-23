package com.hp.householdpolicies.activity.department;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.activity.BaseActivity;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentsDetailActivity extends BaseActivity {
    //搜索录入框
    @BindView(R.id.edtSearch)
    EditText edtSearch;
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
    private AMap mAMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_departments_detail);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        init();
    }
    void init() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.getUiSettings().setLogoBottomMargin(-200);
            mAMap.getUiSettings().setLogoLeftMargin(-200);
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
//            setUpMap();
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

    @Override
    protected void onStart() {
        getData();
        super.onStart();
    }
    private void getData(){
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        String name = intent.getStringExtra("name");

        String phone = intent.getStringExtra("tel");

        String transportation = intent.getStringExtra("travel");

        String latlng = intent.getStringExtra("latlng");
        tvAddress.setText("地址："+address);
        tvPhone.setText("联系电话："+phone);
        tvTransportation.setText("交通出行："+transportation);
        if(StringUtils.isNotBlank(latlng)){
            String[] split = StringUtils.split(latlng,",");
            LatLng latLng = new LatLng(Double.parseDouble(split[1]),Double.parseDouble(split[0]));
            MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .position(latLng)
                    //其中经纬度可以在定位回调成功函数  onLocationChanged中获取
                    .title(name)
                    .snippet(address)
                    //一般可以显示当前位置的详细信息
                    .draggable(false);
            mAMap.addMarker(markerOption);
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
    }
}
