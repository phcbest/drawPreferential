package com.phc.neckrreferential.view;

import com.phc.neckrreferential.base.IBaseCallBack;
import com.phc.neckrreferential.modle.domain.SearchRecommend;
import com.phc.neckrreferential.modle.domain.SearchResult;

import java.util.List;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/10 18
 * 描述：
 */
public interface ISearchViewCallback extends IBaseCallBack {

    /**
     * 加载历史记录
     * @param histories
     */
    void onHistoriesLoaded(List<String> histories);

    /**
     * 删除历史记录
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载更多成功
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更失败
     */
    void onMoreLoadedError();

    /**
     * 加载更多为空
     */
    void onMoreLoadedEmpty();

    /**
     * 推荐词
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
