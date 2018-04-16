package com.yhy.widget.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 21:06
 * version: 1.0.0
 * desc   : View工具类
 */
public class ViewUtils {

    private ViewUtils() {
        throw new UnsupportedOperationException("Can not instantiate utils class.");
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
}
