package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.CallBackUtil;
import com.hp.householdpolicies.utils.JsonParser;
import com.hp.householdpolicies.utils.MsynthesizerListener;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.rsc.impl.OnROSListener;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private boolean isSpeaked=false;
    private Boolean isListening=false;//是否开启录音监听
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);
        MyApp  application = (MyApp) getApplication();
        mTts = application.getmTts();
        mIat = SpeechRecognizer.createRecognizer(HomePageActivity.this, null);
        setParam();
        Date date=new Date();
        SimpleDateFormat sdm = new SimpleDateFormat("yyyy年MM月dd日                 EEEE");
        textDate.setText(sdm.format(date));
        weather();
        hideBottomUIMenu();
//        application.robotActionProvider.combinedActionTtyS4(9);
    }
    public void init() {
        cs = ConnectServer.getInstance(getApplication(), connection);
        cs.registerROSListener(new RosProcess());
    }
    public void release () {
        if (cs != null) {
            cs.release();
            cs = null;
        }
        mIat.cancel();
        mIat.destroy();
    }
    /**
     * 外设监听回调
     */
    public class RosProcess extends OnROSListener {
        @Override
        public void onResult (String result) {
            //Log.e(TAG, "----OnROSListener.onResult()---result:" + result);
            if (result != null) {
                if (result.startsWith("od:")) {
                    //物体识别
                } else if (result.startsWith("laser:[")) {
                    //激光测距
                    String substring = result.substring(result.indexOf("[")+1, result.length() - 1);
                    double v = Double.parseDouble(substring);
                    if(v>0.8){
                        isSpeaked=false;
                        if(isListening){
                            stopListening();
                        }
                    }
                    if(v<=0.8 && !isSpeaked){
                        mTts.startSpeaking("您好，请问您需要什么帮助？", new MsynthesizerListener() {
                            @Override
                            public void onSpeakBegin() {
                                isSpeaked=true;
                                super.onSpeakBegin();
                            }

                            @Override
                            public void onCompleted(SpeechError speechError) {
                                if(!isListening){
                                    startListening();
                                }
                            }
                        });
                    }
                } else if (result.startsWith("pt:[")) {
                    //人体检测（3d摄像头）
                    System.out.println(result);
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
//        mIat =   SpeechRecognizer.createRecognizer(HomePageActivity.this, null);
//        setParam();
        init();
        if(isSpeaked){
            startListening();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        release ();
    }

    @OnClick({R.id.llTransaction, R.id.llinformation, R.id.llDownload, R.id.llSynopsis, R.id.llAdvisory, R.id.llAppointment,R.id.ll_suggestion})
    void ViewClick(View view) {
        switchActivity(view.getId());
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

    private void startListening(){
        int ret = mIat.startListening(mRecognizerListener);
        if (ret == ErrorCode.SUCCESS) {
            isListening=true;
        }
    }
    private void stopListening(){
        mIat.stopListening();
        isListening=false;
    }
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
        }
        @Override
        public void onBeginOfSpeech() {
        }
        @Override
        public void onEndOfSpeech() {
        }
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            StringBuffer resultBuffer = new StringBuffer();
            String RecResult = JsonParser.parseIatResult(results.getResultString());
            String sn = null;
//             读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
            mIatResults.put(sn, RecResult);
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            String result_str = resultBuffer.toString();
            if(StringUtils.isNotBlank(result_str)){
                Integer viewId = null;
                if ("意见建议".contains(result_str)) {
                    viewId = R.id.ll_suggestion;
                }else if("推优办理".contains(result_str)){
                    viewId = R.id.llTransaction;
                }else if("进度查询".contains(result_str)){
                    viewId = R.id.llinformation;
                }else if("下载申请".contains(result_str)){
                    viewId = R.id.llDownload;
                }else if("大厅简介".contains(result_str)){
                    viewId = R.id.llSynopsis;
                }else if("政策咨询".contains(result_str)){
                    viewId = R.id.llAdvisory;
                }else if("在线预约".contains(result_str)){
                    viewId = R.id.llAppointment;
                }
                if(viewId!=null){
                    switchActivity(viewId);
                }else {
                    int ret = mIat.startListening(mRecognizerListener);
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if(isListening){
                int ret = mIat.startListening(mRecognizerListener);
                System.out.println(ret);
            }
        }
    };

    private void switchActivity(Integer viewId){

        switch (viewId) {
            case R.id.llTransaction://推优办理
                Intent intentTransaction= new Intent(this, OptimalPushActivity.class);
                startActivity(intentTransaction);
                break;
            case R.id.llinformation://进度查询
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
    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "5000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "2000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }
}
