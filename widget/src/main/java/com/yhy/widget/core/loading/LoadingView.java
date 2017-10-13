package com.yhy.widget.core.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:19
 * version: 1.0.0
 * desc   :
 */
public class LoadingView extends View {
    //默认颜色
    private static final int COLOR_DEF = Color.parseColor("#FF4400");
    //控件宽
    private int mWidth;
    //控件高
    private int mHeight;
    //控件的中间水平线位置，高度的一半
    private int mMiddle;
    //条目宽度
    private int mItemWidth;
    //每个Item初始值
    private int[] mValues;
    //颜色
    private int mColor;
    //每个Item初始状态，均为ture
    private boolean[] mFlags;
    //增减比例
    private int mRatio;
    //Item最大值
    private int mMaxValue;
    //Item位置
    private Rect mItemRect;
    //画笔
    private Paint mPaint;
    //是否已经绘制过
    private boolean mIsDrawed = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mColor = ta.getColor(R.styleable.LoadingView_loading_color, COLOR_DEF);
        ta.recycle();

        //设置初始状态
        mFlags = new boolean[]{true, true, true, true};
        mPaint = new Paint();
        //是否抗锯齿
        mPaint.setAntiAlias(true);
        //填充效果
        mPaint.setStyle(Paint.Style.FILL);
        //设置颜色
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mIsDrawed) {
            //获取宽高
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();

            //计算中间水平线位置
            mMiddle = mHeight / 2;
            //Item宽度和间隔线宽度一致，即： 总宽度 / ( 2 * 条目总数 - 1 )
            mItemWidth = mWidth / 7;

            //设置画笔宽度
            mPaint.setStrokeWidth(mItemWidth);

            //初始化增减比例为高度的1/40
            mRatio = mHeight / 40;

            //条目最大值，中间值/增减比例
            mMaxValue = mMiddle / mRatio;

            //初始的4个值最大不能超过条目最大值
            int temp = mMaxValue / 4;
            mValues = new int[]{0, temp, temp * 2, temp * 3};

            //更新状态
            mIsDrawed = true;
        }

        //循环绘制每个条目
        for (int i = 0; i < mValues.length; i++) {
            drawItem(canvas, i);
        }

        //更新UI
        postInvalidateDelayed(12);
    }

    private void drawItem(Canvas canvas, int index) {
        //绘制每个条目
        //left：2 * index * mItemWidth
        //right：(2 * index + 1) * mItemWidth
        //top：mMiddle - mValues[index] * mRatio
        //bottom：mMiddle + mValues[index] * mRatio
        if (null == mItemRect) {
            mItemRect = new Rect(2 * index * mItemWidth, mMiddle - mValues[index] * mRatio, (2 * index + 1) * mItemWidth, mMiddle + mValues[index] * mRatio);
        } else {
            mItemRect.left = 2 * index * mItemWidth;
            mItemRect.top = mMiddle - mValues[index] * mRatio;
            mItemRect.right = (2 * index + 1) * mItemWidth;
            mItemRect.bottom = mMiddle + mValues[index] * mRatio;
        }

        //绘制
        canvas.drawRect(mItemRect, mPaint);

        //判断增减
        if (mFlags[index]) {
            mValues[index]++;
            if (mValues[index] >= mMaxValue) {
                mFlags[index] = false;
            }
        } else {
            mValues[index]--;
            if (mValues[index] <= 0) {
                mFlags[index] = true;
            }
        }
    }

    /**
     * 设置颜色
     *
     * @param color 颜色值
     */
    public void setColor(@ColorInt int color) {
        mColor = color;
        mPaint.setColor(mColor);
    }
}