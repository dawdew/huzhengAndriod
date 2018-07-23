package com.hp.householdpolicies.activity.department;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.activity.BaseActivity;
import com.hp.householdpolicies.adapter.DepartmentsOneAdapter;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.OkhttpUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DepartmentsOneActivity extends BaseActivity {
    //搜索框
    @BindView(R.id.rlSearch)
    RelativeLayout rlSearch;
    @BindView(R.id.tvSearch)
    EditText tvSearch;
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
    //左侧按钮
    @BindView(R.id.imgBtnLeft)
    ImageButton imgBtnLeft;
    //右侧按钮
    @BindView(R.id.imgBtnRight)
    ImageButton imgBtnRight;
    //列表
    @BindView(R.id.list)
    RecyclerView list;
    /*
     *indext：上次选择的菜单
     *indextSelect：当前选择的菜单
     */
    int indext = 0, indextSelect = 0;
    //    按钮数组
    Button btns[];
    private List<Map<String,String>> listContent = new ArrayList<Map<String,String>>();
    Context mContext;
    private DepartmentsOneAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_departments_one);
        ButterKnife.bind(this);
        btns = new Button[]{btnDepartment1, btnDepartment2, btnDepartment3, btnDepartment4};
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btns[0].getLayoutParams();
        layoutParams.topMargin = 0;//将默认的距离上方20dp，改为0，这样底部区域全被listview填满。
        btns[0].setLayoutParams(layoutParams);
        mContext = this;
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DepartmentsOneAdapter(listContent, mContext);
        list.setAdapter(adapter);
        AddData("0");
        adapter.setOnItemClickListener(new DepartmentsOneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,Map<String,String> item) {
//                Toast.makeText(mContext,"点击"+position,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,DepartmentsDetailActivity.class);
                intent.putExtra("address",item.get("address"));
                intent.putExtra("name",item.get("name"));
                intent.putExtra("tel",item.get("tel"));
                intent.putExtra("travel",item.get("travel"));
                intent.putExtra("latlng",item.get("latlng"));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onDownloadClick(View view, int position) {

            }
        });
    }

    @OnClick({R.id.rlSearch, R.id.btnDepartment1, R.id.btnDepartment2, R.id.btnDepartment3, R.id.btnDepartment4,R.id.imgBtnLeft,R.id.imgBtnRight})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnDepartment1://部门1
                indextSelect = 0;
                MenuSelect();
                AddData(String.valueOf(indextSelect));
                break;
            case R.id.btnDepartment2://部门2
                indextSelect = 1;
                MenuSelect();
                AddData(String.valueOf(indextSelect));
                break;
            case R.id.btnDepartment3://部门3
                indextSelect = 2;
                MenuSelect();
                AddData(String.valueOf(indextSelect));
                break;
            case R.id.btnDepartment4://部门4
                indextSelect = 3;
                MenuSelect();
                AddData(String.valueOf(indextSelect));
                break;
            case R.id.rlSearch://查询
                String searchText = tvSearch.getText().toString();
                if(StringUtils.isNotBlank(searchText)){
                    Intent intentSearch=new Intent(mContext,DepartmentsSearchActivity.class);
                    intentSearch.putExtra("name",searchText);
                    startActivity(intentSearch);
                }
                break;
             case R.id.imgBtnLeft:
                 if(indextSelect!=0){
                     indextSelect--;
                     MenuSelect();
                     AddData(String.valueOf(indextSelect));
                 }
                 break;
            case R.id.imgBtnRight:
                if(indextSelect!=3){
                    indextSelect++;
                    MenuSelect();
                    AddData(String.valueOf(indextSelect));
                }
                break;
        }

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
    private void AddData(String category) {
        HashMap<String, String> map = new HashMap<>();
        map.put("category",category);
        OkhttpUtil.okHttpGet(Api.deptList, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                listContent = (List<Map<String, String>>) response;
                adapter.setData(listContent);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
