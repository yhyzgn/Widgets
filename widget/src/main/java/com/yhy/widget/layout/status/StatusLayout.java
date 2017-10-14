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
import com.yhy.widget.utils.ViewUtils;

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

    // 加载中
    private static final String STATUS_LOADING = "loading";
    // 成功
    private static final String STATUS_SUCCESS = "success";
    // 错误
    private static final String STATUS_ERROR = "error";
    // 无数据
    private static final String STATUS_EMPTY = "empty";
    // 点击重试
    public static final String TAG_RETRY = "retry";

    // 各状态界面
    private View vLoading;
    private View vSuccess;
    private View vError;
    private View vEmpty;

    // 各状态在布局文件中的界面
    private View vLayoutLoading;
    private View vLayoutError;
    private View vLayoutEmpty;

    // 各状态默认的界面
    private View vDefLoading;
    private View vDefError;
    private View vDefEmpty;

    // 页面助手
    private StaLayoutHelper mHelper;
    // 默认的页面助手
    private StaLayoutHelper mDefHelper;

    // 点击重试事件
    private OnStatusRetryListener mListener;

    // 页面当前状态
    private Status mStatus;
    // 用于更新页面的Handler
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
        mDefHelper = DefLayoutHelper.create(this);

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
            throw new IllegalStateException("StateLayout must has one child for success status at least and four children at most.");
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
                        vLayoutLoading = temp;
                    } else if (TextUtils.equals(Status.SUCCESS.getStatus(), tag)) {
                        // 成功
                        vSuccess = temp;
                    } else if (TextUtils.equals(Status.ERROR.getStatus(), tag)) {
                        // 错误
                        vLayoutError = temp;
                        // 设置点击重试事件
                        setRetryListener(vLayoutError);
                    } else if (TextUtils.equals(Status.EMPTY.getStatus(), tag)) {
                        // 无数据
                        vLayoutEmpty = temp;
                        // 设置点击重试事件
                        setRetryListener(vLayoutEmpty);
                    } else {
                        throw new IllegalStateException("No value matched to tag of " + temp.getClass().getSimpleName());
                    }
                }
            }
        }

        // 刷新界面
        refreshUI();
    }

    /**
     * 设置状态页面助手
     *
     * @param helper 状态页面助手
     * @return 当前对象
     */
    public StatusLayout setHelper(StaLayoutHelper helper) {
        mHelper = helper;
        // 刷新界面
        refreshUI();
        return this;
    }

    /**
     * 设置重试点击事件
     *
     * @param listener 重试点击事件
     * @return 当前对象
     */
    public StatusLayout setOnStatusRetryListener(OnStatusRetryListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 获取重试点击事件
     *
     * @return 重试点击事件
     */
    public OnStatusRetryListener getOnStatusRetryListener() {
        return mListener;
    }

    /**
     * 获取当前页面状态
     *
     * @return 当前页面状态
     */
    public Status getCurrentStatus() {
        return mStatus;
    }

    /**
     * 获取[加载中]状态界面
     *
     * @return [加载中]状态界面
     */
    public View getLoadingView() {
        return vLoading;
    }

    /**
     * 获取[成功]状态界面
     *
     * @return [成功]状态界面
     */
    public View getSuccessView() {
        return vSuccess;
    }

    /**
     * 获取[错误]状态界面
     *
     * @return [错误]状态界面
     */
    public View getErrorView() {
        return vError;
    }

    /**
     * 获取[无数据]状态界面
     *
     * @return [无数据]状态界面
     */
    public View getEmptyView() {
        return vEmpty;
    }

    /**
     * 显示[加载中]界面
     */
    public void showLoading() {
        mStatus = mHandler.showLoading();
    }

    /**
     * 显示[成功]界面
     */
    public void showSuccess() {
        mStatus = mHandler.showSuccess();
    }

    /**
     * 显示[错误]界面
     */
    public void showError() {
        mStatus = mHandler.showError();
    }

    /**
     * 显示[无数据]界面
     */
    public void showEmpty() {
        mStatus = mHandler.showEmpty();
    }

    /**
     * 设置各状态点击重试事件
     *
     * @param statusView 状态界面
     */
    private void setRetryListener(View statusView) {
        View retry = statusView.findViewWithTag(TAG_RETRY);
        if (null != retry) {
            retry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        showLoading();
                        mListener.onRetry();
                    }
                }
            });
        }
    }

    /**
     * 刷新界面
     * <p>
     * 自动检查各状态界面设置情况，并设置使用默认的界面
     */
    private void refreshUI() {
        // 检查并设置默认的界面
        loadViews();
        // 将界面添加到整个页面中
        addViews();
        // 刷新改变页面状态的Handler
        mHandler.setLayout(this);
    }

    /**
     * 检查并设置默认的界面
     */
    private void loadViews() {
        /*
            页面加载的优先级：
                布局 > mHelper > 默认mDefHelper
            给方法可能会被触发两次：
                第一次：
                    onFinishInflate()方法中调用，此时mHelper为空，没有在布局中设置的状态界面将从默认mDefHelper中获取
                第二次：
                    如果外界手动设置了mHelper，将会再次触发该方法，此时就需要判断是否第一次触发时状态是否设置了对应的默认页面，如果设置了，就替换为mHelper中对应的页面，最后再检查页面设置情况，mHelper中也没设置的还是从默认mDefHelper中获取
         */

        // 布局文件中的界面优先使用
        if (null != vLayoutLoading) {
            vLoading = vLayoutLoading;
        }
        if (null != vLayoutError) {
            vError = vLayoutError;
        }
        if (null != vLayoutEmpty) {
            vEmpty = vLayoutEmpty;
        }

        // 再检查并从助手中获取各个状态的界面
        if (null != mHelper) {
            if (null != vDefLoading) {
                // 先将原来的设置的默认页面移除
                removeView(vLoading);
                vLoading = mHelper.getLoadingView();
            }
            if (null != vDefError) {
                removeView(vError);
                vError = mHelper.getErrorView();
            }
            if (null != vDefEmpty) {
                removeView(vEmpty);
                vEmpty = mHelper.getEmptyView();
            }
        }

        // 最后使用默认界面
        // 如果页面不全并且没有默认页面的话，直接抛出异常
        if ((null == vLoading || null == vError || null == vEmpty) && null == mDefHelper) {
            throw new IllegalStateException("Must set views of loading, error and empty status.");
        } else {
            if (null == vLoading) {
                vDefLoading = mDefHelper.getLoadingView();
                vLoading = vDefLoading;
            }
            if (null == vError) {
                vDefError = mDefHelper.getErrorView();
                vError = vDefError;
            }
            if (null == vEmpty) {
                vDefEmpty = mDefHelper.getEmptyView();
                vEmpty = vDefEmpty;
            }
        }
    }

    /**
     * 添加各状态界面到整个页面中
     */
    private void addViews() {
        // 添加之前需要移除各view对应的parent
        addView(ViewUtils.removeParent(vLoading));
        addView(ViewUtils.removeParent(vError));
        addView(ViewUtils.removeParent(vEmpty));
    }

    /**
     * 四种页面状态的枚举类型
     */
    public enum Status {
        LOADING(2001, STATUS_LOADING), SUCCESS(2002, STATUS_SUCCESS), ERROR(2003, STATUS_ERROR), EMPTY(2004, STATUS_EMPTY);

        // 状态码，当更新界面状态时，作为Handler中msg的what值
        int code;
        // 状态信息，所有状态的页面均有该字段识别
        String status;

        /**
         * 构造函数
         *
         * @param code   状态码
         * @param status 状态信息
         */
        Status(int code, String status) {
            this.code = code;
            this.status = status;
        }

        /**
         * 获取状态码
         *
         * @return 状态码
         */
        public int getCode() {
            return code;
        }

        /**
         * 获取状态信息
         *
         * @return 状态信息
         */
        public String getStatus() {
            return status;
        }
    }
}
