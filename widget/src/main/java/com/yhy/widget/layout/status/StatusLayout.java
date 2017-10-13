package com.yhy.widget.layout.status;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.yhy.widget.layout.status.handler.StaHandler;
import com.yhy.widget.layout.status.helper.DefLayoutHelper;
import com.yhy.widget.layout.status.helper.StaLayoutHelper;
import com.yhy.widget.layout.status.listener.OnStatusRetryListener;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 11:38
 * version: 1.0.0
 * desc   : 包含各种状态的页面布局 [加载中][成功][错误][无数据]
 */
public class StatusLayout extends FrameLayout {
    /*
        四种状态的tag值（与四种状态对应）：
            loading -> 加载中
            success -> 成功
            error   -> 错误
            empty   -> 无数据

        点击重试按钮的tag值：
            retry   -> 点击重试
     */
    private static final String STATUS_LOADING = "loading";
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_ERROR = "error";
    private static final String STATUS_EMPTY = "empty";

    public static final String TAG_RETRY = "retry";

    private View vLoading;
    private View vSuccess;
    private View vError;
    private View vEmpty;

    private View vErrorRetry;
    private View vEmptyRetry;

    private StaLayoutHelper mHelper;
    private StaLayoutHelper mDefHelper;

    private OnStatusRetryListener mListener;

    private Status mStatus;
    private StaHandler mHandler;

    public StatusLayout(@NonNull Context context) {
        this(context, null);
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 创建默认的状态布局
        mDefHelper = DefLayoutHelper.getInstance().init(this).getHelper();

        // 默认为加载中状态
        mStatus = Status.LOADING;
        mHandler = new StaHandler();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        // childCount必须在1-4之间
        if (childCount < 1 || childCount > 4) {
            throw new IllegalStateException("StateLayout must has one child for success status at least or four children at most.");
        }

        if (childCount == 1) {
            // 只包含一个成功状态的view
            vSuccess = getChildAt(0);
        } else {
            // 如果不止一个子view，就按tag值来匹配状态类型
            View temp;
            String tag;
            for (int i = 0; i < childCount; i++) {
                temp = getChildAt(i);
                if (null != temp.getTag()) {
                    tag = (String) temp.getTag();
                    if (TextUtils.equals(Status.LOADING.getStatus(), tag)) {
                        // 加载中
                        vLoading = temp;
                    } else if (TextUtils.equals(Status.SUCCESS.getStatus(), tag)) {
                        // 成功
                        vSuccess = temp;
                    } else if (TextUtils.equals(Status.ERROR.getStatus(), tag)) {
                        // 错误
                        vError = temp;
                        vErrorRetry = vError.findViewWithTag(TAG_RETRY);
                        if (null != vErrorRetry) {
                            vErrorRetry.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (null != mListener) {
                                        showLoading();
                                        mListener.onRetry();
                                    }
                                }
                            });
                        }
                    } else if (TextUtils.equals(Status.EMPTY.getStatus(), tag)) {
                        // 无数据
                        vEmpty = temp;
                        vEmptyRetry = vEmpty.findViewWithTag(TAG_RETRY);
                        if (null != vEmptyRetry) {
                            vEmptyRetry.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (null != mListener) {
                                        showLoading();
                                        mListener.onRetry();
                                    }
                                }
                            });
                        }
                    } else {
                        throw new IllegalStateException("No value matched to tag of " + temp.getClass().getSimpleName());
                    }
                }
            }
        }

        mHandler.setLayout(this);
    }

    public StatusLayout setHelper(StaLayoutHelper helper) {
        if (null != helper) {
            mHelper = helper;

            mHelper.setStaLayout(this);

            if (null == vLoading) {
                vLoading = mHelper.getLoadingView();
            }
            if (null == vError) {
                vError = mHelper.getErrorView();
            }
            if (null == vEmpty) {
                vEmpty = mHelper.getEmptyView();
            }
        }

        if ((null == vLoading || null == vError || null == vEmpty) && null == mDefHelper) {
            throw new IllegalStateException("Must set views of loading, error and empty status.");
        } else {
            if (null == vLoading) {
                vLoading = mDefHelper.getLoadingView();
            }
            if (null == vError) {
                vError = mDefHelper.getErrorView();
            }
            if (null == vEmpty) {
                vEmpty = mDefHelper.getEmptyView();
            }
        }

        mHandler.setLayout(this);
        return this;
    }

    public StatusLayout setOnStatusRetryListener(OnStatusRetryListener listener) {
        mListener = listener;
        return this;
    }

    public OnStatusRetryListener getOnStatusRetryListener() {
        return mListener;
    }

    public Status getCurrentStatus() {
        return mStatus;
    }

    public View getLoadingView() {
        return vLoading;
    }

    public View getSuccessView() {
        return vSuccess;
    }

    public View getErrorView() {
        return vError;
    }

    public View getEmptyView() {
        return vEmpty;
    }

    public void showLoading() {
        mStatus = mHandler.showLoading();
    }

    public void showSuccess() {
        mStatus = mHandler.showSuccess();
    }

    public void showError() {
        mStatus = mHandler.showError();
    }

    public void showEmpty() {
        mStatus = mHandler.showEmpty();
    }

    public enum Status {
        LOADING(2001, STATUS_LOADING), SUCCESS(2002, STATUS_SUCCESS), ERROR(2003, STATUS_ERROR), EMPTY(2004, STATUS_EMPTY);

        int code;
        String status;

        Status(int code, String status) {
            this.code = code;
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        public String getStatus() {
            return status;
        }
    }
}
