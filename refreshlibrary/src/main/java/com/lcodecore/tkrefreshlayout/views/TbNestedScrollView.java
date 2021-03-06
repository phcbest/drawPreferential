package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/30 07
 * 描述：
 */
public class TbNestedScrollView extends NestedScrollView {

    private static final String TAG = "TbNestedScrollView";

    private int mHeaderHeight = 0;
    private int originScroll = 0;
    private RecyclerView mRecyclerView;


    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy,
                                  @NonNull int[] consumed, @ViewCompat.NestedScrollType int type) {
        if (target instanceof RecyclerView) {
            this.mRecyclerView = (RecyclerView) target;
        }

        if (originScroll < mHeaderHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setHeaderHeight(int headerHeight){
        this.mHeaderHeight = headerHeight;
    }

    /**
     * 判断是否滑到了底部
     * @return
     */
    public boolean isInBottom() {
        if (mRecyclerView != null) {
            boolean isBottom = !mRecyclerView.canScrollVertically(1);
//            Log.d(TAG, "isInBottom: ============="+isBottom);
            return isBottom;
        }
        return false;
    }
}
