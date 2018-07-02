package com.hp.householdpolicies.utils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018-05-02.
 */

public abstract class ApiCallBack extends CallBackUtil {
    @Override
    public Object onParseResponse(Call call, Response response) {
        if(response.code()!=200){
            return null;
        }
        ResponseBody body = response.body();
        String ddd = null;
        try {
            ddd = body.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap result = JsonParser.fromJson(ddd, HashMap.class);
        return result.get("data");
    }

    @Override
    public void onFailure(Call call, Exception e) {
        e.printStackTrace();
    }
}
