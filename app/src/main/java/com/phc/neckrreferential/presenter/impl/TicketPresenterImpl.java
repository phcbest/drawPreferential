package com.phc.neckrreferential.presenter.impl;

import com.phc.neckrreferential.modle.Api;
import com.phc.neckrreferential.modle.domain.TicketResult;
import com.phc.neckrreferential.modle.domain.TicketParams;
import com.phc.neckrreferential.presenter.ITicketPresenter;
import com.phc.neckrreferential.utils.RetrofitManager;
import com.phc.neckrreferential.utils.UrlUtils;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/2 15
 * 描述：
 */
public class TicketPresenterImpl implements ITicketPresenter {


    private ITicketPagerCallback mViewCallback = null;
    private String mCover = null;
    private TicketResult mTicketResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        this.onTicketLoading();
        this.mCover = cover;
        String targetUrl = UrlUtils.getTicketUrl(url);
        logUtils.d(this, "请求数据====" + title + "\n" + targetUrl + "\n" + cover);
        //获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(targetUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                logUtils.d(this, "状态码====" + code);

                if (code == HttpURLConnection.HTTP_OK) {
                    mTicketResult = response.body();
                    logUtils.d(this, "返回数据====" + mTicketResult.toString());
                    //通知ui更新成功页面
                    onTicketLoadedSuccess();
                } else {
                    onLoadedTicketError();
                }

            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                //请求失败
                onLoadedTicketError();
            }
        });

    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        }else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback!=null) {
            mViewCallback.onError();
        }else {
            mCurrentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = callback;

        if (mCurrentState != LoadState.NONE) {
            //状态改变更新ui
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadedSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoading();
            }
        }
    }

    private void onTicketLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = callback;
    }
}
