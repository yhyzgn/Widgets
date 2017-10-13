package com.yhy.widget.layout.status.handler;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.yhy.widget.layout.status.StatusLayout;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 14:37
 * version: 1.0.0
 * desc   :
 */
public class StaHandler {
    private StatusLayout mLayout;
    private View vLoading;
    private View vSuccess;
    private View vError;
    private View vEmpty;

    private Handler mHandler;

    private StatusLayout.Status mStatus;

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

    public StatusLayout.Status showLoading() {
        if (mStatus == StatusLayout.Status.LOADING) {
            return mStatus;
        }

        mStatus = StatusLayout.Status.LOADING;
        sendHandler();
        return mStatus;
    }

    public StatusLayout.Status showSuccess() {
        mStatus = StatusLayout.Status.SUCCESS;
        sendHandler();
        return mStatus;
    }

    public StatusLayout.Status showError() {
        mStatus = StatusLayout.Status.ERROR;
        sendHandler();
        return mStatus;
    }

    public StatusLayout.Status showEmpty() {
        mStatus = StatusLayout.Status.EMPTY;
        sendHandler();
        return mStatus;
    }

    public void setLayout(StatusLayout layout) {
        mLayout = layout;

        vLoading = mLayout.getLoadingView();
        vSuccess = mLayout.getSuccessView();
        vError = mLayout.getErrorView();
        vEmpty = mLayout.getEmptyView();

        mStatus = mLayout.getCurrentStatus();
        sendHandler();
    }

    private void sendHandler() {
        mHandler.obtainMessage(mStatus.getCode()).sendToTarget();
    }

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
