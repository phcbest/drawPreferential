package com.phc.neckrreferential.presenter;

import com.phc.neckrreferential.base.IBasePresenter;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;
import com.phc.neckrreferential.view.ISelectedPageCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/5 21
 * 描述：
 */
public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {
    /**
     * 获得分类
     */
    void getCategory();

    /**
     * 获得内容
     * @param item 返回结果
     */
    void getContentByCategory(SelectedPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reLoadContent();
}
