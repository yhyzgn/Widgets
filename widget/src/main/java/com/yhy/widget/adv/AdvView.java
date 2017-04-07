package com.yhy.widget.adv;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.yhy.widget.R;
import com.yhy.widget.adv.adapter.AdvAdapter;

/**
 * Created by HongYi Yan on 2017/4/6 13:30.
 */
public class AdvView extends ViewFlipper implements View.OnClickListener {
    private static final int DEFAULT_INTERVAL = 3000; // 默认轮播间隔 3 秒
    private static final int DEFAULT_CURATION = 800; // 默认动画执行 0.8 秒
    private static final int DEFAULT_IN_ANIM = R.anim.adv_in; // 默认进入动画 平移加渐变
    private static final int DEFAULT_OUT_ANIM = R.anim.adv_out; // 默认离开动画 平移加渐变

    private Context mContext;

    private int mInterval;
    private int mAnimDuration;
    private int mAnimInResId;
    private int mAnimOutResId;

    private AdvAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener;

    public AdvView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AdvViewAttrs);
        mInterval = ta.getInteger(R.styleable.AdvViewAttrs_av_interval, DEFAULT_INTERVAL);
        mAnimDuration = ta.getInteger(R.styleable.AdvViewAttrs_av_anim_duration,
                DEFAULT_CURATION);
        mAnimInResId = ta.getResourceId(R.styleable.AdvViewAttrs_av_anim_in,
                DEFAULT_IN_ANIM);
        mAnimOutResId = ta.getResourceId(R.styleable.AdvViewAttrs_av_anim_out,
                DEFAULT_OUT_ANIM);
        ta.recycle();

        setFlipInterval(mInterval);

        Animation animIn = AnimationUtils.loadAnimation(mContext, mAnimInResId);
        animIn.setDuration(mAnimDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, mAnimOutResId);
        animOut.setDuration(mAnimDuration);
        setOutAnimation(animOut);
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(AdvAdapter adapter) {
        if (null == adapter) {
            return;
        }
        mAdapter = adapter;

        removeAllViews();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i);
            view.setTag(i);
            addView(view);
            view.setOnClickListener(this);
        }
        startFlipping();
    }


    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        if (null != mOnItemClickListener) {
            mOnItemClickListener.onItemClick(view, position);
        }
    }

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
     * @param listener 条目点击事件
     */
    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 条目点击事件监听器
     */
    public interface OnItemClickListener {
        /**
         * 天目点击事件
         *
         * @param view     当前View
         * @param position 条目索引
         */
        void onItemClick(View view, int position);
    }
}
