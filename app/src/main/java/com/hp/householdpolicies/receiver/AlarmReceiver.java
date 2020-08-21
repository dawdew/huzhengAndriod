package com.hp.householdpolicies.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hp.householdpolicies.activity.MyApp;
import com.hp.householdpolicies.model.PrintFormat;
import com.reeman.nerves.RobotActionProvider;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yxDELL on 2018/11/26.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyApp app = (MyApp)context.getApplicationContext();
        String xy = app.getContactLocations().get("充电");
        PrintFormat.PEOPLECOUNT = 0;
        PrintFormat.NUMBER = 0;
        if (StringUtils.isNotBlank(xy)) {
            RobotActionProvider.getInstance().sendRosCom("goal:charge[" + xy + "]");
        }
    }
}
