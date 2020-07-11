package com.phc.neckrreferential.presenter;

import com.phc.neckrreferential.base.IBasePresenter;
import com.phc.neckrreferential.view.ISearchViewCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/10 18
 * 描述：
 */
public interface ISearchPagePresenter extends IBasePresenter<ISearchViewCallback> {
    /**
     * 获取历史记录
     */
    void getHistories();

    /**
     * 删除记录
     */
    void delHistories();

    /**
     * 开始搜索
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void reSearch();

    /**
     * 加载更多
     */
    void loaderMore();

    /**
     * 获得推荐词
     */
    void getRecommendWords();

}
