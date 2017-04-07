package com.yhy.widget.titb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yhy.widget.R;
import com.yhy.widget.titb.abs.AbsNormalTitleBar;

/**
 * Created by HongYi Yan on 2017/3/14 13:16.
 */
public class TextTitleBar extends AbsNormalTitleBar {
    private TextView mTv;

    private int mTextColor;
    private int mTextSize;
    private String mTitle;

    public TextTitleBar(Context context) {
        this(context, null);
    }

    public TextTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        textInit(context, attrs);
    }

    private void textInit(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextTitleBarAttrs);

        mTextColor = ta.getColor(R.styleable.TextTitleBarAttrs_ttb_text_color, Color.WHITE);
        mTextSize = ta.getDimensionPixelSize(R.styleable.TextTitleBarAttrs_ttb_text_size, 22);
        mTitle = ta.getString(R.styleable.TextTitleBarAttrs_ttb_text);

        ta.recycle();
    }

    @Override
    protected View getCenterView() {
        mTv = new TextView(getContext());
        mTv.setGravity(Gravity.CENTER);

        setTitle(mTitle);
        setTextColor(mTextColor);
        setTextSize(mTextSize);

        return mTv;
    }

    /**
     * 设置标题文字
     *
     * @param title 标题文字
     */
    public void setTitle(CharSequence title) {
        mTv.setText(title);
    }

    /**
     * 设置标题字体颜色
     *
     * @param color 字体颜色
     */
    public void setTextColor(int color) {
        mTv.setTextColor(color);
    }

    /**
     * 设置标题字体大小
     *
     * @param sp 字体大小
     */
    public void setTextSize(int sp) {
        mTv.setTextSize(sp);
    }
}
