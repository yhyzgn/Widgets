package com.yhy.widget.layout.flow.tag;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-09 9:06
 * version: 1.0.0
 * desc   : 标签流式布局适配器
 */
public abstract class TagFlowAdapter<T> {
    // 数据源
    private List<T> mDataList;
    // 当前选中的数据集合
    private List<T> mCheckedList;
    // 数据改变监听器
    private OnDataChangedListener mListener;

    /**
     * 构造函数
     *
     * @param dataList 数据源
     */
    public TagFlowAdapter(List<T> dataList) {
        mDataList = dataList;
        mCheckedList = new ArrayList<>();
    }

    /**
     * 获取数据源集合大小
     *
     * @return 数据源集合大小
     */
    public int getCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    /**
     * 从数据源中获取每一条数据
     *
     * @param position 当前索引
     * @return 对应数据
     */
    public T getItem(int position) {
        return null != mDataList ? mDataList.get(position) : null;
    }

    /**
     * 获取每条标签对应的view
     *
     * @param parent   父容器
     * @param position 当前索引
     * @param data     对应数据
     * @return 显示该条数据的view
     */
    public abstract View getView(TagFlowLayout parent, int position, T data);

    /**
     * 刷新适配器数据
     */
    public void notifyDataChanged() {
        if (null != mListener) {
            mListener.onDataChanged();
        }
    }

    /**
     * 获取已选中数据列表
     *
     * @return 已选中数据列表
     */
    public List<T> getCheckedList() {
        return mCheckedList;
    }

    /**
     * 设置默认选中数据列表
     *
     * @param dataList 选中的列表
     */
    public void setCheckedList(List<T> dataList) {
        if (null == dataList) {
            return;
        }
        mCheckedList.clear();
        mCheckedList.addAll(dataList);
    }

    /**
     * 按索引设置默认选中
     *
     * @param position 索引
     */
    public void setChecked(int... position) {
        if (null == position) {
            return;
        }

        mCheckedList.clear();
        for (int index : position) {
            mCheckedList.add(getItem(index));
        }
    }

    /**
     * 根据具体情况设置当前tag是否选中
     *
     * @param position 当前索引
     * @param data     对应数据
     * @return 是否应该选中
     */
    public boolean isChecked(int position, T data) {
        return false;
    }

    /**
     * 设置数据改变监听器
     *
     * @param listener 数据改变监听器
     */
    public void setOnDataChangedListener(OnDataChangedListener listener) {
        mListener = listener;
    }

    /**
     * 数据改变监听器
     */
    public interface OnDataChangedListener {
        /**
         * 数据改变时回调
         */
        void onDataChanged();
    }
}
