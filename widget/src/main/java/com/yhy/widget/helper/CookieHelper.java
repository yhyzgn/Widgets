package com.yhy.widget.helper;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.Map;
import java.util.Objects;

/**
 * Cookie 助手
 * <p>
 * Created on 2022-10-09 14:29
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class CookieHelper {
    public static final CookieHelper instance = new CookieHelper() {
    };

    private CookieHelper() {
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @return 单例对象
     */
    public static CookieHelper init(Context context) {
        CookieSyncManager.createInstance(context);
        return instance;
    }

    /**
     * 设置 cookie
     *
     * @param url     URL 地址
     * @param pairMap 键值对
     */
    public void sync(String url, Map<String, String> pairMap) {
        try {
            sync(url, pickDomain(url), "/", pairMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置 cookie
     *
     * @param url     URL 地址
     * @param domain  域名，默认从 url 中提取
     * @param pairMap 键值对
     */
    public void sync(String url, String domain, Map<String, String> pairMap) {
        sync(url, domain, "/", pairMap);
    }

    /**
     * 设置 cookie
     *
     * @param url     URL 地址
     * @param domain  域名，默认从 url 中提取
     * @param path    路径，默认 /
     * @param pairMap 键值对
     */
    public void sync(String url, String domain, String path, Map<String, String> pairMap) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("'url' can not be empty.");
        }
        if (TextUtils.isEmpty(domain)) {
            domain = pickDomain(url);
        }
        if (TextUtils.isEmpty(path)) {
            path = "/";
        }
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        if (null != pairMap && pairMap.size() > 0) {
            for (Map.Entry<String, String> et : pairMap.entrySet()) {
                cm.setCookie(url, et.getKey() + "=" + et.getValue());
            }
        }
        cm.setCookie(url, "Domain=" + domain);
        cm.setCookie(url, "Path=" + path);
        String cookie = cm.getCookie(url);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cm.flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * 获取 cookie
     *
     * @param url  URL 地址
     * @param name 名称
     * @return 值
     */
    public String get(String url, String name) {
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        String cookie = cm.getCookie(url);
        if (!TextUtils.isEmpty(cookie)) {
            String[] cks = cookie.split(";");
            if (cks.length > 0) {
                for (String ck : cks) {
                    String[] kvs = ck.trim().split("=");
                    if (kvs.length > 0 && Objects.equals(kvs[0], name)) {
                        return kvs[1];
                    }
                }
            }
        }
        return null;
    }

    /**
     * 从 url 中提取 domain
     * <p>
     * 不包含端口号
     *
     * @param url URL 地址
     * @return 域名
     */
    private String pickDomain(String url) {
        url = url.matches("^https?://") ? url.replaceAll("^https?://", "") : url;
        String[] split = url.split("/");
        // 不包含端口号
        return split[0].contains(":") ? split[0].split(":")[0] : split[0];
    }
}
