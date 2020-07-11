package com.phc.neckrreferential.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.phc.neckrreferential.base.BaseApplication;
import com.phc.neckrreferential.modle.domain.CacheWithDuration;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/11 08
 * 描述：
 */
public class JsonCacheUtils {

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    private JsonCacheUtils() {
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }


    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String valueStr = mGson.toJson(value);
        if (duration != -1) {
            duration += System.currentTimeMillis();
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        edit.putString(key, cacheWithTime);
        edit.apply();
    }

    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        //对时间进行判断
        Long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() <= 0) {
            return null;
        } else {
            String cache = cacheWithDuration.getCache();
            return mGson.fromJson(cache, clazz);
        }
    }

    private static JsonCacheUtils sJsonCacheUtils = null;

    public static JsonCacheUtils getInstance() {
        if (sJsonCacheUtils == null) {
            sJsonCacheUtils = new JsonCacheUtils();
        }
        return null;
    }
}
