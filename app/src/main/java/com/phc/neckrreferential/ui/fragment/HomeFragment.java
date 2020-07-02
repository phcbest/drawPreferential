package com.phc.neckrreferential.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.Categories;
import com.phc.neckrreferential.presenter.IHomePresenter;
import com.phc.neckrreferential.ui.adapter.HomePagerAdapter;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.IHoneCallback;

import butterknife.BindView;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/6 10
 * 描述：
 * 继承了抽象类，重写了其中的抽象方法，该方法可以传递layout id过去
 */
public class HomeFragment extends BaseFragment implements IHoneCallback {

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.hone_pager)
    public ViewPager mHomePager;

    private IHomePresenter mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;


    /**
     * 这个是必须重写的抽象方法，主要是给baseFragment提供加载成功页面布局,成功页面的布局在base_home_fragment_layout框架内
     * @return
     */
    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    /**
     * 外部框架替换
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logUtils.d(this,"homeFragment_onCreateView.................");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logUtils.d(this,"homeFragment_onDestroyView.................");
    }

    @Override
    protected void initView(View rootView) {
        //给tabLayout和ViewPager绑定
        mTabLayout.setupWithViewPager(mHomePager);
        //给viewPager设置适配器，获取子fragment管理器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        //重写继承BaseFragment需要的方法initPresenter
        //实例化HomePresenterImpl
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        //将重写的IHoneCallback接口传递过去
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        //加载类别数据
        mHomePresenter.getCategories();
    }

    /**
     * 重新加载分类
     */
    @Override
    protected void onRetryClick() {
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }

    /**
     *数据返回成功
     * @param categories 使用这个得到返回json
     */
    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState(State.SUCCESS);
        //加载的数据从这里回来，如果适配器不为null，就调用适配器内部的setCategoryList方法，将有数据的Categories对象传递进去
        if (mHomePagerAdapter!=null) {
//            TODO:这里可以更改首页viewPage的load数量
//            mHomePager.setOffscreenPageLimit(0);
            mHomePagerAdapter.setCategoryList(categories);
        }
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    /**
     * 释放资源
     */
    @Override
    protected void release() {
        //该方法在生命周期onDestroyView中实现
        //如果HomePresenterImpl为null
        if (mHomePresenter != null) {
            //使用取消回调注册
            mHomePresenter.unregisterViewCallback(this);
        }
    }
}
