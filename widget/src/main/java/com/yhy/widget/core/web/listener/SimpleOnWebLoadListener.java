package com.yhy.widget.core.web.listener;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import com.yhy.widget.core.web.HybridWebView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 11:20
 * version: 1.0.0
 * desc   : 默认简化版OnWebLoadListener
 */
public class SimpleOnWebLoadListener implements OnWebLoadListener {
    @Override
    public void onStart(HybridWebView view, String url, Bitmap favicon) {
    }

    @Override
    public void onProgress(HybridWebView view, int newProgress) {
    }

    @Override
    public void onFinish(HybridWebView view, String url) {
    }

    @Override
    public void onReceivedTitle(HybridWebView view, String title) {
    }

    @Override
    public void onReceivedIcon(HybridWebView view, Bitmap icon) {
    }

    @Override
    public void onLoadResource(HybridWebView view, String url) {
    }

    @Override
    public void onReceivedError(HybridWebView view, int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onReceivedHttpError(HybridWebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    }

    @Override
    public void onReceivedSslError(HybridWebView view, SslErrorHandler handler, SslError error) {
    }
}
