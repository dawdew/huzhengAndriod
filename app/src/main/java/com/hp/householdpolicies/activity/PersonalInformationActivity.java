package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.customView.ChosenTimePopupWindown;
import com.hp.householdpolicies.customView.SpinnerPopupWindown;
import com.hp.householdpolicies.model.PersonInfo;
import com.hp.householdpolicies.utils.BaseMethod;
import com.hp.householdpolicies.utils.BeanUtil;
import com.hp.householdpolicies.utils.DateUtils;
import com.iflytek.cloud.SpeechSynthesizer;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;
import com.synjones.idcard.IDCardInfo;
import com.synjones.idcard.OnIDListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class PersonalInformationActivity extends BaseActivity implements ChosenTimePopupWindown.IChosenListener, Validator.ValidationListener {
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
    //动态基础信息容器
    @BindView(R.id.content_base)
    LinearLayout contentBase;
    //婚姻状况
    @BindView(R.id.content_marital)
    LinearLayout contentMaritalStatus;
    //有无子女
    @BindView(R.id.content_children)
    LinearLayout contentChildren;
    //房产信息
    @BindView(R.id.content_house)
    LinearLayout contentHouseInfo;
    //内容数组
    LinearLayout llContent[];
    SpinnerPopupWindown mSpinerPopWindow;
    ChosenTimePopupWindown timePopupWindown;
//    ChosenTimePopupWindown timePopupWindown2;
    //性别
    @BindView(R.id.tvSex)
    TextView tvSex;
    //学历
    @BindView(R.id.tvEducation)
    TextView tvEducation;
    //本市户籍
    @BindView(R.id.tvCurrentCity)
    TextView tvCurrentCity;
    //南开住房
    @BindView(R.id.tvHasNankaiHouse)
    TextView tvHasNankaiHouse;
    //年龄
    @BindView(R.id.edtAge)
    @NotEmpty(messageResId = R.string.errorMessage)
    @Order(1)
    EditText edtAge;
    //性别
    private List<String> listSex = new ArrayList<String>();
    //学历
    private List<String> listEducation = new ArrayList<String>();
    //其他身份
    private List<String> listOther = new ArrayList<String>();
    //婚姻状况
    private List<String> listMarriage = new ArrayList<String>();
    //有无
    private List<String> yw = new ArrayList<String>();
    //结婚满3年
    private List<String> trProperty = new ArrayList<String>();
    //房产信息
    private List<String> listHouseProperty = new ArrayList<String>();
    //下拉选择信息
    private List<String> listValue = new ArrayList<String>();
    private PersonInfo personInfo;
    /*
    下拉菜单listValue
    0：性别
    1：学历
    2：婚姻状况
    3：有无子女
    4：房产信息
     */
    int spIndext = 0;
    private ConnectServer cs;
    private SpeechSynthesizer mtTs;
    Validator validator;
    Boolean verify=false;
    private boolean hasQuestion = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        mtTs = ((MyApp)getApplication()).getmTts();
        personInfo = new PersonInfo();

        llMenu = new LinearLayout[]{llBasicInformation, llMaritalStatus, llChildren};
        llContent = new LinearLayout[]{contentBasicInformation, contentMaritalStatus, contentChildren, contentHouseInfo};
        llMenu[indext].setSelected(true);
        llContent[indext].setVisibility(View.VISIBLE);

        textNext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        AddList();
        mSpinerPopWindow = new SpinnerPopupWindown(this, listValue);
        timePopupWindown = new ChosenTimePopupWindown(this);
        timePopupWindown.setItemListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    @Override
    protected void onStart() {
        cs = ConnectServer.getInstance(getApplication(), impl);
        mtTs.startSpeaking("右肩章可扫描身份证",null);
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
                    tvSex.setText(info.getSex());
                    Date yyyyMMdd = DateUtils.parse(info.getBirthday(), "yyyyMMdd");
                    //tvDateBirth.setText(DateUtils.format(yyyyMMdd,"yyyy-MM-dd"));
                    int age = DateUtils.getAge(yyyyMMdd);
                    edtAge.setText(String.valueOf(age));
                   /* edtName.setText(info.getName());
                    edtIDNumber.setText(info.getIdcardno());
                    edtAddress.setText(info.getAddress());
                    Date yyyyMMdd = DateUtils.parse(info.getBirthday(), "yyyyMMdd");
                    //tvDateBirth.setText(DateUtils.format(yyyyMMdd,"yyyy-MM-dd"));
                    int age = DateUtils.getAge(yyyyMMdd);
                    edtAge.setText(String.valueOf(age));*/
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onStop() {
        if(cs!=null){
            cs.release();
        }
        super.onStop();
    }
    private void AddList() {
        //性别
        listSex.add("男");
        listSex.add("女");
        tvSex.setText(listSex.get(0));
        //学历
        listEducation.add("（全日制）大专");
        listEducation.add("（全日制）本科");
        listEducation.add("（全日制）硕士");
        listEducation.add("（全日制）博士");
        listEducation.add("其他");
        tvEducation.setText(listEducation.get(0));
        //婚姻状况
        listMarriage.add("未婚");
        listMarriage.add("初婚");
        listMarriage.add("复婚");
        listMarriage.add("再婚");
        listMarriage.add("离婚");
        listMarriage.add("丧偶");
        //有无
        yw.add("无");
        yw.add("有");
        tvHasNankaiHouse.setText(yw.get(0));
        //是否
        trProperty.add("否");
        trProperty.add("是");
        tvCurrentCity.setText(trProperty.get(0));
        //房产信息
//        listHouseProperty.add("其他");
//        listHouseProperty.add("2014年5月31日前购买80万以上一手房");
//        listHouseProperty.add("2007年4月1日前购买30万以上一手房");
        //
        listOther.add("无");
        listOther.add("知青上山下乡");
        listOther.add("支援边疆建设");
        listOther.add("支援农业建设");
        listOther.add("支援三线建设");
        listOther.add("四年保留户口");
        listOther.add("垦荒");
        listOther.add("遣送回原籍");
        listOther.add("其他");
    }

    @OnClick({R.id.ll_basic_information, R.id.ll_marital_status, R.id.ll_children, R.id.textNext,
            R.id.tvSex, R.id.tvEducation,R.id.tvCurrentCity,R.id.tvHasNankaiHouse})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_basic_information://基础信息
              //  indextSelect = 0;
               // MenuSelect();
                break;
            case R.id.ll_marital_status://婚姻状况
               // indextSelect = 1;
             //   MenuSelect();
                break;
            case R.id.ll_children://有无子女
               // indextSelect = 2;
             //   MenuSelect();
                break;
            case R.id.textNext://下一步
                if(indextSelect == 0){
                    validator.validate();
                    if(verify){
                        showSelectItem();
                        indextSelect++;
                    }
                }else {
                    showSelectItem();
                    indextSelect++;
                }
                if (!hasQuestion) {
//                    indextSelect = 0;
                    Intent intent=new Intent(this,OptimalPushResultsActivity.class);
                    putExtra(intent);
                    startActivity(intent);
                    return;
                }
                MenuSelect();
                break;
            case R.id.tvSex://性别下拉选择
                mSpinerPopWindow.setData(listSex,tvSex);
                showSpinWindow(tvSex);
                break;
            case R.id.tvEducation://学历下拉选择
                mSpinerPopWindow.setData(listEducation,tvEducation);
                showSpinWindow(tvEducation);
                break;
            case R.id.tvCurrentCity:
                mSpinerPopWindow.setData(trProperty,tvCurrentCity);
                showSpinWindow(tvCurrentCity);
                break;
            case R.id.tvHasNankaiHouse:
                mSpinerPopWindow.setData(yw,tvHasNankaiHouse);
                showSpinWindow(tvHasNankaiHouse);
                break;
        }
    }

    //修改菜单显示状态，并修改内容显示
    void MenuSelect() {
        if (indextSelect != indext) {
            if(indextSelect<llMenu.length&&indext<llMenu.length){
                llMenu[indextSelect].setSelected(true);
                llMenu[indext].setSelected(false);
            }
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
    public void onItemClick(String time,TextView view) {
        view.setText(time);
    }
    private void putExtra(Intent intent){
        intent.putExtra("info",personInfo);
    }
    private void addItem(LinearLayout parent, String name, List<String> selectList, final String fieldName){
        hasQuestion = true;
        final List<String> list=selectList;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item, parent, false);
        TextView title = (TextView)v.findViewById(R.id.tv_title);
        title.setText(name);
        final TextView select = (TextView)v.findViewById(R.id.tv_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpinerPopWindow.setData(list);
                mSpinerPopWindow.setTextView(select);
                showSpinWindow(view);
            }
        });
        select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                Object value;
                if("有".equals(s) || "是".equals(s)){
                    value=true;
                }else if("无".equals(s) || "否".equals(s)){
                    value=false;
                }else {
                    value=s;
                }
                BeanUtil.setFieldValue(personInfo,fieldName,value);
            }
        });
        select.setText(selectList.get(0));
        parent.addView(v);
    }
    void showSelectItem(){
        hasQuestion = false;
        if(indextSelect==0){
            Editable text = edtAge.getText();
            String age = text.toString();
            String sex = tvSex.getText().toString();
            String isCurrentCity = tvCurrentCity.getText().toString();
            String hasNankaiHose = tvHasNankaiHouse.getText().toString();
            boolean currentCity = isCurrentCity.equals("是");
            boolean hasNankaiHoseB = hasNankaiHose.equals("是");
            personInfo.setSex(sex);
            personInfo.setEducation(tvEducation.getText().toString());
            personInfo.setHouseholdTj(currentCity);
            personInfo.setHouse(hasNankaiHoseB);
            if(StringUtils.isNotBlank(age)){
                int age_i = Integer.parseInt(age);
                personInfo.setAge(age_i);
                if(("男".equals(sex)&&age_i>=22)||"女".equals(sex)&&age_i>=20){
                    addItem(contentMaritalStatus,"婚姻状况：",listMarriage,"marriage");
                }
                if("否".equals(isCurrentCity)){
                    addItem(contentMaritalStatus,"有无居住证：",yw,"residencePermit");
                }
                if("有".equals(hasNankaiHose)){
                    addItem(contentMaritalStatus,"2007年4月1日前购买30万以上一手房：",trProperty,"housePrice_a");
                    addItem(contentMaritalStatus,"2014年5月31日前购买80万以上一手房：",trProperty,"housePrice_b");
                    addItem(contentMaritalStatus,"有无房贷：",yw,"houseLoan");
                }
                if("是".equals(isCurrentCity)){
                    addItem(contentMaritalStatus,"特殊身份：",listOther,"special");
                    addItem(contentMaritalStatus,"是否由于特殊原因迁往外地：",trProperty,"specialReasonRelocate");
                }
            }
        }else if(indextSelect==1){
            if(StringUtils.isNotBlank(personInfo.getMarriage())&&!"未婚".equals(personInfo.getMarriage())){
                addItem(contentChildren,"是否本市户口：",trProperty,"householdTj");
                addItem(contentChildren,"是否结婚三年：",trProperty,"marriageThree");
                addItem(contentChildren,"有无子女：",yw,"children");
            }
            if(personInfo.getResidencePermit()!=null&&personInfo.getResidencePermit()){
                addItem(contentChildren,"居住证签注是否满三年：",trProperty,"residencePermit3Year");
            }
        }else if(indextSelect==2){
            if(personInfo.getChildren()!=null&&personInfo.getChildren()){
                addItem(contentHouseInfo,"子女是否为天津户口：",trProperty,"childrensHousehold");
                addItem(contentHouseInfo,"子女年龄是否小于18岁：",trProperty,"childrenInfo1");
            }
            if(personInfo.getChildren()!=null&&personInfo.getHouseholdTj()){
                addItem(contentHouseInfo,"配偶是否为本地户口：",trProperty,"isSpouseIsLocation");
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        verify=true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}
