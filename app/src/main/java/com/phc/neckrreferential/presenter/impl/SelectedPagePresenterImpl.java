package com.phc.neckrreferential.presenter.impl;

import com.phc.neckrreferential.modle.Api;
import com.phc.neckrreferential.modle.domain.SelectedContent;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;
import com.phc.neckrreferential.presenter.ISelectedPagePresenter;
import com.phc.neckrreferential.utils.RetrofitManager;
import com.phc.neckrreferential.utils.UrlUtils;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/6 09
 * 描述：
 */
public class SelectedPagePresenterImpl implements ISelectedPagePresenter {


    private ISelectedPageCallback mViewCallback = null;
    private final Api mApi;

    public SelectedPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);


    }

    @Override
    public void getCategory() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                //显示请求结果
                int resultCode = response.code();
                logUtils.d(this, "精选界面请求代码===" + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedPageCategory result = response.body();
                    //通知ui更新
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriseLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadedError();
            }


        });
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {

        int categoryId = item.getFavorites_id();
        String targetUrl = UrlUtils.getSelectedPageContentUrl(categoryId);
        Call<SelectedContent> task = mApi.getSelectedPageContent(targetUrl);
        logUtils.d(this, "精选界面请求内容参数" + item.getFavorites_id());


        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                //显示请求结果
                int resultCode = response.code();
                logUtils.d(this, "精选界面请求内容返回码===" + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedContent result = response.body();
                    //通知ui更新
                    if (mViewCallback != null) {
                        mViewCallback.onContentLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadedError();
            }
        });


    }

    @Override
    public void reLoadContent() {
        this.getCategory();
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = null;
    }
}
