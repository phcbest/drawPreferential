package com.phc.neckrreferential.view;

import com.phc.neckrreferential.base.IBaseCallBack;
import com.phc.neckrreferential.modle.domain.OnSellContent;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/8 14
 * 描述：
 */
public interface IOnSellPagerCallback extends IBaseCallBack {

    void onContentLoadedSuccess(OnSellContent result);

    void onMoreLoaded(OnSellContent moreResult);

    void onMoreLoadedError();

    void onMoreLoadedEmpty();
}
