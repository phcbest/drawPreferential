package com.phc.neckrreferential.base;

import android.app.Application;
import android.content.Context;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/29 11
 * 描述：
 */
public class BaseApplication extends Application {
    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
    }

    public static Context getAppContext(){
        return appContext;
    }
}
