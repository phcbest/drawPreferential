package com.phc.neckrreferential.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/2 09
 * 描述：
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getLayoutResId());
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
    }

    protected abstract int getLayoutResId();


    protected void initEvent() {

    }

    protected abstract void initView();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

}
