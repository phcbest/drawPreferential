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

    public static String getCoverPath(String pict_url,int size) {
        if (pict_url.startsWith("http")||pict_url.startsWith("https")) {
            return pict_url;
        }else {
            return "https:"+pict_url+"_"+size+"x"+size+".jpg";
        }
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http")||url.startsWith("https")) {
            return url;
        }else {
            return "https:"+url;
        }
    }

    public static String getSelectedPageContentUrl(int categoryId) {
        return "recommend/"+categoryId;
    }

    public static String getOnSellPageUrl(int page) {
        return "onSell/"+page;
    }
}
