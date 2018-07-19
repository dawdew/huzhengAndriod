package com.hp.householdpolicies.utils;

import android.graphics.drawable.Drawable;

import org.xutils.common.Callback;

/**
 * Created by Administrator on 2018-07-19.
 */

public abstract class ImageCallBack implements Callback.CacheCallback<Drawable> {
    @Override
    public boolean onCache(Drawable result) {
        return true;
    }

    @Override
    public void onSuccess(Drawable result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
