package com.yhy.widget.core.web.config;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 10:18
 * version: 1.0.0
 * desc   : 参数配置类
 */
public class Config {
    // 是否需要严格模式
    private boolean strictMode = false;
    // URL标识的名称
    private String urlFlagName = "platform";
    // URL标识的值
    private String urlFlagValue = "app";
    // 桥梁名称
    private String bridgeName = "app";
    // 是否开启缓存
    private boolean cacheEnable = true;
    // 缓存生命周期，单位：s
    private long cacheExpire = 7 * 24 * 60 * 60;

    /**
     * 是否处于严格模式
     *
     * @return 是否处于严格模式
     */
    public boolean isStrictMode() {
        return strictMode;
    }

    /**
     * 是否需要严格模式
     *
     * @param strictMode 是否需要严格模式
     * @return 当前对象
     */
    public Config setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    /**
     * 获取URL标识名称
     *
     * @return URL标识名称
     */
    public String getUrlFlagName() {
        return urlFlagName;
    }

    /**
     * 设置URL标识名称
     *
     * @param urlFlagName URL标识名称
     * @return 当前对象
     */
    public Config setUrlFlagName(String urlFlagName) {
        this.urlFlagName = urlFlagName;
        return this;
    }

    /**
     * 获取URL标识值
     *
     * @return URL标识值
     */
    public String getUrlFlagValue() {
        return urlFlagValue;
    }

    /**
     * 设置URL标识值
     *
     * @param urlFlagValue URL标识值
     * @return 当前对象
     */
    public Config setUrlFlagValue(String urlFlagValue) {
        this.urlFlagValue = urlFlagValue;
        return this;
    }

    /**
     * 获取桥梁名称
     *
     * @return 桥梁名称
     */
    public String getBridgeName() {
        return bridgeName;
    }

    /**
     * 设置桥梁名称
     *
     * @param bridgeName 桥梁名称
     * @return 当前对象
     */
    public Config setBridgeName(String bridgeName) {
        this.bridgeName = bridgeName;
        return this;
    }

    /**
     * 是否开启了缓存
     *
     * @return 是否开启了缓存
     */
    public boolean isCacheEnable() {
        return cacheEnable;
    }

    /**
     * 是否开启缓存
     *
     * @param cacheEnable 是否开启缓存
     * @return 当前对象
     */
    public Config setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
        return this;
    }

    /**
     * 获取缓存时间，单位：ms
     *
     * @return 缓存时间
     */
    public long getCacheExpire() {
        // 将秒转换为毫秒
        return cacheExpire * 1000L;
    }

    /**
     * 设置缓存时间，单位：s
     *
     * @param cacheExpire 缓存时间
     * @return 当前对象
     */
    public Config setCacheExpire(long cacheExpire) {
        this.cacheExpire = cacheExpire;
        return this;
    }
}