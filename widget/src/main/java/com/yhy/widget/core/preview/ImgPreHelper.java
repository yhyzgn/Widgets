package com.yhy.widget.core.preview;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yhy.widget.R;
import com.yhy.widget.utils.WidgetCoreUtils;

import java.io.File;
import java.io.FileNotFoundException;

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
    private ImgDownloader mDownloader;
    private OnDownloadListener mOnDownloadListener;

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
     * @param app 当前Application
     * @return 当前对象
     */
    public ImgPreHelper init(Application app) {
        mApp = app;
        // 设置默认图片下载器
        mDownloader = new DefImgDownloader(mApp);
        return this;
    }

    /**
     * 设置图片加载器
     *
     * @param loader 图片加载器
     * @return 当前对象
     */
    public ImgPreHelper setLoader(ImgLoader loader) {
        this.mLoader = loader;
        return this;
    }

    /**
     * 是否可以下载图片
     *
     * @param enable 是否可下载
     * @return 当前对象
     */
    public ImgPreHelper setDownloadable(boolean enable) {
        this.mDownloadable = enable;
        return this;
    }

    /**
     * 下载按钮图标
     *
     * @param iconId 图标id
     * @return 当前对象
     */
    public ImgPreHelper setDownloadIconId(int iconId) {
        this.mDownloadIconId = iconId;
        return this;
    }

    /**
     * 图片下载器
     *
     * @param downloader 下载器
     * @return 当前对象
     */
    public ImgPreHelper setDownloader(ImgDownloader downloader) {
        this.mDownloader = downloader;
        return this;
    }

    /**
     * 设置下载监听器
     *
     * @param listener 下载监听器
     * @return 当前对象
     */
    public ImgPreHelper setOnDownloadListener(OnDownloadListener listener) {
        this.mOnDownloadListener = listener;
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
     * 获取图片下载器
     *
     * @return 下载器
     */
    public ImgDownloader getDownloader() {
        return mDownloader;
    }

    /**
     * 获取下载监听器
     *
     * @return 下载监听器
     */
    public OnDownloadListener getOnDownloadListener() {
        return mOnDownloadListener;
    }

    /**
     * 刷新媒体库
     *
     * @param file 已下载的图片文件
     * @throws FileNotFoundException 未找到文件异常
     */
    public void refreshMediaStore(File file) throws FileNotFoundException {
        // 把文件插入图库
        // MediaStore.Images.Media.insertImage(mApp.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
        // 通知图库刷新
        // mApp.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        // 以上方法会导致保存两张一模一样的图片，改用一下方法
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, WidgetCoreUtils.getMimeType(file));
        Uri uri = mApp.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // 通知图库刷新
        mApp.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    /**
     * 图片加载器
     */
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

    /**
     * 图片下载器
     */
    public interface ImgDownloader {

        /**
         * 下载图片
         *
         * @param activity 图片预览Activity
         * @param type     数据类型
         * @param model    图片地址
         * @param listener 下载监听器
         */
        void download(PreImgActivity activity, DataSourceType type, String model, OnDownloadListener listener);
    }

    public enum DataSourceType {
        BASE64, URL
    }

    /**
     * 下载监听器
     */
    public interface OnDownloadListener {

        /**
         * 下载进度
         *
         * @param progress 当前进度
         * @param current  已下载大小
         * @param total    总大小
         */
        void onProgress(float progress, long current, long total);

        /**
         * 下载成功
         *
         * @param img 下载的图片文件
         * @param msg 提示消息
         */
        void onSuccess(File img, String msg);

        /**
         * 下载错误
         *
         * @param error 错误信息
         */
        void onError(String error);
    }
}
