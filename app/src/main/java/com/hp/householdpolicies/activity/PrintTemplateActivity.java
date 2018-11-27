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
import android.text.Html;
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
import com.hp.householdpolicies.model.Download;
import com.hp.householdpolicies.model.PersonInfo;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.BaseMethod;
import com.hp.householdpolicies.utils.BeanUtil;
import com.hp.householdpolicies.utils.DateUtils;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;
import com.synjones.idcard.IDCardInfo;
import com.synjones.idcard.OnIDListener;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class PrintTemplateActivity extends BaseActivity implements ChosenTimePopupWindown.IChosenListener, Validator.ValidationListener {
    //    可扫描二维码
    @BindView(R.id.llScan)
    TextView llScan;
    //    基础信息
    @BindView(R.id.ll_basic_information)
    LinearLayout llBasicInformation;
    //    婚姻状况
    @BindView(R.id.ll_marital_status)
    LinearLayout llMaritalStatus;

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

    //内容数组
    LinearLayout llContent[];
    SpinnerPopupWindown mSpinerPopWindow;
    ChosenTimePopupWindown timePopupWindown;
//    ChosenTimePopupWindown timePopupWindown2;
    //性别
    @BindView(R.id.tvSex)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText tvSex;
    @BindView(R.id.tvName)
    TextView tvName;
    //学历
    @BindView(R.id.reqContent)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText reqContent;
    //姓名
    @BindView(R.id.edtName)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText edtName;
    //身份证号
    @BindView(R.id.edtIDNumber)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText edtIDNumber;
    //户籍地
    @BindView(R.id.edtAddress)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText edtAddress;
    //现住地
    @BindView(R.id.edtResidence)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText edtResidence;
    @BindView(R.id.tvEducation)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText tvEducation;
    //业务类型
    @BindView(R.id.edtworkUnit)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText edtworkUnit;
    //年龄
    @BindView(R.id.edtAge)
    @NotEmpty(messageResId = R.string.errorMessage)
    @Order(1)
    EditText edtAge;
    //性别
    private List<String> listSex = new ArrayList<String>();
    //下拉选择信息
    private List<String> listValue = new ArrayList<String>();
    private PersonInfo personInfo;
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

    Validator validator;
    Boolean verify=false;
    private SpeechSynthesizer mTts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_printtemplate);
        ButterKnife.bind(this);
        personInfo = new PersonInfo();
        llMenu = new LinearLayout[]{llBasicInformation, llMaritalStatus};
        llContent = new LinearLayout[]{contentBasicInformation, contentMaritalStatus };
        llMenu[indext].setSelected(true);
        llContent[indext].setVisibility(View.VISIBLE);
        textNext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        AddList();
        MyApp  application = (MyApp) getApplication();
        mTts = application.getmTts();
        mSpinerPopWindow = new SpinnerPopupWindown(this, listValue);
        timePopupWindown = new ChosenTimePopupWindown(this);
        timePopupWindown.setItemListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        edtAge.setText(BaseMethod.getDateNoW());
        edtworkUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                String text = s.toString();
                if(text.equals("变更18岁以下孩子姓名") || text.equals("父母离异，孩子变更姓名") || text.equals("子女投父母")){
                    tvName.setText(" 父或母姓名 ：");
                }else {
                    tvName.setText(" 姓       名 ：");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mTts.startSpeaking("请扫描或输入身份证,姓名,手机号码", new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                cs = ConnectServer.getInstance(getApplication(), impl);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
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
        public boolean  handleMessage(Message msg) {
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
//                    Date yyyyMMdd = DateUtils.parse(info.getBirthday(), "yyyyMMdd");
                    //tvDateBirth.setText(DateUtils.format(yyyyMMdd,"yyyy-MM-dd"));
//                    int age = DateUtils.getAge(yyyyMMdd);
                    edtAge.setText(info.getBirthday());
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
        listValue.add("投配偶申请(无子女)");
        listValue.add("投配偶申请(有子女)");
        listValue.add("18岁以上变更姓名");
        listValue.add("变更18岁以下孩子姓名");
        listValue.add("父母离异，孩子变更姓名");
        listValue.add("补发户口本");
        listValue.add("子女投父母");
        listValue.add("老人投子女(外省市人写)");
        listValue.add("老人投子女(本市人写)");
        listValue.add("知青回津落户(本市人写)");
        listValue.add("知青回津落户(外省市人写)");
        tvSex.setText(listSex.get(0));
    }

    @OnClick({R.id.ll_basic_information, R.id.ll_marital_status, R.id.textNext, R.id.tvSex,R.id.edtworkUnit,R.id.edtAge})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_basic_information://基础信息
                llScan.setText("可扫描身份证");
                textNext.setText("下一步");
                indextSelect=0;
                MenuSelect();
                break;
            case R.id.ll_marital_status://婚姻状况
//                if(indextSelect==0){
//                    llScan.setText("");
//                    getTemplateItem();
//                    indextSelect++;
//                    MenuSelect();
//                }
                break;
            case R.id.tvSex://性别
                mSpinerPopWindow.setData(listSex,tvSex);
                showSpinWindow(tvSex);
                break;
            case R.id.textNext://下一步
                validator.validate();
               if(verify){
                   if(indextSelect==0){
                       textNext.setText("开始打印");
                       llScan.setText("");
                       getTemplateItem();
                       indextSelect++;
                       MenuSelect();
                   }else {
                       submit();
                   }
               }
                break;
            case R.id.edtworkUnit://
                mSpinerPopWindow.setData(listValue,edtworkUnit);
                showSpinWindow(edtworkUnit);
                break;
            case  R.id.edtAge:
                timePopupWindown.setData(edtAge.getText().toString(),edtAge);
                showChosenTimeWindow(edtAge);
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
    public void onItemClick(String time,TextView view) {
        view.setText(time);
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
    private void getTemplateItem() {
        reqContent.getText().clear();
        HashMap<String, String> json_map = new HashMap<>();
        json_map.put("title",edtworkUnit.getText().toString());
        OkhttpUtil.okHttpGet(Api.getDocTemplateItem, json_map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                String templateItem = (String) response;
                if(StringUtils.isNotBlank(templateItem)){
                    reqContent.setText(Html.fromHtml(templateItem));
                }
            }
        });
    }
    private void submit() {
        HashMap<String, String> json_map = new HashMap<>();
        String yewu = edtworkUnit.getText().toString();
        if(yewu.contains("(")){
            yewu = yewu.substring(0,yewu.indexOf("("));
        }
        json_map.put("yewu",yewu);
        json_map.put("content",reqContent.getText().toString());
        json_map.put("name",edtName.getText().toString());
        json_map.put("bd",edtAge.getText().toString());
        json_map.put("sex",tvSex.getText().toString());
        json_map.put("phone",tvEducation.getText().toString());
        json_map.put("idcard",edtIDNumber.getText().toString());
        json_map.put("hujidizhi",edtAddress.getText().toString());
        json_map.put("xiandizhi",edtResidence.getText().toString());
        LemonBubble.showRoundProgress(this,"等待中...");
        OkhttpUtil.okHttpGet(Api.print, json_map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                if(response!=null){
                    LemonBubble.showRight(PrintTemplateActivity.this, "打印成功", 2000);
                    mTts.startSpeaking("打印成功,请去3号窗口打印机领取您的申请表",null);
                }else {
                    LemonBubble.showError(PrintTemplateActivity.this,"打印失败",2000);
                }
            }
        });
    }


}
