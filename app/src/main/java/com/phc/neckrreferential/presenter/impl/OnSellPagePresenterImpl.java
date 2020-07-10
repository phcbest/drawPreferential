package com.phc.neckrreferential.presenter.impl;

import com.phc.neckrreferential.modle.Api;
import com.phc.neckrreferential.modle.domain.OnSellContent;
import com.phc.neckrreferential.presenter.IOnSellPagePresenter;
import com.phc.neckrreferential.utils.RetrofitManager;
import com.phc.neckrreferential.utils.UrlUtils;
import com.phc.neckrreferential.view.IOnSellPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/8 16
 * 描述：
 */
public class OnSellPagePresenterImpl implements IOnSellPagePresenter {

    //page
    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellPagerCallback mOnSellPageCallback = null;
    private final Api mApi;


    public OnSellPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }


    @Override
    public void getOnSellContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //通知ui开始加载
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onLoading();
        }
        //获取特惠内容
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });

    }

    private void onSuccess(OnSellContent result) {
        if (mOnSellPageCallback != null) {
            try {
                if (isEmpty(result)) {
                    onEmpty();
                } else {
                    mOnSellPageCallback.onContentLoadedSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private boolean isEmpty(OnSellContent result) {
        return result.getData().getTbk_dg_optimus_material_response() == null;
//        int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
//        return size == 0;

    }

    private void onEmpty() {
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onEmpty();
        }
    }

    private void onError() {
        mIsLoading = false;
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onError();
        }
    }

    @Override
    public void reLoad() {
        //重新加载
        this.getOnSellContent();
    }

    /**
     * 当前加载状态
     */
    private boolean mIsLoading = false;


    @Override
    public void loaderMore() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //加载更多
        mCurrentPage++;
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onMoreLoaded(result);
                } else {
                    onMoreLoadedError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onMoreLoadedError();
            }
        });
    }

    private void onMoreLoadedError() {
        mIsLoading = false;
        mCurrentPage--;
        mOnSellPageCallback.onMoreLoadedError();
    }

    private void onMoreLoaded(OnSellContent result) {
        if (mOnSellPageCallback != null) {
            if (isEmpty(result)) {
                mCurrentPage--;
                mOnSellPageCallback.onMoreLoadedEmpty();
            } else {
                mOnSellPageCallback.onMoreLoaded(result);
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSellPagerCallback callback) {
        this.mOnSellPageCallback = callback;

    }

    @Override
    public void unregisterViewCallback(IOnSellPagerCallback callback) {
        this.mOnSellPageCallback = null;
    }
}
