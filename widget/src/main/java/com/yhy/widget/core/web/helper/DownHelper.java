package com.yhy.widget.core.web.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-05-25 10:59
 * version: 1.0.0
 * desc   : WebView静态资源下载助手
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "WeakerAccess"})
public class DownHelper {
    private final static String DEF_DIR_NAME = "files_cache";
    private final Map<String, String> mMimeMap;
    private Context mCtx;
    private String mCacheDir;

    // 线程池
    private ExecutorService mExecutor;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public DownHelper(Context context) {
        this(context, DEF_DIR_NAME);
    }

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param cacheDir 缓存目录
     */
    public DownHelper(Context context, String cacheDir) {
        mMimeMap = new HashMap<>();

        //初始化MimeType数据表
        mMimeMap.put(".png", "image/png");
        mMimeMap.put(".gif", "image/gif");
        mMimeMap.put(".jpg", "image/jpeg");
        mMimeMap.put(".jpeg", "image/jpeg");
        mMimeMap.put(".js", "text/javascript");
        mMimeMap.put(".css", "text/css");

        init(context, cacheDir);
    }

    /**
     * 初始化
     *
     * @param context  Context
     * @param cacheDir 缓存文件夹名称
     */
    private void init(Context context, String cacheDir) {
        mCtx = context;

        if (TextUtils.isEmpty(cacheDir)) {
            cacheDir = DEF_DIR_NAME;
        }

        String cachePath = mCtx.getFilesDir().getAbsolutePath();
        mCacheDir = cachePath.endsWith("/") || cachePath.endsWith("\\") ? cachePath + cacheDir : cachePath + "/" + cacheDir;
    }

    /**
     * 下载文件
     *
     * @param url 文件地址
     */
    private void download(final String url) {
        //创建下载任务
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //获取临时文件的保存路径，在此之前需要判断url中是否有拼接参数，有的话就去除参数，保证文件后缀获取成功
                File tempFile = getTempFile(subUrl(url));

                HttpURLConnection conn = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                //文件总大小与已下载大小
                int fileSize = 0, downloadSize = 0;
                //开始下载
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();

                    //设置相关参数
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(6000);
                    conn.setRequestProperty("Charset", "UTF-8");

                    //开启请求
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        fileSize = conn.getContentLength();

                        //先将文件下载为临时文件，下载完成后再更改为正式文件名称
                        bis = new BufferedInputStream(conn.getInputStream());
                        bos = new BufferedOutputStream(new FileOutputStream(tempFile));

                        int len;
                        //定义4M的缓冲区
                        byte[] buffer = new byte[4 * 1024 * 1024];
                        while ((len = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                            downloadSize += len;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //将下载完成的临时文件更名为正式文件名称
                    if (tempFile.exists()) {
                        if (fileSize > 0 && fileSize == downloadSize) {
                            tempFile.renameTo(getSaveFile(url));
                        } else {
                            tempFile.delete();
                        }
                    }

                    //关闭资源
                    try {
                        if (null != bis) {
                            bis.close();
                        }

                        if (null != bos) {
                            bos.close();
                        }

                        if (null != conn) {
                            conn.disconnect();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //开启下载线程
        if (null == mExecutor) {
            mExecutor = createPool();
        }
        mExecutor.execute(task);
    }

    /**
     * 文件是否已经下载
     *
     * @param url 文件地址
     * @return 是否已经下载
     */
    private boolean isDownloaded(String url) {
        File file = getDownloadedFile(url);
        return null != file && file.exists();
    }

    /**
     * 获取已下载的文件
     *
     * @param url 文件地址
     * @return 已下载的文件
     */
    public File getDownloadedFile(String url) {
        url = subUrl(url);
        //获取下载的文件
        return getSaveFile(url);
    }

    /**
     * 获取缓存文件夹路径
     *
     * @return 缓存文件夹路径
     */
    public String getCacheDir() {
        return mCacheDir;
    }

    /**
     * WebView请求拦截，每个请求都会经过该方法
     *
     * @param url 请求地址
     * @return 资源响应
     */
    public WebResourceResponse shouldInterceptRequest(String url) {
        url = subUrl(url);

        WebResourceResponse response = null;
        // 检查该资源是否已经提前下载完成。
        boolean resDown = isDownloaded(url);
        if (resDown) {
            //如果已经下载过文件，就放回已下载文件的资源响应
            if (url.endsWith(".png")) {
                response = getWebResourceResponse(url, getMimeByExt(".png"));
            } else if (url.endsWith(".gif")) {
                response = getWebResourceResponse(url, getMimeByExt(".gif"));
            } else if (url.endsWith(".jpg")) {
                response = getWebResourceResponse(url, getMimeByExt(".jpg"));
            } else if (url.endsWith(".jpeg")) {
                response = getWebResourceResponse(url, getMimeByExt(".jpeg"));
            } else if (url.endsWith(".js")) {
                response = getWebResourceResponse(url, getMimeByExt(".js"));
            } else if (url.endsWith(".css")) {
                response = getWebResourceResponse(url, getMimeByExt(".css"));
            }
        } else {
            //如果没有下载过，就下载文件
            String ext = url.contains(".") ? url.substring(url.lastIndexOf(".")) : "";
            if (shouldDownload(ext)) {
                //判断网络状态，只有在WiFi状态下才自动下载
                ConnectivityManager cm = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo ni = null != cm ? cm.getActiveNetworkInfo() : null;
                if (null != ni && ni.getType() == ConnectivityManager.TYPE_WIFI) {
                    download(url);
                }
            }
        }

        //如果没有下载过资源，返回null给WebView，然后WebView会自动请求网络加载数据
        return response;
    }

    /**
     * 获取资源响应
     *
     * @param url  请求地址
     * @param mime 文件MimeType
     * @return 响应
     */
    private WebResourceResponse getWebResourceResponse(String url, String mime) {
        url = subUrl(url);

        WebResourceResponse response = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(getSaveFile(url)));
            response = new WebResourceResponse(mime, "UTF-8", bis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 创建线程池
     *
     * @return 线程池
     */
    private ExecutorService createPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    /**
     * 从完整url中去除请求参数
     *
     * @param url 完整url
     * @return 去除参数后的url
     */
    private String subUrl(String url) {
        return url.contains("?") ? url.substring(0, url.indexOf("?")) : url;
    }

    /**
     * 根据MimeType表判断某个后缀的url是否应该下载文件
     *
     * @param ext 后缀
     * @return 是否该下载
     */
    private boolean shouldDownload(String ext) {
        if (!TextUtils.isEmpty(ext)) {
            if (!ext.startsWith(".")) {
                ext = "." + ext;
            }

            for (Map.Entry<String, String> et : mMimeMap.entrySet()) {
                if (et.getKey().equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据后缀获取MimeType
     *
     * @param ext 后缀
     * @return MimeType
     */
    private String getMimeByExt(String ext) {
        if (!TextUtils.isEmpty(ext)) {
            if (!ext.startsWith(".")) {
                ext = "." + ext;
            }

            for (Map.Entry<String, String> et : mMimeMap.entrySet()) {
                if (et.getKey().equals(ext)) {
                    return et.getValue();
                }
            }
        }
        return "";
    }

    /**
     * 获取文件的正式保存目标文件
     *
     * @param url 请求地址
     * @return 正式目标文件
     */
    private File getSaveFile(String url) {
        return getFile(url, "");
    }

    /**
     * 获取临时文件的目标文件
     *
     * @param url 请求地址
     * @return 临时目标文件
     */
    private File getTempFile(String url) {
        return getFile(url, "temp_");
    }

    /**
     * 获取保存路径的目标文件
     *
     * @param url    请求地址
     * @param prefix 文件名前缀
     * @return 目标文件
     */
    private File getFile(String url, String prefix) {
        File dir = new File(mCacheDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String ext = url.contains(".") ? url.substring(url.lastIndexOf(".")) : "";
        return new File(dir, prefix + md5(url) + ext);
    }

    /**
     * md5加密
     *
     * @param str 原始字符串
     * @return 加密后字符串
     */
    private String md5(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] result = digest.digest(str.getBytes());
            int i;
            String hexString;
            for (byte b : result) {
                i = b & 0xff;
                hexString = Integer.toHexString(i);
                if (hexString.length() < 2)
                    hexString = "0" + hexString;
                sb.append(hexString);
            }
        } catch (NoSuchAlgorithmException e) {
            // 如果找不到MD5加密方法，就报异常
            e.printStackTrace();
        }
        return sb.toString();
    }
}