package com.yhy.widget.core.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yhy.widget.R;
import com.yhy.widget.utils.WidgetCoreUtils;

import java.util.regex.Pattern;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-06-06 14:42
 * version: 1.0.0
 * desc   : 按比例约束宽高的ImageView
 */
@SuppressWarnings("FieldCanBeLocal")
public class ConstraintImageView extends AppCompatImageView {
    // 参考模式，默认以宽度为准
    private Reference mReference = Reference.WIDTH;
    // 宽高比字符串（宽 : 高），形如：720:480
    private String mOriginalRatio;
    // 具体的比例值
    private float mRealRatio;
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
    // 边框宽度，默认：0dp
    protected float mBorderWidth;
    // 边框颜色，默认：#000000
    protected int mBorderColor;
    // 图片可视区
    protected Path mRoundPath;
    // 图片边框
    protected Path mBorderPath;
    // 边框画笔
    private Paint mBorderPaint;
    // 图片
    protected Bitmap mBitmap;
    // 变换矩阵
    private Matrix mShaderMatrix;
    // 图片画笔
    private Paint mPaint;

    public ConstraintImageView(Context context) {
        this(context, null);
    }

    public ConstraintImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConstraintImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ConstraintImageView);
        int reference = ta.getInt(R.styleable.ConstraintImageView_civ_reference, mReference.code);
        mReference = reference == 0 ? Reference.WIDTH : Reference.HEIGHT;
        mOriginalRatio = ta.getString(R.styleable.ConstraintImageView_civ_original_ratio);
        mRealRatio = ta.getFloat(R.styleable.ConstraintImageView_civ_ratio, 0);
        mRadius = ta.getDimension(R.styleable.ConstraintImageView_civ_radius, 0);
        mRadiusLeftTop = ta.getDimension(R.styleable.ConstraintImageView_civ_radius_left_top, mRadius);
        mRadiusRightTop = ta.getDimension(R.styleable.ConstraintImageView_civ_radius_right_top, mRadius);
        mRadiusRightBottom = ta.getDimension(R.styleable.ConstraintImageView_civ_radius_right_bottom, mRadius);
        mRadiusLeftBottom = ta.getDimension(R.styleable.ConstraintImageView_civ_radius_left_bottom, mRadius);
        mBorderWidth = ta.getDimension(R.styleable.ConstraintImageView_civ_border_width, 0);
        mBorderColor = ta.getColor(R.styleable.ConstraintImageView_civ_border_color, 0);
        ta.recycle();

        calculateRatio();

        mRoundPath = new Path();
        mBorderPath = new Path();
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mShaderMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 设置比例
     *
     * @param width  图片真实宽度
     * @param height 图片真实高度
     */
    public void setRatio(int width, int height) {
        mRealRatio = (float) width / (float) height;
        // 触发 onMeasure() 方法执行
        requestLayout();
        // 触发 onDraw() 方法执行
        postInvalidate();
    }

    /**
     * 设置比例
     *
     * @param originalRatio 图片真实宽高比字符串，如：“100:200”
     */
    public void setRatio(String originalRatio) {
        mOriginalRatio = originalRatio;
        calculateRatio();
        requestLayout();
        postInvalidate();
    }

    /**
     * 设置比例
     *
     * @param ratio 图片真实宽高比值，如：“1.75f”
     */
    public void setRatio(float ratio) {
        mRealRatio = ratio;
        requestLayout();
        postInvalidate();
    }

    /**
     * 计算宽高比
     */
    private void calculateRatio() {
        if (!TextUtils.isEmpty(mOriginalRatio)) {
            mOriginalRatio = mOriginalRatio.replaceAll(" ", "").replace("：", ":");
            if (Pattern.matches("^[1-9][0-9]*(\\.[0-9]+)?:[1-9][0-9]*(\\.[0-9]+)?$", mOriginalRatio)) {
                String[] ratioText = mOriginalRatio.split(":");
                float width = Float.valueOf(ratioText[0]);
                float height = Float.valueOf(ratioText[1]);
                mRealRatio = width / height;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRealRatio > 0) {
            float width = getMeasuredWidth();
            float height = getMeasuredHeight();

            float realWidth = width;
            float realHeight = height;
            if (mReference == Reference.WIDTH) {
                // 以宽度为基准，按约束比例计算高度
                realHeight = width / mRealRatio;
            } else {
                // 以高度为基准，按约束比例计算宽度
                realWidth = height * mRealRatio;
            }
            setMeasuredDimension((int) realWidth, (int) realHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            initBorderPath();
            initRoundPath();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制图片路径
        drawImagePath(canvas);
        // 绘制边框
        drawBorder(canvas);
    }

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

    /**
     * 获取缩放比例
     *
     * @return 缩放比例
     */
    protected float getScale() {
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
        return Math.max(getWidth() * 1.0f / mBitmap.getWidth(), getHeight() * 1.0f / mBitmap.getHeight());
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
     * 变换图片
     */
    private void transform() {
        if (getWidth() == 0 && getHeight() == 0 || null == getDrawable()) {
            return;
        }
        mBitmap = WidgetCoreUtils.drawableToBitmap(getDrawable());
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

    /**
     * 参考方式
     */
    public enum Reference {
        // 以宽为准；以高为准
        WIDTH(0), HEIGHT(1);

        int code;

        /**
         * 构造行数
         *
         * @param code 标识码
         */
        Reference(int code) {
            this.code = code;
        }
    }
}
