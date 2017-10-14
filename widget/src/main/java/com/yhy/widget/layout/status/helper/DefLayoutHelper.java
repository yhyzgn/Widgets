package com.yhy.widget.layout.status.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yhy.widget.layout.status.StatusLayout;
import com.yhy.widget.layout.status.view.StaEmptyView;
import com.yhy.widget.layout.status.view.StaErrorView;
import com.yhy.widget.layout.status.view.StaLoadingView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 17:42
 * version: 1.0.0
 * desc   : 默认的状态布局助手构造器
 */
public class DefLayoutHelper {
    // 布局参数
    private static ViewGroup.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    // 私有化构造方法
    private DefLayoutHelper() {
        throw new IllegalStateException("Can not be instantiated.");
    }

    /**
     * 创建默认助手
     *
     * @param layout 当前状态布局
     * @return 默认助手
     */
    public static StaLayoutHelper create(StatusLayout layout) {
        Context ctx = layout.getContext();
        // 创建助手，并配置助手各状态页面
        StaLayoutHelperBuilder builder = new StaLayoutHelperBuilder.Builder(layout).setLoadingLayout(createLoadingView(ctx)).setErrorLayout(createErrorView(ctx)).setEmptyLayout(createEmptyView(ctx)).build();
        return builder.getHelper();
    }

    /**
     * 创建[加载中]界面
     *
     * @param ctx 上下文对象
     * @return [加载中]界面
     */
    private static View createLoadingView(Context ctx) {
        StaLoadingView slv = new StaLoadingView(ctx);
        slv.setLayoutParams(mParams);
        // 配置tag
        slv.setTag(StatusLayout.Status.LOADING.getStatus());
        return slv;
    }

    /**
     * 创建[错误]界面
     *
     * @param ctx 上下文对象
     * @return [错误]界面
     */
    private static View createErrorView(Context ctx) {
        StaErrorView sev = new StaErrorView(ctx);
        sev.setLayoutParams(mParams);
        // 配置tag
        sev.setTag(StatusLayout.Status.ERROR.getStatus());
        return sev;
    }

    /**
     * 创建[无数据]界面
     *
     * @param ctx 上下文对象
     * @return [无数据]界面
     */
    private static View createEmptyView(Context ctx) {
        StaEmptyView sev = new StaEmptyView(ctx);
        sev.setLayoutParams(mParams);
        // 配置tag
        sev.setTag(StatusLayout.Status.EMPTY.getStatus());
        return sev;
    }
}
