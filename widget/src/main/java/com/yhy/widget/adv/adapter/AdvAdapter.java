package com.yhy.widget.adv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by HongYi Yan on 2017/4/7 13:43.
 */
public abstract class AdvAdapter<T> {

    protected Context mCtx;
    private LayoutInflater mLayoutInflater;
    protected List<T> mData;

    public AdvAdapter(Context context, List<T> list) {
        mCtx = context;
        mData = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 提供数据集合的大小
     *
     * @return 数据集合的大小
     */
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    /**
     * 获取数据条目
     *
     * @param position 索引
     * @return 具体的一条数据
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 获得 item 根 View
     *
     * @param layoutID item 布局 ID
     * @return item 根 View
     */
    protected View getRootView(int layoutID) {
        return mLayoutInflater.inflate(layoutID, null);
    }

    /**
     * 提供具体的 View
     *
     * @param position view 所在的索引
     * @return 具体的 View
     */
    public abstract View getView(int position);
}
