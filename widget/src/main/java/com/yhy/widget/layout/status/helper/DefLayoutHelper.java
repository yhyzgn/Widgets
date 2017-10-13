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
 * desc   :
 */
public class DefLayoutHelper {

    private volatile static DefLayoutHelper instance;

    private StatusLayout mLayout;
    private Context mCtx;

    private StaLayoutHelper mHelper;

    private ViewGroup.LayoutParams mParams;

    private DefLayoutHelper() {
        if (null != instance) {
            throw new IllegalStateException("Can not be instantiate of singleton class.");
        }
        mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static DefLayoutHelper getInstance() {
        if (null == instance) {
            synchronized (DefLayoutHelper.class) {
                if (null == instance) {
                    instance = new DefLayoutHelper();
                }
            }
        }
        return instance;
    }

    public DefLayoutHelper init(StatusLayout layout) {
        mLayout = layout;
        mCtx = mLayout.getContext();
        mHelper = new StaLayoutHelper(mCtx);
        mHelper.setStaLayout(mLayout);
        mHelper.setLoadingLayout(createLoadingView()).setErrorLayout(createErrorView()).setEmptyLayout(createEmptyView());
        return this;
    }

    private View createLoadingView() {
        StaLoadingView slv = new StaLoadingView(mCtx);
        slv.setLayoutParams(mParams);
        slv.setTag(StatusLayout.Status.LOADING.getStatus());
        return slv;
    }

    private View createErrorView() {
        StaErrorView sev = new StaErrorView(mCtx);
        sev.setLayoutParams(mParams);
        sev.setTag(StatusLayout.Status.ERROR.getStatus());
        return sev;
    }

    private View createEmptyView() {
        StaEmptyView sev = new StaEmptyView(mCtx);
        sev.setLayoutParams(mParams);
        sev.setTag(StatusLayout.Status.EMPTY.getStatus());
        return sev;
    }

    public StaLayoutHelper getHelper() {
        return mHelper;
    }
}
