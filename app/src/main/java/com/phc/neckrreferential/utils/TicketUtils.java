package com.phc.neckrreferential.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.phc.neckrreferential.modle.domain.IBaseInfo;
import com.phc.neckrreferential.presenter.ITicketPresenter;
import com.phc.neckrreferential.ui.activity.TicketActivity;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/10 17
 * 描述：
 */
public class TicketUtils {
    public static void toTicketPage(Context context ,IBaseInfo baseInfo){
        //item被点击了 ,需要进入下一个活动
        String title = baseInfo.getTitle();
        String url = baseInfo.getUrl();
        if (TextUtils.isEmpty(url)) {
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        // 在跳转之前处理数据，不会有停滞感
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
