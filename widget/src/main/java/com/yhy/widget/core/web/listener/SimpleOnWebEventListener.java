package com.yhy.widget.core.web.listener;

import android.net.Uri;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.yhy.widget.core.web.HybridWebView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 11:20
 * version: 1.0.0
 * desc   : 默认的简化版OnWebEventListener
 */
public class SimpleOnWebEventListener implements OnWebEventListener {
    @Override
    public void onRequestFocus(HybridWebView view) {
    }

    @Override
    public void onCloseWindow(HybridWebView window) {
    }

    @Override
    public boolean onJsAlert(HybridWebView view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public boolean onJsConfirm(HybridWebView view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public boolean onJsPrompt(HybridWebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    @Override
    public boolean onJsBeforeUnload(HybridWebView view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
    }

    @Override
    public boolean onShowFileChooser(HybridWebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }
}
