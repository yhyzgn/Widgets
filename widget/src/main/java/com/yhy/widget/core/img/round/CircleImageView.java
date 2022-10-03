package com.yhy.widget.core.img.round;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;

import com.yhy.widget.core.img.round.abs.AbsRoundImageView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:19
 * version: 1.0.0
 * desc   : 圆形图片
 */
public class CircleImageView extends AbsRoundImageView {

    public CircleImageView(Context context) {
        this(context, null, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.max(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void initRoundPath() {
        mRoundPath.reset();
        final int width = getWidth();
        final int height = getHeight();
        final float cx = width * 0.5f;
        final float cy = height * 0.5f;
        final float radius = Math.min(width, height) * 0.5f;
        mRoundPath.addCircle(cx, cy, radius, Path.Direction.CW);
    }

    @Override
    protected void initBorderPath() {
        mBorderPath.reset();
        final float halfBorderWidth = mBorderWidth * 0.5f;
        final int width = getWidth();
        final int height = getHeight();
        final float cx = width * 0.5f;
        final float cy = height * 0.5f;
        final float radius = Math.min(width, height) * 0.5f;
        mBorderPath.addCircle(cx, cy, radius - halfBorderWidth, Path.Direction.CW);
    }
}
