package com.phc.neckrreferential.base;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗口灰色，清明节效果
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        View contentContainer = getWindow().getDecorView();
//        contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
        //----------------------------------------------------
        setContentView(getLayoutResId());
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    protected abstract void initPresenter();


    protected abstract int getLayoutResId();


    protected void initEvent() { }

    protected abstract void initView();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        this.relase();
    }

    protected void relase() {

    }

}
