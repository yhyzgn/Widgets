package com.yhy.widget.layout.status.helper;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.yhy.widget.layout.status.StatusLayout;
import com.yhy.widget.layout.status.listener.OnStatusRetryListener;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 13:50
 * version: 1.0.0
 * desc   : 状态布局助手，用来配置各种状态界面
 */
public class StaLayoutHelper {

    private Context mCtx;
    private int mLoadingLayoutId;
    private int mErrorLayoutId;
    private int mEmptyLayoutId;

    private View vLoading;
    private View vError;
    private View vEmpty;

    private View vErrorRetry;
    private View vEmptyRetry;

    private OnStatusRetryListener mListener;

    private StatusLayout mLayout;

    public StaLayoutHelper(Context ctx) {
        mCtx = ctx;
    }

    public StaLayoutHelper setStaLayout(StatusLayout layout) {
        mLayout = layout;
        return this;
    }

    public StaLayoutHelper setLoadingLayout(@LayoutRes int layoutId) {
        mLoadingLayoutId = layoutId;
        return setLoadingLayout(LayoutInflater.from(mCtx).inflate(mLoadingLayoutId, null));
    }

    public StaLayoutHelper setLoadingLayout(View layout) {
        vLoading = layout;
        return this;
    }

    public StaLayoutHelper setErrorLayout(@LayoutRes int layoutId) {
        mErrorLayoutId = layoutId;
        return setErrorLayout(LayoutInflater.from(mCtx).inflate(mErrorLayoutId, null));
    }

    public StaLayoutHelper setErrorLayout(View layout) {
        vError = layout;
        if (null != vError) {
            vErrorRetry = vError.findViewWithTag(StatusLayout.TAG_RETRY);
            if (null != vErrorRetry) {
                vErrorRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener = mLayout.getOnStatusRetryListener();
                        if (null != mListener) {
                            if (null != mLayout) {
                                mLayout.showLoading();
                            }
                            mListener.onRetry();
                        }
                    }
                });
            }
        }
        return this;
    }

    public StaLayoutHelper setEmptyLayout(@LayoutRes int layoutId) {
        mEmptyLayoutId = layoutId;
        return setEmptyLayout(LayoutInflater.from(mCtx).inflate(mEmptyLayoutId, null));
    }

    public StaLayoutHelper setEmptyLayout(View layout) {
        vEmpty = layout;
        if (null != vEmpty) {
            vEmptyRetry = vEmpty.findViewWithTag(StatusLayout.TAG_RETRY);
            if (null != vEmptyRetry) {
                vEmptyRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener = mLayout.getOnStatusRetryListener();
                        if (null != mListener) {
                            if (null != mLayout) {
                                mLayout.showLoading();
                            }
                            mListener.onRetry();
                        }
                    }
                });
            }
        }
        return this;
    }

    public View getLoadingView() {
        return vLoading;
    }

    public View getErrorView() {
        return vError;
    }

    public View getEmptyView() {
        return vEmpty;
    }
}
