package com.yhy.widget.core.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.yhy.widget.R;
import com.yhy.widget.core.web.bridge.HybridBridge;
import com.yhy.widget.core.web.client.ChromeClient;
import com.yhy.widget.core.web.client.WebClient;
import com.yhy.widget.core.web.config.Config;
import com.yhy.widget.core.web.helper.DownHelper;
import com.yhy.widget.core.web.listener.OnWebEventListener;
import com.yhy.widget.core.web.listener.OnWebLoadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-24 15:25
 * version: 1.0.0
 * desc   : 加强版WebView，用于混合开发
 */
public class HybridWebView extends WebView {
    private Config mConfig;
    private Loader mLoader;
    private DownHelper mHelper;
    private OnWebLoadListener mLoadListener;
    private OnWebEventListener mEventListener;
    private WebClient mWebClient;
    private ChromeClient mChromeClient;

    public HybridWebView(Context context) {
        this(context, null);
    }

    public HybridWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HybridWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HybridWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   自定义属性集
     */
    private void init(Context context, AttributeSet attrs) {
        mConfig = new Config();
        mLoader = new Loader();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HybridWebView);
        boolean strictMode = ta.getBoolean(R.styleable.HybridWebView_hwv_strict_mode, mConfig.isStrictMode());
        String urlFlagName = ta.getString(R.styleable.HybridWebView_hwv_url_flag_name);
        String urlFlagValue = ta.getString(R.styleable.HybridWebView_hwv_url_flag_value);
        String urlBridgeName = ta.getString(R.styleable.HybridWebView_hwv_bridge_name);
        boolean cacheEnable = ta.getBoolean(R.styleable.HybridWebView_hwv_cache_enable, mConfig.isCacheEnable());
        int cacheExpire = ta.getInt(R.styleable.HybridWebView_hwv_cache_expire, (int) (mConfig.getCacheExpire() / 1000L));
        ta.recycle();

        urlFlagName = TextUtils.isEmpty(urlFlagName) ? mConfig.getUrlFlagName() : urlFlagName;
        urlFlagValue = TextUtils.isEmpty(urlFlagValue) ? mConfig.getUrlFlagValue() : urlFlagValue;
        urlBridgeName = TextUtils.isEmpty(urlBridgeName) ? mConfig.getBridgeName() : urlBridgeName;

        mConfig.setStrictMode(strictMode).setUrlFlagName(urlFlagName).setUrlFlagValue(urlFlagValue).setBridgeName(urlBridgeName).setCacheEnable(cacheEnable).setCacheExpire(cacheExpire * 1000L);

        // 初始化控件
        initComponent();
    }

    /**
     * 添加请求参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return 当前对象
     */
    public HybridWebView param(String key, Object value) {
        mLoader.param(key, value);
        return this;
    }

    /**
     * 加载HTML代码
     *
     * @param data 数据
     */
    public void loadData(String data) {
        // API提供的标准用法，无法解决乱码问题
        // loadData(data, "text/html", "UTF-8");
        // 这种写法可以正确解码
        loadData(data, "text/html; charset=UTF-8", null);
    }

    /**
     * 加载HTML代码
     *
     * @param data     数据
     * @param mimeType 文档类型
     * @param encoding 编码
     */
    @Override
    public void loadData(String data, @Nullable String mimeType, @Nullable String encoding) {
        mLoader.loadData(data, mimeType, encoding);
    }

    /**
     * 加载HTML代码
     *
     * @param baseUrl    根url
     * @param data       数据
     * @param historyUrl 历史url
     */
    public void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String historyUrl) {
        mLoader.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", historyUrl);
    }

    /**
     * 加载HTML代码
     *
     * @param baseUrl    根url
     * @param data       数据
     * @param mimeType   文档类型
     * @param encoding   编码
     * @param historyUrl 历史url
     */
    @Override
    public void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
        mLoader.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    /**
     * 加载url
     *
     * @param url url
     */
    @Override
    public void loadUrl(String url) {
        mLoader.loadUrl(url);
    }

    /**
     * 加载url，可配置Header参数
     *
     * @param url                   url
     * @param additionalHttpHeaders Header参数
     */
    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        mLoader.loadUrl(url, additionalHttpHeaders);
    }

    /**
     * 将参数添加到url上
     *
     * @param url 原始url
     * @return 添加参数后的url
     */
    public String joinUrl(String url) {
        return mLoader.joinUrl(url);
    }

    /**
     * 设置配置参数
     *
     * @param config 配置参数
     * @return 当前对象
     */
    public HybridWebView setConfig(Config config) {
        mConfig = config;
        return this;
    }

    /**
     * 获取参数配置对象
     *
     * @return 参数配置对象
     */
    public Config getConfig() {
        return mConfig;
    }

    /**
     * 获取下载助手
     *
     * @return 下载助手
     */
    public DownHelper getHelper() {
        return mHelper;
    }

    /**
     * 注册对象到HybridWebView，使用配置参数中的桥梁名称
     * <p>
     * 只有注册过的方法才会被js调用
     *
     * @param bridge 桥梁对象
     * @return 当前对象
     */
    public HybridWebView register(HybridBridge bridge) {
        return register(mConfig.getBridgeName(), bridge);
    }

    /**
     * 注册对象到HybridWebView，使用自定义的桥梁名称
     * <p>
     * 只有注册过的方法才会被js调用
     *
     * @param name   桥梁名称
     * @param bridge 桥梁对象
     * @return 当前对象
     */
    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public HybridWebView register(String name, HybridBridge bridge) {
        if (!TextUtils.isEmpty(name) && null != bridge) {
            addJavascriptInterface(bridge, name);
        }
        return this;
    }

    /**
     * 调用js的函数
     *
     * @param name 函数名称
     * @return 当前对象
     */
    public HybridWebView js(final String name) {
        return js(name, null);
    }

    /**
     * 调用js的函数
     *
     * @param name 函数名称
     * @param args 要传递的参数
     * @return 当前对象
     */
    public HybridWebView js(final String name, final String args) {
        return js(name, args, null);
    }

    /**
     * 调用js函数
     *
     * @param name     函数名称
     * @param args     要传递的参数
     * @param callback 回调，可以接收函数返回值
     * @return 当前对象
     */
    public HybridWebView js(final String name, final String args, final ValueCallback<String> callback) {
        post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (null == args) {
                        HybridWebView.this.evaluateJavascript(name + "();", callback);
                    } else {
                        HybridWebView.this.evaluateJavascript(name + "('" + args + "');", callback);
                    }
                } else {
                    if (null == args) {
                        HybridWebView.super.loadUrl("javascript:" + name + "()");
                    } else {
                        HybridWebView.super.loadUrl("javascript:" + name + "('" + args + "')");
                    }
                }
            }
        });
        return this;
    }

    /**
     * 设置HybridWebView加载进度监听器
     *
     * @param listener 监听器
     * @return 当前对象
     */
    public HybridWebView setOnWebLoadListener(OnWebLoadListener listener) {
        mLoadListener = listener;
        return this;
    }

    /**
     * 设置HybridWebView事件监听器
     *
     * @param listener 监听器
     * @return 当前对象
     */
    public HybridWebView setOnWebEventListener(OnWebEventListener listener) {
        mEventListener = listener;
        return this;
    }

    /**
     * 获取加载进度监听器
     *
     * @return 加载进度监听器
     */
    public OnWebLoadListener getLoadListener() {
        return mLoadListener;
    }

    /**
     * 获取事件监听器
     *
     * @return 事件监听器
     */
    public OnWebEventListener getEventListener() {
        return mEventListener;
    }

    /**
     * 获取当前WebViewClient
     *
     * @return 当前WebViewClient
     */
    public WebClient getWebClient() {
        return mWebClient;
    }

    /**
     * 设置WebViewClient
     *
     * @param client WebViewClient
     * @return 当前对象
     */
    public HybridWebView setWebClient(WebClient client) {
        mWebClient = client;
        super.setWebViewClient(client);
        return this;
    }

    /**
     * 获取当前WebChromeClient
     *
     * @return 当前WebChromeClient
     */
    public ChromeClient getChromeClient() {
        return mChromeClient;
    }

    /**
     * 设置WebChromeClient
     *
     * @param client ChromeClient
     * @return 当前对象
     */
    public HybridWebView setChromeClient(ChromeClient client) {
        mChromeClient = client;
        super.setWebChromeClient(client);
        return this;
    }

    /**
     * 设置WebViewClient
     *
     * @param client WebViewClient
     * @see #setWebClient(WebClient)
     */
    @Deprecated
    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
    }

    /**
     * 设置WebChromeClient
     *
     * @param client WebChromeClient
     * @see #setChromeClient(ChromeClient)
     */
    @Deprecated
    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initComponent() {
        WebSettings settings = getSettings();
        // 设置编码
        settings.setDefaultTextEncodingName("UTF-8");
        // 支持js
        settings.setJavaScriptEnabled(true);
        // 适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // 禁止横向滑动
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 允许js弹出窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 设置背景颜色 透明
        setBackgroundColor(Color.TRANSPARENT);
        // 部分国产机型开启硬件加速导致奔溃，软件加速则内容过大时不显示，为了兼容要讲加速关闭
        setLayerType(View.LAYER_TYPE_NONE, null);
        // 设置 缓存模式
        setCacheConfig();

        // 设置默认Client
        setClient();
    }

    private void setClient() {
        mWebClient = new WebClient(this);
        mChromeClient = new ChromeClient(this);

        setWebClient(mWebClient);
        setChromeClient(mChromeClient);
    }

    private void setCacheConfig() {
        WebSettings settings = getSettings();
        if (mConfig.isCacheEnable()) {
            mHelper = new DownHelper(getContext());

            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setDatabaseEnabled(true);
            // 开启 DOM storage API 功能
            // 开启DOM缓存，关闭的话H5自身的一些操作是无效的
            settings.setDomStorageEnabled(true);
        } else {
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setDatabaseEnabled(false);
            settings.setDomStorageEnabled(false);
        }
    }

    /**
     * 返回
     *
     * @return 是否能返回
     */
    public boolean back() {
        if (canGoBack()) {
            // WebView能返回
            goBack();
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        ViewParent parent = getParent();
        if (parent != null) ((ViewGroup) parent).removeView(this);
        stopLoading();
        getSettings().setJavaScriptEnabled(false);
        clearHistory();
        clearView();
        removeAllViews();
        try {
            super.destroy();
        } catch (Throwable ignored) {
        }
    }

    /**
     * 加载器
     */
    private class Loader {
        private List<Param> mParams;

        private Loader() {
            mParams = new ArrayList<>();
        }

        /**
         * 设置请求参数
         *
         * @param key   参数名称
         * @param value 参数值
         * @return 当前加载器
         */
        private Loader param(String key, Object value) {
            mParams.add(new Param(key, value));
            return this;
        }

        /**
         * 加载URL
         *
         * @param url URL
         */
        private void loadUrl(String url) {
            setCacheConfig();
            if (mConfig.isStrictMode() && !url.contains(mConfig.getUrlFlagName() + "=" + mConfig.getUrlFlagValue())) {
                param(mConfig.getUrlFlagName(), mConfig.getUrlFlagValue());
            }
            url = joinUrl(url);
            HybridWebView.super.loadUrl(url);
        }

        /**
         * 加载URL，携带Header参数
         *
         * @param url                   URL
         * @param additionalHttpHeaders Header参数
         */
        private void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
            setCacheConfig();
            if (mConfig.isStrictMode() && !url.contains(mConfig.getUrlFlagName() + "=" + mConfig.getUrlFlagValue())) {
                param(mConfig.getUrlFlagName(), mConfig.getUrlFlagValue());
            }
            url = joinUrl(url);
            HybridWebView.super.loadUrl(url, additionalHttpHeaders);
        }

        /**
         * 加载HTML代码
         *
         * @param data     数据
         * @param mimeType 文档类型
         * @param encoding 编码
         */
        private void loadData(String data, @Nullable String mimeType, @Nullable String encoding) {
            HybridWebView.super.loadData(data, mimeType, encoding);
        }

        /**
         * 加载HTML代码
         *
         * @param baseUrl    根url
         * @param data       数据
         * @param mimeType   文档类型
         * @param encoding   编码
         * @param historyUrl 历史url
         */
        private void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
            HybridWebView.super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        }

        /**
         * 拼接参数到URL上
         *
         * @param url url
         * @return 完整url
         */
        private String joinUrl(String url) {
            url = url.replaceAll("\\\\", "\\");

            String[] temp = new String[]{url, ""};
            if (url.contains("#")) {
                temp = url.split("#");
            }
            StringBuilder sbUrl = new StringBuilder(temp[0]);
            StringBuilder sbParams = new StringBuilder();

            for (Param param : mParams) {
                if (null != param.value && !url.contains(param.key + "=" + param.value) && !sbParams.toString().contains(param.key + "=" + param.value)) {
                    sbParams.append("&").append(param.key).append("=").append(param.value);
                }
            }

            if (url.endsWith("/") && sbParams.length() > 0) {
                sbUrl.deleteCharAt(sbUrl.length() - 1);
            }

            if (!url.contains("?") && sbParams.length() > 0) {
                sbParams.replace(0, 1, "?");
            }
            // 考虑锚点
            return sbUrl.append(sbParams).append(temp.length == 2 && !TextUtils.isEmpty(temp[1]) ? "#" + temp[1] : "").toString();
        }

        /**
         * 参数类
         */
        private class Param {
            private String key;
            private Object value;

            /**
             * 构造行数
             *
             * @param key   参数名称
             * @param value 参数值
             */
            Param(String key, Object value) {
                this.key = key;
                this.value = value;
            }
        }
    }
}
