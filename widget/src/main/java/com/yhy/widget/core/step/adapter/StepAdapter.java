package com.yhy.widget.core.step.adapter;

import android.view.View;

import com.yhy.widget.core.step.StepView;
import com.yhy.widget.core.step.entity.StepAble;

import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 11:04
 * version: 1.0.0
 * desc   : StepView适配器
 */
public abstract class StepAdapter<T extends StepAble> {
    // 数据集
    protected List<T> mDataList;
    // 当前适配器所属的StepView
    protected StepView<T> mView;
    // 数据改变监听
    private OnDataChangedListener mListener;

    /**
     * 构造函数
     *
     * @param dataList 数据集
     */
    public StepAdapter(List<T> dataList) {
        mDataList = dataList;
    }

    /**
     * 数据集数量
     *
     * @return 数据集数量
     */
    public int getCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    /**
     * 获取每个条目的view
     *
     * @param stepView 当前StepView
     * @param position 索引
     * @param data     索引对应的数据
     * @return itemView
     */
    public abstract View getItem(StepView<T> stepView, int position, T data);

    /**
     * 绑定StepView
     *
     * @param stepView 设置了改适配器的StepView
     */
    public void bindStepView(StepView<T> stepView) {
        mView = stepView;
        mListener = mView;
    }

    /**
     * 获取数据集
     *
     * @return 数据集
     */
    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 刷新数据到页面
     */
    public void notifiedDataChanged() {
        mListener.onDataChanged();
    }

    /**
     * 数据发生改变的监听器
     */
    public interface OnDataChangedListener {
        /**
         * 当数据改变时回调
         */
        void onDataChanged();
    }
}
