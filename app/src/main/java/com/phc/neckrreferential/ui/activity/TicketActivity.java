package com.phc.neckrreferential.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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
    private boolean mHasTaoBaoApp = false;


    @Override
    protected void relase() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        //判断是否安装淘宝
        // cmp=com.taobao.taobao/com.taobao.tao.TBMainActivity
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaoBaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaoBaoApp = false;
        }
        logUtils.d(this, "有没有安装淘宝=====" + mHasTaoBaoApp);
        //根据有没有安装淘宝修改textview文字
        mOpenOrCopyBtn.setText(mHasTaoBaoApp ? "打开淘宝领券" : "复制口令");
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
        //复制按键
        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行复制
                String tickerCode = mTicketCode.getText().toString().trim();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴板
                ClipData clipData = ClipData.newPlainText("tao_kou_ling",tickerCode);
                cm.setPrimaryClip(clipData);
                //判断有没有淘宝
                if (mHasTaoBaoApp) {
                    Intent taobaoIntent = new Intent();
//                    (name=com.taobao.taobao/com.taobao.tao.TBMainActivity)
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    try {
                        Toast.makeText(TicketActivity.this, "正在拉起淘宝，请稍作等待", Toast.LENGTH_SHORT).show();
                        startActivity(taobaoIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(TicketActivity.this, "复制成功，粘贴分享或打开淘宝", Toast.LENGTH_SHORT).show();
                }
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
        if (loadingView != null) {
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
        if (loadingView != null) {
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
