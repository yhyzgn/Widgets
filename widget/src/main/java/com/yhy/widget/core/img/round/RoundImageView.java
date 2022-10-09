package com.yhy.widget.core.img.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.yhy.widget.R;
import com.yhy.widget.core.img.round.abs.AbsRoundImageView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:18
 * version: 1.0.0
 * desc   : 圆角图片
 */
@SuppressWarnings("FieldCanBeLocal")
public class RoundImageView extends AbsRoundImageView {
    // 圆角半径，默认：0dp
    private float mRadius;
    // 左上角圆角半径，默认：0dp
    private float mRadiusLeftTop;
    // 右上角圆角半径，默认：0dp
    private float mRadiusRightTop;
    // 右下角圆角半径，默认：0dp
    private float mRadiusRightBottom;
    // 左下角圆角半径，默认：0dp
    private float mRadiusLeftBottom;

    public RoundImageView(Context context) {
        this(context, null, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化属性集
     *
     * @param attrs 属性集
     */
    protected void init(AttributeSet attrs) {
        super.init(attrs);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            mRadius = ta.getDimension(R.styleable.RoundImageView_riv_radius, 0);
            mRadiusLeftTop = ta.getDimension(R.styleable.RoundImageView_riv_radius_left_top, mRadius);
            mRadiusRightTop = ta.getDimension(R.styleable.RoundImageView_riv_radius_right_top, mRadius);
            mRadiusRightBottom = ta.getDimension(R.styleable.RoundImageView_riv_radius_right_bottom, mRadius);
            mRadiusLeftBottom = ta.getDimension(R.styleable.RoundImageView_riv_radius_left_bottom, mRadius);
            ta.recycle();
        }
    }

    @Override
    protected void initRoundPath() {
        mRoundPath.reset();
        final int width = getWidth();
        final int height = getHeight();
        mRadiusLeftTop = Math.min(mRadiusLeftTop, Math.min(width, height) * 0.5f);
        mRadiusRightTop = Math.min(mRadiusRightTop, Math.min(width, height) * 0.5f);
        mRadiusRightBottom = Math.min(mRadiusRightBottom, Math.min(width, height) * 0.5f);
        mRadiusLeftBottom = Math.min(mRadiusLeftBottom, Math.min(width, height) * 0.5f);

        RectF rect = new RectF(0, 0, width, height);
        mRoundPath.addRoundRect(rect, new float[]{mRadiusLeftTop, mRadiusLeftTop, mRadiusRightTop, mRadiusRightTop, mRadiusRightBottom, mRadiusRightBottom, mRadiusLeftBottom, mRadiusLeftBottom}, Path.Direction.CW);
    }

    @Override
    protected void initFillPath() {
        mFillPath.reset();
        final int width = getWidth();
        final int height = getHeight();
        mRadiusLeftTop = Math.min(mRadiusLeftTop, Math.min(width, height) * 0.5f);
        mRadiusRightTop = Math.min(mRadiusRightTop, Math.min(width, height) * 0.5f);
        mRadiusRightBottom = Math.min(mRadiusRightBottom, Math.min(width, height) * 0.5f);
        mRadiusLeftBottom = Math.min(mRadiusLeftBottom, Math.min(width, height) * 0.5f);

        RectF rect = new RectF(0, 0, width, height);
        mFillPath.addRoundRect(rect, new float[]{mRadiusLeftTop, mRadiusLeftTop, mRadiusRightTop, mRadiusRightTop, mRadiusRightBottom, mRadiusRightBottom, mRadiusLeftBottom, mRadiusLeftBottom}, Path.Direction.CW);
    }

    @Override
    protected void initBorderPath() {
        mBorderPath.reset();

        // 乘以0.5会导致border在圆角处不能包裹原图
        final float halfBorderSize = mBorderWidth * 0.35f;
        final int width = getWidth();
        final int height = getHeight();
        mRadiusLeftTop = Math.min(mRadiusLeftTop, Math.min(width, height) * 0.5f);
        mRadiusRightTop = Math.min(mRadiusRightTop, Math.min(width, height) * 0.5f);
        mRadiusRightBottom = Math.min(mRadiusRightBottom, Math.min(width, height) * 0.5f);
        mRadiusLeftBottom = Math.min(mRadiusLeftBottom, Math.min(width, height) * 0.5f);

        RectF rect = new RectF(halfBorderSize, halfBorderSize, width - halfBorderSize, height - halfBorderSize);
        mBorderPath.addRoundRect(rect, new float[]{mRadiusLeftTop, mRadiusLeftTop, mRadiusRightTop, mRadiusRightTop, mRadiusRightBottom, mRadiusRightBottom, mRadiusLeftBottom, mRadiusLeftBottom}, Path.Direction.CW);
    }
}
