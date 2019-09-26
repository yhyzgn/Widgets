package com.yhy.widget.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:18
 * version: 1.0.0
 * desc   : 工具类
 */
public class WidgetCoreUtils {
    private WidgetCoreUtils() {
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

    /**
     * 移除view的父控件
     *
     * @param view view
     * @return 当前view
     */
    public static View removeParent(View view) {
        if (null != view && null != view.getParent() && view.getParent() instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view.getParent();
            vg.removeView(view);
        }
        return view;
    }

    /**
     * 将Drawable转换为Bitmap
     *
     * @param drawable 要转换的Drawable
     * @return 转换后的Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap;
        if (drawable instanceof ColorDrawable) {
            bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }

        Canvas canvas = new Canvas(bitmap);
        // 设置显示区域
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 显示隐藏view
     *
     * @param view    要操作的view
     * @param visible 是否显示
     */
    public static void visible(View view, boolean visible) {
        if (null == view) {
            return;
        }
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 显示隐藏view
     *
     * @param view 要操作的view
     * @param gone 是否隐藏
     */
    public static void gone(View view, boolean gone) {
        if (null == view) {
            return;
        }
        view.setVisibility(gone ? View.GONE : View.VISIBLE);
    }

    /**
     * 获取文件后缀名
     *
     * @param filename 文件名
     * @return 文件后缀
     */
    public static String getExt(String filename) {
        if (!TextUtils.isEmpty(filename)) {
            if (filename.contains(".")) {
                return filename.substring(filename.lastIndexOf(".") + 1);
            }
            return "";
        }
        return null;
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件
     * @return 后缀
     */
    public static String getExt(File file) {
        return null == file ? null : getExt(file.getName());
    }

    /**
     * 获取文件MimeType
     *
     * @param filename 文件名
     * @return MimeType
     */
    public static String getMimeType(String filename) {
        String ext = getExt(filename);
        return TextUtils.isEmpty(ext) ? null : MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
    }

    /**
     * 获取文件MimeType
     *
     * @param file 文件
     * @return MimeType
     */
    public static String getMimeType(File file) {
        return null == file ? null : getMimeType(file.getName());
    }
}