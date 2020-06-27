package com.phc.neckrreferential.utils;

import android.util.Log;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/9 22
 * 描述： log的可管控的工具类
 */
public class LogUtils {
    /**
     * 当前的log等级 可以在项目上线时候进行改动，不让低级log出现
     */
    private static int currentLev = 4 ;
    /**
     *     不变的log等级
     */
    private static final int DEBU_GLEV = 4 ;
    private static final int INFO_LEV = 3 ;
    private static final int WARNING_LEV = 2 ;
    private static final int ERROR_LEV = 1 ;

    public static void d (Object object,String log){
        if (currentLev >=  DEBU_GLEV){
            Log.d(object.getClass().getName(), "(DEBUG)\n"+log);
        }
    }
    public static void i (Object object,String log){
        if (currentLev >=  INFO_LEV){
            Log.i(object.getClass().getName(), "(INFO)\n"+log);
        }
    }
    public static void w (Object object,String log){
        if (currentLev >=  WARNING_LEV){
            Log.w(object.getClass().getName(), "(WARNING)\n"+log);
        }
    }
    public static void e (Object object,String log){
        if (currentLev >=  ERROR_LEV){
            Log.e(object.getClass().getName(), "(ERROR)\n"+log);
        }
    }
}
