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

    /**
     * 私有化构造方法
     *
     * @param context 上下文对象
     */
    private StatusDialogManager(Context context) {
        mContext = context;
    }

    /**
     * 设置上下文对象
     *
     * @param context 上下文对象
     * @return 当前管理器对象
     */
    public static StatusDialogManager with(Context context) {
        return new StatusDialogManager(context);
    }

    /**
     * 设置自定义加载中弹窗
     *
     * @param dialog 加载中弹窗
     * @return 当前管理器对象
     */
    public StatusDialogManager loadingDialog(LoadingDialog dialog) {
        mLoadingDialog = dialog;
        return this;
    }

    /**
     * 设置自定义成功弹窗
     *
     * @param dialog 成功弹窗
     * @return 当前管理器对象
     */
    public StatusDialogManager successDialog(SuccessDialog dialog) {
        mSuccessDialog = dialog;
        return this;
    }

    /**
     * 设置自定义失败弹窗
     *
     * @param dialog 失败弹窗
     * @return 当前管理器对象
     */
    public StatusDialogManager failedDialog(FailedDialog dialog) {
        mFailedDialog = dialog;
        return this;
    }

    /**
     * 设置自定义错误弹窗
     *
     * @param dialog 错误弹窗
     * @return 当前管理器对象
     */
    public StatusDialogManager errorDialog(ErrorDialog dialog) {
        mErrorDialog = dialog;
        return this;
    }

    /**
     * 设置加载中文本
     *
     * @param text 加载中文本
     * @return 当前管理器对象
     */
    public StatusDialogManager loadingText(String text) {
        mLoadingText = text;
        return this;
    }

    /**
     * 设置成功文本
     *
     * @param text 成功文本
     * @return 当前管理器对象
     */
    public StatusDialogManager successText(String text) {
        mSuccessText = text;
        return this;
    }

    /**
     * 设置失败文本
     *
     * @param text 失败文本
     * @return 当前管理器对象
     */
    public StatusDialogManager failedText(String text) {
        mFailedText = text;
        return this;
    }

    /**
     * 设置错误文本
     *
     * @param text 错误文本
     * @return 当前管理器对象
     */
    public StatusDialogManager errorText(String text) {
        mErrorText = text;
        return this;
    }

    /**
     * 设置加载中弹窗是否可取消
     * <p>
     * 仅对【加载中】弹窗有效
     *
     * @param enable 是否可取消，默认true
     * @return 当前管理器对象
     */
    public StatusDialogManager enableLoadingCancel(boolean enable) {
        mEnableLoadingCancel = enable;
        return this;
    }

    /**
     * 设置弹窗显示时间
     * <p>
     * 仅对【成功|失败|错误】三种弹窗有效
     *
     * @param duration 显示时长，默认3s
     * @return 当前管理器对象
     */
    public StatusDialogManager duration(long duration) {
        mDuration = duration;
        return this;
    }

    /**
     * 创建管理器对象
     *
     * @return 管理器对象
     */
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

    /**
     * 显示加载中弹窗
     * <p>
     * 默认文本
     */
    public void loading() {
        loading(mLoadingText);
    }

    /**
     * 显示加载中弹窗
     *
     * @param text 设置文本
     */
    public void loading(String text) {
        if (null != mLoadingDialog) {
            mLoadingDialog.setText(text);
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏加载中弹窗
     * <p>
     * 仅对【加载中】弹窗有效
     */
    public void dismiss() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 显示成功弹窗
     * <p>
     * 默认文本
     */
    public void success() {
        success(mSuccessText);
    }

    /**
     * 显示成功弹窗
     *
     * @param text 设置文本
     */
    public void success(String text) {
        if (null != mSuccessDialog) {
            mSuccessDialog.setText(text);
            mSuccessDialog.show();
        }
    }

    /**
     * 显示失败弹窗
     * <p>
     * 默认文本
     */
    public void failed() {
        failed(mFailedText);
    }

    /**
     * 显示失败弹窗
     *
     * @param text 设置文本
     */
    public void failed(String text) {
        if (null != mFailedDialog) {
            mFailedDialog.setText(mFailedText);
            mFailedDialog.show();
        }
    }

    /**
     * 显示错误弹窗
     * <p>
     * 默认文本
     */
    public void error() {
        error(mErrorText);
    }

    /**
     * 显示错误弹窗
     *
     * @param text 设置文本
     */
    public void error(String text) {
        if (null != mErrorDialog) {
            mErrorDialog.setText(text);
            mErrorDialog.show();
        }
    }
}
