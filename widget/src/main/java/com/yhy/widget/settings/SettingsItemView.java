package com.yhy.widget.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhy.widget.R;
import com.yhy.widget.sb.SwitchButton;
import com.yhy.widget.utils.DensityUtils;

/**
 * Created by HongYi Yan on 2017/5/31 13:47.
 */
public class SettingsItemView extends LinearLayout {

    private ImageView ivIcon;
    private ImageView ivArrow;
    private TextView tvName;
    private TextView tvText;
    private SwitchButton sbSwitch;
    private Drawable mIcon;
    private Drawable mArrow;
    private boolean mShowIcon;
    private boolean mShowArrow;
    private String mName;
    private String mText;
    private float mNameSize;
    private float mTextSize;
    private int mNameColor;
    private int mTextColor;
    private boolean mCheckedSwitch;
    private boolean mShowSwitch;
    private OnSwitchStateChangeListener mSwitchListener;

    public SettingsItemView(Context context) {
        this(context, null);
    }

    public SettingsItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_settings_item_view, this);
        ivIcon = (ImageView) view.findViewById(R.id.iv_settings_icon);
        ivArrow = (ImageView) view.findViewById(R.id.iv_settings_arrow);
        tvName = (TextView) view.findViewById(R.id.tv_settings_name);
        tvText = (TextView) view.findViewById(R.id.tv_settings_text);
        sbSwitch = (SwitchButton) view.findViewById(R.id.sb_switch);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingsItemViewAttrs);
        mIcon = ta.getDrawable(R.styleable.SettingsItemViewAttrs_siv_icon);
        mArrow = ta.getDrawable(R.styleable.SettingsItemViewAttrs_siv_arrow);
        mShowIcon = ta.getBoolean(R.styleable.SettingsItemViewAttrs_siv_show_icon, false);
        mShowArrow = ta.getBoolean(R.styleable.SettingsItemViewAttrs_siv_show_arrow, false);
        mName = ta.getString(R.styleable.SettingsItemViewAttrs_siv_name);
        mText = ta.getString(R.styleable.SettingsItemViewAttrs_siv_text);
        //获取到字体大小，单位是px
        mNameSize = ta.getDimension(R.styleable.SettingsItemViewAttrs_siv_name_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        mTextSize = ta.getDimension(R.styleable.SettingsItemViewAttrs_siv_text_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        mNameColor = ta.getColor(R.styleable.SettingsItemViewAttrs_siv_name_color, Color.BLACK);
        mTextColor = ta.getColor(R.styleable.SettingsItemViewAttrs_siv_text_color, Color.BLACK);
        mCheckedSwitch = ta.getBoolean(R.styleable.SettingsItemViewAttrs_siv_checked_switch, false);
        mShowSwitch = ta.getBoolean(R.styleable.SettingsItemViewAttrs_siv_show_switch, false);
        ta.recycle();

        //将字体大小单位转换为sp
        mNameSize = DensityUtils.px2sp(getContext(), mNameSize);
        mTextSize = DensityUtils.px2sp(getContext(), mTextSize);

        setIcon(mIcon)
                .showIcon(mShowIcon)
                .setArrow(mArrow)
                .showArrow(mShowArrow)
                .setName(mName)
                .setNameColor(mNameColor)
                .setNameSize(mNameSize)
                .setText(mText)
                .setTextColor(mTextColor)
                .setTextSize(mTextSize)
                .checkSwitch(mCheckedSwitch)
                .showSwitch(mShowSwitch);

        sbSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (null != mSwitchListener) {
                    mSwitchListener.onStateChanged(SettingsItemView.this, view, isChecked);
                }
            }
        });
    }

    public SettingsItemView setIconRes(@DrawableRes int resId) {
        return setIcon(getContext().getResources().getDrawable(resId));
    }

    public SettingsItemView setIcon(Drawable drawable) {
        mIcon = drawable;
        ivIcon.setImageDrawable(mIcon);
        return this;
    }

    public SettingsItemView setArrowRes(@DrawableRes int resId) {
        return setArrow(getContext().getResources().getDrawable(resId));
    }

    public SettingsItemView setArrow(Drawable drawable) {
        mArrow = drawable;
        ivArrow.setImageDrawable(mArrow);
        return this;
    }

    public SettingsItemView showIcon(boolean show) {
        mShowIcon = show;
        ivIcon.setVisibility(mShowIcon ? VISIBLE : GONE);
        return this;
    }

    public SettingsItemView showArrow(boolean show) {
        mShowArrow = show;
        ivArrow.setVisibility(mShowArrow ? VISIBLE : GONE);
        return this;
    }

    public SettingsItemView setNameRes(@StringRes int resId) {
        return setName(getContext().getString(resId));
    }

    public SettingsItemView setName(String name) {
        mName = name;
        tvName.setText(mName);
        return this;
    }

    public String getName() {
        return tvName.getText().toString();
    }

    public SettingsItemView setTextRes(@StringRes int resId) {
        return setText(getContext().getString(resId));
    }

    public SettingsItemView setText(String text) {
        mText = text;
        tvText.setText(mText);
        return this;
    }

    public String getText() {
        return tvText.getText().toString();
    }

    public SettingsItemView setNameSize(float sp) {
        mNameSize = sp;
        tvName.setTextSize(mNameSize);
        return this;
    }

    public SettingsItemView setTextSize(float sp) {
        mTextSize = sp;
        tvText.setTextSize(mTextSize);
        return this;
    }

    public SettingsItemView setNameColorRes(@ColorRes int resId) {
        return setNameColor(getContext().getResources().getColor(resId));
    }

    public SettingsItemView setNameColor(@ColorInt int color) {
        mNameColor = color;
        tvName.setTextColor(mNameColor);
        return this;
    }

    public SettingsItemView setTextColorRes(@ColorRes int resId) {
        return setTextColor(getContext().getResources().getColor(resId));
    }

    public SettingsItemView setTextColor(@ColorInt int color) {
        mTextColor = color;
        tvText.setTextColor(mTextColor);
        return this;
    }

    public SettingsItemView checkSwitch(boolean checked) {
        mCheckedSwitch = checked;
        sbSwitch.setChecked(mCheckedSwitch);
        return this;
    }

    public SettingsItemView showSwitch(boolean show) {
        mShowSwitch = show;
        sbSwitch.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    public SettingsItemView setOnSwitchStateChangeListener(OnSwitchStateChangeListener listener) {
        mSwitchListener = listener;
        return this;
    }

    public interface OnSwitchStateChangeListener {
        void onStateChanged(SettingsItemView siv, SwitchButton sb, boolean isChecked);
    }
}