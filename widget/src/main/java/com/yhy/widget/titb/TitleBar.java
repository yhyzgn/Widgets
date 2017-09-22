package com.yhy.widget.titb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yhy.widget.R;

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
    private TextView tvRIght;
    private ImageView ivLeft;
    private ImageView ivRight;
    private String mTitle;
    private String mLeftText;
    private String mRightText;
    @DrawableRes
    private int mLeftIcon;
    @DrawableRes
    private int mRightIcon;
    @ColorInt
    private int mFontColor;
    private OnTitleBarListener mListener;

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
        tvRIght = $(mView, R.id.tv_right);
        ivLeft = $(mView, R.id.iv_left);
        ivRight = $(mView, R.id.iv_right);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mTitle = ta.getString(R.styleable.TitleBar_tb_title);
        mLeftText = ta.getString(R.styleable.TitleBar_tb_left_text);
        mRightText = ta.getString(R.styleable.TitleBar_tb_right_text);
        mLeftIcon = ta.getResourceId(R.styleable.TitleBar_tb_left_icon, 0);
        mRightIcon = ta.getResourceId(R.styleable.TitleBar_tb_right_icon, 0);
        mFontColor = ta.getColor(R.styleable.TitleBar_tb_font_color, Color.WHITE);
        ta.recycle();

        setTitle(mTitle).setLeftText(mLeftText).setRightText(mRightText).setLeftIcon(mLeftIcon).setRightIcon(mRightIcon).setFontColor(mFontColor);
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
            tvRIght.setVisibility(GONE);
        } else {
            tvRIght.setVisibility(VISIBLE);
            tvRIght.setText(text);
            tvRIght.setOnClickListener(new OnClickListener() {
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
     * 设置左右字体颜色
     *
     * @param color 字体颜色
     * @return 当前对象
     */
    public TitleBar setFontColor(@ColorInt int color) {
        tvTitle.setTextColor(color);
        tvLeft.setTextColor(color);
        tvRIght.setTextColor(color);
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
