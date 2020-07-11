package com.phc.neckrreferential.modle.domain;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/11 09
 * 描述：
 */
public class CacheWithDuration {
    private Long duration;
    private String cache;

    public CacheWithDuration(Long duration, String cache) {
        this.duration = duration;
        this.cache = cache;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}
