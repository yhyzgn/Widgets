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
 * time   : 2018-05-25 10:36
 * version: 1.0.0
 * desc   : HybridWebView事件监听器
 */
public interface OnWebEventListener {

    /**
     * 当WebView获取到焦点是回调
     *
     * @param view 当前HybridWebView
     */
    void onRequestFocus(HybridWebView view);

    /**
     * 当窗口关闭时回调
     *
     * @param window 当前HybridWebView
     */
    void onCloseWindow(HybridWebView window);

    /**
     * 当js中弹出alert时回调
     *
     * @param view    当前HybridWebView
     * @param url     url
     * @param message 弹出信息
     * @param result  弹窗对象
     * @return 是否消费当前事件
     */
    boolean onJsAlert(HybridWebView view, String url, String message, JsResult result);

    /**
     * 当js弹出confirm类型的弹窗时回调
     *
     * @param view    当前HybridWebView
     * @param url     url
     * @param message 弹出信息
     * @param result  弹窗对象
     * @return 是否消费当前事件
     */
    boolean onJsConfirm(HybridWebView view, String url, String message, JsResult result);

    /**
     * 当js弹出prompt弹窗时回调
     *
     * @param view         当前HybridWebView
     * @param url          url
     * @param message      弹出信息
     * @param defaultValue 默认值
     * @param result       弹窗对象
     * @return 是否消费当前事件
     */
    boolean onJsPrompt(HybridWebView view, String url, String message, String defaultValue, JsPromptResult result);

    /**
     * WebView上传文件前回调
     *
     * @param view    当前HybridWebView
     * @param url     url
     * @param message 弹出信息
     * @param result  弹窗对象
     * @return 是否消费当前事件
     */
    boolean onJsBeforeUnload(HybridWebView view, String url, String message, JsResult result);

    /**
     * 当控制台有信息输出时回调
     *
     * @param message    具体信息
     * @param lineNumber 行号
     * @param sourceID   信息id
     */
    void onConsoleMessage(String message, int lineNumber, String sourceID);

    /**
     * 当需要选择文件时回调
     *
     * @param webView           当前HybridWebView
     * @param filePathCallback  用来接收最终的文件选择结果
     * @param fileChooserParams 网页上传过来的相关参数
     * @return 是否消费当前事件
     */
    boolean onShowFileChooser(HybridWebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);
}
