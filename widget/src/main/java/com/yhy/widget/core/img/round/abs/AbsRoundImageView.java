package com.yhy.widget.core.img.round.abs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
    // 用来帮助clipPath方法消除锯齿
    protected Paint mClipPaint;
    // 图片边框
    protected Path mBorderPath;
    // 边框宽度
    protected float mBorderWidth;
    // 边框颜色
    protected int mBorderColor;
    // 边框画笔
    private Paint mBorderPaint;

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

        mClipPaint = new Paint();
        mClipPaint.setStyle(Paint.Style.STROKE);
        mClipPaint.setColor(Color.TRANSPARENT);
        mClipPaint.setStrokeWidth(0.1f);
        mClipPaint.setAntiAlias(true);
        mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStrokeWidth(mBorderWidth);


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
     * 获取图片区域纯颜色Bitmap
     *
     * @return 图片区域纯颜色Bitmap
     */
    protected Bitmap getRoundBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawPath(mRoundPath, paint);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制图片路径
        drawImagePath(canvas);
        // 调用父类onDraw方法，将图片绘制到画布上
        super.onDraw(canvas);
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
            canvas.clipPath(mRoundPath);
            // 消除锯齿
            canvas.drawPath(mRoundPath, mClipPaint);
        }
    }
}
