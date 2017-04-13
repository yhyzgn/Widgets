package com.yhy.widget.titb.abs;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.yhy.widget.R;

/**
 * Created by HongYi Yan on 2017/4/6 22:58.
 */
public abstract class AbsTitleBar extends AppBarLayout {
    protected AppBarLayout ablBar;
    protected Toolbar tbBar;
    protected FrameLayout flTitle;
    protected AppCompatActivity mActivity;

    public AbsTitleBar(Context context) {
        this(context, null);
    }

    public AbsTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        absInit(context, attrs);
    }

    private void absInit(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.widget_titb_abs, this);
            ablBar = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
            tbBar = (Toolbar) view.findViewById(R.id.tb_title_bar);
            flTitle = (FrameLayout) view.findViewById(R.id.fl_title);
        }
    }

    /**
     * 让当前Activity支持ActionBar
     *
     * @param activity 当前Activity
     */
    public void support(AppCompatActivity activity) {
        mActivity = activity;
        mActivity.setSupportActionBar(tbBar);
        // 隐藏标题
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        View titleView = getTitleView();
        if (null != titleView) {
            flTitle.removeAllViews();
            flTitle.addView(titleView);
        }
    }

    /**
     * 子类回调设置标题View
     *
     * @return 标题View
     */
    protected abstract View getTitleView();
}
