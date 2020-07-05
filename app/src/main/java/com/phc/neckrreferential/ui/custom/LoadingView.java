package com.phc.neckrreferential.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.utils.logUtils;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/4 07
 * 描述：
 */
public class LoadingView extends AppCompatImageView {


    private float mDegrees = 0;
    private boolean mNeedRotate = true;

    public LoadingView(Context context) {
        this(context, null);
    }

    //tmd老子找了一下午bug，原来是AttributeSet没有传递给下一层，下一层AttributeSet我给的是null，一直报错，我tm的气死了
    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        startRotate();
    }

    private void startRotate() {
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees += 10;
                if (mDegrees >= 360) {
                    mDegrees = 0;
                }
                invalidate();
//                判断是否要继续旋转
                if (getVisibility() != VISIBLE && !mNeedRotate) {
                    logUtils.d(this, "不继续旋转了，将post移除队列");
                    removeCallbacks(this);
                } else {
                    logUtils.d(this, "保持旋转中");
                    postDelayed(this, 10);
                }
            }
        });

    }

    /**
     * 该方法在当前activity退出后回调
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
