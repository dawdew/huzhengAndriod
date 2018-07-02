package com.hp.householdpolicies.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class BaseMethod {

    //获取当前时间
    public static String getDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    //获取当前时间日期
    public static String getDateNoW() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        return date;
    }


}
