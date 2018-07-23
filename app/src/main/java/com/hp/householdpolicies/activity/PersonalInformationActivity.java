package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.customView.ChosenTimePopupWindown;
import com.hp.householdpolicies.customView.SpinnerPopupWindown;
import com.hp.householdpolicies.utils.BaseMethod;
import com.hp.householdpolicies.utils.DateUtils;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;
import com.synjones.idcard.IDCardInfo;
import com.synjones.idcard.OnIDListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class PersonalInformationActivity extends BaseActivity implements SpinnerPopupWindown.IOnItemSelectListener, ChosenTimePopupWindown.IChosenListener {
    //    可扫描二维码
    @BindView(R.id.llScan)
    TextView llScan;
    //    基础信息
    @BindView(R.id.ll_basic_information)
    LinearLayout llBasicInformation;
    //    婚姻状况
    @BindView(R.id.ll_marital_status)
    LinearLayout llMaritalStatus;
    //    有无子女
    @BindView(R.id.ll_children)
    LinearLayout llChildren;
    //    房产信息
    @BindView(R.id.ll_house_info)
    LinearLayout llHouseInfo;
    /*
indext：上次选择的菜单
indextSelect：当前选择的菜单
     */
    int indext = 0, indextSelect = 0;
    //菜单数组
    LinearLayout llMenu[];
    //下一步
    @BindView(R.id.textNext)
    TextView textNext;
    //基础信息
    @BindView(R.id.content_basic_information)
    LinearLayout contentBasicInformation;
    //婚姻状况
    @BindView(R.id.content_marital_status)
    LinearLayout contentMaritalStatus;
    //有无子女
    @BindView(R.id.content_children)
    LinearLayout contentChildren;
    //房产信息
    @BindView(R.id.content_house_info)
    LinearLayout contentHouseInfo;
    //内容数组
    LinearLayout llContent[];
    SpinnerPopupWindown mSpinerPopWindow;
    ChosenTimePopupWindown timePopupWindown;
    //性别
    @BindView(R.id.tvSex)
    TextView tvSex;
    //学历
    @BindView(R.id.tvEducation)
    TextView tvEducation;
    //婚姻状况
    @BindView(R.id.tvMarriage)
    TextView tvMarriage;
    //有无子女
    @BindView(R.id.tvChildren)
    TextView tvChildren;
    //房产信息
    @BindView(R.id.tvHouseProperty)
    TextView tvHouseProperty;
    //出生日期
    @BindView(R.id.tvDateBirth)
    TextView tvDateBirth;
    //姓名
    @BindView(R.id.edtName)
    EditText edtName;
    //身份证号
    @BindView(R.id.edtIDNumber)
    EditText edtIDNumber;
    //户籍地
    @BindView(R.id.edtAddress)
    EditText edtAddress;
    //现住地
    @BindView(R.id.edtResidence)
    EditText edtResidence;
    //工作单位
    @BindView(R.id.edtworkUnit)
    EditText edtworkUnit;
    //年龄
    @BindView(R.id.edtAge)
    EditText edtAge;
    //性别
    private List<String> listSex = new ArrayList<String>();
    //学历
    private List<String> listEducation = new ArrayList<String>();
    //婚姻状况
    private List<String> listMarriage = new ArrayList<String>();
    //有无子女
    private List<String> listChildren = new ArrayList<String>();
    //房产信息
    private List<String> listHouseProperty = new ArrayList<String>();
    //下拉选择信息
    private List<String> listValue = new ArrayList<String>();
    /*
    下拉菜单
    0：性别
    1：学历
    2：婚姻状况
    3：有无子女
    4：房产信息
     */
    int spIndext = 0;
    private ConnectServer cs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        llMenu = new LinearLayout[]{llBasicInformation, llMaritalStatus, llChildren, llHouseInfo};
        llContent = new LinearLayout[]{contentBasicInformation, contentMaritalStatus, contentChildren, contentHouseInfo};
        llMenu[indext].setSelected(true);
        llContent[indext].setVisibility(View.VISIBLE);
        textNext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        AddList();
        mSpinerPopWindow = new SpinnerPopupWindown(this, listValue);
        timePopupWindown = new ChosenTimePopupWindown(this);
        mSpinerPopWindow.setItemListener(this);
        timePopupWindown.setItemListener(this);
        tvDateBirth.setText(BaseMethod.getDateNoW());
    }
    @Override
    protected void onStart() {
//        cs = ConnectServer.getInstance(getApplication(), impl);
        super.onStart();
    }
    private RscServiceConnectionImpl impl = new RscServiceConnectionImpl() {
        public void onServiceConnected(int name) {
            if (cs == null)
                return;
            if (name == ConnectServer.Connect_Pr_Id) {
                cs.registerIDListener(Ilistener);
            }
        }

        public void onServiceDisconnected(int name) {
            System.out.println("onServiceDisconnected......");
        }
    };
    private OnIDListener Ilistener = new OnIDListener.Stub() {

        @Override
        public void onResult(IDCardInfo info, byte[] photo)
                throws RemoteException {
            Log.e("ID",
                    "name: " + info.getName() + ",nation: " + info.getNation()
                            + ",birthday: " + info.getBirthday() + ",sex: "
                            + info.getSex() + ",address: " + info.getAddress()
                            + ",append: " + info.getAppendAddress()
                            + ",fpname: " + info.getFpName() + ",grantdept: "
                            + info.getGrantdept() + ",idcardno: "
                            + info.getIdcardno() + ",lifebegin: "
                            + info.getUserlifebegin() + ",lifeend: "
                            + info.getUserlifeend());
            if (photo != null)
                Log.e("ID", "photo: " + photo.length);
            else
                Log.e("ID", "photo=null");
            Bundle bundle = new Bundle();
            bundle.putParcelable("info", info);
            bundle.putByteArray("photo",photo);
            Message msg = mHandler.obtainMessage();
            msg.setData(bundle);
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };
    private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean  handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle0 = msg.getData();
                    if (bundle0 == null)
                        return false;
                    IDCardInfo info = (IDCardInfo) bundle0.get("info");
                    byte[] photos = bundle0.getByteArray("photo");
                    //TODO
                    edtName.setText(info.getName());
                    tvSex.setText(info.getSex());
                    edtIDNumber.setText(info.getIdcardno());
                    edtAddress.setText(info.getAddress());
                    Date yyyyMMdd = DateUtils.parse(info.getBirthday(), "yyyyMMdd");
                    tvDateBirth.setText(DateUtils.format(yyyyMMdd,"yyyy-MM-dd"));
                    int age = DateUtils.getAge(yyyyMMdd);
                    edtAge.setText(String.valueOf(age));
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onStop() {
        cs.release();
        super.onStop();
    }
    private void AddList() {
        //性别
        listSex.add("男");
        listSex.add("女");
        tvSex.setText(listSex.get(0));
        //学历
        listEducation.add("大专");
        listEducation.add("本科");
        listEducation.add("硕士");
        listEducation.add("博士");
        listEducation.add("其他");
        tvEducation.setText(listEducation.get(0));
        //婚姻状况
        listMarriage.add("未婚");
        listMarriage.add("已婚");
        tvMarriage.setText(listMarriage.get(0));
        //有无子女
        listChildren.add("有");
        listChildren.add("无");
        tvChildren.setText(listChildren.get(0));
        //房产信息
        listHouseProperty.add("有");
        listHouseProperty.add("无");
        tvHouseProperty.setText(listHouseProperty.get(0));

    }

    @OnClick({R.id.ll_basic_information, R.id.ll_marital_status, R.id.ll_children, R.id.ll_house_info, R.id.textNext, R.id.tvSex, R.id.tvEducation, R.id.tvMarriage, R.id.tvChildren, R.id.tvHouseProperty, R.id.tvDateBirth})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_basic_information://基础信息
                indextSelect = 0;
                MenuSelect();
                break;
            case R.id.ll_marital_status://婚姻状况
                indextSelect = 1;
                MenuSelect();
                break;
            case R.id.ll_children://有无子女
                indextSelect = 2;
                MenuSelect();
                break;
            case R.id.ll_house_info://房产信息
                indextSelect = 3;
                MenuSelect();
                break;
            case R.id.textNext://下一步
                if (indextSelect == 3) {
//                    indextSelect = 0;
                    Intent intent=new Intent(this,OptimalPushResultsActivity.class);
                    putExtra(intent);
                    startActivity(intent);
                } else {
                    indextSelect++;
                }
                MenuSelect();
                break;
            case R.id.tvSex://性别下拉选择
                spIndext = 0;
                mSpinerPopWindow.setData(listSex);
                showSpinWindow(tvSex);
                break;
            case R.id.tvEducation://学历下拉选择
                spIndext = 1;
                mSpinerPopWindow.setData(listEducation);
                showSpinWindow(tvEducation);
                break;
            case R.id.tvMarriage://婚姻状况下拉选择
                spIndext = 2;
                mSpinerPopWindow.setData(listMarriage);
                showSpinWindow(tvMarriage);
                break;
            case R.id.tvChildren://有无子女下拉选择
                spIndext = 3;
                mSpinerPopWindow.setData(listChildren);
                showSpinWindow(tvChildren);
                break;
            case R.id.tvHouseProperty://房产信息
                spIndext = 4;
                mSpinerPopWindow.setData(listHouseProperty);
                showSpinWindow(tvHouseProperty);
                break;
            case R.id.tvDateBirth://出生日期
                timePopupWindown.setData(tvDateBirth.getText().toString());
                showChosenTimeWindow(tvDateBirth);
                break;
        }
    }

    //修改菜单显示状态，并修改内容显示
    void MenuSelect() {
        if (indextSelect != indext) {
            llMenu[indextSelect].setSelected(true);
            llMenu[indext].setSelected(false);
            llContent[indextSelect].setVisibility(View.VISIBLE);
            llContent[indext].setVisibility(View.GONE);
            indext = indextSelect;
        }
    }

    //下拉选择窗口显示
    private void showSpinWindow(View v) {
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v, 0, 10);
        mSpinerPopWindow.setFocusable(true);
        mSpinerPopWindow.update();

    }

    //时间选择窗口
    private void showChosenTimeWindow(View v) {
        timePopupWindown.setTouchable(true);
        timePopupWindown.setOutsideTouchable(true);
        timePopupWindown.setFocusable(true);
        timePopupWindown.setBackgroundDrawable(new BitmapDrawable());
        timePopupWindown.showAtLocation(v, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onItemClick(int pos) {
        switch (spIndext) {
            case 0:
                tvSex.setText(listSex.get(pos));
                break;
            case 1:
                tvEducation.setText(listEducation.get(pos));
                break;
            case 2:
                tvMarriage.setText(listMarriage.get(pos));
                break;
            case 3:
                tvChildren.setText(listChildren.get(pos));
                break;
            case 4:
                tvHouseProperty.setText(listHouseProperty.get(pos));
                break;
        }

    }

    @Override
    public void onItemClick(String time) {
        tvDateBirth.setText(time);
    }
    private void putExtra(Intent intent){
        intent.putExtra("address",edtAddress.getText().toString());
        intent.putExtra("age",edtAge.getText().toString());

        intent.putExtra("sex",tvSex.getText());
        intent.putExtra("education",tvEducation.getText());
        intent.putExtra("marriage",tvMarriage.getText());
        intent.putExtra("children",tvChildren.getText());
//        intent.putExtra("houseProperty",tvHouseProperty.getText());
    }
}
