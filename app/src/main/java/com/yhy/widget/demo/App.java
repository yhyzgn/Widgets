package com.yhy.widget.demo;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
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
        ImgUtils.init(new ImgUtils.ImgLoader() {
            @Override
            public <T> void load(Context ctx, final ImageView iv, T model) {
                loadImgByPicasso(ctx, iv, model);

//                Glide.with(ctx)
//                        .load(model)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .error(R.mipmap.ic_launcher)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)//使用磁盘缓存
//                        .skipMemoryCache(true)//跳过内存缓存
//                        .animate(R.anim.anim_alpha_image_load)
//                        .crossFade()//渐变切换
//                        .into(iv);
////                        .into(new SimpleTarget<Bitmap>() {
////                            @Override
////                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super
////                                    Bitmap> glideAnimation) {
////                                iv.setImageBitmap(bitmap);
////                            }
////                        });
            }
        });

        ImgPreHelper.getInstance().init(this).setLoader(new ImgPreHelper.ImgLoader() {
            @Override
            public <T> void load(ImageView iv, T model, ProgressBar pbLoading) {
//                Glide.with(iv.getContext()).load(model).into(iv);
                loadImgByPicasso(iv.getContext(), iv, model);
            }
        }).setOnDownloadListener(new ImgPreHelper.OnDownloadListener() {
            @Override
            public void onProgress(float progress, long current, long total) {
                Log.i("ImgDownloader", "下载进度：" + (progress * 100F) + "%，总大小：" + total + " bytes, 已下载：" + current + " bytes.");
            }

            @Override
            public void onSuccess(File img, String msg) {
                ToastUtils.shortT(msg);
            }

            @Override
            public void onError(String error) {
                ToastUtils.shortT(error);
            }
        });
    }


    private <T> void loadImgByPicasso(Context ctx, ImageView iv, T model) {
        if (null == model) {
            return;
        }

        Picasso picasso = Picasso.with(ctx);
        RequestCreator rc = null;
        if (model instanceof String) {
            rc = picasso.load((String) model);
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
