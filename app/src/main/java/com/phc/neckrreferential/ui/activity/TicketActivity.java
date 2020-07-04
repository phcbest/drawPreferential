package com.phc.neckrreferential.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseActivity;
import com.phc.neckrreferential.modle.domain.TicketResult;
import com.phc.neckrreferential.presenter.ITicketPresenter;
import com.phc.neckrreferential.utils.PresenterManager;
import com.phc.neckrreferential.utils.UrlUtils;
import com.phc.neckrreferential.utils.logUtils;
import com.phc.neckrreferential.view.ITicketPagerCallback;

import butterknife.BindView;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/2 09
 * 描述：
 */
public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_back_press)
    public View backPress;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;


    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mOpenOrCopyBtn;

    @BindView(R.id.ticket_cover_loading)
    public View loadingView;

    @BindView(R.id.ticket_load_retry)
    public TextView retryLoadText;

    private ITicketPresenter mTicketPresenter;


    @Override
    protected void relase() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        mTicketPresenter.registerViewCallback(this);
    }


    @Override
    protected void initEvent() {
        //返回按键
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
//
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            ViewGroup.LayoutParams layoutParams = mCover.getLayoutParams();
            int mCoverSize = Math.max(layoutParams.height, layoutParams.width) / 100 * 50;
            String coverPath = UrlUtils.getCoverPath(cover, mCoverSize);
            logUtils.d(this, "领券页面的图片url=====" + coverPath);
            Glide.with(this).load(coverPath).into(mCover);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String mResult = result.getData().getTbk_tpwd_create_response().getData().getModel();
            mTicketCode.setText(mResult);
        }
        if(loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoading() {
        if (loadingView != null){
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
