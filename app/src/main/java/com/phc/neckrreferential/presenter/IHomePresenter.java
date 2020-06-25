package com.phc.neckrreferential.presenter;

import com.phc.neckrreferential.base.IBasePresenter;
import com.phc.neckrreferential.view.IHoneCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/11 15
 * 描述：该层负责逻辑处理
 */
public interface IHomePresenter extends IBasePresenter<IHoneCallback> {
    /**
     * 获取商品分类
     */
    void getCategories ();



}
