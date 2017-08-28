package com.yhy.widget.adv.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-08-28 14:05
 * version: 1.0.0
 * desc   :
 */
public abstract class SimpleAdvAdapter<T> extends AdvAdapter<T> {
    // 每组显示条目数量
    private int mGroupSize;
    // 总的分组数目
    private int mGroupCount;
    // 条目点击事件
    private OnItemClickListener<T> mItemClickListener;

    /**
     * 构造函数，默认单条目
     *
     * @param context 上下文对象
     * @param list    数据集合
     */
    public SimpleAdvAdapter(Context context, List<T> list) {
        this(context, list, 1);
    }

    /**
     * 构造函数，多条目
     *
     * @param context   上下文对象
     * @param list      数据集合
     * @param groupSize 每组条目数量
     */
    public SimpleAdvAdapter(Context context, List<T> list, int groupSize) {
        super(context, list);
        // 每组数量
        mGroupSize = groupSize;
        // 分组数量
        mGroupCount = null == mData ? 0 : mData.size() % mGroupSize == 0 ? mData.size() / mGroupSize : mData.size() / mGroupSize + 1;
    }

    /**
     * 获取适配器条目
     *
     * @param position view 所在的索引
     * @return 适配器条目View
     */
    @Override
    public View getView(int position) {
        final View groupView = setGroupItemView(createGorupView(), position);

        return groupView;
    }

    /**
     * 获取适配器条目数量，这里返回的是分组数
     *
     * @return 分组数
     */
    @Override
    public int getCount() {
        return mGroupCount;
    }

    /**
     * 为每个分组创建子条目
     *
     * @param groupView 分组View
     * @param position  分组position
     * @return 创建好子条目的分组View
     */
    private View setGroupItemView(ViewGroup groupView, int position) {
        int currentGroupSize = mGroupSize;
        int lastGroupSize = mData.size() % mGroupSize;
        if (position == (mGroupCount - 1) && lastGroupSize != 0) {
            // 是最后一组，并且最后一组未满
            currentGroupSize = lastGroupSize;
        }

        // 真正的条目position
        View itemView;
        for (int i = 0; i < currentGroupSize; i++) {
            final int index = position * mGroupSize + i;
            // 调用抽象方法从子类获取itemView
            itemView = getItemView(index, mData.get(index));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mItemClickListener) {
                        mItemClickListener.onItemClick(SimpleAdvAdapter.this, index, mData.get(index));
                    }
                }
            });
            groupView.addView(itemView);
        }
        return groupView;
    }

    /**
     * 创建分组View
     *
     * @return 分组View
     */
    private ViewGroup createGorupView() {
        LinearLayout ll = new LinearLayout(mCtx);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return ll;
    }

    /**
     * 获取分组内的每个itemView
     *
     * @param position 真实的条目position
     * @param data     某一条数据
     * @return 分组内的每个itemView
     */
    protected abstract View getItemView(int position, T data);

    /**
     * 设置条目点击事件
     *
     * @param listener 条目点击事件
     */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mItemClickListener = listener;
    }

    /**
     * 条目点击事件监听器
     *
     * @param <T> 数据类型
     */
    public interface OnItemClickListener<T> {

        /**
         * 条目点击回调方法
         *
         * @param adapter  当前适配器
         * @param position 真实的条目position
         * @param data     点击条目的数据
         */
        void onItemClick(SimpleAdvAdapter adapter, int position, T data);
    }
}
