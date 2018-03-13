package com.yhy.widget.core.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:19
 * version: 1.0.0
 * desc   :
 */
public class RecyclerScrollView extends ScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;
    private OnScrollListener mListener;

    public RecyclerScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public RecyclerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public RecyclerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                // 调用一下父类方法，解决“Invalid pointerId=-1 in onTouchEvent”问题
                super.onInterceptTouchEvent(e);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        // 不让super.onInterceptTouchEvent(event);调用的时候直接返回false
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mListener) {
            mListener.onScroll(this, l, t, oldl, oldt);
        }
    }

    /**
     * 设置滚动监听器
     *
     * @param listener 滚动监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    /**
     * 滚动监听器
     */
    public interface OnScrollListener {

        /**
         * 当ScrollView发生滚动时回调
         *
         * @param view 当前控件
         * @param x    x方向当前值
         * @param y    y方向当前值
         * @param oldX x方向前一次值
         * @param oldY y方向前一次值
         */
        void onScroll(RecyclerScrollView view, int x, int y, int oldX, int oldY);
    }
}
