package com.phc.neckrreferential.presenter;

import com.phc.neckrreferential.base.IBasePresenter;
import com.phc.neckrreferential.view.ICategoryPagerCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/25 17
 * 描述：
 */
public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {
    /**
     * 根据分类id获取内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void loaderMore(int categoryId);

    void reload(int categoryId);



}
