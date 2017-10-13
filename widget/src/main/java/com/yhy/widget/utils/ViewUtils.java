package com.yhy.widget.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 21:06
 * version: 1.0.0
 * desc   :
 */
public class ViewUtils {

    private ViewUtils() {
        throw new IllegalStateException("Can not instantiate utils class.");
    }

    public static View removeParent(View view) {
        if (null != view && null != view.getParent() && view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }
}
