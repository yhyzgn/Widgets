package com.yhy.widget.core.dialog.status;

import android.content.Context;

import com.yhy.widget.core.dialog.status.dialog.ErrorDialog;
import com.yhy.widget.core.dialog.status.dialog.FailedDialog;
import com.yhy.widget.core.dialog.status.dialog.LoadingDialog;
import com.yhy.widget.core.dialog.status.dialog.SuccessDialog;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2019-04-02 17:18
 * version: 1.0.0
 * desc   : StatusDialog管理器
 */
public class StatusDialogManager {
    private Context mContext;

    private String mLoadingText = "加载中...";
    private String mSuccessText = "加载成功";
    private String mFailedText = "加载失败";
    private String mErrorText = "加载错误";
    private boolean mEnableLoadingCancel = true;
    private long mDuration = 3000;

    private LoadingDialog mLoadingDialog;
    private SuccessDialog mSuccessDialog;
    private FailedDialog mFailedDialog;
    private ErrorDialog mErrorDialog;

    private StatusDialogManager(Context context) {
        mContext = context;
    }

    public static StatusDialogManager get(Context context) {
        return new StatusDialogManager(context);
    }

    public StatusDialogManager loadingText(String text) {
        mLoadingText = text;
        return this;
    }

    public StatusDialogManager successText(String text) {
        mSuccessText = text;
        return this;
    }

    public StatusDialogManager failedText(String text) {
        mFailedText = text;
        return this;
    }

    public StatusDialogManager errorText(String text) {
        mErrorText = text;
        return this;
    }

    public StatusDialogManager enableLoadingCancel(boolean enable) {
        mEnableLoadingCancel = enable;
        return this;
    }

    public StatusDialogManager duration(long duration) {
        mDuration = duration;
        return this;
    }

    public StatusDialogManager create() {
        // 加载中
        mLoadingDialog = new LoadingDialog(mContext, mLoadingText);
        mLoadingDialog.setCancelable(mEnableLoadingCancel, false);
        // 成功
        mSuccessDialog = new SuccessDialog(mContext, mSuccessText, mDuration);
        // 失败
        mFailedDialog = new FailedDialog(mContext, mFailedText, mDuration);
        // 错误
        mErrorDialog = new ErrorDialog(mContext, mErrorText, mDuration);
        return this;
    }

    public void loading() {
        loading(mLoadingText);
    }

    public void loading(String text) {
        mLoadingDialog.setText(text);
        mLoadingDialog.show();
    }

    public void dismiss() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void success() {
        success(mSuccessText);
    }

    public void success(String text) {
        mSuccessDialog.setText(text);
        mSuccessDialog.show();
    }

    public void failed() {
        failed(mFailedText);
    }

    public void failed(String text) {
        mFailedDialog.setText(mFailedText);
        mFailedDialog.show();
    }

    public void error() {
        error(mErrorText);
    }

    public void error(String text) {
        mErrorDialog.setText(text);
        mErrorDialog.show();
    }
}
