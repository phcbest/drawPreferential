package com.phc.neckrreferential.view;

import com.phc.neckrreferential.base.IBaseCallBack;
import com.phc.neckrreferential.modle.domain.SelectedContent;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/5 21
 * 描述：
 */
public interface ISelectedPageCallback extends IBaseCallBack {

    /**
     * 加载分类条目
     * @param categories 分类条目
     */
    void onCategoriseLoaded(SelectedPageCategory categories);

    /**
     *加载分类内容
     * @param content 内容
     */
    void onContentLoaded(SelectedContent content);
}
