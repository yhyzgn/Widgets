package com.yhy.widget.core.preview;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.UUID;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-02-07 13:26
 * version: 1.0.0
 * desc   : 默认图片下载器
 */
public class DefImgDownloader implements ImgPreHelper.ImgDownloader {

    private final Application mApp;

    public DefImgDownloader(Application app) {
        mApp = app;
    }

    @Override
    public void download(final PreImgActivity activity, final String imgUrl, final ImgPreHelper.OnDownloadListener listener) {
        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                File saveDir = new File(getSaveDir(activity));
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }

                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                try {
                    URL url = new URL(imgUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8 * 1000);
                    conn.setReadTimeout(8 * 1000);
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        long totalSize = conn.getContentLength();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            totalSize = conn.getContentLengthLong();
                        }

                        final File img = new File(saveDir, getFilename(conn));

                        Log.e("Img", img.getAbsolutePath());

                        bis = new BufferedInputStream(conn.getInputStream());
                        bos = new BufferedOutputStream(new FileOutputStream(img));
                        byte[] buffer = new byte[1024 * 8];
                        int len = 0;
                        long downloaded = 0;
                        while ((len = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                            bos.flush();

                            downloaded += len;

                            final long total = totalSize;
                            final long current = downloaded;
                            if (null != listener) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onProgress((float) current / (float) total, current, total);
                                        if (current == total) {
                                            listener.onSuccess(img, "图片下载成功");
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    if (null != listener) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(e.getMessage());
                            }
                        });
                    }
                } finally {
                    try {
                        if (null != bos) {
                            bos.close();
                        }
                        if (null != bis) {
                            bis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private String getFilename(URLConnection conn) {
        // 尝试从header中获取文件名
        String filename = conn.getHeaderField("Content-Disposition");
        if (TextUtils.isEmpty(filename)) {
            // 获得实际下载文件的URL
            URL downloadUrl = conn.getURL();
            filename = downloadUrl.getFile();
            filename = filename.substring(filename.lastIndexOf("/") + 1);
        } else {
            try {
                filename = URLDecoder.decode(filename.substring(filename.indexOf("filename=") + 9), "UTF-8");
                // 有些文件名会被包含在""里面，所以要去掉，不然无法读取文件后缀
                filename = filename.replaceAll("\"", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // 如果获取失败，就设置默认文件名
        if (TextUtils.isEmpty(filename)) {
            filename = UUID.randomUUID().toString() + ".jpg";
        }
        return filename;
    }

    private String getSaveDir(Context context) {
        String root = context.getFilesDir().getAbsolutePath();
        // 如果外置内存卡可用的话，SD卡优先使用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            root = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        root += "/" + getAppName() + "/";
        return root.replaceAll("//", "/");
    }

    private String getAppName() {
        try {
            PackageManager packageManager = mApp.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mApp.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return mApp.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ImgPreview";
    }
}
