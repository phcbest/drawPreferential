package com.phc.neckrreferential.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.phc.neckrreferential.R;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/1 16
 * 描述：自动轮播
 */
public class AutoLoopViewPager extends ViewPager {

    //切换间隔时长
    public static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;

    public AutoLoopViewPager(@NonNull Context context) {
        super(context,null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        //拿到属性
        mDuration = t.getInteger(R.styleable.AutoLoopStyle_duration, (int) DEFAULT_DURATION);
        //回收
        t.recycle();
    }

    /**
     * 设置切换事长，单位毫秒
     * @param duration
     */
    public void setDuration(int duration){
        this.mDuration = duration ;
    }

    private boolean isLoop = false;

    public void startLoop() {
        isLoop = true;
        post(mTask);
    }
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            //拿到当前位置
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this, mDuration);
            }
        }
    };


    public void stopLoop() {
        isLoop = false;
        //这个方法用于在消息队列中删除指定的Runnable
        removeCallbacks(mTask);
    }
}
