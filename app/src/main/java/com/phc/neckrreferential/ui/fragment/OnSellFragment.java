package com.phc.neckrreferential.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.IBaseInfo;
import com.phc.neckrreferential.modle.domain.OnSellContent;
import com.phc.neckrreferential.presenter.IOnSellPagePresenter;
import com.phc.neckrreferential.ui.adapter.OnSellContentAdapter;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.SizeUtils;
import com.phc.neckrreferential.utils.TicketUtils;
import com.phc.neckrreferential.utils.ToastUtils;
import com.phc.neckrreferential.view.IOnSellPagerCallback;

import butterknife.BindView;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/6 10
 * 描述：
 * 继承了抽象类，重写了其中的抽象方法，该方法可以传递layout id过去
 */
public class OnSellFragment extends BaseFragment implements IOnSellPagerCallback, OnSellContentAdapter.OnSellPageItemClickListener {

    @BindView(R.id.on_sell_content_list)
    RecyclerView mContent;
    @BindView(R.id.on_sell_refresh_layout)
    TwinklingRefreshLayout mTwinklingRefreshLayout;



    public static final int DEFAULT_SPAN_COUNT = 2 ;

    private IOnSellPagePresenter mOnSellPagePresenter;
    private OnSellContentAdapter mOnSellContentAdapter;
    private StaggeredGridLayoutManager mLayoutManager;

    @Override
    protected void initPresenter() {
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();

    }

    @Override
    protected void release() {
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.unregisterViewCallback(this);
        }
    }



    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_with_bar_layout, container, false);
        TextView barTitleTv = (TextView)view.findViewById(R.id.fragment_bar_title_tv);
        barTitleTv.setText(R.string.text_on_sell_title);
        return view;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initListener() {
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //加载更多
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loaderMore();
                }
            }
        });
        mOnSellContentAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    protected void initView(View rootView) {
        mOnSellContentAdapter = new OnSellContentAdapter();
        mLayoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mContent.setLayoutManager(mLayoutManager);
        mContent.setAdapter(mOnSellContentAdapter);
        mContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),2.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),2.5f);
                outRect.left = SizeUtils.dip2px(getContext(),2.5f);
                outRect.right = SizeUtils.dip2px(getContext(),2.5f);
            }
        });
        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    protected void onRetryClick() {
        mOnSellPagePresenter.reLoad();
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        setUpState(State.SUCCESS);
        //数据回来了 更新ui 给适配器数据
        mOnSellContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        mTwinklingRefreshLayout.finishLoadmore();
        //键数据添加到适配器中
        mOnSellContentAdapter.onMoreLoaded(moreResult);
    }

    @Override
    public void onMoreLoadedError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("网络阻塞，请检查网络配置");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("没有更多的内容");
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

    @Override
    public void onSellItemClick(IBaseInfo item) {
        TicketUtils.toTicketPage(getContext(),item);
    }
}
