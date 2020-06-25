package com.phc.neckrreferential.base;

import com.phc.neckrreferential.view.IHoneCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/25 18
 * 描述：
 */
public interface IBasePresenter<T> {
    /**
     * 注册通知接口
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消注册通知接口
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
