package com.hp.householdpolicies.utils;

import android.os.Bundle;

import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by yxDELL on 2018/8/29.
 */

public abstract class MsynthesizerListener implements SynthesizerListener {
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
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }
}
