package com.phc.neckrreferential.presenter.impl;

import com.phc.neckrreferential.presenter.ICategoryPagerPresenter;
import com.phc.neckrreferential.view.ICategoryPagerCallback;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/25 18
 * 描述：
 */
public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private CategoryPagePresenterImpl(){

    }
    private static ICategoryPagerPresenter sInstance = null;

    public static  ICategoryPagerPresenter getInstance(){
        if (sInstance == null) {
            sInstance = new CategoryPagePresenterImpl();
        }
        return sInstance;
    }


    @Override
    public void getContentByCategoryId(int categoryId) {
        //分根据分类id加载内容
    }

    @Override
    public void loaderMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {

    }
}
