package com.yhy.widget.iv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.yhy.widget.R;

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
        RectF rectF = new RectF(mDrawableRect.left, mDrawableRect.top, mDrawableRect.right, mDrawableRect.bottom);
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawRoundRect(rectF, mRadius, mRadius, mFillPaint);
        }
        canvas.drawRoundRect(rectF, mRadius, mRadius, mFillPaint);
    }
}
