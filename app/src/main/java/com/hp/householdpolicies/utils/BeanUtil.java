package com.hp.householdpolicies.utils;

import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhuxiaomeng
 * @date 2017/12/18.
 * @email 154040976@qq.com
 * 对象操作
 */
public class BeanUtil {

    public static Object getFieldValue(Object obj,String fieldName){
        try {
        if(obj==null || StringUtils.isBlank(fieldName)){
            return obj;
        }
        Class userCla =  obj.getClass();
        String getMethodName = "get" + upperFirstLetter(fieldName);
        Method method = null;

            method = userCla.getMethod(getMethodName);

        if(method!=null){
            obj = method.invoke(obj);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static Object setFieldValue(Object obj,String fieldName,Object param){
        try {
            if(obj==null || StringUtils.isBlank(fieldName)){
                return obj;
            }
            Class userCla =  obj.getClass();
            String getMethodName = "set" + upperFirstLetter(fieldName);
            Method method = null;
            method = userCla.getDeclaredMethod(getMethodName,param.getClass());

            if(method!=null){
                obj = method.invoke(obj,param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * 首字母大写
     * @param letter
     * @return
     */
    public static String upperFirstLetter(String letter){
        if(StringUtils.isBlank(letter)){
            return letter;
        }
        String firstLetter = letter.substring(0, 1).toUpperCase();
        return firstLetter + letter.substring(1);
    }
    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        for(int i=0;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }
    public static String BooleanToString(Boolean value){
        if(value!=null){
            return value.toString();
        }
        return "false";
    }
    public static String toString(String value){
        if(StringUtils.isNotBlank(value)){
            return value;
        }
        return "";
    }
}
