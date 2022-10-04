package com.yhy.widget.core.img;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.yhy.widget.R;
import com.yhy.widget.helper.ImageViewScaleMatrixHelper;
import com.yhy.widget.utils.WidgetCoreUtils;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-07 16:28
 * version: 1.0.0
 * desc   : 方形ImageView
 */
public class SquareImageView extends AppCompatImageView {
    // Bitmap 画笔
    private Paint mPaint;
    // Bitmap
    private Bitmap mBitmap;
    // 整个控件实际大小
    private int mSize;
    // 右上角按钮图片，默认为空
    private Bitmap mBtnImg;
    // 右上角按钮宽度
    private int mBtnWidth;
    // 右上角按钮高度
    private int mBtnHeight;
    // 右上角按钮内边距，默认为2dp
    private int mBtnPadding;
    // 鼠标按下时的坐标
    private int mDownX, mDownY;
    // 右上角按钮点击事件
    private OnBtnClickListener mListener;

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     * @param attrs   属性集
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView);
        if (ta.hasValue(R.styleable.SquareImageView_siv_btn_img)) {
            mBtnImg = WidgetCoreUtils.drawableToBitmap(ta.getDrawable(R.styleable.SquareImageView_siv_btn_img));
        }
        int size = ta.getDimensionPixelSize(R.styleable.SquareImageView_siv_btn_size, 0);
        mBtnPadding = ta.getDimensionPixelSize(R.styleable.SquareImageView_siv_btn_padding, WidgetCoreUtils.dp2px(context, 2.0f));
        ta.recycle();

        if (size == 0 && null != mBtnImg) {
            mBtnWidth = mBtnImg.getWidth();
            mBtnHeight = mBtnImg.getHeight();
        } else {
            mBtnWidth = mBtnHeight = size;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 测量大小
     *
     * @param widthMeasureSpec  宽度参考值
     * @param heightMeasureSpec 高度参考值
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(size, size);

        size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    /**
     * 绘制
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 缩放变换
        Matrix matrix = ImageViewScaleMatrixHelper.with(this).apply();
        setImageMatrix(matrix);

        super.onDraw(canvas);

        if (null != mBtnImg) {
            // 绘制按钮
            drawBtn(canvas);
        }
    }

    /**
     * 绘制按钮
     *
     * @param canvas 画布
     */
    private void drawBtn(Canvas canvas) {
        // 绘制按钮图片，包含内边距
        Rect src = new Rect(0, 0, mBtnImg.getWidth(), mBtnImg.getHeight());
        Rect dst = new Rect(mSize - mBtnWidth + mBtnPadding, mBtnPadding, mSize - mBtnPadding, mBtnHeight - mBtnPadding);
        canvas.drawBitmap(mBtnImg, src, dst, null);
    }

    /**
     * 设置右上角图片
     *
     * @param bitmap 图片
     */
    public void setBtn(Bitmap bitmap) {
        mBtnImg = bitmap;
        postInvalidate();
    }

    /**
     * 设置右上角图片
     *
     * @param drawable 图片
     */
    public void setBtn(Drawable drawable) {
        mBtnImg = null == drawable ? null : WidgetCoreUtils.drawableToBitmap(drawable);
        postInvalidate();
    }

    /**
     * 设置右上角图片
     *
     * @param resId 图片资源id
     */
    public void setBtn(int resId) {
        if (resId <= 0) {
            return;
        }
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), resId, getContext().getTheme());
        mBtnImg = WidgetCoreUtils.drawableToBitmap(drawable);
        postInvalidate();
    }

    /**
     * 触摸事件处理
     *
     * @param event 具体事件
     * @return 是否已经消费当前事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                // 判断按钮点击有效范围
                if (null != mBtnImg && upX >= mSize - mBtnWidth && upX <= mSize && upY >= 0 && upY <= mBtnHeight && Math.abs(upX - mDownX) < mBtnWidth / 2 && Math.abs(upY - mDownY) < mBtnHeight / 2) {
                    // 点击了删除按钮
                    if (null != mListener) {
                        mListener.onClick(this);
                    }
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 大小改变回调
     *
     * @param w    宽度
     * @param h    高度
     * @param oldw 原始宽度
     * @param oldh 原始高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 由于宽高相等，所以任意一个值都行，这里以宽度为准
        mSize = getMeasuredWidth();
    }

    /**
     * 设置右上角按钮点击事件监听器
     *
     * @param listener 右上角按钮点击事件监听器
     */
    public void setOnBtnClickListener(OnBtnClickListener listener) {
        mListener = listener;
    }

    /**
     * 右上角按钮点击事件监听器
     */
    public interface OnBtnClickListener {
        /**
         * 右上角按钮被点击回调
         *
         * @param siv 当前控件
         */
        void onClick(SquareImageView siv);
    }
}
