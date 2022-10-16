package com.yhy.widget.core.checked;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.yhy.widget.R;
import com.yhy.widget.utils.WidgetCoreUtils;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-01-22 11:07
 * version: 1.0.0
 * desc   : 带动画的多选框
 */
public class CheckBox extends View implements Checkable {
    private static final String KEY_INSTANCE_STATE = "InstanceState";

    private static final int COLOR_TICK = Color.WHITE;
    private static final int COLOR_UNCHECKED = Color.WHITE;
    private static final int COLOR_CHECKED = Color.parseColor("#FB4846");
    private static final int COLOR_FLOOR_UNCHECKED = Color.parseColor("#DFDFDF");

    private static final int DEF_DRAW_SIZE = 25;
    private static final int DEF_ANIM_DURATION = 300;
    private static InverseBindingListener mCheckedBindingListener;

    private Paint mPaint, mTickPaint, mFloorPaint;
    private Point[] mTickPoints;
    private Point mCenterPoint;
    private Path mTickPath;

    private float mLeftLineDistance, mRightLineDistance, mDrewDistance;
    private float mScaleVal = 1.0f, mFloorScale = 1.0f;
    private int mWidth, mAnimDuration, mStrokeWidth;
    private int mCheckedColor, mUnCheckedColor, mBorderColor, mBorderUnCheckedColor;
    // 是否可手动取消，只针对手动操作，默认为true
    private boolean mClickCancelAble;

    private boolean mChecked;
    private boolean mTickDrawing;
    private OnCheckedChangeListener mListener;

    public CheckBox(Context context) {
        this(context, null);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs 属性集
     */
    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CheckBox);
        int tickColor = ta.getColor(R.styleable.CheckBox_cb_color_tick, COLOR_TICK);
        mAnimDuration = ta.getInt(R.styleable.CheckBox_cb_duration, DEF_ANIM_DURATION);
        mBorderColor = ta.getColor(R.styleable.CheckBox_cb_color_unchecked_stroke, COLOR_FLOOR_UNCHECKED);
        mCheckedColor = ta.getColor(R.styleable.CheckBox_cb_color_checked, COLOR_CHECKED);
        mUnCheckedColor = ta.getColor(R.styleable.CheckBox_cb_color_unchecked, COLOR_UNCHECKED);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.CheckBox_cb_stroke_width, WidgetCoreUtils.dp2px(getContext(), 0));
        mClickCancelAble = ta.getBoolean(R.styleable.CheckBox_cb_click_cancel_able, true);
        ta.recycle();

        mBorderUnCheckedColor = mBorderColor;
        mTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickPaint.setStyle(Paint.Style.STROKE);
        mTickPaint.setStrokeCap(Paint.Cap.ROUND);
        mTickPaint.setColor(tickColor);

        mFloorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFloorPaint.setStyle(Paint.Style.FILL);
        mFloorPaint.setColor(mBorderColor);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCheckedColor);

        mTickPath = new Path();
        mCenterPoint = new Point();
        mTickPoints = new Point[3];
        mTickPoints[0] = new Point();
        mTickPoints[1] = new Point();
        mTickPoints[2] = new Point();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(KEY_INSTANCE_STATE, isChecked());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            boolean isChecked = bundle.getBoolean(KEY_INSTANCE_STATE);
            setChecked(isChecked);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    /**
     * 设置是否选中
     *
     * @param checked 是否选中
     * @param animate 是否带动画
     */
    public void setChecked(boolean checked, boolean animate) {
        if (checked == mChecked) {
            return;
        }
        mChecked = checked;

        if (animate) {
            mTickDrawing = false;
            mDrewDistance = 0f;
            if (checked) {
                startCheckedAnimation();
            } else {
                startUnCheckedAnimation();
            }
        } else {
            reset();
            invalidate();
        }
        if (mListener != null) {
            mListener.onCheckedChanged(this, mChecked);
        }
        if (null != mCheckedBindingListener) {
            mCheckedBindingListener.onChange();
        }
    }

    /**
     * 重置状态
     */
    private void reset() {
        mTickDrawing = true;
        mFloorScale = 1.0f;
        mScaleVal = isChecked() ? 0f : 1.0f;
        mBorderColor = isChecked() ? mCheckedColor : mBorderUnCheckedColor;
        mDrewDistance = isChecked() ? (mLeftLineDistance + mRightLineDistance) : 0;
    }

    /**
     * 测量大小
     *
     * @param measureSpec 测量模式
     * @return 真实大小
     */
    private int measureSize(int measureSpec) {
        int defSize = WidgetCoreUtils.dp2px(getContext(), DEF_DRAW_SIZE);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(widthMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getMeasuredWidth();
        mStrokeWidth = (mStrokeWidth == 0 ? getMeasuredWidth() / 10 : mStrokeWidth);
        mStrokeWidth = Math.min(mStrokeWidth, getMeasuredWidth() / 5);
        mStrokeWidth = Math.max(mStrokeWidth, 3);
        mCenterPoint.x = mWidth / 2;
        mCenterPoint.y = getMeasuredHeight() / 2;

        mTickPoints[0].x = Math.round((float) getMeasuredWidth() / 30 * 7);
        mTickPoints[0].y = Math.round((float) getMeasuredHeight() / 30 * 14);
        mTickPoints[1].x = Math.round((float) getMeasuredWidth() / 30 * 13);
        mTickPoints[1].y = Math.round((float) getMeasuredHeight() / 30 * 20);
        mTickPoints[2].x = Math.round((float) getMeasuredWidth() / 30 * 22);
        mTickPoints[2].y = Math.round((float) getMeasuredHeight() / 30 * 10);

        mLeftLineDistance = (float) Math.sqrt(Math.pow(mTickPoints[1].x - mTickPoints[0].x, 2) + Math.pow(mTickPoints[1].y - mTickPoints[0].y, 2));
        mRightLineDistance = (float) Math.sqrt(Math.pow(mTickPoints[2].x - mTickPoints[1].x, 2) + Math.pow(mTickPoints[2].y - mTickPoints[1].y, 2));
        mTickPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBorder(canvas);
        drawCenter(canvas);
        drawTick(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                // 如果当前已选中，并且不可以手动取消，就阻止选中
                if (mChecked && !mClickCancelAble) {
                    return true;
                }
                if (isEnabled()) {
                    toggle();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 绘制中心点
     *
     * @param canvas 画布
     */
    private void drawCenter(Canvas canvas) {
        mPaint.setColor(mUnCheckedColor);
        float radius = (mCenterPoint.x - mStrokeWidth) * mScaleVal;
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaint);
    }

    /**
     * 绘制边框
     *
     * @param canvas 画布
     */
    private void drawBorder(Canvas canvas) {
        mFloorPaint.setColor(mBorderColor);
        int radius = mCenterPoint.x;
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius * mFloorScale, mFloorPaint);
    }

    /**
     * 绘制对钩
     *
     * @param canvas 画布
     */
    private void drawTick(Canvas canvas) {
        if (mTickDrawing && isChecked()) {
            drawTickPath(canvas);
        }
    }

    /**
     * 绘制对钩路径
     *
     * @param canvas 画布
     */
    private void drawTickPath(Canvas canvas) {
        mTickPath.reset();
        // draw left of the tick
        if (mDrewDistance < mLeftLineDistance) {
            float step = (mWidth / 20.0f) < 3 ? 3 : (mWidth / 20.0f);
            mDrewDistance += step;
            float stopX = mTickPoints[0].x + (mTickPoints[1].x - mTickPoints[0].x) * mDrewDistance / mLeftLineDistance;
            float stopY = mTickPoints[0].y + (mTickPoints[1].y - mTickPoints[0].y) * mDrewDistance / mLeftLineDistance;

            mTickPath.moveTo(mTickPoints[0].x, mTickPoints[0].y);
            mTickPath.lineTo(stopX, stopY);
            canvas.drawPath(mTickPath, mTickPaint);

            if (mDrewDistance > mLeftLineDistance) {
                mDrewDistance = mLeftLineDistance;
            }
        } else {

            mTickPath.moveTo(mTickPoints[0].x, mTickPoints[0].y);
            mTickPath.lineTo(mTickPoints[1].x, mTickPoints[1].y);
            canvas.drawPath(mTickPath, mTickPaint);

            // draw right of the tick
            if (mDrewDistance < mLeftLineDistance + mRightLineDistance) {
                float stopX = mTickPoints[1].x + (mTickPoints[2].x - mTickPoints[1].x) * (mDrewDistance - mLeftLineDistance) / mRightLineDistance;
                float stopY = mTickPoints[1].y - (mTickPoints[1].y - mTickPoints[2].y) * (mDrewDistance - mLeftLineDistance) / mRightLineDistance;

                mTickPath.reset();
                mTickPath.moveTo(mTickPoints[1].x, mTickPoints[1].y);
                mTickPath.lineTo(stopX, stopY);
                canvas.drawPath(mTickPath, mTickPaint);

                float step = Math.max((mWidth / 20), 3);
                mDrewDistance += step;
            } else {
                mTickPath.reset();
                mTickPath.moveTo(mTickPoints[1].x, mTickPoints[1].y);
                mTickPath.lineTo(mTickPoints[2].x, mTickPoints[2].y);
                canvas.drawPath(mTickPath, mTickPaint);
            }
        }

        // invalidate
        if (mDrewDistance < mLeftLineDistance + mRightLineDistance) {
            postDelayed(this::postInvalidate, 10);
        }
    }

    /**
     * 开始选中状态的动画
     */
    private void startCheckedAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0f);
        animator.setDuration(mAnimDuration / 3 * 2);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            mScaleVal = (float) animation.getAnimatedValue();
            mBorderColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mScaleVal);
            postInvalidate();
        });
        animator.start();

        ValueAnimator floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f);
        floorAnimator.setDuration(mAnimDuration);
        floorAnimator.setInterpolator(new LinearInterpolator());
        floorAnimator.addUpdateListener(animation -> {
            mFloorScale = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        floorAnimator.start();

        drawTickDelayed();
    }

    /**
     * 开始取消选中的动画
     */
    private void startUnCheckedAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setDuration(mAnimDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            mScaleVal = (float) animation.getAnimatedValue();
            mBorderColor = getGradientColor(mCheckedColor, mBorderUnCheckedColor, mScaleVal);
            postInvalidate();
        });
        animator.start();

        ValueAnimator floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f);
        floorAnimator.setDuration(mAnimDuration);
        floorAnimator.setInterpolator(new LinearInterpolator());
        floorAnimator.addUpdateListener(animation -> {
            mFloorScale = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        floorAnimator.start();
    }

    /**
     * 绘制对钩
     */
    private void drawTickDelayed() {
        postDelayed(() -> {
            mTickDrawing = true;
            postInvalidate();
        }, mAnimDuration);
    }

    /**
     * 获取渐变颜色值
     *
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     * @param percent    百分比
     * @return 计算结果
     */
    private static int getGradientColor(int startColor, int endColor, float percent) {
        int startA = Color.alpha(startColor);
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);

        int endA = Color.alpha(endColor);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);

        int currentA = (int) (startA * (1 - percent) + endA * percent);
        int currentR = (int) (startR * (1 - percent) + endR * percent);
        int currentG = (int) (startG * (1 - percent) + endG * percent);
        int currentB = (int) (startB * (1 - percent) + endB * percent);
        return Color.argb(currentA, currentR, currentG, currentB);
    }

    @BindingAdapter({"android:checked"})
    public static void setCheckBoxChecked(CheckBox cb, boolean checked) {
        cb.setChecked(checked, true);
    }

    @InverseBindingAdapter(attribute = "android:checked", event = "cbStatusChanged")
    public static boolean isCheckBoxChecked(CheckBox cb) {
        return cb.isChecked();
    }

    @BindingAdapter("cbStatusChanged")
    public static void sivTextChanged(CheckBox cb, InverseBindingListener listener) {
        mCheckedBindingListener = listener;
    }

    /**
     * 设置状态改变监听事件
     *
     * @param listener 监听器
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 状态改变监听器
     */
    public interface OnCheckedChangeListener {
        /**
         * 状态改变时回调
         *
         * @param checkBox  当前控件
         * @param isChecked 是否被选中
         */
        void onCheckedChanged(CheckBox checkBox, boolean isChecked);
    }
}