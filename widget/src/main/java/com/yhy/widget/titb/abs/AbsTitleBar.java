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

    protected Toolbar tbBar;
    private FrameLayout flTitle;
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
            tbBar = (Toolbar) view.findViewById(R.id.tb_title_bar);
            flTitle = (FrameLayout) view.findViewById(R.id.fl_title);
        }
    }

    public void init(AppCompatActivity activity) {
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

    protected abstract View getTitleView();
}
