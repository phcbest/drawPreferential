package com.phc.neckrreferential.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.SearchRecommend;
import com.phc.neckrreferential.modle.domain.SearchResult;
import com.phc.neckrreferential.presenter.ISearchPagePresenter;
import com.phc.neckrreferential.ui.custom.TextFlowLayout;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.ISearchViewCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/6 10
 * 描述：
 */
public class SearchFragment extends BaseFragment implements ISearchViewCallback {


    @BindView(R.id.search_recommend_view)
    TextFlowLayout mRecommendView;
    @BindView(R.id.search_history_view)
    TextFlowLayout mHistoryView;

    @BindView(R.id.search_history_container)
    LinearLayout mHistoryContainer;
    @BindView(R.id.search_recommend_container)
    LinearLayout mRecommendContainer;

    @BindView(R.id.search_history_delete)
    ImageView mHistoryDelete;

    @BindView(R.id.search_result_list)
    RecyclerView searchResultList;
    @BindView(R.id.search_result_container)
    TwinklingRefreshLayout searchResultContainer;

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
        //给自定义view数据
//        String [] data = {"守望先锋","门阵特工","overWatch","我需要治疗","我的终极技能正在充能 0%","毁天灭地","上勾拳","你好啊","打得不错"};
//        ArrayList<String> strings = new ArrayList<>(Arrays.asList(data));
//        mHistoryView.setOnFlowTextItemClickListener(ToastUtils::showToast);
    }

    @Override
    protected void initListener() {
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空历史
                mSearchPresenter.delHistories();
            }
        });
    }

    @Override
    public void onHistoriesLoaded(@NotNull List<String> histories) {
//        logUtils.d(this, "onHistoriesLoaded" + histories.toString());
        //判断是否显示,优先执行第一个条件，如果第一个条件没满足就不会进行下一个条件，如果传入的是null就证明没有数据，需要隐藏
        if (histories == null || histories.size() == 0 ) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistoryView.setTextList(histories);
        }
    }


    @Override
    public void onHistoriesDeleted() {
        //接到删除的通知，需要更新ui
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        logUtils.d(this, "onSearchSuccess" + result);
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
        logUtils.d(this, "推荐词条数量" + recommendWords.size());
        List<String> reCommendKeyWords = new ArrayList<>();
        for (SearchRecommend.DataBean item : recommendWords) {
            reCommendKeyWords.add(item.getKeyword());
        }
        if (recommendWords == null || recommendWords.size() == 0) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendContainer.setVisibility(View.VISIBLE);
            mRecommendView.setTextList(reCommendKeyWords);
        }
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
