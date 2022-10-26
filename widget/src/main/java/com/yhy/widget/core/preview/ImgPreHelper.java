package com.yhy.widget.core.preview;

import android.app.Application;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-02-07 11:05
 * version: 1.0.0
 * desc   : 图片预览辅助类
 */
public class ImgPreHelper {
    private volatile static ImgPreHelper instance;

    private Application mApp;
    private ImgLoader mLoader;
    private boolean mDownloadable;
    private int mDownloadIconId;

    private ImgPreHelper() {
        if (null != instance) {
            throw new UnsupportedOperationException("Can not instantiate singleton class.");
        }
        mDownloadable = true;
        mDownloadIconId = R.mipmap.ic_def_download;
    }

    public static ImgPreHelper getInstance() {
        if (null == instance) {
            synchronized (ImgPreHelper.class) {
                if (null == instance) {
                    instance = new ImgPreHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param app    当前Application
     * @param loader 图片加载器
     * @return 当前对象
     */
    public ImgPreHelper init(Application app, ImgLoader loader) {
        mApp = app;
        this.mLoader = loader;
        return this;
    }

    /**
     * 是否可以下载图片
     *
     * @param enable 是否可下载
     * @return 当前对象
     */
    public ImgPreHelper downloadable(boolean enable) {
        this.mDownloadable = enable;
        return this;
    }

    /**
     * 下载按钮图标
     *
     * @param iconId 图标id
     * @return 当前对象
     */
    public ImgPreHelper downloadIconId(int iconId) {
        this.mDownloadIconId = iconId;
        return this;
    }

    /**
     * 获取当前Application
     *
     * @return 当前Application
     */
    public Application getApp() {
        return mApp;
    }

    /**
     * 获取图片加载器
     *
     * @return 图片加载器
     */
    public ImgLoader getLoader() {
        return mLoader;
    }

    /**
     * 获取是否可下载
     *
     * @return 是否可下载
     */
    public boolean isDownloadable() {
        return mDownloadable;
    }

    /**
     * 获取下载按钮图标id
     *
     * @return 图标id
     */
    public int getDownloadIconId() {
        return mDownloadIconId;
    }

    /**
     * 图片加载器
     */
    @FunctionalInterface
    public interface ImgLoader {

        /**
         * 加载图片
         *
         * @param iv        图片控件
         * @param model     图片路径
         * @param pbLoading 加载进度条，默认不显示
         * @param <T>       路径泛型参数
         */
        <T> void load(ImageView iv, T model, ProgressBar pbLoading);
    }
}
