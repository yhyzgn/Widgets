package com.yhy.widget.core.img.corner.abs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
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
public abstract class AbsCornerImageView extends AppCompatImageView {

    private static final PorterDuffXfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    private Paint mBitmapPaint;

    /**
     * 图片可视区
     */
    protected Path roundPath;

    /**
     * 图片边框
     */
    protected Path borderPath;

    /**
     * 边框宽度
     */
    protected float borderWidth;

    /**
     * 边框颜色
     */
    protected int borderColor;

    private Paint borderPaint;

    public AbsCornerImageView(Context context) {
        this(context, null, 0);
    }

    public AbsCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    /**
     * 初始化
     *
     * @param attrs 属性集
     */
    protected void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AbsCornerImageView);
            borderWidth = ta.getDimension(R.styleable.AbsCornerImageView_civ_border_width, 0);
            borderColor = ta.getColor(R.styleable.AbsCornerImageView_civ_border_color, 0);
            ta.recycle();
        }
    }

    private void init() {
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        roundPath = new Path();
        borderPath = new Path();

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStrokeWidth(borderWidth);

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
        canvas.drawPath(roundPath, paint);
        return bitmap;
    }

    /**
     * 绘制边框
     *
     * @param canvas 画布
     */
    private void drawBorder(Canvas canvas) {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        canvas.drawPath(borderPath, borderPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawImage(canvas);
        drawBorder(canvas);
    }

    /**
     * 绘制图片
     *
     * @param canvas 画布
     */
    private void drawImage(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (!isInEditMode() && drawable != null) {
            try {
                Bitmap bitmap;
                if (drawable instanceof ColorDrawable) {
                    bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
                } else {
                    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                }
                Canvas drawCanvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, drawCanvas.getWidth(), drawCanvas.getHeight());
                drawable.draw(drawCanvas);

                Bitmap roundBm = getRoundBitmap();
                mBitmapPaint.reset();
                mBitmapPaint.setFilterBitmap(false);
                mBitmapPaint.setXfermode(xFermode);
                drawCanvas.drawBitmap(roundBm, 0, 0, mBitmapPaint);
                mBitmapPaint.setXfermode(null);
                canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
