package com.yhy.widget.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.yhy.widget.R;
import com.yhy.widget.img.abs.AbsImageView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:18
 * version: 1.0.0
 * desc   :
 */
public class RoundImageView extends AbsImageView {

    private float mRadius;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AbsImageView);
        mRadius = ta.getDimension(R.styleable.AbsImageView_riv_radius, 0f);
        ta.recycle();
    }

    @Override
    protected void drawView(Canvas canvas) {
        //设置圆角半径
        mBorderRadius = mRadius;

        //设置边框绘制区域
        /*
        left/top需要加上边框宽度
        right/bottom需要减去边框宽度
        这样才能保证边框能完全显示
         */
        mBorderRect.left += mBorderWidth;
        mBorderRect.top += mBorderWidth;
        mBorderRect.right -= mBorderWidth;
        mBorderRect.bottom -= mBorderWidth;

        //将绘制图片区域设置为和边框区域一致
        mDrawableRect.set(mBorderRect);

        //如果不允许边框覆盖，就将图片与边框隔开，间隔宽度为边框宽度
        if (!mBorderOverlay && mBorderWidth > 0) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }

        //绘制图片
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawRoundRect(mDrawableRect, mRadius, mRadius, mFillPaint);
        }
        canvas.drawRoundRect(mDrawableRect, mRadius, mRadius, mBitmapPaint);

        //绘制边框
        if (mBorderWidth > 0) {
            canvas.drawRoundRect(mBorderRect, mBorderRadius, mBorderRadius, mBorderPaint);
        }
    }
}
