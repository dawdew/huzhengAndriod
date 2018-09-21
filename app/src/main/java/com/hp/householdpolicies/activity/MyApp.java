package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.hp.householdpolicies.utils.PropUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.ResourceUtil;
import com.reeman.nerves.RobotActionProvider;

import org.xutils.ImageManager;
import org.xutils.common.util.FileUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MyApp extends Application{
    private static List<Activity> lists = new ArrayList<>();
    private Map<String, String> contactLocations;
//    public   RobotActionProvider robotActionProvider;
// 讯飞语音听写对象
    private SpeechRecognizer mIat;
    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    @Override
    public void onCreate() {
        x.Ext.init(this);
        StringBuffer param = new StringBuffer();
        param.append("appid="+"5b70ef31");
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(MyApp.this, param.toString());
//        mIat = SpeechRecognizer.createRecognizer(MyApp.this, null);
        mTts = SpeechSynthesizer.createSynthesizer(MyApp.this, null);
        setParam();
        initLocation();
//        robotActionProvider = RobotActionProvider.getInstance();
        super.onCreate();
    }

    public static void addActivity(Activity activity) {
        lists.add(activity);
    }

    public static void clearActivity() {
        if (lists != null) {
            for (Activity activity : lists) {
                activity.finish();
            }
            lists.clear();
        }
    }

    public static void removeActivity(Activity activity) {
        if (lists != null) {
            lists.remove(activity);
        }
    }
    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {

//        // 设置听写引擎
//        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
//        // 设置返回结果格式
//        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//        // 设置语言
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        // 设置语言区域
//        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
//        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//        mIat.setParameter(SpeechConstant.VAD_BOS, "5000");
//
//        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//        mIat.setParameter(SpeechConstant.VAD_EOS, "2000");
//        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE,mEngineType); //设置云端
//        mTts.setParameter( ResourceUtil.TTS_RES_PATH, getResourcePath() );
    }

    public SpeechRecognizer getmIat() {
        return mIat;
    }

    public void setmIat(SpeechRecognizer mIat) {
        this.mIat = mIat;
    }

    public SpeechSynthesizer getmTts() {
        return mTts;
    }

    public void setmTts(SpeechSynthesizer mTts) {
        this.mTts = mTts;
    }

    private void initLocation() {
        String location = PropUtils.readFileFromSDCard("/reeman/data/","locations.cfg");
        if(!TextUtils.isEmpty(location)) {
            try {
                String[] item = location.split(";");
                Map<String, String> locations = new HashMap();
                if(item != null && item.length > 0) {
                    String[] var8 = item;
                    int var7 = item.length;

                    for(int var6 = 0; var6 < var7; ++var6) {
                        String s = var8[var6];
                        String name = s.substring(0, s.indexOf(":"));
                        String value = s.substring(s.indexOf(":") + 1, s.length());
                        locations.put(name, value);
                    }
                }

                this.contactLocations=locations;
                Log.d("ReemanSpeechPlugin", "加载导航位置信息完成");
            } catch (Exception var11) {
                Log.d("ReemanSpeechPlugin", "加载导航位置信息出错，文件格式错误");
            }

        }
    }

    public Map<String, String> getContactLocations() {
        return contactLocations;
    }
}
