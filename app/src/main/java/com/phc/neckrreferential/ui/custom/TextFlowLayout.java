package com.phc.neckrreferential.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.utils.logUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/11 19
 * 描述：
 */
public class TextFlowLayout extends ViewGroup {


    //边距属性
    public static final int DEFAULT_SPACE = 30;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;

    private List<String> mTextList = new ArrayList<>();
    private int mSelfWidth;
    private int mItemHeight;

    private onFlowTextItemClickListener mItemClickListener = null;

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //拿到相关属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        logUtils.d(this, "TextFlowLayout宽高" + mItemHorizontalSpace + "\t" + mItemVerticalSpace);
        ta.recycle();
    }

    /**
     * 拿到内容尺寸
     */
    public int getContentSize() {
        return mTextList.size();
    }


    public void setTextList(List<String> textList) {
        //因为多次进来会产生多个ui，没有清空集合，所以需要清空集合
        removeAllViews();//移除历史view
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        //因为历史记录是倒过来的，需要反转一下集合
        Collections.reverse(mTextList);
        //遍历内容
        for (String text : mTextList) {
            //添加chileView,该方法最后一个尝试为是否绑定当前viewGroup，如果为false需要自己添加进去
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            //添加点击事件
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onFlowItemClick(text);
                }
            });
            addView(item);
        }
    }

    //二维集合用于描述全部的
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        lines.clear();//因为该方法会调用多次，需要清空集合让最后一次才生效
        List<View> line = null;
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //测量child
        //得到child的数量
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            //测量每个child
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != View.VISIBLE) {
                continue;
            }
//            logUtils.d(this,"before height"+itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
//            logUtils.d(this,"after height"+itemView.getMeasuredHeight());
            if (line == null) {
                line = createNewLine(itemView);
            } else {
                //判断当前页面宽度能否放下child
                if (canBeAdd(itemView, line)) {
                    line.add(itemView);
                } else {
                    //不能创建进去，新创建一行
                    line = createNewLine(itemView);
                }
            }
        }
        //测量自己
        //需要拿到所有的高度
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        setMeasuredDimension(mSelfWidth, selfHeight);
        //
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行能否放下
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        int totalWith = itemView.getMeasuredWidth();
        for (View view : line) {
            totalWith += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWith += mItemHorizontalSpace * (line.size() + 1);
        return totalWith <= mSelfWidth;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放child
        int leftOffset = (int) mItemHorizontalSpace;
        int topOffset = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            for (View view : views) {
                int i = leftOffset + view.getMeasuredWidth();
                int x = topOffset + view.getMeasuredHeight();
                view.layout(leftOffset, topOffset, i, x);
//                logUtils.d(this, "=================textflow布局=====================");
//                logUtils.d(this, "textflow布局" + leftOffset);
//                logUtils.d(this, "textflow布局" + topOffset);
//                logUtils.d(this, "textflow布局" + i);
//                logUtils.d(this, "textflow布局" + x);
                //将指针后移
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
//            logUtils.d(this, "textflow布局 行的高" + topOffset);
            //重新定义一下下一行的位置
            topOffset = (int) (topOffset + mItemHeight + mItemHorizontalSpace);
            leftOffset = (int) mItemHorizontalSpace;
//            logUtils.d(this, "textflow布局 行的高" + topOffset);
        }
    }


    public void setOnFlowTextItemClickListener(onFlowTextItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface onFlowTextItemClickListener {
        void onFlowItemClick(String text);
    }
}
