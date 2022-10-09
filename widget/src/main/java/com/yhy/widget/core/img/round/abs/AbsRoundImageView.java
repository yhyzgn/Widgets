package com.yhy.widget.core.img.round.abs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.yhy.widget.R;
import com.yhy.widget.helper.ImageViewScaleMatrixHelper;
import com.yhy.widget.utils.WidgetCoreUtils;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-03-15 13:38
 * version: 1.0.0
 * desc   : 圆角图片抽象类
 */
public abstract class AbsRoundImageView extends AppCompatImageView {
    // 边框宽度，默认：0dp
    protected float mBorderWidth;
    // 边框颜色，默认：#000000
    protected int mBorderColor;
    // 背景填充颜色；默认：#000000
    protected int mFillColor;
    // 图片可视区
    protected Path mRoundPath;
    // 图片可视区
    protected Path mFillPath;
    // 图片边框
    protected Path mBorderPath;
    // 背景填充画笔
    private Paint mFillPaint;
    // 边框画笔
    private Paint mBorderPaint;
    // 图片
    protected Bitmap mBitmap;
    // 图片画笔
    private Paint mPaint;

    public AbsRoundImageView(Context context) {
        this(context, null, 0);
    }

    public AbsRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs 属性集
     */
    protected void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AbsRoundImageView);
            mBorderWidth = ta.getDimension(R.styleable.AbsRoundImageView_riv_border_width, 0);
            mBorderColor = ta.getColor(R.styleable.AbsRoundImageView_riv_border_color, 0);
            mFillColor = ta.getColor(R.styleable.AbsRoundImageView_riv_fill_color, 0);
            ta.recycle();
        }

        mRoundPath = new Path();
        mFillPath = new Path();

        mBorderPath = new Path();
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 初始化边框 Path
     */
    protected abstract void initBorderPath();

    /**
     * 初始化图片区域 Path
     */
    protected abstract void initRoundPath();

    /**
     * 初始化背景填充区域 Path
     */
    protected abstract void initFillPath();

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景填充
        drawFillColor(canvas);
        // 绘制图片路径
        drawImagePath(canvas);
        // 绘制边框
        drawBorder(canvas);
    }

    private void drawFillColor(Canvas canvas) {
        initFillPath();
        mFillPaint.setColor(mFillColor);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        canvas.drawPath(mFillPath, mFillPaint);
    }


    /**
     * 绘制边框
     *
     * @param canvas 画布
     */
    private void drawBorder(Canvas canvas) {
        initBorderPath();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        canvas.drawPath(mBorderPath, mBorderPaint);
    }

    /**
     * 绘制图片
     *
     * @param canvas 画布
     */
    private void drawImagePath(Canvas canvas) {
        if (null != mRoundPath) {
            if (null == getDrawable()) {
                return;
            }
            transform();
            initRoundPath();
            canvas.drawPath(mRoundPath, mPaint);
        }
    }

    /**
     * 变换图片
     */
    private void transform() {
        Drawable drawable = getDrawable();
        if (getWidth() == 0 && getHeight() == 0 || null == drawable) {
            return;
        }
        mBitmap = WidgetCoreUtils.drawableToBitmap(drawable);
        if (mBitmap == null) {
            invalidate();
            return;
        }

        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // 获取缩放变换矩阵
        Matrix shaderMatrix = ImageViewScaleMatrixHelper.with(this).apply();

        // 设置变换矩阵
        bitmapShader.setLocalMatrix(shaderMatrix);
        // 设置shader
        mPaint.setShader(bitmapShader);
    }
}
