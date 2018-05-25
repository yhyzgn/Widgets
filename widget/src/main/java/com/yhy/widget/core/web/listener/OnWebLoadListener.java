package com.yhy.widget.core.web.listener;

import android.graphics.Bitmap;

import com.yhy.widget.core.web.HybridWebView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 10:22
 * version: 1.0.0
 * desc   : HybridWebView加载过程监听器
 */
public interface OnWebLoadListener {

    /**
     * 开始加载
     *
     * @param view    当前HybridWebView
     * @param url     url
     * @param favicon 图标
     */
    void onStart(HybridWebView view, String url, Bitmap favicon);

    /**
     * 加载进度
     *
     * @param view        当前HybridWebView
     * @param newProgress 加载进度
     */
    void onProgress(HybridWebView view, int newProgress);

    /**
     * 加载结束
     *
     * @param view 当前HybridWebView
     * @param url  url
     */
    void onFinish(HybridWebView view, String url);

    /**
     * 接收到标题
     *
     * @param view  当前HybridWebView
     * @param title 标题
     */
    void onReceivedTitle(HybridWebView view, String title);

    /**
     * 接收到图标
     *
     * @param view 当前HybridWebView
     * @param icon 图标
     */
    void onReceivedIcon(HybridWebView view, Bitmap icon);
}