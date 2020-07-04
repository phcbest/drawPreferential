package com.phc.neckrreferential.view;

import com.phc.neckrreferential.base.IBaseCallBack;
import com.phc.neckrreferential.modle.domain.TicketResult;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/2 14
 * 描述：
 */
public interface ITicketPagerCallback extends IBaseCallBack {
    /**
     * 淘口令加载结果
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);
}
