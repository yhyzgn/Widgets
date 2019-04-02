package com.yhy.widget.layout.status.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 16:28
 * version: 1.0.0
 * desc   : 默认[无数据]界面
 */
public class StaEmptyView extends LinearLayout {
    public StaEmptyView(Context context) {
        this(context, null);
    }

    public StaEmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StaEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_status_empty, this);
    }
}
