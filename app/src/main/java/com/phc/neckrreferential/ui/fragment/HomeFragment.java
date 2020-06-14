package com.phc.neckrreferential.ui.fragment;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.Categories;
import com.phc.neckrreferential.presenter.impl.HomePresenterImpl;
import com.phc.neckrreferential.ui.adapter.HomePagerAdapter;
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

    private HomePresenterImpl mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initPresenter() {
        //重写继承BaseFragment需要的方法initPresenter
        //创建主页管理者层presenter
        mHomePresenter = new HomePresenterImpl();
        //this是调用了onCategoriesLoaded
        mHomePresenter.registerCallback(this);
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(mHomePager);
        //给viewPager设置适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void loadData() {
        //加载数据
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        //加载的数据从这里回来
        if (mHomePagerAdapter!=null) {
            mHomePagerAdapter.setCategoryList(categories);
        }
    }


    @Override
    protected void release() {
        //该方法在生命周期onDestroyView中实现
        //取消回调注册
        if (mHomePresenter != null) {
            mHomePresenter.unregisterCallback(this);
        }
    }
}
