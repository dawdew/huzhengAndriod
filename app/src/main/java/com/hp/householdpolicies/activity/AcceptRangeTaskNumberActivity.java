package com.hp.householdpolicies.activity;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hp.householdpolicies.R;
import com.hp.householdpolicies.utils.Api;
import com.hp.householdpolicies.utils.ApiCallBack;
import com.hp.householdpolicies.utils.GlideImageLoader;
import com.hp.householdpolicies.utils.OkhttpUtil;
import com.iflytek.cloud.SpeechSynthesizer;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AcceptRangeTaskNumberActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tv_content;//介绍

    @BindView(R.id.sv_content)
    ScrollView sv_content;//介绍

    @BindView(R.id.restart)
    ImageView restart;//重说
    @BindView(R.id.mute)
    ImageView mute;//静音

    @BindView(R.id.banner)
    Banner banner;//图片
    private List<Map<String, String>> hallList;
    MediaPlayer mediaPlayer;
    boolean ismute=false;
    boolean isScroll=false;
    Timer timer;
    AutoScrollTask autoTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_accept_range_task_number);
        ButterKnife.bind(this);
        banner.setImageLoader(new GlideImageLoader());
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

        AddData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        int stringInt = 0;
        int rawType = 0;
        switch (type){
            case "公安南开分局人口管理中心":
                rawType = R.raw.zxjj;
                stringInt=R.string.hall;
                break;
            case "大厅":
                rawType =  R.raw.dating;
                stringInt=R.string.hukou;
                break;
            case "身份证":
                rawType = R.raw.sfz;
                stringInt=R.string.shenfenzheng;
                break;
            case "户政":
                rawType = R.raw.huxian;
                stringInt=R.string.huxian;
                break;
        }

        mediaPlayer = MediaPlayer.create(this,rawType);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        timer=new Timer();
        isScroll=true;
        autoTask=new AutoScrollTask();
        timer.schedule(autoTask, 3, 100);
        tv_content.setText(stringInt);
    }

    @Override
    protected void onStop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onStop();
    }

    private void AddData(){
        Intent intent = getIntent();
        HashMap<String, String> map = new HashMap<>();
        map.put("type",intent.getStringExtra("type"));
        OkhttpUtil.okHttpGet(Api.hall, map, new ApiCallBack() {
            @Override
            public void onResponse(Object response) {
                hallList = (List<Map<String, String>>) response;
                if(hallList.size()>0){
                    ArrayList<String> images = new ArrayList<>();
//                    ArrayList<String> title = new ArrayList<>();
                    for(Map<String, String> m:hallList){
                        images.add(Api.baseFile+m.get("src"));
//                        title.add(m.get("desc"));
                    }
                    banner.setImages(images);
//                    banner.setBannerTitles(title);
                    banner.start();
                }
            }
        });
    }
    @OnClick({R.id.restart,R.id.mute})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.restart:
                mediaPlayer.start();
                ismute=false;
                mute.setImageResource(R.mipmap.icon_03);
                break;
            case R.id.mute:
                if(ismute){
                    ismute=false;
                    mute.setImageResource(R.mipmap.icon_03);
                    mediaPlayer.start();
                }else {
                    ismute=true;
                    mute.setImageResource(R.mipmap.icon1_03);
                    mediaPlayer.pause();
                }
                break;
        }
    }
    class AutoScrollTask extends TimerTask {
        public void run() {
            if (isScroll) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
    }
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    //如果没有到底端，Y偏移量增加10
                    if(sv_content.getScrollY()<tv_content.getMeasuredHeight()-10){
                        sv_content.scrollBy(0, 2);
                    }
                    else {
                        //直接到底端
                        sv_content.scrollTo(0, tv_content.getMeasuredHeight());
                    }
                }
                super.handleMessage(msg);
            }
        };

    }
