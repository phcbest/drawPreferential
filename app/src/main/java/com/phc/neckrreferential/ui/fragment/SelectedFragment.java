package com.phc.neckrreferential.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.modle.domain.SelectedContent;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;
import com.phc.neckrreferential.presenter.ISelectedPagePresenter;
import com.phc.neckrreferential.utils.PresenterManager;
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
public class SelectedFragment extends BaseFragment implements ISelectedPageCallback {


    @BindView(R.id.left_category_list)
    RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    RecyclerView rightContentList;

    private ISelectedPagePresenter mSelectedPagePresenter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initPresenter() {
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategory();
    }

    @Override
    public void onCategoriseLoaded(SelectedPageCategory categories) {
        //拿到分类内容
        logUtils.d(this, "精选页面分类数据" + categories.toString());
        List<SelectedPageCategory.DataBean> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));
        //更新ui
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        logUtils.d(this, "精选页面分类内容数据" + content.getData().getTbk_uatm_favorites_item_get_response().toString());
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
