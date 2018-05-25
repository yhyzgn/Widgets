package com.yhy.widget.core.web.client;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.yhy.widget.core.web.HybridWebView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 11:02
 * version: 1.0.0
 * desc   : 默认的WebChromeClient
 */
public class ChromeClient extends WebChromeClient {
    private HybridWebView mWebView;

    /**
     * 构造函数
     *
     * @param webView 当前WebView
     */
    public ChromeClient(HybridWebView webView) {
        this.mWebView = webView;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (null != mWebView.getLoadListener()) {
            mWebView.getLoadListener().onProgress((HybridWebView) view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (null != mWebView.getLoadListener()) {
            mWebView.getLoadListener().onReceivedTitle((HybridWebView) view, title);
        }
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        if (null != mWebView.getLoadListener()) {
            mWebView.getLoadListener().onReceivedIcon((HybridWebView) view, icon);
        }
    }

    @Override
    public void onRequestFocus(WebView view) {
        super.onRequestFocus(view);
        if (null != mWebView.getEventListener()) {
            mWebView.getEventListener().onRequestFocus((HybridWebView) view);
        }
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
        if (null != mWebView.getEventListener()) {
            mWebView.getEventListener().onCloseWindow((HybridWebView) window);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (null != mWebView.getEventListener()) {
            return mWebView.getEventListener().onJsAlert((HybridWebView) view, url, message, result);
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (null != mWebView.getEventListener()) {
            return mWebView.getEventListener().onJsConfirm((HybridWebView) view, url, message, result);
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (null != mWebView.getEventListener()) {
            return mWebView.getEventListener().onJsPrompt((HybridWebView) view, url, message, defaultValue, result);
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        if (null != mWebView.getEventListener()) {
            return mWebView.getEventListener().onJsBeforeUnload((HybridWebView) view, url, message, result);
        }
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        super.onConsoleMessage(message, lineNumber, sourceID);
        if (null != mWebView.getEventListener()) {
            mWebView.getEventListener().onConsoleMessage(message, lineNumber, sourceID);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (null != mWebView.getEventListener()) {
            return mWebView.getEventListener().onShowFileChooser((HybridWebView) webView, filePathCallback, fileChooserParams);
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}