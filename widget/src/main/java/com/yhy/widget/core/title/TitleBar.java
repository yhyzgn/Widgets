package com.yhy.widget.core.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yhy.widget.R;
import com.yhy.widget.utils.WidgetCoreUtils;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-22 9:22
 * version: 1.0.0
 * desc   : 标题栏
 */
public class TitleBar extends FrameLayout {

    private View mView;
    private TextView tvTitle;
    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivLeft;
    private ImageView ivRight;
    private OnTitleBarListener mListener;
    private int mActionBarHeight;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.widget_titb_bar, this);
        tvTitle = $(mView, R.id.tv_title);
        tvLeft = $(mView, R.id.tv_left);
        tvRight = $(mView, R.id.tv_right);
        ivLeft = $(mView, R.id.iv_left);
        ivRight = $(mView, R.id.iv_right);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        String title = ta.getString(R.styleable.TitleBar_tb_title);
        String leftText = ta.getString(R.styleable.TitleBar_tb_left_text);
        String rightText = ta.getString(R.styleable.TitleBar_tb_right_text);
        int leftIcon = ta.getResourceId(R.styleable.TitleBar_tb_left_icon, 0);
        int rightIcon = ta.getResourceId(R.styleable.TitleBar_tb_right_icon, 0);
        int fontColor = ta.getColor(R.styleable.TitleBar_tb_font_color, Color.WHITE);
        int titleColor = ta.getColor(R.styleable.TitleBar_tb_title_color, fontColor);
        int leftColor = ta.getColor(R.styleable.TitleBar_tb_left_text_color, fontColor);
        int rightColor = ta.getColor(R.styleable.TitleBar_tb_right_text_color, fontColor);
        float titleSize = WidgetCoreUtils.px2sp(getContext(), ta.getDimension(R.styleable.TitleBar_tb_title_size, WidgetCoreUtils.sp2px(getContext(), 18)));
        float leftSize = WidgetCoreUtils.px2sp(getContext(), ta.getDimension(R.styleable.TitleBar_tb_left_text_size, WidgetCoreUtils.sp2px(getContext(), 14)));
        float rightSize = WidgetCoreUtils.px2sp(getContext(), ta.getDimension(R.styleable.TitleBar_tb_right_text_size, WidgetCoreUtils.sp2px(getContext(), 14)));

        ta.recycle();

        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            mActionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        if (mActionBarHeight == 0) {
            mActionBarHeight = WidgetCoreUtils.dp2px(getContext(), 56);
        }

        setTitle(title)
                .setLeftText(leftText)
                .setRightText(rightText)
                .setLeftIcon(leftIcon)
                .setRightIcon(rightIcon)
                .setFontColor(fontColor)
                .setTitleColor(titleColor)
                .setLeftTextColor(leftColor)
                .setRightTextColor(rightColor)
                .setTitleSize(titleSize)
                .setLeftTextSize(leftSize)
                .setRightTextSize(rightSize);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return 当前对象
     */
    public TitleBar setTitle(String title) {
        tvTitle.setText(title);
        tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    mListener.titleClick(view);
                }
            }
        });
        return this;
    }

    /**
     * 设置左边文本
     *
     * @param text 左边文本
     * @return 当前对象
     */
    public TitleBar setLeftText(String text) {
        if (TextUtils.isEmpty(text)) {
            tvLeft.setVisibility(GONE);
        } else {
            tvLeft.setVisibility(VISIBLE);
            tvLeft.setText(text);
            tvLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.leftTextClick(view);
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置右边文本
     *
     * @param text 右边文本
     * @return 当前对象
     */
    public TitleBar setRightText(String text) {
        if (TextUtils.isEmpty(text)) {
            tvRight.setVisibility(GONE);
        } else {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(text);
            tvRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.rightTextClick(view);
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置左边图标
     *
     * @param resId 左边图标
     * @return 当前对象
     */
    public TitleBar setLeftIcon(@DrawableRes int resId) {
        if (resId == 0) {
            ivLeft.setVisibility(GONE);
        } else {
            ivLeft.setVisibility(VISIBLE);
            ivLeft.setImageResource(resId);
            ivLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.leftIconClick(view);
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置右边图标
     *
     * @param resId 右边图标
     * @return 当前对象
     */
    public TitleBar setRightIcon(@DrawableRes int resId) {
        if (resId == 0) {
            ivRight.setVisibility(GONE);
        } else {
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(resId);
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.rightIconClick(view);
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param res 字体颜色id
     * @return 当前对象
     */
    public TitleBar setFontColorRes(@ColorRes int res) {
        return setFontColor(getContext().getResources().getColor(res));
    }

    /**
     * 设置字体颜色
     *
     * @param color 字体颜色
     * @return 当前对象
     */
    public TitleBar setFontColor(@ColorInt int color) {
        tvTitle.setTextColor(color);
        tvLeft.setTextColor(color);
        tvRight.setTextColor(color);
        return this;
    }

    /**
     * 设置标题文字颜色
     *
     * @param res 颜色id
     * @return 当前对象
     */
    public TitleBar setTitleColorRes(@ColorRes int res) {
        return setTitleColor(getContext().getResources().getColor(res));
    }

    /**
     * 设置标题文字颜色
     *
     * @param color 颜色
     * @return 当前对象
     */
    public TitleBar setTitleColor(@ColorInt int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    /**
     * 设置左侧文字颜色
     *
     * @param res 颜色id
     * @return 当前对象
     */
    public TitleBar setLeftTextColorRes(@ColorRes int res) {
        return setLeftTextColor(getContext().getResources().getColor(res));
    }

    /**
     * 设置左侧文字颜色
     *
     * @param color 颜色
     * @return 当前对象
     */
    public TitleBar setLeftTextColor(@ColorInt int color) {
        tvLeft.setTextColor(color);
        return this;
    }

    /**
     * 设置右侧文字颜色
     *
     * @param res 颜色id
     * @return 当前对象
     */
    public TitleBar setRightTextColorRes(@ColorRes int res) {
        return setRightTextColor(getContext().getResources().getColor(res));
    }

    /**
     * 设置右侧文字颜色
     *
     * @param color 颜色
     * @return 当前对象
     */
    public TitleBar setRightTextColor(@ColorInt int color) {
        tvRight.setTextColor(color);
        return this;
    }

    /**
     * 设置标题字体大小
     *
     * @param sp 字体大小
     * @return 当前对象
     */
    public TitleBar setTitleSize(float sp) {
        tvTitle.setTextSize(sp);
        return this;
    }

    /**
     * 设置左侧字体大小
     *
     * @param sp 字体大小
     * @return 当前对象
     */
    public TitleBar setLeftTextSize(float sp) {
        tvLeft.setTextSize(sp);
        return this;
    }

    /**
     * 设置右侧字体大小
     *
     * @param sp 字体大小
     * @return 当前对象
     */
    public TitleBar setRightTextSize(float sp) {
        tvRight.setTextSize(sp);
        return this;
    }

    /**
     * 根据id获取控件
     *
     * @param parent 父控件
     * @param resId  id
     * @param <T>    控件类型
     * @return 控件
     */
    public <T extends View> T $(View parent, int resId) {
        return parent.findViewById(resId);
    }

    /**
     * 设置各控件点击事件
     *
     * @param listener 点击事件
     */
    public void setOnTitleBarListener(OnTitleBarListener listener) {
        mListener = listener;
    }

    /**
     * 事件监听器
     */
    public static class OnTitleBarListener {
        /**
         * 标题栏被点击
         *
         * @param view 当前控件
         */
        public void titleClick(View view) {
        }

        /**
         * 左边文本被点击
         *
         * @param view 当前控件
         */
        public void leftTextClick(View view) {
        }

        /**
         * 右边文本被点击
         *
         * @param view 当前控件
         */
        public void rightTextClick(View view) {
        }

        /**
         * 左边图标被点击
         *
         * @param view 当前控件
         */
        public void leftIconClick(View view) {
        }

        /**
         * 右边图标被点击
         *
         * @param view 当前控件
         */
        public void rightIconClick(View view) {
        }
    }
}
