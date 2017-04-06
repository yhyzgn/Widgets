package com.yhy.widget.titb.abs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yhy.widget.R;
import com.yhy.widget.utils.DensityUtils;

/**
 * Created by HongYi Yan on 2017/4/6 23:06.
 */
public abstract class AbsNormalTitleBar extends AbsTitleBar {
    //判断有没有资源id的参照
    private static final int CODE_NO_RES_ID = -1;

    private ImageView ivLeftIcon;
    private ImageView ivRightIcon;
    private TextView tvLeftText;
    private TextView tvRightText;
    private FrameLayout flCenterView;

    private String mLeftText;
    private String mRightText;
    private int mLeftIcon;
    private int mRightIcon;

    private int mBgColor;
    private int mStatusBarColor;
    private int mLeftRightPadding;
    private int mPaddingTop;

    private OnNormalTitleListener mListener;

    public AbsNormalTitleBar(Context context) {
        this(context, null);
    }

    public AbsNormalTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        normalInit(context, attrs);
    }

    private void normalInit(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable
                .AbsNormalTitleBarAttrs);
        mLeftText = ta.getString(R.styleable.AbsNormalTitleBarAttrs_titb_left_text);
        mRightText = ta.getString(R.styleable.AbsNormalTitleBarAttrs_titb_right_text);
        mLeftIcon = ta.getResourceId(R.styleable.AbsNormalTitleBarAttrs_titb_left_icon,
                CODE_NO_RES_ID);
        mRightIcon = ta.getResourceId(R.styleable.AbsNormalTitleBarAttrs_titb_right_icon,
                CODE_NO_RES_ID);

        mBgColor = ta.getColor(R.styleable.AbsNormalTitleBarAttrs_titb_bg_color, Color.BLUE);
        mStatusBarColor = ta.getColor(R.styleable.AbsNormalTitleBarAttrs_titb_status_bar_color,
                mBgColor);
        mLeftRightPadding = ta.getDimensionPixelSize(R.styleable
                .AbsNormalTitleBarAttrs_titb_left_right_padding, 0);
        mPaddingTop = ta.getDimensionPixelSize(R.styleable.AbsNormalTitleBarAttrs_titb_padding_top,
                0);
        ta.recycle();
    }

    @Override
    protected View getTitleView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_titb_abs_normal,
                null);
        ivLeftIcon = (ImageView) view.findViewById(R.id.iv_left_icon);
        ivRightIcon = (ImageView) view.findViewById(R.id.iv_right_icon);
        tvLeftText = (TextView) view.findViewById(R.id.tv_left_text);
        tvRightText = (TextView) view.findViewById(R.id.tv_right_text);

        flCenterView = (FrameLayout) view.findViewById(R.id.fl_center_view);

        View centerView = getCenterView();
        if (null != centerView) {
            flCenterView.removeAllViews();
            flCenterView.addView(centerView);
        }

        configTitle();

        return view;
    }

    protected abstract View getCenterView();

    protected void configTitle() {
        if (!TextUtils.isEmpty(mLeftText)) {
            setLeftBtn(mLeftText);
        }

        if (!TextUtils.isEmpty(mRightText)) {
            setRightBtn(mRightText);
        }

        if (mLeftIcon > CODE_NO_RES_ID) {
            setLeftBtn(mLeftIcon);
        }

        if (mRightIcon > CODE_NO_RES_ID) {
            setRightBtn(mRightIcon);
        }

        //设置背景
        setTitleBarBgColor();

        //设置左右按钮边距
        setLeftRightPadding();

        setMarginTop();
    }

    public AbsNormalTitleBar setLeftBtn(CharSequence text) {
        setTextBtn(text, true);
        return this;
    }

    public AbsNormalTitleBar setRightBtn(CharSequence text) {
        setTextBtn(text, false);
        return this;
    }

    public AbsNormalTitleBar setLeftBtn(int iconId) {
        setIconBtn(iconId, true);
        return this;
    }

    public AbsNormalTitleBar setRightBtn(int iconId) {
        setIconBtn(iconId, false);
        return this;
    }

    private void setMarginTop() {
        //先修改高度
        LayoutParams params = (LayoutParams) tbBar.getLayoutParams();
        params.height = params.height + DensityUtils.dp2px(mActivity, mPaddingTop);
        tbBar.setLayoutParams(params);
        //在设置Toolbar的padding值
        tbBar.setPadding(0, DensityUtils.dp2px(mActivity, mPaddingTop), 0, 0);
    }

    private void setLeftRightPadding() {
        int leftRightPadding = DensityUtils.dp2px(mActivity, mLeftRightPadding);
        flCenterView.setPadding(leftRightPadding, 0, leftRightPadding, 0);
    }

    /**
     * 设置背景颜色
     */
    private void setTitleBarBgColor() {
        tbBar.setBackgroundColor(mBgColor);

        //设置状态栏背景颜色
        setStatusBarColor(mStatusBarColor);
    }

    public AbsNormalTitleBar setStatusBarColor(int color) {
        //设置状态栏颜色
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = mActivity.getWindow();
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }
        return this;
    }

    private void setTextBtn(CharSequence text, boolean isLeft) {
        if (!TextUtils.isEmpty(text)) {
            if (isLeft) {
                ivLeftIcon.setVisibility(View.GONE);
                tvLeftText.setVisibility(View.VISIBLE);
                tvLeftText.setText(text);
                tvLeftText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            mListener.onLeftClick(v);
                        }
                    }
                });
            } else {
                ivRightIcon.setVisibility(View.GONE);
                tvRightText.setVisibility(View.VISIBLE);
                tvRightText.setText(text);
                tvRightText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            mListener.onRightClick(v);
                        }
                    }
                });
            }
        }
    }

    private void setIconBtn(int iconId, boolean isLeft) {
        if (iconId > CODE_NO_RES_ID) {
            if (isLeft) {
                tvLeftText.setVisibility(View.GONE);
                ivLeftIcon.setVisibility(View.VISIBLE);
                ivLeftIcon.setImageResource(iconId);
                ivLeftIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            mListener.onLeftClick(v);
                        }
                    }
                });
            } else {
                tvRightText.setVisibility(View.GONE);
                ivRightIcon.setVisibility(View.VISIBLE);
                ivRightIcon.setImageResource(iconId);
                ivRightIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            mListener.onRightClick(v);
                        }
                    }
                });
            }
        }
    }

    public void setOnNormalTitleListener(OnNormalTitleListener listener) {
        mListener = listener;
    }

    public abstract class OnNormalTitleListener {

        public abstract void onLeftClick(View v);

        public void onRightClick(View v) {
        }
    }
}
