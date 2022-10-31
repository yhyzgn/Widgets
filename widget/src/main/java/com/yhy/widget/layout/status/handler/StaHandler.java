package com.yhy.widget.layout.status.handler;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.yhy.widget.layout.status.StatusLayout;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 14:37
 * version: 1.0.0
 * desc   : 用于更新页面状态的Handler
 */
public class StaHandler {
    // 当前状态布局
    private StatusLayout mLayout;
    // 各状态界面
    private View vLoading;
    private View vSuccess;
    private View vError;
    private View vEmpty;
    // 具体执行操作的 Handler
    private Handler mHandler;
    // 当前页面状态
    private StatusLayout.Status mStatus;

    @SuppressLint("HandlerLeak")
    public StaHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (null != msg) {
                    updateUI(msg.what);
                }
            }
        };
    }

    /**
     * 设置状态布局控件
     *
     * @param layout 布局控件
     */
    public void setLayout(StatusLayout layout) {
        mLayout = layout;
        // 获取到各状态界面
        vLoading = mLayout.getLoadingView();
        vSuccess = mLayout.getSuccessView();
        vError = mLayout.getErrorView();
        vEmpty = mLayout.getEmptyView();
        // 获取到当前页面状态
        mStatus = mLayout.getCurrentStatus();
        // 发送handler更新界面
        sendHandler();
    }

    /**
     * 显示[加载中]界面
     *
     * @return 当前状态
     */
    public StatusLayout.Status showLoading() {
        if (mStatus == StatusLayout.Status.LOADING) {
            return mStatus;
        }

        mStatus = StatusLayout.Status.LOADING;
        sendHandler();
        return mStatus;
    }

    /**
     * 显示[成功]界面
     *
     * @return 当前状态
     */
    public StatusLayout.Status showSuccess() {
        mStatus = StatusLayout.Status.SUCCESS;
        sendHandler();
        return mStatus;
    }

    /**
     * 显示[错误]界面
     *
     * @return 当前状态
     */
    public StatusLayout.Status showError() {
        mStatus = StatusLayout.Status.ERROR;
        sendHandler();
        return mStatus;
    }

    /**
     * 显示[无数据]界面
     *
     * @return 当前状态
     */
    public StatusLayout.Status showEmpty() {
        mStatus = StatusLayout.Status.EMPTY;
        sendHandler();
        return mStatus;
    }

    /**
     * 发送handler，更新界面
     */
    private void sendHandler() {
        // 利用当前页面状态码来发送msg，便于之后控制页面显示与隐藏
        mHandler.obtainMessage(mStatus.getCode()).sendToTarget();
    }

    /**
     * 真正更新页面的操作
     * <p>
     * 根据当前状态码更新界面
     *
     * @param code 当前状态码
     */
    private void updateUI(int code) {
        if (null != vLoading) {
            vLoading.setVisibility(code == StatusLayout.Status.LOADING.getCode() ? View.VISIBLE : View.GONE);
        }
        if (null != vSuccess) {
            vSuccess.setVisibility(code == StatusLayout.Status.SUCCESS.getCode() ? View.VISIBLE : View.GONE);
        }
        if (null != vError) {
            vError.setVisibility(code == StatusLayout.Status.ERROR.getCode() ? View.VISIBLE : View.GONE);
        }
        if (null != vEmpty) {
            vEmpty.setVisibility(code == StatusLayout.Status.EMPTY.getCode() ? View.VISIBLE : View.GONE);
        }
    }
}
