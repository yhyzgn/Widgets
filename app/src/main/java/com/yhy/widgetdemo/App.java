package com.yhy.widgetdemo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yhy.widget.core.activity.ImgPreCfg;
import com.yhy.widgetdemo.utils.ImgUtils;
import com.yhy.widgetdemo.utils.ToastUtils;

/**
 * Created by HongYi Yan on 2017/4/10 19:40.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        //图片加载器工具初始化
        ImgUtils.init(new ImgUtils.ImgLoader() {
            @Override
            public <T> void load(Context ctx, final ImageView iv, T model) {
                Glide.with(ctx)
                        .load(model)
                        .asBitmap()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//使用磁盘缓存
                        .skipMemoryCache(true)//跳过内存缓存
                        .animate(R.anim.anim_alpha_image_load)
//                        .crossFade()//渐变切换
//                        .dontAnimate()
//                        .into(iv);
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super
                                    Bitmap> glideAnimation) {
                                iv.setImageBitmap(bitmap);
                            }
                        });
            }
        });

        ImgPreCfg.init(new ImgPreCfg.ImgLoader() {
            @Override
            public <T> void load(ImageView iv, T model, ProgressBar pbLoading) {
                Glide.with(iv.getContext()).load(model).into(iv);
            }
        });

        ToastUtils.init(this);
    }
}
