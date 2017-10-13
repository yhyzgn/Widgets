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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhy.widget.R;
import com.yhy.widget.toggle.SwitchButton;
import com.yhy.widget.utils.DensityUtils;

import java.lang.reflect.Field;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:18
 * version: 1.0.0
 * desc   :
 */
public class SettingsItemView extends LinearLayout {
    //默认字体大小，14sp
    private static final int DEF_FONT_SIZE = 14;
    //默认开关控件宽度，48dp
    private static final int DEF_SWITCH_WIDTH = 48;
    //默认开关控件高度，28dp
    private static final int DEF_SWITCH_HEIGHT = 28;

    private ImageView ivIcon;
    private ImageView ivArrow;
    private TextView tvName;
    private TextView tvText;
    private EditText etText;
    private SwitchButton sbSwitch;
    private Drawable mIcon;
    private Drawable mArrow;
    private boolean mShowIcon;
    private boolean mShowArrow;
    private String mName;
    private int mNameWidth;
    private int mNameGravity;
    private String mText;
    private int mTextGravity;
    private String mHint;
    private float mNameSize;
    private float mTextSize;
    private int mSwitchWidth;
    private int mSwitchHeight;
    private int mNameColor;
    private int mTextColor;
    private boolean mSwitchOn;
    private boolean mShowSwitch;
    private boolean mEditable;
    private int mCursorDrawableRes;
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
        etText = (EditText) view.findViewById(R.id.et_settings_text);
        sbSwitch = (SwitchButton) view.findViewById(R.id.sb_switch);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingsItemView);
        mIcon = ta.getDrawable(R.styleable.SettingsItemView_siv_icon);
        mArrow = ta.getDrawable(R.styleable.SettingsItemView_siv_arrow);
        mShowIcon = ta.getBoolean(R.styleable.SettingsItemView_siv_show_icon, false);
        mShowArrow = ta.getBoolean(R.styleable.SettingsItemView_siv_show_arrow, false);
        mName = ta.getString(R.styleable.SettingsItemView_siv_name);
        mText = ta.getString(R.styleable.SettingsItemView_siv_text);
        mHint = ta.getString(R.styleable.SettingsItemView_siv_hint);
        //获取到字体大小，单位是px
        mNameSize = ta.getDimension(R.styleable.SettingsItemView_siv_name_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_FONT_SIZE, getResources().getDisplayMetrics()));
        mTextSize = ta.getDimension(R.styleable.SettingsItemView_siv_text_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_FONT_SIZE, getResources().getDisplayMetrics()));
        mNameColor = ta.getColor(R.styleable.SettingsItemView_siv_name_color, Color.BLACK);
        mTextColor = ta.getColor(R.styleable.SettingsItemView_siv_text_color, Color.BLACK);
        mNameWidth = ta.getDimensionPixelSize(R.styleable.SettingsItemView_siv_name_width, 0);
        mSwitchOn = ta.getBoolean(R.styleable.SettingsItemView_siv_switch_on, false);
        //获取开关控件宽高
        mSwitchWidth = ta.getDimensionPixelSize(R.styleable.SettingsItemView_siv_switch_width, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_SWITCH_WIDTH, getResources().getDisplayMetrics()));
        mSwitchHeight = ta.getDimensionPixelSize(R.styleable.SettingsItemView_siv_switch_height, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_SWITCH_HEIGHT, getResources().getDisplayMetrics()));
        mShowSwitch = ta.getBoolean(R.styleable.SettingsItemView_siv_show_switch, false);

        mEditable = ta.getBoolean(R.styleable.SettingsItemView_siv_editable, false);

        mCursorDrawableRes = ta.getResourceId(R.styleable.SettingsItemView_siv_cursor_drawable, -1);

        int nameGravity = ta.getInt(R.styleable.SettingsItemView_siv_name_gravity, 0);
        mNameGravity = nameGravity == 0 ? Gravity.LEFT : nameGravity == 1 ? Gravity.CENTER : Gravity.RIGHT;
        mNameGravity |= Gravity.CENTER_VERTICAL;

        int textGravity = ta.getInt(R.styleable.SettingsItemView_siv_text_gravity, 2);
        mTextGravity = textGravity == 0 ? Gravity.LEFT : textGravity == 1 ? Gravity.CENTER : Gravity.RIGHT;
        mTextGravity |= Gravity.CENTER_VERTICAL;

        ta.recycle();

        //将字体大小单位转换为sp
        mNameSize = DensityUtils.px2sp(getContext(), mNameSize);
        mTextSize = DensityUtils.px2sp(getContext(), mTextSize);

        //开关控件宽高
        sbSwitch.getLayoutParams().width = mSwitchWidth;
        sbSwitch.getLayoutParams().height = mSwitchHeight;

        setIcon(mIcon)
                .showIcon(mShowIcon)
                .setArrow(mArrow)
                .showArrow(mShowArrow)
                .setName(mName)
                .setNameWidth(mNameWidth)
                .setNameColor(mNameColor)
                .setNameSize(mNameSize)
                .setText(mText)
                .setHint(mHint)
                .setEditable(mEditable)
                .setTextColor(mTextColor)
                .setTextSize(mTextSize)
                .onSwitch(mSwitchOn)
                .showSwitch(mShowSwitch)
                .setNameGravity(mNameGravity)
                .setTextGravity(mTextGravity)
                .setCursorDrawableRes(mCursorDrawableRes);

        sbSwitch.setOnStateChangeListener(new SwitchButton.OnStateChangeListener() {
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
        etText.setText(mText);
        return this;
    }

    public String getText() {
        return tvText.getVisibility() == VISIBLE ? tvText.getText().toString() : etText.getText().toString();
    }

    public SettingsItemView setNameSize(float sp) {
        mNameSize = sp;
        tvName.setTextSize(mNameSize);
        return this;
    }

    public SettingsItemView setTextSize(float sp) {
        mTextSize = sp;
        tvText.setTextSize(mTextSize);
        etText.setTextSize(mTextSize);
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
        etText.setTextColor(mTextColor);
        return this;
    }

    public SettingsItemView setNameWidth(int width) {
        if (width > 0) {
            //指定了确定宽度，否则自适应
            tvName.setWidth(width);
        }
        return this;
    }

    public SettingsItemView setNameGravity(int gravity) {
        tvName.setGravity(gravity);
        return this;
    }

    public SettingsItemView setTextGravity(int gravity) {
        tvText.setGravity(gravity);
        etText.setGravity(gravity);
        return this;
    }

    public SettingsItemView setHint(String hint) {
        tvText.setHint(hint);
        etText.setHint(hint);
        return this;
    }

    public SettingsItemView setCursorDrawableRes(@DrawableRes int resId) {
        if (resId <= 0) {
            return this;
        }
        //利用反射设置光标样式
        //EditText继承于TextView，mCursorDrawableRes是TextView中的私有成员，获取私有成员需要getDeclaredField()而不是getField()
        try {
            Field cursorDrawableResField = TextView.class.getDeclaredField("mCursorDrawableRes");
            cursorDrawableResField.setAccessible(true);
            cursorDrawableResField.set(etText, resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public SettingsItemView setEditable(boolean editable) {
        if (editable) {
            tvText.setVisibility(GONE);
            etText.setVisibility(VISIBLE);
        } else {
            tvText.setVisibility(VISIBLE);
            etText.setVisibility(GONE);
        }
        return this;
    }

    public SettingsItemView onSwitch(boolean checked) {
        mSwitchOn = checked;
        sbSwitch.onOrOff(mSwitchOn);
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
        void onStateChanged(SettingsItemView siv, SwitchButton sb, boolean isOn);
    }
}