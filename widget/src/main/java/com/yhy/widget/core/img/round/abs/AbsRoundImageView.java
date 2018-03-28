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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-03-15 13:38
 * version: 1.0.0
 * desc   : 圆角图片抽象类
 */
public abstract class AbsRoundImageView extends AppCompatImageView {
    // 图片可视区
    protected Path mRoundPath;
    // 图片边框
    protected Path mBorderPath;
    // 边框宽度
    protected float mBorderWidth;
    // 边框颜色
    protected int mBorderColor;
    // 边框画笔
    private Paint mBorderPaint;
    // 图片
    protected Bitmap mBitmap;
    // 变换矩阵
    private Matrix mShaderMatrix;
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
            ta.recycle();
        }

        mRoundPath = new Path();
        mBorderPath = new Path();
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mShaderMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            initBorderPath();
            initRoundPath();
        }
    }

    /**
     * 初始化边框Path
     */
    protected abstract void initBorderPath();

    /**
     * 初始化图片区域Path
     */
    protected abstract void initRoundPath();

    /**
     * 获取缩放比例
     *
     * @return 缩放比例
     */
    protected abstract float getScale();

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制图片路径
        drawImagePath(canvas);
        // 绘制边框
        drawBorder(canvas);
    }

    /**
     * 绘制边框
     *
     * @param canvas 画布
     */
    private void drawBorder(Canvas canvas) {
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
            canvas.drawPath(mRoundPath, mPaint);
        }
    }

    /**
     * 将Drawable转换为Bitmap
     *
     * @param drawable 原始drawable
     * @return bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        if (drawable instanceof ColorDrawable) {
            bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        // 设置显示区域
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 变换图片
     */
    private void transform() {
        if (getWidth() == 0 && getHeight() == 0 || null == getDrawable()) {
            return;
        }
        mBitmap = drawableToBitmap(getDrawable());
        if (mBitmap == null) {
            invalidate();
            return;
        }

        BitmapShader mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = getScale();
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mShaderMatrix.setScale(scale, scale);
        // 将放大后的图片向上移动，达到中心位置
        mShaderMatrix.postTranslate(-(mBitmap.getWidth() * scale - getWidth()) / 2.0f, -(mBitmap.getHeight() * scale - getHeight()) / 2.0f);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mShaderMatrix);
        // 设置shader
        mPaint.setShader(mBitmapShader);
    }
}
