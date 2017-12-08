package com.yhy.widget.layout.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yhy.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-08 15:53
 * version: 1.0.0
 * desc   : 流式布局
 */
public class FlowLayout extends ViewGroup {
    // 所有子控件集合
    protected List<List<View>> mAllViewList = new ArrayList<>();
    // 行宽集合
    protected List<Integer> mLineWidthList = new ArrayList<>();
    // 行高集合
    protected List<Integer> mLineHeightList = new ArrayList<>();
    // 每行子控件集合
    protected List<View> mLineViewList = new ArrayList<>();
    // 控件总宽度
    private int mWidth;
    // 布局对其方式，默认为左对齐
    private Gravity mGravity;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     * @param attrs   属性集
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        int gravity = ta.getInt(R.styleable.FlowLayout_fl_gravity, Gravity.LEFT.value);
        mGravity = gravity == 0 ? Gravity.CENTER : gravity == 1 ? Gravity.RIGHT : Gravity.LEFT;
        ta.recycle();
    }

    /**
     * 测量大小
     *
     * @param widthMeasureSpec  宽度参考值
     * @param heightMeasureSpec 高度参考值
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 参考大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 参考模式
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // 记录总宽度
        mWidth = sizeWidth;

        // 实际宽度和高度
        int width = 0, height = 0;
        // 行宽度和高度
        int lineWidth = 0, lineHeight = 0;

        int childCount = getChildCount();

        View child;
        MarginLayoutParams params;
        int childWidth, childHeight;

        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                if (i == childCount - 1) {
                    width += Math.max(lineWidth, width);
                    height += lineHeight;
                }
                continue;
            }

            // 测量子控件
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            params = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                // 当前行已经放不下，需要开启下一行
                width = Math.max(width, lineWidth);
                height += lineHeight;
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                // 继续在当前行上放
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            // 最后一个子控件
            if (i == childCount - 1) {
                width += Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        // 设置宽高
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width, modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    /**
     * 布局
     *
     * @param changed 是否发生变化
     * @param l       左
     * @param t       上
     * @param r       右
     * @param b       下
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 清空所有缓存，避免onLayout和onMeasure互相重复掉用
        mAllViewList.clear();
        mLineWidthList.clear();
        mLineHeightList.clear();
        mLineViewList.clear();

        View child;
        int lineWidth = 0, lineHeight = 0;
        int childWidth, childHeight;
        MarginLayoutParams params;

        // 缓存所有子控件，行宽、高等
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            params = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            if (childWidth + lineWidth + params.leftMargin + params.rightMargin > mWidth - getPaddingLeft() - getPaddingRight()) {
                // 当前行放不下，需要开启下一行
                mLineHeightList.add(lineHeight);
                mAllViewList.add(mLineViewList);
                mLineWidthList.add(lineWidth);

                // 开启下一行
                lineWidth = 0;
                lineHeight = childHeight + params.topMargin + params.bottomMargin;
                mLineViewList = new ArrayList<>();
            }
            lineWidth += childWidth + params.leftMargin + params.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + params.topMargin + params.bottomMargin);
            mLineViewList.add(child);
        }
        mLineWidthList.add(lineWidth);
        mLineHeightList.add(lineHeight);
        mAllViewList.add(mLineViewList);

        // 开始布局
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineCount = mAllViewList.size();

        int curLineWidth;
        int cl, ct, cr, cb;
        for (int i = 0; i < lineCount; i++) {
            mLineViewList = mAllViewList.get(i);
            lineHeight = mLineHeightList.get(i);

            // 设置对其方式
            curLineWidth = mLineWidthList.get(i);
            switch (mGravity) {
                case LEFT:
                    left = getPaddingLeft();
                    break;
                case CENTER:
                    left = (mWidth - curLineWidth) / 2 + getPaddingLeft();
                    break;
                case RIGHT:
                    left = mWidth - curLineWidth + getPaddingLeft();
                    break;
            }

            // 具体到每个子控件的布局
            for (int j = 0; j < mLineViewList.size(); j++) {
                child = mLineViewList.get(j);
                if (child.getVisibility() == GONE) {
                    continue;
                }

                params = (MarginLayoutParams) child.getLayoutParams();

                cl = left + params.leftMargin;
                ct = top + params.topMargin;
                cr = cl + child.getMeasuredWidth();
                cb = ct + child.getMeasuredHeight();
                child.layout(cl, ct, cr, cb);

                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            top += lineHeight;
        }
    }

    /**
     * 创建布局参数
     *
     * @param attrs 属性集
     * @return 布局参数
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 创建默认布局参数
     *
     * @return 默认布局参数
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 创建布局参数
     *
     * @param p 默认布局参数
     * @return 布局参数
     */
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    /**
     * 对其方式枚举
     */
    public enum Gravity {
        // 三种对其方式
        LEFT(-1), CENTER(0), RIGHT(1);

        // 各种方式对应的value
        int value;

        /**
         * 构造函数
         *
         * @param value 标记值
         */
        Gravity(int value) {
            this.value = value;
        }
    }
}
