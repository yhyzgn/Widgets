package com.yhy.widget.layout.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;
import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 11:38
 * version: 1.0.0
 * desc   : 仿QQ 5.0 侧滑菜单
 */
public class SlideLayout extends FrameLayout {
    //滑动过程中背景渐变颜色，默认是黑色
    private static final int DEF_ANIM_ALPHA_COLOR = Color.BLACK;
    //是否开启边缘滑动，默认是true
    private static final boolean DEF_EDGE_ENABLE = true;
    //是否允许滑动过程中主面板透明度变化，默认是false
    private static final boolean DEF_MAIN_ALPHA_ENABLE = true;
    //最小滑动速度，默认是400
    private static final int DEF_MIN_FLING_VELOCITY = 400;
    //ViewDragHelper灵敏度，默认1.0f
    private static final float DEF_SENSITIVITY = 1.0f;
    //ViewDragHelper回调处理
    private DragCallback mCallback;
    //ViewDragHelper
    private ViewDragHelper mDragHelper;
    //屏幕触控事件监听器
    private GestureListener mGestureListener;
    //屏幕触控处理
    private GestureDetectorCompat mGestureDetector;
    //左侧菜单栏
    private ViewGroup mLeftView;
    //主面板
    private ViewGroup mMainView;
    //整个控件的宽度
    private int mWidth;
    //整个控间的高度
    private int mHeight;
    //滑动范围，左侧菜单栏的宽度
    private int mSlideRang;
    //滑动过程中背景的渐变颜色
    private int mAlphaColor;
    //是否开启边缘滑动
    private boolean mEdgeEnable;
    //当前已滑动的距离
    private int mSlideDx;
    //当前状态
    private Status mStatus;
    //滑动状态变化监听器
    private OnStateChangeListener mListener;
    //决定侧边栏是否可滑动的观察器
    private OnSlideEnableWatcher mWatcher;
    //手指按下的x坐标
    private float mDownX;
    //移动过的x方向距离
    private float mMoveX;
    //主面板是否可透明
    private boolean mMainAlphaEnable;

    public SlideLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //获取相关属性设置
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideLayout);
        mAlphaColor = ta.getColor(R.styleable.SlideLayout_sl_anim_alpha_color, DEF_ANIM_ALPHA_COLOR);
        mEdgeEnable = ta.getBoolean(R.styleable.SlideLayout_sl_edge_enable, DEF_EDGE_ENABLE);
        mMainAlphaEnable = ta.getBoolean(R.styleable.SlideLayout_sl_main_alpha_enable, DEF_MAIN_ALPHA_ENABLE);
        ta.recycle();

        //初始化ViewDragHelper相关
        mCallback = new DragCallback();
        mDragHelper = ViewDragHelper.create(this, DEF_SENSITIVITY, mCallback);

        if (mEdgeEnable) {
            //设置边缘滑动检测
            mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
            //设置最小滑动速度
            mDragHelper.setMinVelocity(DEF_SENSITIVITY * DEF_MIN_FLING_VELOCITY);
        }

        //初始化屏幕触控处理相关
        mGestureListener = new GestureListener();
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);

        //设置初始状态为关闭
        mStatus = Status.CLOSED;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果当前为滑动中状态，不让ViewDragHelper拦截事件
        //解决滑动过程中点击界面后动画停止的bug，返回true或者false都可以，目的只是不让ViewDragHelper拦截事件
        if (mStatus == Status.DRAGGING) {
            return true;
        }

        int action = MotionEventCompat.getActionMasked(ev);
        if (mStatus == Status.OPENED) {
            //如果当前为打开状态，就在点击主面板时关闭侧边栏
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = ev.getRawX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //如果点击的位置在可滑动范围外，滑动时就直接拦截事件，避免打开侧边栏后内容页面还可滑动
                    if (mDownX > mSlideRang) {
                        return true;
                    }
                    mMoveX = ev.getRawX() - mDownX;
                    break;
                case MotionEvent.ACTION_UP:
                    //如果点击的位置在滑动范围外并且未进行移动过，就说明是单纯的点击，需要关闭侧边栏
                    if (mDownX > mSlideRang && mMoveX < 1.0f) {
                        close();
                    }
                    mMoveX = 0;
                    break;
                default:
                    mDownX = 0;
                    mMoveX = 0;
                    break;
            }
        }

        //通过该观察者判断是否允许侧边栏滑动
        if (null != mWatcher && !mWatcher.shouldEnable()) {
            return false;
        }
        //将当前事件交给ViewDragHelper处理
        return mDragHelper.shouldInterceptTouchEvent(ev) & mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将事件交给ViewDragHelper处理
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //消耗当前事件
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //判断子View的有效性
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalStateException("SlideLayout must has two child views.");
        }

        if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalStateException("The every child view of SlideLayout must be instance of ViewGroup.");
        }

        //分别设置侧边栏控件和主面板控件
        mLeftView = (ViewGroup) getChildAt(0);
        mMainView = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //获取高度和宽度
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //侧边栏宽度，滑动的范围
        mSlideRang = mLeftView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //布局子View
        layoutViews();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        //必须重写该方法，并让ViewDragHelper持续处理动画
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 布局子view
     */
    private void layoutViews() {
        mLeftView.layout(0, 0, mSlideRang, mHeight);
        mMainView.layout(mSlideDx, 0, mSlideDx + mWidth, mHeight);
    }

    private void dispatchDragEvent() {
        //伴随动画，返回当前滑动的比例
        float percent = animViews();

        //执行回调
        //判断mSlideDx > 0 && mSlideDx < mSlideRang，目的是在侧边栏打开或者关闭状态时不再回调onDragging()事件
        if (mSlideDx > 0 && mSlideDx < mSlideRang && null != mListener) {
            mListener.onDragging(percent, mSlideDx, mSlideRang);
        }

        Status preStatus = mStatus;
        //改变状态
        mStatus = updateStatus(percent);
        if (mStatus != preStatus) {
            if (mStatus == Status.OPENED) {
                if (null != mListener) {
                    mListener.onOpened();
                }
            } else if (mStatus == Status.CLOSED) {
                if (null != mListener) {
                    mListener.onClosed();
                }
            }
        }
    }

    /**
     * 更新状态
     *
     * @param percent 当前滑动比例
     * @return 更新后的状态
     */
    private Status updateStatus(float percent) {
        if (percent == 0.0f) {
            return Status.CLOSED;
        } else if (percent == 1.0f) {
            return Status.OPENED;
        }
        return Status.DRAGGING;
    }

    /**
     * 伴随动画
     *
     * @return 当前滑动的比例
     */
    private float animViews() {
        float percent = mSlideDx * 1.0f / mSlideRang;

        // 左面板: 缩放动画, 平移动画, 透明度动画
//        ViewHelper.setScaleX(mLeftView, 0.4f + 0.4f * percent);
//        ViewHelper.setScaleY(mLeftView, 0.4f + 0.4f * percent);
        ViewHelper.setScaleX(mLeftView, evaluate(percent, 0.4f, 1.0f));
        ViewHelper.setScaleY(mLeftView, evaluate(percent, 0.4f, 1.0f));
        // 平移动画: -mWidth / 2.0f -> 0.0f
        ViewHelper.setTranslationX(mLeftView, evaluate(percent, -mWidth / 2.0f, 0));
        // 透明度: 0.4 -> 1.0f
        ViewHelper.setAlpha(mLeftView, evaluate(percent, 0.4f, 1.0f));

        // 主面板: 缩放动画  1.0f -> 0.8f
        ViewHelper.setScaleX(mMainView, evaluate(percent, 1.0f, 0.8f));
        ViewHelper.setScaleY(mMainView, evaluate(percent, 1.0f, 0.8f));

        // 透明度: 1.0 -> 0.6f
        if (mMainAlphaEnable) {
            ViewHelper.setAlpha(mMainView, evaluate(percent, 1.0f, 0.6f));
        }

        // 整个控件的背景动画: 亮度变化 (颜色变化)
        if (null != getBackground()) {
            getBackground().setColorFilter((Integer) evaluateColor(percent, mAlphaColor, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
        }

        return percent;
    }

    /**
     * 估值器
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 颜色变化过度估值器
     */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    /**
     * 判断当前状态是否是打开侧边栏状态
     *
     * @return 是否已经打开侧边栏
     */
    public boolean isOpened() {
        return mStatus == Status.OPENED;
    }

    /**
     * 打开侧边栏，带动画效果
     */
    public void open() {
        open(true);
    }

    /**
     * 打开侧边栏，可关闭动画效果
     *
     * @param smooth 是否带动画效果
     */
    public void open(boolean smooth) {
        mSlideDx = mSlideRang;
        openOrClose(smooth);
    }

    /**
     * 关闭侧边栏，带动画效果
     */
    public void close() {
        close(true);
    }

    /**
     * 关闭侧边栏，可关闭动画效果
     *
     * @param smooth 是否带动画效果
     */
    public void close(boolean smooth) {
        mSlideDx = 0;
        openOrClose(smooth);
    }

    /**
     * 打开或者关闭都可重用
     *
     * @param smooth 是否带动画效果
     */
    private void openOrClose(boolean smooth) {
        if (smooth) {
            // 引发动画的开始
            if (mDragHelper.smoothSlideViewTo(mMainView, mSlideDx, 0)) {
                // 需要重写computeScroll方法，并使用continueSettling方法才能将动画继续下去（因为ViewDragHelper使用了scroller）。
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            //直接重新布局view
            layoutViews();
            //执行动画，更新状态
            dispatchDragEvent();
        }
    }

    /**
     * 设置状态监听器
     *
     * @param listener 状态监听器
     */
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置可滑动观察器
     *
     * @param watcher 可滑动观察器
     */
    public void setOnSlideEnableWatcher(OnSlideEnableWatcher watcher) {
        mWatcher = watcher;
    }

    /**
     * ViewDragHelper的回调处理
     */
    private class DragCallback extends ViewDragHelper.Callback {

        /**
         * 决定当前被拖拽的child是否拖的动。(抽象方法，必须重写)
         *
         * @param child     当前拖拽的view
         * @param pointerId pointerId
         * @return 是否可捕获
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //如果开启了边缘滑动，就只捕获侧边栏控件
            if (mEdgeEnable) {
                return child == mLeftView;
            }
            //否则捕获任意控件
            return true;
        }

        /**
         * 决定拖拽的范围
         * 这里并不能限制滑动的范围，只是决定动画执行的效果
         *
         * @param child 当前拖拽的view
         * @return 滑动范围
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mSlideRang;
        }

        /**
         * 决定拖动时的位置，可在这里进行位置修正
         * 若想在此方向拖动，必须重写，因为默认返回0
         * <p>
         * 参数说明：
         * left = child.getLeft() + dx
         *
         * @param child 当前滑动的view
         * @param left  新的left值，也就是下一个位置的left值
         * @param dx    滑动过的距离
         * @return 最终决定位置的left值
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mMainView) {
                //修正期望值
                left = clampDx(left);
            }
            return left;
        }

        /**
         * 决定了当View被拖动时，希望同时引发的其他变化
         *
         * @param changedView 当前在改变的view
         * @param left        新的left值
         * @param top         新的top值
         * @param dx          x方向上滑动的距离
         * @param dy          y方向上滑动的距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            mSlideDx = left;

            //如果改变的view是侧边栏控件，就把相关变化同步给主面板控件，只让主面板发生变化
            if (changedView == mLeftView) {
                mSlideDx = mMainView.getLeft() + dx;
            }

            //值的修正
            mSlideDx = clampDx(mSlideDx);

            //重新布局
            layoutViews();
            // 为了兼容低版本, 每次修改值之后, 进行重绘
            invalidate();

            //触发回调事件
            dispatchDragEvent();
        }

        /**
         * 决定当childView被释放时，希望做的事情——执行打开/关闭动画，更新状态
         *
         * @param releasedChild 被释放的view
         * @param xvel          x方向上的速度
         * @param yvel          y方向上的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel > 0 || xvel == 0 && mSlideDx > mSlideRang / 2) {
                //如果当前x方向上的速度不为0或者滑动的距离已经超过可滑动范围的一半，就打开侧边栏
                open();
            } else {
                //否则就关闭侧边栏
                close();
            }
        }

        /**
         * 开始边缘滑动
         *
         * @param edgeFlags 边缘标识，具体方向
         * @param pointerId pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //滑动边缘时
            if (mEdgeEnable) {
                //如果开启边缘滑动，就捕获侧边栏控件
                mDragHelper.captureChildView(mLeftView, pointerId);
            } else {
                //否则就捕获主面板控件
                mDragHelper.captureChildView(mMainView, pointerId);
            }
        }

        /**
         * 修正值
         *
         * @param left 新的left值
         * @return 最终修正后的left值
         */
        private int clampDx(int left) {
            if (left < 0) {
                return 0;
            }
            if (left > mSlideRang) {
                return mSlideRang;
            }
            return left;
        }
    }

    /**
     * 屏幕触控监听器，重写onScroll方法，决定监听的滑动方向
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceX) >= Math.abs(distanceY);
        }
    }

    /**
     * 状态的枚举
     */
    public enum Status {
        OPENED, CLOSED, DRAGGING;
    }

    /**
     * 状态监听器
     */
    public interface OnStateChangeListener {
        /**
         * 侧边栏已打开
         */
        void onOpened();

        /**
         * 侧边栏已关闭
         */
        void onClosed();

        /**
         * 正在拖动
         *
         * @param percent 当前拖动的比例
         */
        void onDragging(float percent, int dx, int total);
    }

    /**
     * 决定侧边栏是否可滑动的观察器
     */
    public interface OnSlideEnableWatcher {
        /**
         * 返回当前侧边栏是否可滑动
         * true -> 可用 false -> 不可用
         *
         * @return 当前侧边栏是否可滑动
         */
        boolean shouldEnable();
    }
}