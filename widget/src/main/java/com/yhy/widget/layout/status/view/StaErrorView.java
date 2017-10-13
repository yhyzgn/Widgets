package com.yhy.widget.layout.status.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 16:28
 * version: 1.0.0
 * desc   : 默认[错误]界面
 */
public class StaErrorView extends LinearLayout {
    public StaErrorView(Context context) {
        this(context, null);
    }

    public StaErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StaErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_status_error, this);
    }
}
