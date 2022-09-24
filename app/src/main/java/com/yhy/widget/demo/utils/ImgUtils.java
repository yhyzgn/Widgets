package com.yhy.widget.demo.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by HongYi Yan on 2017/4/6 16:48.
 */
public class ImgUtils {

    private static ImgLoader mLoader;

    private ImgUtils() {
        throw new RuntimeException("Can not instantiate ImgUtils.");
    }

    public static void init(ImgLoader loader) {
        mLoader = loader;
    }

    public static <T> void load(Context ctx, ImageView iv, T model) {
        if (null != mLoader) {
            mLoader.load(ctx, iv, model);
        }
    }

    /**
     * 图片加载器
     */
    public interface ImgLoader {
        <T> void load(Context ctx, ImageView iv, T model);
    }
}
