package com.phc.neckrreferential.presenter;

import com.phc.neckrreferential.base.IBasePresenter;
import com.phc.neckrreferential.view.IOnSellPagerCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/8 14
 * 描述：
 */
public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPagerCallback> {

    void getOnSellContent();

    /**
     * 重新加载内容
     */
    void reLoad();

    void loaderMore();
}
