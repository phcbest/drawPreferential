package com.phc.neckrreferential.utils;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/25 19
 * 描述：
 */
public class UrlUtils {
    public static String createHomePagerUrl(int materialId,int page){
        return "discovery/"+materialId+"/"+page;
    }

    public static String getCoverPath(String pict_url) {
        return "https:"+pict_url;
    }
}
