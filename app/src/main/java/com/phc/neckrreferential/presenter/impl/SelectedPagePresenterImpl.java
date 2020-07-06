package com.phc.neckrreferential.presenter.impl;

import com.phc.neckrreferential.modle.Api;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;
import com.phc.neckrreferential.presenter.ISelectedPagePresenter;
import com.phc.neckrreferential.utils.RetrofitManager;
import com.phc.neckrreferential.view.ISelectedPageCallback;

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
    @Override
    public void getCategory() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<SelectedPageCategory> selectPageCategories = api.getSelectPageCategories();
        selectPageCategories.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {

            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {

            }
        });
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {

    }

    @Override
    public void reLoadContent() {

    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {

    }
}
