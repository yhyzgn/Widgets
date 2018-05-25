package com.yhy.widget.core.web.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yhy.widget.core.web.HybridWebView;

import static android.webkit.WebView.SCHEME_TEL;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 10:55
 * version: 1.0.0
 * desc   : 默认的WebViewClient
 */
public class WebClient extends WebViewClient {
    private HybridWebView mWebView;

    /**
     * 构造函数
     *
     * @param webView 当前WebView
     */
    public WebClient(HybridWebView webView) {
        this.mWebView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http:") || url.startsWith("https:")) {
            // 采用助手中的方法进行重定向，其内部拼接了判断是否是app的标志
            mWebView.loadUrl(url);
        } else if (url.startsWith(SCHEME_TEL)) {
            if (ActivityCompat.checkSelfPermission(mWebView.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                mWebView.getContext().startActivity(intent);
            }
        }
        return true;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        if (mWebView.getConfig().isCacheEnable() && null != mWebView.getHelper()) {
            WebResourceResponse response = mWebView.getHelper().shouldInterceptRequest(url);
            return null != response ? response : super.shouldInterceptRequest(webView, url);
        }
        return super.shouldInterceptRequest(webView, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (null != mWebView.getLoadListener()) {
            mWebView.getLoadListener().onStart((HybridWebView) view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (null != mWebView.getLoadListener()) {
            mWebView.getLoadListener().onFinish((HybridWebView) view, url);
        }
    }
}