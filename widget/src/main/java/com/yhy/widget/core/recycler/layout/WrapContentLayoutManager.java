package com.yhy.widget.core.recycler.layout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-03-30 13:09
 * version: 1.0.0
 * desc   :  RecyclerView Bug：IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter 解决方案
 */
public class WrapContentLayoutManager extends LinearLayoutManager {
    public WrapContentLayoutManager(Context context) {
        super(context);
    }

    public WrapContentLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapContentLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("ContentLayoutManager", "meet a IOOBE in RecyclerView");
        }
    }
}
