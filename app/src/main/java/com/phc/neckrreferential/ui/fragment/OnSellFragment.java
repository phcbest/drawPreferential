package com.phc.neckrreferential.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.OnSellContent;
import com.phc.neckrreferential.presenter.IOnSellPagePresenter;
import com.phc.neckrreferential.ui.adapter.OnSellContentAdapter;
import com.phc.neckrreferential.utils.PresenterManager;
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
public class OnSellFragment extends BaseFragment implements IOnSellPagerCallback {

    @BindView(R.id.on_sell_content_list)
    RecyclerView mContent;
    @BindView(R.id.on_sell_refresh_layout)
    TwinklingRefreshLayout onSellRefreshLayout;

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
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mOnSellContentAdapter = new OnSellContentAdapter();
        mLayoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mContent.setLayoutManager(mLayoutManager);
        mContent.setAdapter(mOnSellContentAdapter);
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        //数据回来了 更新ui 给适配器数据
        mOnSellContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {

    }

    @Override
    public void onMoreLoadedError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
