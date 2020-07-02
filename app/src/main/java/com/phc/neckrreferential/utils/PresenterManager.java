package com.phc.neckrreferential.utils;

import com.phc.neckrreferential.presenter.ICategoryPagerPresenter;
import com.phc.neckrreferential.presenter.IHomePresenter;
import com.phc.neckrreferential.presenter.ITicketPresenter;
import com.phc.neckrreferential.presenter.impl.CategoryPagePresenterImpl;
import com.phc.neckrreferential.presenter.impl.HomePresenterImpl;
import com.phc.neckrreferential.presenter.impl.TicketPresenterImpl;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/2 15
 * 描述：
 */
public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;

    public static PresenterManager getInstance() {
        return ourInstance;
    }

    public ICategoryPagerPresenter getCategoryPagePresenter() {
        return mCategoryPagePresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
    }
}
