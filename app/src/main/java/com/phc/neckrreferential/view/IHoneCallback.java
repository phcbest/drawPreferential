package com.phc.neckrreferential.view;

import com.phc.neckrreferential.base.IBaseCallBack;
import com.phc.neckrreferential.modle.domain.Categories;

/**
 * 版权：没有版权 看得上就用
 * @author peng
 * 创建日期：2020/6/11 15
 * 描述：
 */
public interface IHoneCallback extends IBaseCallBack {
    /**
     * @param categories 使用这个得到返回json
     */
    void onCategoriesLoaded(Categories categories);
}
