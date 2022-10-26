package com.yhy.widget.demo;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yhy.widget.core.preview.ImgPreHelper;
import com.yhy.widget.demo.utils.ImgUtils;
import com.yhy.widget.demo.utils.ToastUtils;

import java.io.File;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-03-15 13:38
 * version: 1.0.0
 * desc   :
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        ToastUtils.init(this);

        //图片加载器工具初始化
        //                loadImgByGlide(ctx, iv, model);
        ImgUtils.init(this::loadImgByPicasso);

        ImgPreHelper.getInstance().init(this, new ImgPreHelper.ImgLoader() {
            @Override
            public <T> void load(ImageView iv, T model, ProgressBar pbLoading) {
                loadImgByPicasso(iv.getContext(), iv, model);
            }
        });
    }

    private <T> void loadImgByGlide(Context ctx, ImageView iv, T model) {
        RequestBuilder<Drawable> builder = GlideApp.with(ctx).load(model);
        if (null != iv.getDrawable()) {
            // 图片占位仅通过 xml 方式设置，布局中未设置占位图的这里就不设置了
            builder = builder.placeholder(iv.getDrawable());
        }
        if (null != iv.getDrawable()) {
            builder = builder.error(iv.getDrawable());
        } else {
            builder = builder.error(R.mipmap.img_def_loading);
        }
        builder.diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(iv);
        Log.i("App", "图片加载完成：" + model);
    }

    private <T> void loadImgByPicasso(Context ctx, ImageView iv, T model) {
        if (null == model) {
            return;
        }

        Log.i("loadImgByPicasso", model.toString());

        Picasso picasso = Picasso.get();
        picasso.setLoggingEnabled(true);

        RequestCreator rc = null;
        if (model instanceof String) {
            String filepath = (String) model;
            if (filepath.startsWith("/")) {
                filepath = "file://" + filepath;
            }
            rc = picasso.load(filepath);
        } else if (model instanceof Integer) {
            rc = picasso.load((Integer) model);
        } else if (model instanceof Uri) {
            rc = picasso.load((Uri) model);
        } else if (model instanceof File) {
            rc = picasso.load((File) model);
        } else {
            throw new IllegalArgumentException("Unknown model [ " + model + " ] of image resource.");
        }

        Drawable drawable = iv.getDrawable();
        if (null == drawable) {
            rc.placeholder(R.mipmap.img_def_loading).error(R.mipmap.img_def_loading);
        } else {
            rc.placeholder(drawable).error(drawable);
        }

        rc.into(iv);
    }
}
