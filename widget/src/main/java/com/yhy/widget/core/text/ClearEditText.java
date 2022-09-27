package com.yhy.widget.core.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.yhy.widget.R;
import com.yhy.widget.utils.WidgetCoreUtils;

import java.util.Objects;

/**
 * 可清空内容的 AppCompatEditText 控件
 * <p>
 * Created on 2022-09-27 10:19
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnFocusChangeListener {

    private Drawable mIcon;
    private boolean mHasFocus;

    public ClearEditText(@NonNull Context context) {
        this(context, null);
    }

    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        mIcon = ta.getDrawable(R.styleable.ClearEditText_cet_icon);
        mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());
        ta.recycle();

        // 默认隐藏图标
        setIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    private void setIconVisible(boolean visible) {
        Drawable icon = visible ? mIcon : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], icon, getCompoundDrawables()[3]);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
        // 当我们按下的位置在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // getWidth() 得到控件的宽度
            // event.getX() 抬起时的坐标(改坐标是相对于控件本身而言)
            // getTotalPaddingRight() clear 的图标左边缘至控件右边缘的距离
            // getPaddingRight() clear 的图标右边缘至控件右边缘的距离
            // 于是 getWidth() - getTotalPaddingRight() 表示控件左边到 clear 的图标左边缘的区域
            Drawable icon = getCompoundDrawables()[2];
            float x = event.getX();
            if (null != icon && x >= getWidth() - getTotalPaddingRight() && x <= getWidth() - getPaddingRight()) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 当输入框里面内容发生变化的时候回调的方法
        setIconVisible(mHasFocus && Objects.requireNonNull(getText()).length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocus = hasFocus;
        // 当 ClearEditText 焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
        setIconVisible(mHasFocus && Objects.requireNonNull(getText()).length() > 0);
    }
}
