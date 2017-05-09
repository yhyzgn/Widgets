package com.yhy.widget.tv;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhy.widget.R;

/**
 * Created by HongYi Yan on 2017/5/8 15:45.
 */
public class ExpandTextView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = ExpandTextView.class.getSimpleName();

    //默认显示行数
    private static final int MAX_COLLAPSED_LINES = 2;
    //默认动画执行时间
    private static final int DEFAULT_ANIM_DURATION = 400;
    //默认动画开始数值
    private static final float DEFAULT_ANIM_ALPHA_START = 0.6f;

    //显示内容的TextView
    protected TextView tvContent;
    //点击展开和收起按钮
    protected View vExpand;
    //是否重新布局
    private boolean mRelayout;
    //是否折叠，默认这折叠
    private boolean mCollapsed = true;
    //折叠后的高度
    private int mCollapsedHeight;
    //展开后的高度
    private int mTextHeightWithMaxLines;
    //折叠时显示的行数
    private int mMaxCollapsedLines;
    //TextView Margin
    private int mMarginBetweenTxtAndBottom;
    //动画执行时间
    private int mAnimationDuration;
    //动画开始数值
    private float mAnimAlphaStart;
    //是否正在执行动画
    private boolean mAnimating;
    //展开收缩状态改变事件监听器
    private OnExpandStateChangeListener mListener;
    //用来保存状态的队列
    private SparseBooleanArray mCollapsedStatus;
    //用来在状态队列中标记当前ExpandTextView的key
    private int mPosition;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
//            throw new IllegalArgumentException("ExpandTextView only supports Vertical Orientation.");
            //强制使用垂直布局
            super.setOrientation(LinearLayout.VERTICAL);
        }
        super.setOrientation(orientation);
    }

    /**
     * 设置显示内容的TextView的id和扩展按钮的id
     *
     * @param tvContentId 显示内容的TextView的id
     * @param vExpandId   扩展按钮的id
     */
    public void mapViewId(@IdRes int tvContentId, @IdRes int vExpandId) {
        if (tvContentId > 0) {
            tvContent = (TextView) ((Activity) getContext()).findViewById(tvContentId);
        }
        if (vExpandId > 0) {
            vExpand = ((Activity) getContext()).findViewById(vExpandId);
            vExpand.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        //改变状态
        mCollapsed = !mCollapsed;

        //添加状态到状态队列中
        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }

        //正在执行动画
        mAnimating = true;

        //创建动画
        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() + mTextHeightWithMaxLines - tvContent.getHeight());
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                applyAlphaAnimation(tvContent, mAnimAlphaStart);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后，清除动画
                clearAnimation();
                //没有正在执行动画
                mAnimating = false;

                //回调状态监听器
                if (mListener != null) {
                    mListener.onExpandStateChanged(tvContent, !mCollapsed);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        clearAnimation();
        startAnimation(animation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果正在执行动画，就拦截事件，不在往下传递，否则就往下传递事件
        return mAnimating;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //如果布局没有改变过，就直接返回
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        //如果布局改变过，就更新为未改变
        mRelayout = false;

        //设置TextView最大行数
        tvContent.setMaxLines(Integer.MAX_VALUE);

        //测量一遍尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //如果文本行数<=显示的最大行数，就隐藏扩展按钮，并返回
        if (tvContent.getLineCount() <= mMaxCollapsedLines) {
            vExpand.setVisibility(GONE);
            return;
        }

        //否则就显示扩展按钮
        vExpand.setVisibility(VISIBLE);

        // 获取到TextView的真实高度
        mTextHeightWithMaxLines = getRealTextViewHeight(tvContent);

        //设置TextView最大行数为允许显示的行数
        if (mCollapsed) {
            tvContent.setMaxLines(mMaxCollapsedLines);
        }

        //重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCollapsed) {
            //获取TextView的Margin值等
            tvContent.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - tvContent.getHeight();
                }
            });
            mCollapsedHeight = getMeasuredHeight();
        }
    }

    /**
     * 设置状态监听事件
     *
     * @param listener 监听事件
     */
    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置文本内容；mapViewId() 之后调用
     *
     * @param text 文本内容
     */
    public void setText(@Nullable CharSequence text) {
        if (null == tvContent) {
            throw new RuntimeException("Must call method mapViewId to set view id at first.");
        }
        mRelayout = true;
        tvContent.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置文本内容；mapViewId() 之后调用
     *
     * @param text            文本内容
     * @param collapsedStatus 状态队列
     * @param position        在状态队列中标记当前ExpandTextView的key
     */
    public void setText(@Nullable CharSequence text, @NonNull SparseBooleanArray collapsedStatus, int position) {
        if (null == tvContent) {
            throw new RuntimeException("Must call method mapViewId to set view id at first.");
        }

        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    /**
     * 获取文本内容
     *
     * @return 文本内容
     */
    @Nullable
    public CharSequence getText() {
        if (tvContent == null) {
            return "";
        }
        return tvContent.getText();
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandTextViewAttrs);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandTextViewAttrs_etv_max_collapsed_lines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandTextViewAttrs_etv_anim_duration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandTextViewAttrs_etv_anim_alpha_start, DEFAULT_ANIM_ALPHA_START);

        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);

        setVisibility(GONE);
    }

    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void applyAlphaAnimation(View view, float alpha) {
        if (isPostHoneycomb()) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    private static int getRealTextViewHeight(@NonNull TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            tvContent.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimation(tvContent, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
            }
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    /**
     * 状态改变事件监听器
     */
    public interface OnExpandStateChangeListener {

        /**
         * 状态改变事件回调函数
         *
         * @param textView   显示内容的TextView
         * @param isExpanded 是否已展开
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }
}