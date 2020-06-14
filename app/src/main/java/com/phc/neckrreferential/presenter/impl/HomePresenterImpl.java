package com.phc.neckrreferential.presenter.impl;

import com.phc.neckrreferential.modle.Api;
import com.phc.neckrreferential.modle.domain.Categories;
import com.phc.neckrreferential.presenter.IHomePresenter;
import com.phc.neckrreferential.utils.LogUtils;
import com.phc.neckrreferential.utils.RetrofitManager;
import com.phc.neckrreferential.view.IHoneCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/11 15
 * 描述：
 */
public class HomePresenterImpl implements IHomePresenter {

    private IHoneCallback mCallBack;

    @Override
    public void getCategories() {
        //调用单例模式，得到build好的Retrofit对象
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();

        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this,""+code);
                if (code == HttpURLConnection.HTTP_OK){
                    //请求成功
                    Categories categories = response.body();
                    LogUtils.d(HomePresenterImpl.this,categories.toString());
                    if (mCallBack != null){
                        mCallBack.onCategoriesLoaded(categories);

                    }
                }else {
                    //请求失败
                    LogUtils.d(HomePresenterImpl.this,"请求失败");
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败的结果
                // TODO:
                LogUtils.e(HomePresenterImpl.this,"请求失败"+t);
            }
        });
    }

    /**
     *
     * @param callback 返回请求
     */
    @Override
    public void registerCallback(IHoneCallback callback) {
        this.mCallBack = callback;

    }

    /**
     *
     * @param callback 返回取消请求
     */

    @Override
    public void unregisterCallback(IHoneCallback callback) {
        mCallBack = null;
    }
}
