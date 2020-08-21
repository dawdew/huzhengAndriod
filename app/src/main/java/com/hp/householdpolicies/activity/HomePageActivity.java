package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.PrintFormat;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.CallBackUtil;
import com.hp.householdpolicies.utils.JsonParser;
import com.hp.householdpolicies.utils.MsynthesizerListener;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.reeman.nerves.RobotActionProvider;
import com.rsc.aidl.OnPrintListener;
import com.rsc.impl.OnROSListener;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;
import com.synjones.idcard.IDCardInfo;
import com.synjones.idcard.OnIDListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
    //    @BindView(R.id.textName)
//    EditText textName;
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
    private boolean isSpeaked = false;
    //    private boolean ismoving =false ;//导航移动标识
    private boolean atposition = false;//到达目的地标识
    private Boolean isListening = false;//是否开启录音监听
    private AIUIAgent mAgent;
    //当前AIUI使用的配置
    private JSONObject config;
    private AIUIMessage msgv;
    private RosProcess rp1 = new RosProcess();
    ;//静止状态使用
    private RosProcess2 rp2 = new RosProcess2();
    ;//移动状态使用
    long[] mHitsCharge = new long[5];
    long[] mHitsPoint = new long[5];
    long[] mHitsLaser = new long[3];
    private boolean msgSendFlag = false;//是否得到语义识别结果标识

    private AlertDialog guanzhuDialg;
    private DismissDialogTimer dialogTimer;
    private TextView dialogCancelTv;
    private SimpleDateFormat printDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);
        MyApp application = (MyApp) getApplication();
        mTts = application.getmTts();
        mIat = SpeechRecognizer.createRecognizer(HomePageActivity.this, null);
        setParam();
        initFilter();

    }

    public void init() {
        hideBottomUIMenu();
        Date date = new Date();
        SimpleDateFormat sdm = new SimpleDateFormat("yyyy年MM月dd日                 EEEE");
        textDate.setText(sdm.format(date));
        weather();
        cs = ConnectServer.getInstance(getApplication(), connection);
        cs.registerROSListener(rp1);
    }

    public void release() {
        if (cs != null) {
            cs.release();
            cs = null;
        }
        mIat.cancel();
        mIat.destroy();
        mAgent.destroy();
    }

    /**
     * 外设监听回调
     */
    long mLaserHitLastValidTime = 0;
    long LASER_CHECK_INTERVAL = 400;    //激光测距检查时间间隔

    //检查是否需要激光测距处理
    boolean isTimeToLaserCheck(long currentTime) {
        if ((currentTime - mLaserHitLastValidTime) >= LASER_CHECK_INTERVAL) {
            mLaserHitLastValidTime = currentTime;
            return true;
        } else
            return false;
    }

    public class RosProcess extends OnROSListener {
        @Override
        public void onResult(String result) {
            //Log.e(TAG, "----OnROSListener.onResult()---result:" + result);
            if (result != null) {
                if (result.startsWith("od:")) {
                    //物体识别
                } else if (result.startsWith("laser[")) {      //"laser:["
                    //激光测距
                    long lCurrentTime = SystemClock.uptimeMillis();
                    if (!isTimeToLaserCheck(lCurrentTime)) return;     //距离上次激光测距时间太近，不做任何处理

                    String substring = result.substring(result.indexOf("[") + 1, result.length() - 1);
                    double v = Double.parseDouble(substring);
                    if (v > 0.8) {
                        isSpeaked = false;
                        if (mTts.isSpeaking()) {
                            mTts.stopSpeaking();
                        }
                        if (isListening) {
                            stopListening();
                        }
                    }
                    if (v <= 0.8 && !isSpeaked) {
                        System.arraycopy(mHitsLaser, 1, mHitsLaser, 0, mHitsLaser.length - 1);
                        mHitsLaser[mHitsLaser.length - 1] = lCurrentTime;   //SystemClock.uptimeMillis();
                        //是否在2秒内检测到3次
                        //if(mHitsLaser[0] <= SystemClock.uptimeMillis() - 2000){
                        if ((lCurrentTime - mHitsLaser[0]) > 2000) {
                            //第一次测距不在2秒以内
                            return;
                        }

                        Log.d("RosProcess", "Before Speaking!");
                        //您好，我是公安南开分局人口服务管理中心的小南,取号请到一号窗口,请问您需要办理什么户籍业务？
                        //isSpeaked = true;
                        mTts.startSpeaking("您好，我是公安南开分局人口服务管理中心的小南,取号请到一号窗口,请问您需要办理什么户籍业务？", new MsynthesizerListener() {
                            @Override
                            public void onSpeakBegin() {
                                isSpeaked = true;
                                super.onSpeakBegin();
                            }

                            @Override
                            public void onCompleted(SpeechError speechError) {
                                if (!isListening) {
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
                    navigationUpdate(result);
                } else if (result.equals("bat:reached")) {
                    //充电信息
                } else if (result.equals("sys:uwb:0")) {
                    //导航uwb错误
                }
            }
        }
    }

    public class RosProcess2 extends OnROSListener {
        @Override
        public void onResult(String result) {
            //Log.e(TAG, "----OnROSListener.onResult()---result:" + result);
            if (result != null) {
                if (result.startsWith("od:")) {
                    //物体识别
                } else if (result.startsWith("laser:[")) {

                } else if (result.startsWith("pt:[")) {
                    //人体检测（3d摄像头）
                    System.out.println(result);
                } else if (result.startsWith("move_status:")) {
                    //导航信息
                    navigationUpdate(result);
                } else if (result.equals("bat:reached")) {
                    //充电信息
                } else if (result.equals("sys:uwb:0")) {
                    //导航uwb错误
                }
            }
        }
    }

    private RscServiceConnectionImpl connection = new RscServiceConnectionImpl() {
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
            print();
        }
    };

    private void print() {
        PrintFormat.NUMBER++;
        PrintFormat.PEOPLECOUNT++;
        final PrintFormat format = new PrintFormat();
        final List<PrintFormat.DataBean> list = new ArrayList<>();
        list.add(new PrintFormat.DataBean().setText("您的号码为").setChangerow("1"));
        list.add(new PrintFormat.DataBean().setText("   " + PrintFormat.NUMBER).setSizetext("2").setAlignmen("1").setChangerow("1"));
        list.add(new PrintFormat.DataBean().setText("  业务预约").setChangerow("1"));
        list.add(new PrintFormat.DataBean().setText("在您前面还有   " + PrintFormat.PEOPLECOUNT + "      人等待").setChangerow("1"));
        list.add(new PrintFormat.DataBean().setText("请您在休息区等候").setChangerow("1"));
        list.add(new PrintFormat.DataBean().setText("过号无效，请重新取号").setChangerow("1"));
        list.add(new PrintFormat.DataBean().setText(printDateFormat.format(new Date())).setChangerow("1").setAlignmen("1").setFeedline("1"));
        list.add(new PrintFormat.DataBean().setText("abc32").setQr("1").setChangerow("1").setAlignmen("1").setSizetext("6").setCutpaper("0"));
        format.setData(list);
        new Thread() {
            @Override
            public void run() {
                cs.onPrint(JsonParser.toJson(format), 1, new OnPrintListener.Stub() {
                    @Override
                    public void onResult(int i) throws RemoteException {

                    }
                });
            }
        }.start();
    }

    @Override
    protected void onStart() {
        init();
        if (isSpeaked) {
            startListening();
        }
        mAgent = AIUIAgent.createAgent(this, config.toString(), mAIUIListener);
        super.onStart();

    }

    @Override
    protected void onStop() {
        release();
        super.onStop();
    }

    @OnClick({R.id.textDate, R.id.llTransaction, R.id.llinformation, R.id.llDownload, R.id.llSynopsis, R.id.llAdvisory, R.id.llAppointment, R.id.ll_suggestion, R.id.textTemperature, R.id.ah_btn_guanzhu})
    void ViewClick(View view) {
        if (view.getId() == R.id.textDate) {
            System.arraycopy(mHitsPoint, 1, mHitsPoint, 0, mHitsPoint.length - 1);
            mHitsPoint[mHitsPoint.length - 1] = SystemClock.uptimeMillis();
            //当0出的值大于当前时间-4000时  证明在4000毫秒内点击了5次
            if (mHitsPoint[0] > SystemClock.uptimeMillis() - 4000) {
                MyApp app = (MyApp) getApplication();
                String charge_xy = app.getContactLocations().get("充电");
                if (StringUtils.isNotBlank(charge_xy)) {
                    RobotActionProvider.getInstance().sendRosCom("goal:charge[" + charge_xy + "]");
                }
            }
        } else if (view.getId() == R.id.textTemperature) {
            System.arraycopy(mHitsCharge, 1, mHitsCharge, 0, mHitsCharge.length - 1);
            mHitsCharge[mHitsCharge.length - 1] = SystemClock.uptimeMillis();
            if (mHitsCharge[0] > SystemClock.uptimeMillis() - 4000) {
                MyApp app = (MyApp) getApplication();
                String xy = app.getContactLocations().get("原点");
                RobotActionProvider.getInstance().sendRosCom("goal:nav["+xy+"]");
            }
        } else if (view.getId() == R.id.ah_btn_guanzhu) {
            if (guanzhuDialg == null) {
                initDialog();
            }
            guanzhuDialg.show();
            View decorView = guanzhuDialg.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            if (dialogTimer != null) {
                dialogTimer.cancel();
                dialogTimer = null;
            }
            dialogTimer = new DismissDialogTimer(20 * 1000, 1000);
            dialogTimer.start();
        }
        switchActivity(view.getId());
    }

    class DismissDialogTimer extends CountDownTimer {

        public DismissDialogTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            dialogCancelTv.setText("取消(" + (l / 1000) + ")");
        }

        @Override
        public void onFinish() {
            guanzhuDialg.dismiss();
        }
    }


    private void initDialog() {
        View view = View.inflate(this, R.layout.dialog_guanzhu, null);
        dialogCancelTv = view.findViewById(R.id.dg_tv_cancel);
        dialogCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTimer.cancel();
                dialogTimer = null;
                guanzhuDialg.dismiss();
            }
        });
        guanzhuDialg = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        guanzhuDialg.setView(view, 0, 0, 0, 0);
        guanzhuDialg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                hideBottomUIMenu();
            }
        });
        guanzhuDialg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dialogTimer != null) {
                    hideBottomUIMenu();
                    dialogTimer.cancel();
                    dialogTimer = null;
                }
            }
        });

    }

    public void weather() {
        HashMap<String, String> json_map = new HashMap<>();
        json_map.put("key", "fb606c21a2b5b95f8c7745b01dfd1695");
        json_map.put("city", "120100");
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
                if (response != null) {
                    Map result = (Map) response;
                    String status = (String) result.get("status");
                    if ("1".equals(status)) {
                        List lives = (List) result.get("lives");
                        Map<String, String> o = (Map) lives.get(0);
                        textTemperature.setText("今天天气:  " + o.get("temperature") + "°C");
                        textState.setText(o.get("weather"));
                        String result_pinyin = null;
                        try {
                            result_pinyin = PinyinHelper.convertToPinyinString(o.get("weather"), "", PinyinFormat.WITHOUT_TONE);
                        } catch (PinyinException e) {
                            e.printStackTrace();
                        }
                        int resId = getResources().getIdentifier("weather_" + result_pinyin, "drawable", getBaseContext().getPackageName());
                        imgState.setImageResource(resId);
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

    private void startListening() {
        int ret = mIat.startListening(mRecognizerListener);
        if (ret == ErrorCode.SUCCESS) {
            isListening = true;
        }
    }

    private void stopListening() {
        mIat.stopListening();
        isListening = false;
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
            if (StringUtils.isNotBlank(result_str)) {
                Integer viewId = null;
                if ("意见建议".contains(result_str)) {
                    viewId = R.id.ll_suggestion;
                } else if ("推优办理".contains(result_str)) {
                    viewId = R.id.llTransaction;
                } else if ("进度查询".contains(result_str)) {
                    viewId = R.id.llinformation;
                } else if ("下载申请".contains(result_str)) {
                    viewId = R.id.llDownload;
                } else if ("大厅简介".contains(result_str)) {
                    viewId = R.id.llSynopsis;
                } else if ("政策咨询".contains(result_str)) {
                    viewId = R.id.llAdvisory;
                } else if ("在线预约".contains(result_str)) {
                    viewId = R.id.llAppointment;
                } else {
//                    //写入文本
                    byte[] content = result_str.getBytes();
                    String params = "data_type=text";
                    msgv = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, params, content);
                    msgSendFlag = true;
                    mAgent.sendMessage(msgv);
                }
                if (viewId != null) {
                    switchActivity(viewId);
                } else {
                    int ret = mIat.startListening(mRecognizerListener);
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if (isListening) {
                int ret = mIat.startListening(mRecognizerListener);
                System.out.println(ret);
            }
        }
    };

    private void switchActivity(Integer viewId) {

        switch (viewId) {
            case R.id.llTransaction://推优办理
                Intent intentTransaction = new Intent(this, OptimalPushActivity.class);
                startActivity(intentTransaction);
                break;
            case R.id.llinformation://进度查询
                Intent intentInformation = new Intent(this, InformationInquiryActivity.class);
                startActivity(intentInformation);
                break;
            case R.id.llDownload://下载申请
//                Intent da = new Intent(this, DownloadActivity.class);
                Intent da = new Intent(this, PrintTemplateActivity.class);
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
                Intent intentSuggestion = new Intent(this, SuggestionActivity.class);
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
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
        //设置aiui
        try {
            config = new JSONObject();
            config.putOpt("attachparams", null);
            config.putOpt("tts", new JSONObject().put("play_mode", "user"));

            config.putOpt("speech", new JSONObject().put("interact_mode", "continuous"));
            config.optJSONObject("speech").put("wakeup_mode", "off");
            config.optJSONObject("speech").put("data_source", "sdk");

            config.putOpt("global", new JSONObject().put("scene", "main"));
            config.optJSONObject("global").put("clean_dialog_history", "auto");

            config.putOpt("interact", new JSONObject().put("result_timeout", "5000"));
            config.optJSONObject("interact").put("interact_timeout", "60000");

            config.putOpt("login", new JSONObject().put("appid", "5b70ef31"));
            config.optJSONObject("login").put("key", "");

            config.putOpt("vad", new JSONObject().put("engine_type", "meta"));
            config.optJSONObject("vad").put("res_path", "vad/meta_vad_16k.jet");
            config.optJSONObject("vad").put("vad_eos", "1000");
            config.optJSONObject("vad").put("res_type", "assets");
            config.optJSONObject("vad").put("vad_enable", "1");

            config.putOpt("log", new JSONObject().put("save_datalog", "0"));
            config.optJSONObject("log").put("debug_log", "1");
            config.optJSONObject("log").put("raw_audio_path", "");
            config.optJSONObject("log").put("datalog_path", "");
            config.optJSONObject("log").put("datalog_size", "1024");

            config.putOpt("iat", new JSONObject().put("sample_rate", "16000"));
            config.putOpt("audioparams", new JSONObject().put("pers_param", "{\\\"appid\\\":\\\"\\\",\\\"uid\\\":\\\"\\\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final AIUIListener mAIUIListener = new AIUIListener() {
        @Override
        public void onEvent(AIUIEvent aiuiEvent) {
            System.out.println(aiuiEvent.eventType);
            switch (aiuiEvent.eventType) {
                case AIUIConstant.EVENT_CONNECTED_TO_SERVER:
                    AIUIMessage msg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, null, null);
                    mAgent.sendMessage(msg);
                    break;
                case AIUIConstant.EVENT_RESULT:
                    processResult(aiuiEvent);
                    msgSendFlag = false;
                    break;
                case AIUIConstant.EVENT_WAKEUP:
                    if (msgSendFlag) {
                        mAgent.sendMessage(msgv);
                    }
                    break;
                case AIUIConstant.EVENT_ERROR:
                    AIUIMessage msg1 = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, null, null);
                    mAgent.sendMessage(msg1);
                    break;
            }
        }
    };

    /**
     * 处理AIUI结果事件（听写结果和语义结果）
     *
     * @param event 结果事件
     */
    private void processResult(AIUIEvent event) {
        try {
            JSONObject bizParamJson = new JSONObject(event.info);
            JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
            JSONObject params = data.getJSONObject("params");
            JSONObject content = data.getJSONArray("content").getJSONObject(0);

            long rspTime = event.data.getLong("eos_rslt", -1);  //响应时间

            if (content.has("cnt_id")) {
                String cnt_id = content.getString("cnt_id");
                JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));

                String sub = params.optString("sub");
                if ("nlp".equals(sub)) {
                    JSONObject semanticResult = cntJson.optJSONObject("intent");
                    if (semanticResult != null && semanticResult.length() != 0) {
                        //解析得到语义结果，将语义结果作为消息插入到消息列表中
                        if (!semanticResult.has("semantic")) {
                            stopListening();
                            //isSpeaked = true;
                            mTts.startSpeaking("目前我还没有找到适合您的相关政策,请您到1号咨询台进行详细咨询", new SynthesizerListener() {
                                @Override
                                public void onSpeakBegin() {
                                    isSpeaked = true;
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
                                    if (!isListening) {
                                        startListening();
                                    }
                                }

                                @Override
                                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                                }
                            });
                            return;
                        }
                        JSONArray semantic = semanticResult.getJSONArray("semantic");
                        if (semantic == null) {
                            return;
                        }
                        JSONObject o = (JSONObject) semantic.get(0);
                        String intent = (String) o.get("intent");
                        JSONArray slots = (JSONArray) o.get("slots");
                        JSONObject o1 = (JSONObject) slots.get(0);
                        String normValue = (String) o1.get("normValue");
//                        textName.setText(intent);
                        MyApp app = (MyApp) getApplication();
                        switch (intent) {
                            case "zczx":
                                Intent intentAdvisory = new Intent(this, AdvisoryActivity.class);
                                startActivity(intentAdvisory);
                                break;
                            case "gai":
                                Intent intent_sxbg = new Intent(this, AdvisoryDetailsActivity.class);
                                intent_sxbg.putExtra("category", "4");
                                intent_sxbg.putExtra("word", normValue);
                                startActivity(intent_sxbg);
                                break;
                            case "csdj":
                                Intent intentBirthrEgistration = new Intent(this, AdvisoryDetailsActivity.class);
                                intentBirthrEgistration.putExtra("category", "1");
                                intentBirthrEgistration.putExtra("word", normValue);
                                startActivity(intentBirthrEgistration);
                                break;
                            case "zhuxiao":
                                Intent intent_zxhk = new Intent(this, AdvisoryDetailsActivity.class);
                                intent_zxhk.putExtra("category", "2");
                                intent_zxhk.putExtra("word", normValue);
                                startActivity(intent_zxhk);
                                break;
                            case "bu":
                                Intent intent_bu = new Intent(this, AdvisoryDetailsActivity.class);
                                intent_bu.putExtra("category", "5");
                                intent_bu.putExtra("word", normValue);
                                startActivity(intent_bu);
                                break;
                            case "charge"://充电
                                String charge_xy = app.getContactLocations().get("充电");
                                if (StringUtils.isNotBlank(charge_xy)) {
                                    RobotActionProvider.getInstance().sendRosCom("goal:charge[" + charge_xy + "]");
                                }
                                break;
                            case "move"://移动意图
                                String xy = app.getContactLocations().get(normValue);
                                if (StringUtils.isNotBlank(xy)) {
                                    atposition = false;
                                    cs.registerROSListener(rp2);
                                    RobotActionProvider.getInstance().sendRosCom("goal:nav[" + xy + "]");
                                }
                                break;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_LOW.equals(action)) {
                MyApp app = (MyApp) getApplication();
                String xy = app.getContactLocations().get("充电");
                if (StringUtils.isNotBlank(xy)) {
                    RobotActionProvider.getInstance().sendRosCom("goal:charge[" + xy + "]");
                }
            }
        }
    };

    /**
     * 处理导航回调
     *
     * @param result move_status:x = ?  0 : 静止待命   1 : 上次目标失败，等待新的导航命令   2 : 上次目标完成，等待新的导航命令  
     *               3 : 移动中，正在前往目的地   4 : 前方障碍物   5 : 目的地被遮挡 6：用户取消导航 7：收到新的导航
     */
    public void navigationUpdate(String result) {
        switch (result) {
            case "move_status:0":
//                ismoving=false;
                cs.registerROSListener(rp1);
                break;
            case "move_status:1":
//                ismoving=false;
                mTts.startSpeaking("导航失败", null);
                cs.registerROSListener(rp1);
                break;
            case "move_status:2":
//                ismoving=false;
                if (!atposition) {
                    RobotActionProvider.getInstance().combinedActionTtyS4(3);
                    atposition = true;
                    mTts.startSpeaking("到达目的地", new SynthesizerListener() {
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
                            MyApp app = (MyApp) getApplication();
                            String xy = app.getContactLocations().get("原点");
                            //xy = app.getContactLocations().get("origin");
                            if (StringUtils.isNotBlank(xy)) {
                                RobotActionProvider.getInstance().sendRosCom("goal:nav[" + xy + "]");
                            }
                            //RobotActionProvider.getInstance().sendRosCom("goal:nav[2.45,-2.25,-90.0]");
                        }

                        @Override
                        public void onEvent(int i, int i1, int i2, Bundle bundle) {
                        }
                    });
                } else {
                    cs.registerROSListener(rp1);
                }

                break;
            case "move_status:3":
//                ismoving=true;
                break;
            case "move_status:4":
                mTts.startSpeaking("前方有障碍物", null);
                break;
            case "move_status:5":
                mTts.startSpeaking("目的地被遮挡", null);
                break;
            case "move_status:6":
                mTts.startSpeaking("用户取消导航", null);
//                ismoving=false;
                cs.registerROSListener(rp1);
                break;
            case "move_status:7":
                //  atposition = false;
                //   mTts.startSpeaking("收到新的导航",null);
                break;
            default:
                break;
        }
    }
}