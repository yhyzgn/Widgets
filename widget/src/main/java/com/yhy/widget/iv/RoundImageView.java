package com.yhy.widget.iv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.yhy.widget.R;
import com.yhy.widget.iv.abs.AbsImageView;

/**
 * Created by HongYi Yan on 2017/5/8 17:35.
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

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AbsImageViewAttrs);
        mRadius = ta.getDimension(R.styleable.AbsImageViewAttrs_riv_radius, 0f);
        ta.recycle();
    }

    @Override
    protected void drawView(Canvas canvas) {
        mBorderRadius = mRadius;

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawRoundRect(mDrawableRect, mRadius, mRadius, mFillPaint);
        }
        canvas.drawRoundRect(mDrawableRect, mRadius, mRadius, mBitmapPaint);

        if (mBorderWidth > 0) {
            canvas.drawRoundRect(mBorderRect, mBorderRadius, mBorderRadius, mBorderPaint);
        }
    }
}
