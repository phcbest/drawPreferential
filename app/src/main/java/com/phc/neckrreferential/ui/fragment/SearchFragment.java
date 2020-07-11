package com.phc.neckrreferential.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.SearchRecommend;
import com.phc.neckrreferential.modle.domain.SearchResult;
import com.phc.neckrreferential.presenter.ISearchPagePresenter;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.ISearchViewCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/6 10
 * 描述：
 */
public class SearchFragment extends BaseFragment implements ISearchViewCallback {


    private ISearchPagePresenter mSearchPresenter = null;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }


    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取搜索推荐词
        mSearchPresenter.getRecommendWords();
        //进行搜索
        mSearchPresenter.doSearch("keyBoard");
        //获取历史记录
        mSearchPresenter.getHistories();

    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

    @Override
    public void onHistoriesLoaded(@NotNull List<String> histories) {
        logUtils.d(this,"onHistoriesLoaded"+histories.toString());
    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        logUtils.d(this,"onSearchSuccess"+result);
    }

    @Override
    public void onMoreLoaded(SearchResult result) {

    }

    @Override
    public void onMoreLoadedError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        logUtils.d(this,"推荐词条数量"+recommendWords.size());
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
