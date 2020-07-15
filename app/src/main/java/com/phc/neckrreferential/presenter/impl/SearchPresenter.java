package com.phc.neckrreferential.presenter.impl;

import com.phc.neckrreferential.modle.Api;
import com.phc.neckrreferential.modle.domain.Histories;
import com.phc.neckrreferential.modle.domain.SearchRecommend;
import com.phc.neckrreferential.modle.domain.SearchResult;
import com.phc.neckrreferential.presenter.ISearchPagePresenter;
import com.phc.neckrreferential.utils.JsonCacheUtils;
import com.phc.neckrreferential.utils.RetrofitManager;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.ISearchViewCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/10 19
 * 描述：
 */
public class SearchPresenter implements ISearchPagePresenter {

    private final Api mApi;
    private ISearchViewCallback mSearchViewCallback = null;

    public static final int DEFAULT_HISTORIES_SIZE = 9;
    private int mHistoriesMaxSize = DEFAULT_HISTORIES_SIZE;


    public static final int DEFAULT_PAGE = 0;
    /**
     * 搜索的当前页
     */
    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyword = null;
    private final JsonCacheUtils mJsonCacheUtils;

    public SearchPresenter() {
        //进行retrofit初始化
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtils = JsonCacheUtils.getInstance();

    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtils.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchViewCallback != null && histories != null
                && histories.getHistories() != null && histories.getHistories().size() != 0) {
            logUtils.d(this, "presenter中的历史记录数据" + histories.getHistories().toString());
            mSearchViewCallback.onHistoriesLoaded(histories.getHistories());
        } else {
            //必须要回调该方法，用来控制组件是否显示
            mSearchViewCallback.onHistoriesLoaded(null);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtils.delCache(KEY_HISTORIES);
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onHistoriesDeleted();
        }
    }

    public static final String KEY_HISTORIES = "key_histories";

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtils.getValue(KEY_HISTORIES, Histories.class);
        logUtils.d(this,""+histories);
        //如果关键字存在就干掉后添加
        List<String> historiesList = null;
        if (histories != null && histories.getHistories().size() != 0) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }

        //限制个数
        if (historiesList.size() > mHistoriesMaxSize) {
            historiesList = historiesList.subList(0, mHistoriesMaxSize);
            historiesList.remove(0);
        }
        historiesList.add(history);

        histories.setHistories(historiesList);
        logUtils.d(this, "保存进去的搜索历史" +historiesList.toString());
        //保存记录
        mJsonCacheUtils.saveCache(KEY_HISTORIES, histories);
    }


    @Override
    public void doSearch(String keyword) {
        //将用户输入的关键字转为全局变量
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)) {
            //保存搜索关键字
            this.saveHistory(keyword);
            this.mCurrentKeyword = keyword;
        }
        //更新ui状态
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });
    }

    private void handleSearchResult(SearchResult result) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(result)) {
                //数据为null
                mSearchViewCallback.onEmpty();
            } else {
                mSearchViewCallback.onSearchSuccess(result);
            }
        }
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response()
                    .getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            //TODO 可能有bug ，返回真假的逻辑没捋顺
            return false;
        }
    }

    private void onError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onError();
        }
    }

    @Override
    public void reSearch() {
        if (mCurrentKeyword == null) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            //进行重新搜索
            this.doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;
        if (mCurrentKeyword == null) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            //搜索
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderMoreError();

            }
        });

    }

    /**
     * 处理加载更多的结果
     *
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(result)) {
                //数据为null
                mSearchViewCallback.onMoreLoadedEmpty();
            } else {
                mSearchViewCallback.onMoreLoaded(result);
            }
        }
    }

    /**
     * 加载更多失败了
     */
    private void onLoaderMoreError() {
        mCurrentPage--;
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onMoreLoadedError();
        }
    }

    @Override
    public void getRecommendWords() {
        //拿到推荐的数据
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    if (mSearchViewCallback != null) {
                        mSearchViewCallback.onRecommendWordsLoaded(response.body().getData());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
            }
        });
    }


    @Override
    public void registerViewCallback(ISearchViewCallback callback) {
        this.mSearchViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchViewCallback callback) {
        this.mSearchViewCallback = null;
    }
}
