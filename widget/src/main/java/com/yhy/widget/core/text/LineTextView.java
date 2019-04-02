package com.yhy.widget.core.text;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-01-24 9:20
 * version: 1.0.0
 * desc   : 添加线条的TextView
 */
public class LineTextView extends AppCompatTextView {
    private float mLineSize;
    private boolean mSetLineColor;
    private int mLineColor;
    private float mLineInterval;
    private LineStyle mLineStyle;

    private float mWidth;
    private float mHeight;

    private PointF mStartPoint;
    private PointF mEndPoint;
    private Paint mPaint;

    public LineTextView(Context context) {
        this(context, null);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mLineStyle = LineStyle.UNDERLINE;
        mStartPoint = new PointF();
        mEndPoint = new PointF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineTextView);
        mLineSize = ta.getDimensionPixelSize(R.styleable.LineTextView_ltv_line_size, 0);
        if ((mSetLineColor = ta.hasValue(R.styleable.LineTextView_ltv_line_color))) {
            mLineColor = ta.getColor(R.styleable.LineTextView_ltv_line_color, Color.BLACK);
        }
        mLineInterval = ta.getDimensionPixelSize(R.styleable.LineTextView_ltv_line_interval, 0);
        int startStyle = ta.getInt(R.styleable.LineTextView_ltv_line_style, mLineStyle.style);
        mLineStyle = startStyle == 1 ? LineStyle.UNDERLINE : startStyle == 2 ? LineStyle.DELETE_MIDDLE : LineStyle.DELETE_OBLIQUE;
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        equipPaint();
        drawLine(canvas);
    }

    /**
     * 绘制线条
     *
     * @param canvas 画布
     */
    private void drawLine(Canvas canvas) {
        mLineColor = mSetLineColor ? mLineColor : getCurrentTextColor();
        mPaint.setColor(mLineColor);
        canvas.drawLine(mStartPoint.x, mStartPoint.y, mEndPoint.x, mEndPoint.y, mPaint);
    }

    /**
     * 配置画笔
     */
    private void equipPaint() {
        mPaint.setStrokeWidth(mLineSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        calculate();
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        // 强制刷新，保证线条颜色是最新设置的颜色（比如线条颜色和字体颜色相同时，如果TextView状态改变，而线条颜色不会改变，需要再绘制一遍）
        postInvalidate();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        // 强制刷新，保证线条颜色是最新设置的颜色（比如线条颜色和字体颜色相同时，如果TextView状态改变，而线条颜色不会改变，需要再绘制一遍）
        postInvalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        // 强制刷新，保证线条颜色是最新设置的颜色（比如线条颜色和字体颜色相同时，如果TextView状态改变，而线条颜色不会改变，需要再绘制一遍）
        postInvalidate();
    }

    /**
     * 根据模式计算各种坐标
     */
    private void calculate() {
        mLineSize = mLineSize == 0 ? getTextSize() / 6 : mLineSize;
        mLineInterval = mLineInterval == 0 ? mLineSize : mLineInterval;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        float textSize = getTextSize();

        switch (mLineStyle) {
            case UNDERLINE:
                // 下划线模式
                mStartPoint.x = paddingLeft;
                mEndPoint.x = mWidth - paddingRight;
                mStartPoint.y = mEndPoint.y = mHeight - paddingBottom + mLineInterval;
                break;
            case DELETE_MIDDLE:
                // 中间删除线模式
                mStartPoint.x = paddingLeft;
                mEndPoint.x = mWidth - paddingRight;
                mStartPoint.y = mEndPoint.y = (mHeight - paddingTop - paddingBottom) / 2 + paddingTop;
                break;
            case DELETE_OBLIQUE:
                // 对角删除线模式
                mStartPoint.x = paddingLeft;
                mStartPoint.y = paddingTop + textSize / 4;
                mEndPoint.x = mWidth - paddingRight;
                mEndPoint.y = mHeight - paddingBottom - textSize / 4;
                break;
        }

        if (mLineStyle == LineStyle.UNDERLINE) {
            mHeight += mLineSize + mLineInterval;
        }
    }

    /**
     * 线条风格
     */
    public enum LineStyle {
        // 下划线；中间删除线；对角删除线
        UNDERLINE(1), DELETE_MIDDLE(2), DELETE_OBLIQUE(3);

        public int style;

        /**
         * 构造函数
         *
         * @param style 风格
         */
        LineStyle(int style) {
            this.style = style;
        }
    }
}
