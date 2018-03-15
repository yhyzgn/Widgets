package com.yhy.widget.core.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yhy.widget.R;
import com.yhy.widget.utils.DensityUtils;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-07 16:28
 * version: 1.0.0
 * desc   : 方形ImageView
 */
public class SquareImageView extends AppCompatImageView {
    // 整个控件实际大小
    private int mSize;
    // 右上角按钮图片，默认为空
    private Bitmap mBtnImg;
    // 右上角按钮大小，默认为右上角按钮图片的大小
    private int mBtnSize;
    // 右上角按钮背景颜色，默认为透明
    private int mBtnColor;
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
            mBtnImg = d2b(ta.getDrawable(R.styleable.SquareImageView_siv_btn_img));
        }
        mBtnSize = ta.getDimensionPixelSize(R.styleable.SquareImageView_siv_btn_size, 0);
        mBtnPadding = ta.getDimensionPixelSize(R.styleable.SquareImageView_siv_btn_padding, DensityUtils.dp2px(context, 2.0f));
        mBtnColor = ta.getColor(R.styleable.SquareImageView_siv_btn_color, Color.TRANSPARENT);
        ta.recycle();

        // 默认使用 CENTER_CROP 模式显示图片
        setScaleType(ScaleType.CENTER_CROP);
    }

    /**
     * 测量大小
     *
     * @param widthMeasureSpec  宽度参考值
     * @param heightMeasureSpec 高度参考值
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置参考值长宽相等，都设置为宽度
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    /**
     * 绘制
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
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
        // 绘制背景
        Rect bg = new Rect(mSize - mBtnSize, 0, mSize, mBtnSize);
        Paint paint = new Paint();
        paint.setColor(mBtnColor);
        paint.setAntiAlias(true);
        canvas.drawRect(bg, paint);

        // 绘制按钮图片，包含内边距
        Rect src = new Rect(0, 0, mBtnImg.getWidth(), mBtnImg.getHeight());
        Rect dst = new Rect(mSize - mBtnSize + mBtnPadding, mBtnPadding, mSize - mBtnPadding, mBtnSize - mBtnPadding);
        canvas.drawBitmap(mBtnImg, src, dst, paint);
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
        mBtnImg = null == drawable ? null : d2b(drawable);
        postInvalidate();
    }

    /**
     * 设置右上角图片
     *
     * @param resId 图片资源id
     */
    public void setBtn(int resId) {
        mBtnImg = resId <= 0 ? null : d2b(getResources().getDrawable(resId));
        postInvalidate();
    }

    /**
     * 触摸事件处理
     *
     * @param event 具体事件
     * @return 是否已经消费当前事件
     */
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
                if (null != mBtnImg && upX >= mSize - mBtnSize && upX <= mSize && upY >= 0 && upY <= mBtnSize && Math.abs(upX - mDownX) < mBtnSize / 2 && Math.abs(upY - mDownY) < mBtnSize / 2) {
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
        // 检查并设置按钮默认宽度
        if (null != mBtnImg && mBtnSize == 0) {
            mBtnSize = Math.max(mBtnImg.getWidth(), mBtnImg.getHeight());
        }
        // 内边距最大不能超过整体大小的1/4
        if (mBtnPadding >= mBtnSize / 2) {
            mBtnPadding = mBtnSize / 2;
        }
    }

    /**
     * 将Drawable转换为Bitmap
     *
     * @param drawable 要转换的Drawable
     * @return 转换后的Bitmap
     */
    private Bitmap d2b(Drawable drawable) {
        if (null != drawable) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
        return null;
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
