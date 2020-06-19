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

    /**
     * 这个是方法的核心
     */
    @Override
    public void getCategories() {
        //如果传递进来的IHoneCallback不为null
        if (mCallBack != null) {
            //调用该接口中的onLoading抽象方法，切换加页面
            mCallBack.onLoading();
        }
        //调用单例模式，得到build好的Retrofit对象
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();

        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            /**
             *  成功
             */
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //请求结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功，response对象的.body就是javaBean类
                    Categories categories = response.body();
                    //如果接口不等于null
                    if (mCallBack != null) {
                        //如果categories内部是没数据的
                        if (categories == null || categories.getData().size() == 0) {
                            //切换空页面
                            mCallBack.onEmpty();
                        }else {
                            //切换加载成功页面，并且设置tabLayout的文字
                            mCallBack.onCategoriesLoaded(categories);
                        }
                    }
                } else {
                    //请求失败
                    LogUtils.d(HomePresenterImpl.this, "请求失败");
                    if (mCallBack!=null) {
                        //切换请求失败页面
                        mCallBack.onNetWorkError();
                    }
                }
            }

            /**
             * 失败
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败的结果
                // TODO:
                LogUtils.e(HomePresenterImpl.this, "请求失败" + t);
                if (mCallBack!=null) {
                    mCallBack.onNetWorkError();
                }
            }
        });
    }

    /**
     * @param callback 返回请求
     */
    @Override
    public void registerCallback(IHoneCallback callback) {
        this.mCallBack = callback;

    }

    /**
     * @param callback 返回取消请求
     */

    @Override
    public void unregisterCallback(IHoneCallback callback) {
        //将IHoneCallback清空
        mCallBack = null;
    }
}
