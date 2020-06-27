package com.phc.neckrreferential.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.Categories;
import com.phc.neckrreferential.modle.domain.HomePagerContent;
import com.phc.neckrreferential.presenter.ICategoryPagerPresenter;
import com.phc.neckrreferential.presenter.impl.CategoryPagePresenterImpl;
import com.phc.neckrreferential.ui.adapter.HomePageContentAdapter;
import com.phc.neckrreferential.ui.adapter.LooperPagerAdapter;
import com.phc.neckrreferential.utils.Constants;
import com.phc.neckrreferential.utils.LogUtils;
import com.phc.neckrreferential.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/14 10
 * 描述：该类在适配器中实现
 */
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;
    private HomePageContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category){
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = CategoryPagePresenterImpl.getInstance();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO：加载数据
        LogUtils.d(this,"title------------------------"+title);
        LogUtils.d(this,"materialId-------------------"+ mMaterialId);
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }
    }


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public ViewPager looperPager;

    @Override
    protected void initView(View rootView) {
        //布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view
                    , @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //创建适配器，设置适配器
        mContentAdapter = new HomePageContentAdapter();
        mContentList.setAdapter(mContentAdapter);

        mLooperPagerAdapter = new LooperPagerAdapter();
        looperPager.setAdapter(mLooperPagerAdapter);
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //数据列表加载到了
        mContentAdapter.setData(contents);
        setUpState(State.SUCCESS);

    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        //网络错误
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {

    }

    @Override
    public void onLoaderMoreEmpty() {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        mLooperPagerAdapter.setData(contents);
    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }
}
