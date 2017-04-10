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

import de.hdodenhof.circleimageview.CircleImageView;

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

    private CircleImageView civLeft;
    private CircleImageView civRight;

    private int mBgColor;
    private int mStatusBarColor;
    private int mLeftRightPadding;
    private int mPaddingTop;

    private OnNormalTitleListener mListener;
    private CircleImgAdapter mAdapter;

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

        civLeft = (CircleImageView) view.findViewById(R.id.civ_left);
        civRight = (CircleImageView) view.findViewById(R.id.civ_right);

        flCenterView = (FrameLayout) view.findViewById(R.id.fl_center_view);

        View centerView = getCenterView();
        if (null != centerView) {
            flCenterView.removeAllViews();
            flCenterView.addView(centerView);
        }

        configTitle();

        return view;
    }

    /**
     * 从子类中获取到标题中间的View
     *
     * @return 中间的View
     */
    protected abstract View getCenterView();

    private void configTitle() {
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

    /**
     * 设置左边按钮文字
     *
     * @param text 左边按钮文字
     * @return 当前对象
     */
    public AbsNormalTitleBar setLeftBtn(CharSequence text) {
        setTextBtn(text, true);
        return this;
    }

    /**
     * 设置右边按钮文字
     *
     * @param text 右边按钮文字
     * @return 当前对象
     */
    public AbsNormalTitleBar setRightBtn(CharSequence text) {
        setTextBtn(text, false);
        return this;
    }

    /**
     * 设置左边按钮图标资源id
     *
     * @param iconId 左边按钮资源id
     * @return 当前对象
     */
    public AbsNormalTitleBar setLeftBtn(int iconId) {
        setIconBtn(iconId, true);
        return this;
    }

    /**
     * 设置右边按钮图标资源id
     *
     * @param iconId 右边按钮资源id
     * @return 当前对象
     */
    public AbsNormalTitleBar setRightBtn(int iconId) {
        setIconBtn(iconId, false);
        return this;
    }

    /**
     * 设置标题栏上内边距
     */
    private void setMarginTop() {
        //先修改高度
        LayoutParams params = (LayoutParams) tbBar.getLayoutParams();
        params.height = params.height + DensityUtils.dp2px(mActivity, mPaddingTop);
        tbBar.setLayoutParams(params);
        //在设置Toolbar的padding值
        tbBar.setPadding(0, DensityUtils.dp2px(mActivity, mPaddingTop), 0, 0);
    }

    /**
     * 设置标题栏左右内边距
     */
    private void setLeftRightPadding() {
        int leftRightPadding = DensityUtils.dp2px(mActivity, mLeftRightPadding);
        tbBar.setPadding(leftRightPadding, 0, leftRightPadding, 0);
    }

    /**
     * 设置背景颜色
     */
    private void setTitleBarBgColor() {
        tbBar.setBackgroundColor(mBgColor);

        //设置状态栏背景颜色
        setStatusBarColor(mStatusBarColor);
    }

    /**
     * 设置状态栏背景颜色
     *
     * @param color 背景颜色
     * @return 当前对象
     */
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
                civLeft.setVisibility(GONE);
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
                civRight.setVisibility(GONE);
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
                civLeft.setVisibility(GONE);
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
                civRight.setVisibility(GONE);
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

    private void setCircleImg() {
        if (null != mAdapter) {
            if (mAdapter.leftCiv(civLeft)) {
                civLeft.setVisibility(VISIBLE);
                tvLeftText.setVisibility(View.GONE);
                ivLeftIcon.setVisibility(View.GONE);
            }
            if (mAdapter.rightCiv(civRight)) {
                civRight.setVisibility(VISIBLE);
                tvRightText.setVisibility(View.GONE);
                ivRightIcon.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置左右按钮的点击事件
     *
     * @param listener 点击事件监听器
     * @return 当前对象
     */
    public AbsNormalTitleBar setOnNormalTitleListener(OnNormalTitleListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 设置左右圆形图标的适配器
     *
     * @param adapter 适配器
     * @return 当前对象
     */
    public AbsNormalTitleBar setCircleImgAdapter(CircleImgAdapter adapter) {
        mAdapter = adapter;
        setCircleImg();
        return this;
    }

    /**
     * 圆形图标适配器
     */
    public static class CircleImgAdapter {
        /**
         * 左边图标适配
         *
         * @param leftCiv 左边图标
         * @return 是否显示
         */
        public boolean leftCiv(CircleImageView leftCiv) {
            return false;
        }

        /**
         * 右边图标适配
         *
         * @param rightCiv 右边图标
         * @return 是否显示
         */
        public boolean rightCiv(CircleImageView rightCiv) {
            return false;
        }
    }

    /**
     * 左右按钮点击事件监听器
     */
    public static abstract class OnNormalTitleListener {
        /**
         * 左边按钮点击事件
         *
         * @param v 当前按钮
         */
        public abstract void onLeftClick(View v);

        /**
         * 右边按钮点击事件
         *
         * @param v 当前按钮
         */
        public void onRightClick(View v) {
        }
    }
}
