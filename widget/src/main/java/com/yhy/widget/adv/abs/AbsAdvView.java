package com.yhy.widget.adv.abs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.yhy.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HongYi Yan on 2017/4/6 13:30.
 */
public abstract class AbsAdvView extends ViewFlipper {

    private Context mContext;
    private List<? extends View> mViewList;
    private boolean mIsSetAnimDuration = false;
    private OnItemClickListener onItemClickListener;

    private int mInterval = 2000;
    private int mAnimDuration = 500;

    public AbsAdvView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (mViewList == null) {
            mViewList = new ArrayList<>();
        }

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AbsAdvViewAttrs);
        mInterval = ta.getInteger(R.styleable.AbsAdvViewAttrs_aav_interval, mInterval);
        mIsSetAnimDuration = ta.hasValue(R.styleable.AbsAdvViewAttrs_aav_anim_duration);
        mAnimDuration = ta.getInteger(R.styleable.AbsAdvViewAttrs_aav_anim_duration, mAnimDuration);
        ta.recycle();

        setFlipInterval(mInterval);

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.adv_in);
        if (mIsSetAnimDuration) {
            animIn.setDuration(mAnimDuration);
        }
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.adv_out);
        if (mIsSetAnimDuration) {
            animOut.setDuration(mAnimDuration);
        }
        setOutAnimation(animOut);
    }

    /**
     * 开始轮播
     *
     * @return 是否开始
     */
    protected boolean start() {
        mViewList = getViewList();

        if (mViewList == null || mViewList.isEmpty()) {
            return false;
        }
        removeAllViews();

        for (int i = 0; i < mViewList.size(); i++) {
            final View view = mViewList.get(i);
            view.setTag(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, (int) view.getTag());
                    }
                }
            });
            addView(view);
        }

        if (mViewList.size() > 1) {
            startFlipping();
        }
        return true;
    }

    /**
     * 从子类中获取View集合
     *
     * @return View集合
     */
    protected abstract List<? extends View> getViewList();

    /**
     * 获取当前展示View的索引
     *
     * @return View的index
     */
    public int getCurrentItem() {
        return (int) getCurrentView().getTag();
    }

    /**
     * 设置当前展示View的索引
     *
     * @param index View的index
     */
    public void setCurrentItem(int index) {
        setDisplayedChild(index);
    }

    /**
     * 设置条目点击事件
     *
     * @param onItemClickListener 条目点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 条目点击事件监听器
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
