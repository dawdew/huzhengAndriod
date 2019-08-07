package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.model.AdvisorySearch;
import com.hp.householdpolicies.model.Article;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.JsonParser;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvisoryActivity extends BaseActivity {
    //搜索框
    @BindView(R.id.rlSearch)
    RelativeLayout rlSearch;
    //出生登记
    @BindView(R.id.tv_birthr_egistration)
    TextView tvBirthrEgistration;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.editSearch)
    EditText editSearch;

    private SpeechRecognizer mIat;
    private SpeechSynthesizer mTts;
    private boolean isSpeaked=false;
    private boolean isListening=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_advisory);
        ButterKnife.bind(this);
        MyApp  application = (MyApp) getApplication();
        mTts = application.getmTts();
    }

    @Override
    protected void onStart() {
        mTts.startSpeaking("政策咨询，请点击或说出您想了解的政策", new SynthesizerListener() {
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
                mIat = SpeechRecognizer.createRecognizer(AdvisoryActivity.this, null);
                setParam();
                 mIat.startListening(mRecognizerListener);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
        super.onStart();
    }

    @OnClick({R.id.rlSearch,R.id.tv_birthr_egistration,R.id.tv_zxhk,R.id.tv_hkqy,R.id.tv_sxbg,R.id.tv_zjbf,R.id.tv_hfhk})
    void ViewClick(View view) {
        switchActivity(view.getId());
    }
    @OnClick({R.id.imgSearch})
    void imgSearchClick(View view) {
        String searchString = editSearch.getText().toString();
        Intent intentSearch = new Intent(this, AdvisorySearchActivity.class);
        intentSearch.putExtra("keyword",searchString);
        startActivity(intentSearch);
    }
    private void switchActivity(Integer viewId){
        switch (viewId) {
            case R.id.tv_birthr_egistration://出生登记
                Intent intentBirthrEgistration = new Intent(this, AdvisoryDetailsActivity.class);
                intentBirthrEgistration.putExtra("category","1");
                startActivity(intentBirthrEgistration);
                break;
            case R.id.tv_zxhk://注销户口
                Intent intent_zxhk = new Intent(this, AdvisoryDetailsActivity.class);
                intent_zxhk.putExtra("category","2");
                startActivity(intent_zxhk);
                break;
            case R.id.tv_hkqy://户口迁移
                Intent intent_hkqy = new Intent(this, AdvisoryDetailsActivity.class);
                intent_hkqy.putExtra("category","3");
                startActivity(intent_hkqy);
                break;
            case R.id.tv_sxbg://事项变更
                Intent intent_sxbg = new Intent(this, AdvisoryDetailsActivity.class);
                intent_sxbg.putExtra("category","4");
                startActivity(intent_sxbg);
                break;
            case R.id.tv_zjbf://证件补发
                Intent intent_zjbf = new Intent(this, AdvisoryDetailsActivity.class);
                intent_zjbf.putExtra("category","5");
                startActivity(intent_zjbf);
                break;
            case R.id.tv_hfhk://恢复户口
                Intent intent_hfhk = new Intent(this, AdvisoryDetailsActivity.class);
                intent_hfhk.putExtra("category","6");
                startActivity(intent_hfhk);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( null != mIat ){
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
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
                if (result_str.contains("出生登记")) {
                    viewId = R.id.tv_birthr_egistration;
                }else if(result_str.contains("注销")){
                    viewId = R.id.tv_zxhk;
                }else if(result_str.contains("迁移")){
                    viewId = R.id.tv_hkqy;
                }else if(result_str.contains("事项变更")){
                    viewId = R.id.tv_sxbg;
                }else if(result_str.contains("证件补发")){
                    viewId = R.id.tv_zjbf;
                }else if(result_str.contains("恢复")){
                    viewId = R.id.tv_hfhk;
                }else {
                    AddData(result_str);
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
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    private Toast mToast;
    private void showTip(final String str)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    private void AddData(final String keyword){
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword",keyword);
        OkhttpUtil.okHttpGet(Api.search, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                if(response!=null){
                    List<Map<String, String>> list = (List<Map<String, String>>) response;
                    if(list.size()>0){
                        Intent intentSearch = new Intent(AdvisoryActivity.this, AdvisorySearchActivity.class);
                        intentSearch.putExtra("keyword",keyword);
                        startActivity(intentSearch);
                    }
                }
            }
        });
    }
}
