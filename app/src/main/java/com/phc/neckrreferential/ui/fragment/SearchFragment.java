package com.phc.neckrreferential.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.SearchRecommend;
import com.phc.neckrreferential.modle.domain.SearchResult;
import com.phc.neckrreferential.presenter.ISearchPagePresenter;
import com.phc.neckrreferential.ui.adapter.LinearItemContentAdapter;
import com.phc.neckrreferential.ui.custom.TextFlowLayout;
import com.phc.neckrreferential.utils.KeyboardUtils;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.SizeUtils;
import com.phc.neckrreferential.utils.TicketUtils;
import com.phc.neckrreferential.utils.ToastUtils;
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
public class SearchFragment extends BaseFragment implements ISearchViewCallback, TextFlowLayout.onFlowTextItemClickListener {


    /**
     * 推荐词条view，历史记录view
     */
    @BindView(R.id.search_recommend_view)
    TextFlowLayout mRecommendView;
    @BindView(R.id.search_history_view)
    TextFlowLayout mHistoryView;
    /**
     * 历史记录容器，推荐词条容器，删除历史按钮
     */
    @BindView(R.id.search_history_container)
    LinearLayout mHistoryContainer;
    @BindView(R.id.search_recommend_container)
    LinearLayout mRecommendContainer;
    @BindView(R.id.search_history_delete)
    ImageView mHistoryDelete;
    /**
     * 刷新和排列控件
     */
    @BindView(R.id.search_result_list)
    RecyclerView mResultList;
    @BindView(R.id.search_result_container)
    TwinklingRefreshLayout mRefreshContainer;
    /**
     * 搜索按钮，输入框，清除输入按钮，
     */
    @BindView(R.id.search_btn)
    TextView mSearchBtn;
    @BindView(R.id.search_input_box)
    EditText mSearchInputBox;
    @BindView(R.id.search_clean_btn)
    ImageView mCleanInputBtn;


    private ISearchPagePresenter mSearchPresenter = null;
    private LinearItemContentAdapter mSearchResultAdapter;


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
        // 设置查询内容的布局管理器和适配器
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResultAdapter = new LinearItemContentAdapter();
        mResultList.setAdapter(mSearchResultAdapter);
        //设置刷新器的工作范围，和越界回弹
        mRefreshContainer.setEnableLoadmore(true);
        mRefreshContainer.setEnableRefresh(false);
        mRefreshContainer.setEnableOverScroll(true);
        //设置结果列表显示的间距
        mResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view
                    , @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //这个参数好像越拉越大
                outRect.top = SizeUtils.dip2px(getContext(), 2f);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2f);
            }
        });

    }

    /**
     * 判断是否有文字
     *
     * @param containSpace 是否需要判断空格，true是不需要，false是需要
     * @return
     */
    private boolean hasInput(boolean containSpace) {
        if (containSpace) {
            return mSearchInputBox.getText().toString().length() > 0;
        } else {
            return mSearchInputBox.getText().toString().trim().length() > 0;
        }
    }


    @Override
    protected void initListener() {
        //推荐和历史的点击事件
        mHistoryView.setOnFlowTextItemClickListener(this);
        mRecommendView.setOnFlowTextItemClickListener(this);
//        搜索按钮的搜索发起
        mSearchBtn.setOnClickListener(v -> {
            if (hasInput(false)) {
                if (mSearchPresenter != null) {
//                    mSearchPresenter.doSearch(mSearchInputBox.getText().toString());
                    toSearch(mSearchInputBox.getText().toString());
                    //需要收起键盘
                    KeyboardUtils.hide(getContext(), v);
                }
            }else {
                KeyboardUtils.hide(getContext(), v);
            }
        });
//        mCleanInputBtn删除输入框内容的事件
        mCleanInputBtn.setOnClickListener(v -> {
            //需要按了清除历史，需要清空输入框And显示历史记录和推荐
            mSearchInputBox.setText("");
            switch2HistoryPage();
        });
        //监听输入框的变换，如果有字存在，就显示清空文字的按钮
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                文字变化前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //文字变化的时候，如果长度不为0，显示删除按钮，
                mCleanInputBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
                //文字变化的时候，如果有内容，就搜索框右侧按钮修改为搜索
                mSearchBtn.setText(hasInput(false) ? "搜索" : "取消");

            }

            @Override
            public void afterTextChanged(Editable s) {
                //文字变化后
            }
        });
        //editText的搜索事件,是编辑器的事件，这个id是标识符的事件，我设置的是搜索，这里需要拿到搜索标识符IME_ACTION_SEARCH
        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //标识符为搜索,进行搜索动作
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    //判断拿到的内容是否为null
                    String keyWord = v.getText().toString();
                    if (TextUtils.isEmpty(keyWord)) {
                        return false;
                    }
//                    mSearchPresenter.doSearch(keyWord);
                    logUtils.d(this, "用户输入---》" + keyWord);
                    toSearch(keyWord);
                }
                //返回事件是否结束
                return false;
            }
        });

        //删除历史按钮的点击事件
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空历史
                mSearchPresenter.delHistories();
            }
        });
        //设置刷新控件的刷新事件
        mRefreshContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //开始加载更多
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });
        //设置item的点击事件
        mSearchResultAdapter.setOnListItemClickListener(item -> {
            //item被点击了
            TicketUtils.toTicketPage(getContext(), item);
        });
    }

    /**
     * 切换到历史和推荐界面
     */
    private void switch2HistoryPage() {
        if (mSearchPresenter != null ) {
            mSearchPresenter.getHistories();
        }
        mHistoryContainer.setVisibility(mHistoryView.getContentSize() != 0 ? View.VISIBLE : View.GONE);
        mRecommendContainer.setVisibility(mRecommendView.getContentSize() != 0 ? View.VISIBLE : View.GONE);
        //隐藏内容
        mRefreshContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onRetryClick() {
        //重新搜索
        mSearchPresenter.reSearch();
    }

    @Override
    public void onHistoriesLoaded(@NotNull List<String> histories) {
//        logUtils.d(this, "onHistoriesLoaded" + histories.toString());
        //判断是否显示,优先执行第一个条件，如果第一个条件没满足就不会进行下一个条件，如果传入的是null就证明没有数据，需要隐藏
        if (histories == null || histories.size() == 0) {
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
        //切换成成功状态
        setUpState(State.SUCCESS);
        //得到结果成功，需要隐藏掉历史记录和推荐来显示搜索结果
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        //确保显示并设置数据
        mRefreshContainer.setVisibility(View.VISIBLE);
        try {
            mSearchResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mRefreshContainer.finishLoadmore();
        //这里处理加载更多的结果 ，数据添加到适配器
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> moreData
                = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(moreData);
    }

    @Override
    public void onMoreLoadedError() {
        mRefreshContainer.finishLoadmore();
        ToastUtils.showToast("网络异常，请检查网络或稍后重试");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mRefreshContainer.finishLoadmore();
        ToastUtils.showToast("到底了，没有更多的数据了");
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
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onFlowItemClick(String text) {
        //发起搜索
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mResultList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchPresenter.doSearch(text);
        }
    }
}
