package com.yhy.widget.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:18
 * version: 1.0.0
 * desc   :
 */
public class DensityUtils {
    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("Can not be instantiated");
    }

    /**
     * dp转px
     *
     * @param context 上下文对象
     * @param dpVal   dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context
                .getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context 上下文对象
     * @param spVal   sp值
     * @return px值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context
                .getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context 上下文对象
     * @param pxVal   px值
     * @return dp值
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context 上下文对象
     * @param pxVal   px值
     * @return sp值
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}