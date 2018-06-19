package com.yhy.widget.layout.checked;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-08 9:44
 * version: 1.0.0
 * desc   : 可选中的ViewGroup
 */
public abstract class CheckedLayout extends ViewGroup implements Checkable {
    // 指示控件已经选中时的状态，如果给控件指定这个属性，就代表控件就已经选中，会调用selector里的android:state_checked="true"来加载它的显示图片
    private final static int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    // 选中状态，默认为false
    private boolean mIsChecked;
    // 选中状态改变事件监听器
    private OnCheckedChangeListener mListener;
    // 状态改变前观察者
    private BeforeCheckWatcher mWatcher;

    public CheckedLayout(Context context) {
        this(context, null);
    }

    public CheckedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置选中状态
     *
     * @param checked 是否选中
     */
    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked == checked) {
            return;
        }

        if (null != mWatcher && !mWatcher.beforeChange(this, checked)) {
            // 状态改变前阻止改变
            return;
        }

        mIsChecked = checked;
        // 刷新状态样式
        refreshDrawableState();
        // 刷新子控件状态样式
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).refreshDrawableState();
        }

        // 回调状态改变事件
        if (null != mListener) {
            mListener.onChanged(this, mIsChecked);
        }
    }

    /**
     * 获取选中状态
     *
     * @return 是否被选中
     */
    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    /**
     * 切换状态
     */
    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }

    /**
     * 创建各状态下样式资源
     *
     * @param extraSpace 额外控件，即有多少种状态，状态种类数量
     * @return 总共有哪些状态，指具体状态
     */
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        // 新增一个状态（可被选中的状态）
        int[] currentState = super.onCreateDrawableState(extraSpace + 1);
        if (mIsChecked) {
            mergeDrawableStates(currentState, CHECKED_STATE_SET);
        }
        return currentState;
    }

    /**
     * 事件分发处理
     *
     * @param event 当前事件
     * @return 是否拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // 切换状态
                if(isEnabled()) {
                    toggle();
                }
                break;
            default:
                break;
        }
        // return true 拦截事件，不然子控件触发触摸事件
        return true;
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
        // 子视图与父控件perssed 等状态保持一致
        dispatchChildren(this);
    }

    /**
     * 设置子视图与父控件perssed 等状态保持一致
     *
     * @param view 各级子控件
     */
    private void dispatchChildren(View view) {
        // 子视图与父控件perssed 等状态保持一致
        view.setDuplicateParentStateEnabled(true);
        if (view instanceof ViewGroup && ((ViewGroup) view).getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                dispatchChildren(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    /**
     * 设置选中状态改变事件监听器
     *
     * @param listener 选中状态改变事件监听器
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置状态改变前观察者
     *
     * @param watcher 状态改变前观察者
     */
    public void setBeforeCheckWatcher(BeforeCheckWatcher watcher) {
        mWatcher = watcher;
    }

    /**
     * 状态改变前观察者
     */
    public interface BeforeCheckWatcher {
        /**
         * 状态改变前回调
         *
         * @param cl        当前控件
         * @param isChecked 是否将要被选中
         * @return 是否可被选中
         */
        boolean beforeChange(CheckedLayout cl, boolean isChecked);
    }

    /**
     * 选中状态改变事件监听器
     */
    public interface OnCheckedChangeListener {
        /**
         * 状态改变回调
         *
         * @param cl        当前控件
         * @param isChecked 是否被选中
         */
        void onChanged(CheckedLayout cl, boolean isChecked);
    }
}
