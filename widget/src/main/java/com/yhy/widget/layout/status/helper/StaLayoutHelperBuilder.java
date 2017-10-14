package com.yhy.widget.layout.status.helper;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.yhy.widget.layout.status.StatusLayout;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-14 8:23
 * version: 1.0.0
 * desc   : 状态页面助手生产类
 */
public class StaLayoutHelperBuilder {
    // 助手
    private StaLayoutHelper mHelper;

    /**
     * 构造函数
     *
     * @param builder 构造器
     */
    private StaLayoutHelperBuilder(Builder builder) {
        mHelper = builder.mHelper;
    }

    /**
     * 获取当前创建的助手
     *
     * @return 创建的助手
     */
    public StaLayoutHelper getHelper() {
        return mHelper;
    }

    /**
     * 构造器
     */
    public static class Builder {
        // 助手
        private StaLayoutHelper mHelper;

        /**
         * 构造函数
         *
         * @param layout 当前状态布局
         */
        public Builder(StatusLayout layout) {
            mHelper = new StaLayoutHelper(layout);
        }

        /**
         * 设置[加载中]页面布局id
         *
         * @param layoutId 页面布局id
         * @return 当前对象
         */
        public Builder setLoadingLayout(@LayoutRes int layoutId) {
            mHelper.setLoadingLayout(layoutId);
            return this;
        }

        /**
         * 设置[加载中]页面布局
         *
         * @param layout 页面布局
         * @return 当前对象
         */
        public Builder setLoadingLayout(View layout) {
            mHelper.setLoadingLayout(layout);
            return this;
        }

        /**
         * 设置[错误]页面布局id
         *
         * @param layoutId 页面布局id
         * @return 当前对象
         */
        public Builder setErrorLayout(@LayoutRes int layoutId) {
            mHelper.setErrorLayout(layoutId);
            return this;
        }

        /**
         * 设置[错误]页面布局
         *
         * @param layout 页面布局
         * @return 当前对象
         */
        public Builder setErrorLayout(View layout) {
            mHelper.setErrorLayout(layout);
            return this;
        }

        /**
         * 设置[无数据]页面布局id
         *
         * @param layoutId 页面布局id
         * @return 当前对象
         */
        public Builder setEmptyLayout(@LayoutRes int layoutId) {
            mHelper.setEmptyLayout(layoutId);
            return this;
        }

        /**
         * 设置[无数据]页面布局
         *
         * @param layout 页面布局
         * @return 当前对象
         */
        public Builder setEmptyLayout(View layout) {
            mHelper.setEmptyLayout(layout);
            return this;
        }

        /**
         * 利用构造器创建助手
         *
         * @return 助手构造器
         */
        public StaLayoutHelperBuilder build() {
            return new StaLayoutHelperBuilder(this);
        }
    }
}
