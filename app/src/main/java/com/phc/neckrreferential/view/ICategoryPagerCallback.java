package com.phc.neckrreferential.view;

import com.phc.neckrreferential.base.IBaseCallBack;
import com.phc.neckrreferential.modle.domain.HomePagerContent;

import java.util.List;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/25 17
 * 描述：
 */
public interface ICategoryPagerCallback extends IBaseCallBack {
    /**
     * 数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents );

    int getCategoryId();


    /**
     * 加载更多网络错误
     */
    void onLoaderMoreError();
    /**
     * 没有更多内容
     */
    void onLoaderMoreEmpty();

    /**
     * 加到了更多内容
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean > contents);
    /**
     * 加到了轮播图
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean > contents);


}
