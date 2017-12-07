package com.yhy.widget.core.checked;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Checkable;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-06 14:57
 * version: 1.0.0
 * desc   : 可选中的TextView
 */
public class CheckedTextView extends AppCompatTextView implements Checkable {
    // 指示控件已经选中时的状态，如果给控件指定这个属性，就代表控件就已经选中，会调用selector里的android:state_checked="true"来加载它的显示图片
    private final static int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    // 选中状态，默认为false
    private boolean mIsChecked;
    // 是否阻止click和longClick事件，默认为true
    private boolean mIsPrevent;
    // 选中状态改变事件监听器
    private OnCheckedChangeListener mListener;
    // 记录点击事件按下时的坐标
    private float mDownX, mDownY;

    public CheckedTextView(Context context) {
        this(context, null);
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckedTextView);
        mIsPrevent = ta.getBoolean(R.styleable.CheckedTextView_ctv_prevent, true);
        ta.recycle();
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
        mIsChecked = checked;
        // 刷新状态样式
        refreshDrawableState();
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
     * 触摸事件
     *
     * @param event 当前事件
     * @return 事件是否被消费
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                // 如果设置了监听器并且阻止事件的话，就阻止事件。这里主要阻止longClick事件
                return null != mListener && mIsPrevent || super.onTouchEvent(event);
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                // 判断事件触发有效区域
                if (Math.abs(upX - mDownX) < 20 && Math.abs(upY - mDownY) < 20) {
                    // 切换状态
                    toggle();
                    // 如果设置了监听器，就触发状态改变事件
                    if (null != mListener) {
                        mListener.onChanged(this, mIsChecked);
                        // 如果阻止事件，就阻止，这里主要阻止click事件
                        return mIsPrevent || super.onTouchEvent(event);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
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
     * 选中状态改变事件监听器
     */
    public interface OnCheckedChangeListener {
        /**
         * 状态改变回调
         *
         * @param ctv       当前控件
         * @param isChecked 是否被选中
         */
        void onChanged(CheckedTextView ctv, boolean isChecked);
    }
}
