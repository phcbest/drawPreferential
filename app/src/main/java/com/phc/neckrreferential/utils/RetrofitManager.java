package com.phc.neckrreferential.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/12 10
 * 描述： retrofit 的管理者，调用getRetrofit可以拿到mRetrofit对象来访问网络
 */
public class RetrofitManager {

    private static final RetrofitManager ourInstance = new RetrofitManager();
    private final Retrofit mRetrofit;

    //这是单例模式，调用这个方法可返回实例化的对象
    public static RetrofitManager getInstance() {
        return ourInstance;
    }

    private RetrofitManager() {
        //创建retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                //工厂转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //
    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
