package com.phc.neckrreferential.presenter;

import com.phc.neckrreferential.base.IBasePresenter;
import com.phc.neckrreferential.view.ITicketPagerCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/2 14
 * 描述：
 */
public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {
    /**
     * 获取淘口令
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title,String url,String cover);
}
