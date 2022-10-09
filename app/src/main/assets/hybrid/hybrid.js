/**
 * author  : 颜洪毅
 * e-mail  : yhyzgn@gmail.com
 * time    : 2018-05-24 11:07
 * version : 1.0.0
 * desc    : 与原生交互的js插件
 */

(function (fn, extend) {
    if (typeof fn !== "function" || typeof extend !== "object") {
        throw new Error("框架加载错误");
    }

    // 定义框架的访问通道
    window.Hybrid = window.Hy = Hybrid = Hy = fn;

    // 类属性/方法扩展
    for (let key in extend) {
        fn[key] = extend[key];
    }
})(function () {
}, {
    /**
     * 配置参数

     * bridge：Android端与js交互的桥梁，默认是window.app
     */
    config: {
        // Android 端交互桥梁名称
        namespace: window.app
    },

    /**
     * 浏览器版本仓库
     */
    browser: {
        versions: function () {
            const u = navigator.userAgent;
            return {
                trident: u.indexOf('Trident') > -1, // IE内核
                presto: u.indexOf('Presto') > -1, // opera内核
                webKit: u.indexOf('AppleWebKit') > -1, // 苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') === -1, // 火狐内核
                mobile: !!u.match(/Android|webOS|iPhone|iPod|BlackBerry/i), // 是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, // 是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1, // 是否iPad
                webApp: u.indexOf('Safari') === -1,// 是否web应该程序，没有头部与底部
                google: u.indexOf('Chrome') > -1 // Google浏览器
            };
        }(),
        language: (navigator.browserLanguage || navigator.language).toLowerCase()
    },

    /**
     * 初始化方法
     * @param config 配置参数（该参数可不传，代表使用默认配置）
     * @param callback 初始化回调方法
     */
    init: function (config, callback) {
        let cb = callback;
        let args = arguments;

        if (args.length === 1) {
            cb = args[0];
            config = null;
        }
        if (document.readyState === "interactive" || document.readyState === "complete") {
            setTimeout(doInit, 1);
        } else {
            Hybrid.event(document, "DOMContentLoaded", doInit);
        }

        function doInit() {
            if (config) {
                for (let key in config) {
                    Hybrid.config[key] = config[key];
                }
            }
            Hybrid.environment(function (mobile, android, ios) {
                // 为所有的a标签加上URL标识
                let as = null;
                if (mobile && (as = document.getElementsByTagName("a"))) {
                    let href = null;
                    for (let i = 0; i < as.length; i++) {
                        href = as[i].getAttribute("href");
                        if (href && href.length > 0 && href !== "/" &&
                            href.indexOf("javascript:") === -1 &&
                            href.indexOf("tel:") === -1 &&
                            href.indexOf("mailto:") === -1) {

                            href = href.replace(/\/$/, "");
                            href += href.indexOf("?") === -1 ? "?" : "&";
                            as[i].setAttribute("href", href);
                        }
                    }
                }
                cb(mobile, android, ios);
            });
        }
    },

    /**
     * 绑定事件
     * @param element 目标元素
     * @param type 事件类型
     * @param fn 事件方法
     */
    event: function (element, type, fn) {
        element.addEventListener(type, fn, false);
    },

    /**
     * 点击事件
     * @param element 目标元素
     * @param fn 事件方法
     */
    click: function (element, fn) {
        Hybrid.event(element, "click", fn);
    },

    /**
     * 注册方法到window对象，原生才能调用js的方法
     * @param name 方法名称
     * @param fn 方法体
     * @param target 方法要将要绑定的对象
     *
     * 当只传入一个参数时，该参数必须是js对象类型
     */
    register: function (name, fn, target) {
        if (arguments.length === 3) {
            target = typeof target === "object" || typeof target === "function" ? target : window;
            target[name] = fn;
        } else if (arguments.length === 2) {
            if (typeof arguments[0] === "string" && typeof arguments[1] === "function") {
                window[arguments[0]] = arguments[1];
            } else if (typeof arguments[0] === "object" && (typeof arguments[1] === "object" || typeof target === "function")) {
                for (let key in arguments[0]) {
                    arguments[1][key] = arguments[0][key];
                }
            } else {
                throw new Error("未知的函数注册方式");
            }
        } else if (arguments.length === 1 && typeof arguments[0] === "object") {
            for (let key in arguments[0]) {
                window[key] = arguments[0][key];
            }
        }
        else {
            throw new Error("未知的函数注册方式");
        }
    },

    /**
     * js调用原生方法
     * @param fn 方法名称
     * @param args 要传递的参数，必须是【字符串、数字、布尔和js对象】四种类型之一（该参数可不传，代表不传递任何参数）
     * @param callback 方法调用结果的回调（该参数可不传，代表没有任何回调）
     */
    native: function (fn, args, callback) {
        const outArgs = arguments;
        Hybrid.environment(function (mobile, android, ios) {
            if (mobile) {
                // Android 或者 IOS（WKWebView或者UIWebView）
                // Android使用桥梁【bridge[方法名](参数)】方式
                // WKWebView使用【window.webkit.messageHandlers[方法名].postMessage(参数)】方式
                // UIWebView使用【JavaScriptCore】库，直接【window[方法名](参数)】方式
                let bridge = android ? Hybrid.config.namespace : ios ? (isWKWebView() ? window.webkit.messageHandlers : window) : undefined;

                if (bridge === undefined || typeof bridge[fn] === "undefined") {
                    return;
                }

                if (outArgs.length === 1) {
                    if (isWKWebView()) {
                        // WKWebView
                        bridge[fn].postMessage();
                    } else {
                        // Android和IOS的UIWebView
                        bridge[fn]();
                    }
                } else if (outArgs.length >= 2) {
                    if (typeof outArgs[1] !== "string" && typeof outArgs[1] !== "number" && typeof outArgs[1] !== "boolean" && typeof outArgs[1] !== "object") {
                        throw new Error("需要传递到原生环境的参数必须是【字符串、数字、布尔和js对象】四种类型之一");
                    }
                    if (typeof outArgs[1] === "object") {
                        args = JSON.stringify(args);
                    }

                    let result;
                    if (isWKWebView()) {
                        // WKWebView
                        result = bridge[fn].postMessage(args);
                    } else {
                        // Android和IOS的UIWebView
                        result = bridge[fn](args);
                    }
                    if (typeof callback === "function") {
                        callback(result);
                    }
                }

                /**
                 * 判断是否是IOS的WKWebView
                 * @returns {boolean} 是否是IOS的WKWebView
                 */
                function isWKWebView() {
                    return typeof window.webkit !== "undefined" && typeof window.webkit.messageHandlers !== "undefined";
                }
            } else {
                throw new Error("该方法仅支持移动端");
            }
        });
    },

    /**
     * 浏览器环境检查
     *
     * 只有携带urlFlag的页面，才算是移动端页面（便于区分wap站和WebView加载的页面）
     *
     * @param callback 回调方法
     */
    environment: function (callback) {
         callback(Hybrid.browser.versions.mobile, Hybrid.browser.versions.android, Hybrid.browser.versions.ios);
    },

    /**
     * 获取URL上的参数
     * @param name 参数名
     * @returns string 参数值
     */
    urlParam: function (name) {
        let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        let result = window.location.search.substr(1).match(reg);
        if (result != null) {
            return decodeURIComponent(result[2]);
        }
        return "";
    },

    /**
     * 扩展方法
     *
     * 传入一个参数时必须是js对象
     * 传入两个参数时必须是方法名和方法体
     *
     * @param name 方法名
     * @param fn 方法体
     */
    extend: function (name, fn) {
        if (arguments.length === 2) {
            Hybrid[name] = fn;
        } else if (arguments.length === 1) {
            for (let key in arguments[0]) {
                Hybrid[key] = arguments[0][key];
            }
        } else {
            throw new Error("参数格式错误");
        }
    }
});