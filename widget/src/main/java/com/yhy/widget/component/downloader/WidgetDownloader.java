package com.yhy.widget.component.downloader;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

import com.permissionx.guolindev.PermissionX;
import com.yhy.widget.utils.WidgetCoreUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 下载器
 * <p>
 * Created on 2022-10-26 11:22
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class WidgetDownloader {
    private static final String TAG = "WidgetDownloader";
    private final FragmentActivity mActivity;
    private final Map<String, String> headerMap = new HashMap<>();
    private boolean mAllowedMobileNetwork;
    private boolean mAllowScanningByMediaScanner;
    private boolean mAllowedNotification;
    private boolean mAllowedOverRoaming;
    private String mTitle;
    private String mDescription;
    private String mDestRelativePath;
    private String mFilename;
    private String mUrl;
    private DownloadManager mDm;
    private long mDownloadId;
    private DownloadChangeObserver mObserver;

    private WidgetDownloader(FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * 获取一个实例
     *
     * @param activity Activity 上下文
     * @return 实例
     */
    public static WidgetDownloader with(FragmentActivity activity) {
        return new WidgetDownloader(activity);
    }

    /**
     * 是否允许移动数据网络下载
     *
     * @param allowed 是否允许
     * @return 当前实例
     */
    public WidgetDownloader allowedMobileNetwork(boolean allowed) {
        mAllowedMobileNetwork = allowed;
        return this;
    }

    /**
     * 用于设置是否允许本 MediaScanner 扫描
     *
     * @param allowed 是否允许
     * @return 当前实例
     */
    public WidgetDownloader allowScanningByMediaScanner(boolean allowed) {
        mAllowScanningByMediaScanner = allowed;
        return this;
    }

    /**
     * 用于设置下载时时候在状态栏显示通知信息
     *
     * @param allowed 是否显示
     * @return 当前实例
     */
    public WidgetDownloader allowedNotification(boolean allowed) {
        mAllowedNotification = allowed;
        return this;
    }

    /**
     * 置 Notification 的 title 信息
     *
     * @param title title
     * @return 当前实例
     */
    public WidgetDownloader title(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 置 Notification 的 description 信息
     *
     * @param description description
     * @return 当前实例
     */
    public WidgetDownloader description(String description) {
        mDescription = description;
        return this;
    }

    /**
     * 用于设置漫游状态下是否可以下载
     *
     * @param allowed 是否允许
     * @return 当前实例
     */
    public WidgetDownloader allowedOverRoaming(boolean allowed) {
        mAllowedOverRoaming = allowed;
        return this;
    }

    /**
     * 下载文件夹相对路径
     * <p>
     * 相对于系统的下载目录
     * <p>
     * <code>Environment.DIRECTORY_DOWNLOADS/${destRelativePath}</code>
     *
     * @param destRelativePath 文件夹名称
     * @return 当前实例
     */
    public WidgetDownloader destRelativePath(String destRelativePath) {
        mDestRelativePath = destRelativePath;
        return this;
    }

    /**
     * 文件名
     * <p>
     * <code>Environment.DIRECTORY_DOWNLOADS/${destRelativePath}/${filename}</code>
     *
     * @param filename 文件夹名称
     * @return 当前实例
     */
    public WidgetDownloader filename(String filename) {
        mFilename = filename;
        return this;
    }

    /**
     * 设置请求头
     *
     * @param name  名称
     * @param value 值
     * @return 当前实例
     */
    public WidgetDownloader addHeader(String name, String value) {
        headerMap.put(name, value);
        return this;
    }

    /**
     * 文件下载地址
     *
     * @param url 文件下载地址
     * @return 当前实例
     */
    public WidgetDownloader url(String url) {
        mUrl = url;
        return this;
    }

    /**
     * 开始下载
     *
     * @param progressChangedListener 进度监听器
     * @param finishedListener        结束监听器
     * @return 当前实例
     */
    public WidgetDownloader launch(OnProgressChangedListener progressChangedListener, OnFinishedListener finishedListener) {
        // 检查SD卡读取权限
        PermissionX.init(mActivity).permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).request((allGranted, grantedList, deniedList) -> {
            if (allGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    PermissionX.init(mActivity).permissions(Manifest.permission.MANAGE_EXTERNAL_STORAGE).request((al, gl, dl) -> {
                        if (al) {
                            if (Environment.isExternalStorageManager()) {
                                Log.d(TAG, "此手机是 Android 11 或更高的版本，且已获得访问所有文件权限");
                                download(progressChangedListener, finishedListener);
                            } else {
                                ActivityResultLauncher<Intent> launcher = mActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                                    boolean granted = result.getResultCode() == Activity.RESULT_OK;
                                    Log.i(TAG, "权限申请结果：" + granted);
                                    if (granted) {
                                        download(progressChangedListener, finishedListener);
                                    }
                                });
                                Log.d(TAG, "此手机是 Android 11 或更高的版本，但没有访问所有文件权限");
                                launcher.launch(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
                            }
                        }
                    });
                }
            } else {
                Log.d(TAG, "此手机是 Android 11 以下的版本，且已获得访问所有文件权限");
                download(progressChangedListener, finishedListener);
            }
        });
        return this;
    }

    /**
     * 取消下载
     */
    public void cancel() {
        if (null != mDm && mDownloadId > 0) {
            mDm.remove(mDownloadId);
            mActivity.getContentResolver().unregisterContentObserver(mObserver);
            mObserver.mScheduleExecutor.shutdown();
        }
    }

    private void download(OnProgressChangedListener progressChangedListener, OnFinishedListener finishedListener) {
        if (TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("请先设置下载地址");
        }

        if (!isInternetFile()) {
            throw new IllegalArgumentException("只能下载网络文件");
        }

        int networkTypes = DownloadManager.Request.NETWORK_WIFI;
        if (mAllowedMobileNetwork) {
            networkTypes |= DownloadManager.Request.NETWORK_MOBILE;
        }

        if (null == mDestRelativePath) {
            mDestRelativePath = "";
        }
        if (TextUtils.isEmpty(mFilename)) {
            mFilename = FilenameGenerator.generate(mUrl);
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = "文件下载";
        }
        if (TextUtils.isEmpty(mDescription)) {
            mDescription = mFilename;
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mUrl))
                .setAllowedNetworkTypes(networkTypes)
                .setNotificationVisibility(mAllowedNotification ? DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED : DownloadManager.Request.VISIBILITY_HIDDEN)
                .setTitle(mTitle)
                .setDescription(mDescription)
                .setAllowedOverRoaming(mAllowedOverRoaming)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mDestRelativePath + File.separator + mFilename)
                .setVisibleInDownloadsUi(true);

        for (Map.Entry<String, String> et : headerMap.entrySet()) {
            request.addRequestHeader(et.getKey(), et.getValue());
        }
        if (mAllowScanningByMediaScanner) {
            request.allowScanningByMediaScanner();
        }

        mDm = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadId = mDm.enqueue(request);

        mObserver = new DownloadChangeObserver(null, this, progressChangedListener, finishedListener);
        mActivity.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, mObserver);
    }

    private void progress() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mDownloadId);
        Cursor cursor = mDm.query(query);
        if (null != cursor && cursor.moveToFirst()) {
            long total = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            long current = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            float percent = current * 100.0f / total;
            if (null != mObserver) {
                if (null != mObserver.mOnProgressChangedListener) {
                    mObserver.mOnProgressChangedListener.progress(current, total, percent);
                }
                if (percent == 100.0f) {
                    Uri fileUri = mDm.getUriForDownloadedFile(mDownloadId);
                    if (null != mObserver.mOnFinishedListener) {
                        mObserver.mOnFinishedListener.finished(fileUri);
                    }
                    // 刷新媒体库
                    scanMedia(fileUri);
                    mActivity.getContentResolver().unregisterContentObserver(mObserver);
                    mObserver.mScheduleExecutor.shutdown();
                }
            }
            cursor.close();
        }
    }

    private void scanMedia(Uri fileUri) {
        if (mAllowScanningByMediaScanner && null != fileUri) {
            String filepath = WidgetCoreUtils.getPath(mActivity, fileUri);
            if (!TextUtils.isEmpty(filepath)) {
                MediaScannerConnection.scanFile(mActivity, new String[]{filepath}, null, (path, uri) -> {
                    Log.i(TAG, "MediaScanner scanned " + path);
                    Log.i(TAG, "MediaScanner -> uri=" + uri);
                });
            }
        }
    }

    private boolean isInternetFile() {
        return mUrl.matches("^((http)|(ftp)s?://).+");
    }

    private static class DownloadChangeObserver extends ContentObserver {
        private final WidgetDownloader mWd;
        private final ScheduledExecutorService mScheduleExecutor;
        private final OnProgressChangedListener mOnProgressChangedListener;
        private final OnFinishedListener mOnFinishedListener;
        private final Handler mDownloadHandler;

        public DownloadChangeObserver(Handler handler, WidgetDownloader wd, OnProgressChangedListener progressChangedListener, OnFinishedListener finishedListener) {
            super(handler);
            mWd = wd;
            mOnProgressChangedListener = progressChangedListener;
            mOnFinishedListener = finishedListener;
            mScheduleExecutor = Executors.newSingleThreadScheduledExecutor();
            mDownloadHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void onChange(boolean selfChange) {
            // 每秒执行一次
            mScheduleExecutor.scheduleAtFixedRate(() -> mDownloadHandler.post(mWd::progress), 0, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * 进度监听器
     */
    @FunctionalInterface
    public interface OnProgressChangedListener {

        /**
         * 进度变化监听
         *
         * @param current 下载大小
         * @param total   总大小
         * @param percent 已下载比例
         */
        void progress(long current, long total, float percent);
    }

    /**
     * 结束监听器
     */
    @FunctionalInterface
    public interface OnFinishedListener {

        /**
         * 下载结束
         *
         * @param uri 已下载文件的 uri
         */
        void finished(Uri uri);
    }
}
