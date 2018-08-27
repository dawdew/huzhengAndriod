package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.CallBackUtil;
import com.hp.householdpolicies.utils.JsonParser;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.rsc.impl.OnROSListener;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class HomePageActivity extends Activity {
    //日期
    @BindView(R.id.textDate)
    TextView textDate;
    //温度
    @BindView(R.id.textTemperature)
    TextView textTemperature;
    @BindView(R.id.textState)
    TextView textState;//天气
    //天气图片
    @BindView(R.id.imgState)
    ImageView imgState;
    //信息查询
    @BindView(R.id.llinformation)
    LinearLayout llinformation;
    //推优办理
    @BindView(R.id.llTransaction)
    LinearLayout llTransaction;
    //大厅简介
    @BindView(R.id.llSynopsis)
    LinearLayout llSynopsis;
    //政策咨询
    @BindView(R.id.llAdvisory)
    LinearLayout llAdvisory;
    //在线预约
    @BindView(R.id.llAppointment)
    LinearLayout llAppointment;
    //下载申请
    @BindView(R.id.llDownload)
    LinearLayout llDownload;
    //synopsis_tv
    @BindView(R.id.ll_suggestion)
    LinearLayout llSuggestion;
    private ConnectServer cs;
    private SpeechRecognizer mIat;
    private SpeechSynthesizer mTts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);
        MyApp  application = (MyApp) getApplication();
        hideBottomUIMenu();
        init();
//        application.robotActionProvider.combinedActionTtyS4(9);
    }
    public void init() {
        cs = ConnectServer.getInstance(getApplication(), connection);
        cs.registerROSListener(new RosProcess());
    }
    public void uninit () {
        if (cs != null) {
            cs.release();
            cs = null;
        }
    }
    /**
     * 外设监听回调
     */
    public class RosProcess extends OnROSListener {
        private final String TAG = RosProcess.class.getSimpleName();
        @Override
        public void onResult (String result) {
            Log.e(TAG, "----OnROSListener.onResult()---result:" + result);
            if (result != null) {
                if (result.startsWith("od:")) {
                    //物体识别
                } else if (result.startsWith("laser:[")) {
                    //激光测距
                } else if (result.startsWith("pt:[")) {
                    //人体检测（3d摄像头）
                } else if (result.startsWith("move_status:")) {
                   //导航信息
                } else if (result.equals("bat:reached")) {
                    //充电信息
                } else if (result.equals("sys:uwb:0")) {
                    //导航uwb错误
                }
            }
        }
    }
    private RscServiceConnectionImpl connection = new RscServiceConnectionImpl() {
        public void onServiceConnected (int name) {
            if (cs == null)
                return;
            if (name == ConnectServer.Connect_Pr_Id) {

            }
        }
    public void onServiceDisconnected (int name) {
        System.out.println("onServiceDisconnected......");
    }
};


    @Override
    protected void onStart() {
        Date date=new Date();
        SimpleDateFormat sdm = new SimpleDateFormat("yyyy年MM月dd日                 EEEE");
        textDate.setText(sdm.format(date));
        weather();
        super.onStart();
    }

    @OnClick({R.id.llTransaction, R.id.llinformation, R.id.llDownload, R.id.llSynopsis, R.id.llAdvisory, R.id.llAppointment,R.id.ll_suggestion})
    void ViewClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.llTransaction://推优办理
                Intent intentTransaction= new Intent(this, OptimalPushActivity.class);
                startActivity(intentTransaction);
                break;
            case R.id.llinformation://信息查询
                Intent intentInformation = new Intent(this, InformationInquiryActivity.class);
                startActivity(intentInformation);
                break;
            case R.id.llDownload://下载申请
                Intent da = new Intent(this, DownloadActivity.class);
                startActivity(da);
                break;
            case R.id.llSynopsis://大厅简介
                Intent intentSynopsis = new Intent(this, SynopsisActivity.class);
                startActivity(intentSynopsis);
                break;
            case R.id.llAdvisory://政策咨询
                Intent intentAdvisory = new Intent(this, AdvisoryActivity.class);
                startActivity(intentAdvisory);
                break;
            case R.id.llAppointment://在线预约
                Intent intentAppointment = new Intent(this, InputActivity.class);
                startActivity(intentAppointment);
                break;
            case R.id.ll_suggestion://意见建议
                Intent intentSuggestion=new Intent(this,SuggestionActivity.class);
                startActivity(intentSuggestion);
                break;
        }
    }
    public void weather(){
        HashMap<String, String> json_map = new HashMap<>();
        json_map.put("key","fb606c21a2b5b95f8c7745b01dfd1695");
        json_map.put("city","120100");
        OkhttpUtil.okHttpGet(Api.weather, json_map, new CallBackUtil() {
            @Override
            public Object onParseResponse(Call call, Response response) {
                ResponseBody body = response.body();
                Map map = null;
                try {
                 map = JsonParser.fromJson(body.string(), Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return map;
            }

            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(Object response) {
                if(response!=null){
                    Map result = (Map) response;
                    String status = (String) result.get("status");
                    if("1".equals(status)){
                        List lives = (List) result.get("lives");
                        Map<String, String> o = (Map) lives.get(0);
                        textTemperature.setText("今天天气:  "+o.get("temperature")+"°C");
                        textState.setText(o.get("weather"));
                        String result_pinyin = null;
                        try {
                            result_pinyin = PinyinHelper.convertToPinyinString(o.get("weather"), "", PinyinFormat.WITHOUT_TONE);
                        } catch (PinyinException e) {
                            e.printStackTrace();
                        }
                        int resId = getResources().getIdentifier("weather_"+result_pinyin, "drawable", getBaseContext().getPackageName());
                        imgState.setImageResource(resId );
                    }
                }
            }
        });
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
}
