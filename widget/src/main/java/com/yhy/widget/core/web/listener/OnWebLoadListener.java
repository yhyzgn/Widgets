package com.yhy.widget.core.web.listener;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

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

    /**
     * 加载资源，如加载图片
     *
     * @param view 当前HybridWebView
     * @param url  资源url
     */
    void onLoadResource(HybridWebView view, String url);

    /**
     * 接收到错误
     *
     * @param view        当前HybridWebView
     * @param errorCode   错误码
     * @param description 错误信息
     * @param failingUrl  错误url
     */
    void onReceivedError(HybridWebView view, int errorCode, String description, String failingUrl);

    /**
     * 接收到http错误
     *
     * @param view          当前HybridWebView
     * @param request       请求
     * @param errorResponse 错误
     */
    void onReceivedHttpError(HybridWebView view, WebResourceRequest request, WebResourceResponse errorResponse);

    /**
     * 接收到ssl错误
     *
     * @param view    当前HybridWebView
     * @param handler Handler
     * @param error   错误信息
     */
    void onReceivedSslError(HybridWebView view, SslErrorHandler handler, SslError error);
}