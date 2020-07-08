package com.phc.neckrreferential.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.SelectedContent;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;
import com.phc.neckrreferential.presenter.ISelectedPagePresenter;
import com.phc.neckrreferential.presenter.ITicketPresenter;
import com.phc.neckrreferential.ui.activity.TicketActivity;
import com.phc.neckrreferential.ui.adapter.SelectedPageContentAdapter;
import com.phc.neckrreferential.ui.adapter.SelectedPageLeftAdapter;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.SizeUtils;
import com.phc.neckrreferential.utils.ToastUtils;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.ISelectedPageCallback;

import java.util.List;

import butterknife.BindView;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/6 10
 * 描述：
 */
public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener, SelectedPageContentAdapter.OnSelectedPageContentItemClickListener {


    @BindView(R.id.left_category_list)
    RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    RecyclerView rightContentList;

    private ISelectedPagePresenter mSelectedPagePresenter;
    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mRightAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(), 4);
                int leftAndRight = SizeUtils.dip2px(getContext(), 8);
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom ;
                outRect.left =  leftAndRight;
                outRect.right = leftAndRight ;
            }
        });

    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnSelectedPageContentItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategory();
    }

    @Override
    public void onCategoriseLoaded(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setData(categories);
        //拿到分类内容
        logUtils.d(this, "精选页面分类数据" + categories.toString());
        List<SelectedPageCategory.DataBean> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));

    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        logUtils.d(this, "精选页面分类内容数据" + content.getData().getTbk_uatm_favorites_item_get_response().toString());
        mRightAdapter.setData(content);
        //拿到了数据，直接显示
        rightContentList.setVisibility(View.VISIBLE);
        rightContentList.scrollToPosition(0);
    }


    @Override
    protected void onRetryClick() {
        //重试
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reLoadContent();
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

    }

    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        mSelectedPagePresenter.getContentByCategory(item);
        //开始加载数据，让右侧界面不能显示，等数据加载到了再显示
        rightContentList.setVisibility(View.GONE);
        logUtils.d(this,"点击了精选页面左边");
    }

    @Override
    public void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item) {
        //内容被点击了  判断有无优惠了，没有就不让点击
        if (TextUtils.isEmpty(item.getCoupon_click_url())) {
            ToastUtils.showToast(getContext().getString(R.string.text_no_off));
            return;
        }
        String title = item.getTitle();
        String url = item.getCoupon_click_url();
        if (TextUtils.isEmpty(url)) {
            url = item.getClick_url();
        }
        String cover = item.getPict_url();
        // 在跳转之前处理数据，不会有停滞感
        //TODO 启动TicketActivity
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}
