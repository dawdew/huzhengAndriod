package com.hp.householdpolicies.activity;

import android.app.Activity;
import android.app.Application;

import com.reeman.nerves.RobotActionProvider;

import org.xutils.ImageManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MyApp extends Application{
    private static List<Activity> lists = new ArrayList<>();
//    public   RobotActionProvider robotActionProvider;

    @Override
    public void onCreate() {
        x.Ext.init(this);
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
}
