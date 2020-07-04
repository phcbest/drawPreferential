package com.phc.neckrreferential.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.Categories;
import com.phc.neckrreferential.modle.domain.HomePagerContent;
import com.phc.neckrreferential.presenter.ICategoryPagerPresenter;
import com.phc.neckrreferential.presenter.ITicketPresenter;
import com.phc.neckrreferential.ui.activity.TicketActivity;
import com.phc.neckrreferential.ui.adapter.HomePageContentAdapter;
import com.phc.neckrreferential.ui.adapter.LooperPagerAdapter;
import com.phc.neckrreferential.ui.custom.AutoLoopViewPager;
import com.phc.neckrreferential.utils.Constants;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.SizeUtils;
import com.phc.neckrreferential.utils.ToastUtils;
import com.phc.neckrreferential.utils.logUtils;
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
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePageContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLoopPageItemClickListener {

    private ICategoryPagerPresenter mPagerPresenter;
    private int mMaterialId;
    private HomePageContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected void initPresenter() {
        mPagerPresenter = PresenterManager.getInstance().getCategoryPagePresenter();
        mPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO：加载数据
        logUtils.d(this, "title------------------------" + title);
        logUtils.d(this, "materialId-------------------" + mMaterialId);
        if (mPagerPresenter != null) {
            mPagerPresenter.getContentByCategoryId(mMaterialId);
        }
        if (currentCategoryTitleTv != null) {
            currentCategoryTitleTv.setText(title);
        }
    }



    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;



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

        //设置refresh内容
//        TODO 设置twinklingRefreshLayout页面能否越界拉伸
        twinklingRefreshLayout.setEnableOverScroll(true);
        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始ViewPager轮播
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //结束viewPager轮播
        looperPager.stopLoop();
    }

    @Override
    protected void initListener() {
        //调用set接口逻辑的方法
        mContentAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setOnLoopPageItemClickListener(this);

        //添加最外层的LinearLayout全局布局监听器
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null) {
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                logUtils.d(this,"headerHeight"+headerHeight);
                homePagerNestedView.setHeaderHeight(headerHeight);

                //这个方法动态获得高度
                int measuredHeight = homePagerParent.getMeasuredHeight();
                logUtils.d(getContext(),"measuredHeight====="+measuredHeight);
                //在这里修改mContentList也就是RecyclerView的高度
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);

                if (measuredHeight!=0) {
                    //删除全局布局监听器
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        currentCategoryTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int measuredHeight = mContentList.getMeasuredHeight();
                logUtils.d(this,"recyclerView的高度"+measuredHeight);
            }
        });

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                logUtils.d(this,"getDataSize+++++++++"+mLooperPagerAdapter.getDataSize());
                if (mLooperPagerAdapter.getDataSize() == 0){
                    return;
                }
                //TODO：不知道为啥这里第二次获取会等于0，稍后看看
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                //切换指示器
                upDataLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                logUtils.d(this,"开始加载更多");
                if (mPagerPresenter!=null) {
                    mPagerPresenter.loaderMore(mMaterialId);
                }
            }
        });


    }

    /**
     * 切换looper页面的指示器
     * @param targetPosition
     */
    private void upDataLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i== targetPosition){
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            }else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
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
        ToastUtils.showToast("网络异常，请稍后重试");
        finishLoadmore();
    }

    private void finishLoadmore() {
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtils.showToast("没有更多的数据");
        finishLoadmore();
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        mContentAdapter.addData(contents);
        finishLoadmore();
        ToastUtils.showToast("加载了"+contents.size()+"条");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        mLooperPagerAdapter.setData(contents);
        //设置在中间点，不一定为0,所以不是第一个
        int dx =(Integer.MAX_VALUE / 2)- ((Integer.MAX_VALUE / 2) % contents.size());
        looperPager.setCurrentItem(dx);
        logUtils.d(this,"位置++++++"+dx%contents.size());
        //清空知指示点列表
        looperPointContainer.removeAllViews();
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            //user utils to px
            int size = SizeUtils.dip2px(getContext(), 8);
            int Margin = SizeUtils.dip2px(getContext(), 5);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = Margin;
            layoutParams.rightMargin = Margin;
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (mPagerPresenter != null) {
            mPagerPresenter.unregisterViewCallback(this);
        }
    }

    /**
     * 列表内容被点击了
     * @param item
     */
    @Override
    public void onItemClick(HomePagerContent.DataBean item) {
        logUtils.d(this,"点击了条目");
        handleItemClick(item);
    }

    private void handleItemClick(HomePagerContent.DataBean item) {
        String title = item.getTitle();
        String url = item.getClick_url();
        String cover = item.getPict_url();
        // 在跳转之前处理数据，不会有停滞感
        //TODO 启动TicketActivity
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    /**
     * 横幅内容被点击了
     * @param item
     */
    @Override
    public void onLooperItemClick(HomePagerContent.DataBean item) {
        logUtils.d(this,"点击了滚动条目");
        handleItemClick(item);
    }
}
