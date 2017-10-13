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
    // 当前状态布局
    private StatusLayout mLayout;
    // 上下文对象
    private Context mCtx;

    // 各状态界面
    private View vLoading;
    private View vError;
    private View vEmpty;

    // 点击重试事件
    private OnStatusRetryListener mListener;

    /**
     * 构造函数
     *
     * @param layout 状态布局
     */
    public StaLayoutHelper(StatusLayout layout) {
        mLayout = layout;
        mCtx = mLayout.getContext();
    }

    /**
     * 设置[加载中]页面布局id
     *
     * @param layoutId 页面布局id
     * @return 当前对象
     */
    public StaLayoutHelper setLoadingLayout(@LayoutRes int layoutId) {
        return setLoadingLayout(LayoutInflater.from(mCtx).inflate(layoutId, null));
    }

    /**
     * 设置[加载中]页面布局
     *
     * @param layout 页面布局
     * @return 当前对象
     */
    public StaLayoutHelper setLoadingLayout(View layout) {
        vLoading = layout;
        return this;
    }

    /**
     * 设置[错误]页面布局id
     *
     * @param layoutId 页面布局id
     * @return 当前对象
     */
    public StaLayoutHelper setErrorLayout(@LayoutRes int layoutId) {
        return setErrorLayout(LayoutInflater.from(mCtx).inflate(layoutId, null));
    }

    /**
     * 设置[错误]页面布局
     *
     * @param layout 页面布局
     * @return 当前对象
     */
    public StaLayoutHelper setErrorLayout(View layout) {
        vError = layout;
        setRetryListener(vError);
        return this;
    }

    /**
     * 设置[无数据]页面布局id
     *
     * @param layoutId 页面布局id
     * @return 当前对象
     */
    public StaLayoutHelper setEmptyLayout(@LayoutRes int layoutId) {
        return setEmptyLayout(LayoutInflater.from(mCtx).inflate(layoutId, null));
    }

    /**
     * 设置[无数据]页面布局
     *
     * @param layout 页面布局
     * @return 当前对象
     */
    public StaLayoutHelper setEmptyLayout(View layout) {
        vEmpty = layout;
        setRetryListener(vEmpty);
        return this;
    }

    /**
     * 获取[加载中]界面
     *
     * @return [加载中]界面
     */
    public View getLoadingView() {
        return vLoading;
    }

    /**
     * 获取[错误]界面
     *
     * @return [错误]界面
     */
    public View getErrorView() {
        return vError;
    }

    /**
     * 获取[无数据]界面
     *
     * @return [无数据]界面
     */
    public View getEmptyView() {
        return vEmpty;
    }

    /**
     * 设置各状态点击重试事件
     *
     * @param statusView 状态界面
     */
    private void setRetryListener(View statusView) {
        View retry = statusView.findViewWithTag(StatusLayout.TAG_RETRY);
        if (null != retry) {
            retry.setOnClickListener(new View.OnClickListener() {
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
}
