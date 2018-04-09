package com.yhy.widget.layout.flow.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yhy.widget.R;
import com.yhy.widget.layout.flow.FlowLayout;
import com.yhy.widget.utils.DensityUtils;
import com.yhy.widget.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-09 9:04
 * version: 1.0.0
 * desc   : 标签流式布局
 */
public class TagFlowLayout<T> extends FlowLayout implements TagFlowAdapter.OnDataChangedListener {
    // 最大选中数量，默认为-1，无限
    private int mMaxCount;
    // 是否单选，默认false，多选
    private boolean mIsSingle;
    // 单选模式，默认radio，选中后不能点击取消
    private int mSingleMode;
    // 适配器
    private TagFlowAdapter<T> mAdapter;
    // 所有tag包装后的集合
    private List<TagContainer> mTagContainerList;
    // 所有选中tag的索引集合
    private List<Integer> mCheckedList;
    // 状态改变监听器
    private OnCheckChangedListener<T> mListener;

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     * @param attrs   属性集
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mMaxCount = ta.getInt(R.styleable.TagFlowLayout_tfl_max_count, -1);
        mIsSingle = ta.getBoolean(R.styleable.TagFlowLayout_tfl_is_single, false);
        mSingleMode = ta.getInt(R.styleable.TagFlowLayout_tfl_single_mode, 0);
        ta.recycle();

        mTagContainerList = new ArrayList<>();
        mCheckedList = new ArrayList<>();
    }

    /**
     * 测量控件
     *
     * @param widthMeasureSpec  宽度参考值
     * @param heightMeasureSpec 高度参考值
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        TagContainer tc;
        // 遍历判断所有标签，把改隐藏的隐藏
        for (int i = 0; i < getChildCount(); i++) {
            tc = (TagContainer) getChildAt(i);
            if (tc.getVisibility() == GONE) {
                continue;
            }
            if (tc.getTagView().getVisibility() == GONE) {
                tc.setVisibility(GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(TagFlowAdapter<T> adapter) {
        mAdapter = adapter;
        if (null != mAdapter) {
            mAdapter.setOnDataChangedListener(this);
        }
        // 重置所有TagView，并刷新适配器
        mTagContainerList.clear();
        mCheckedList.clear();
        refreshAdapter();
    }

    /**
     * 刷新适配器
     */
    private void refreshAdapter() {
        removeAllViews();
        if (null != mAdapter) {
            MarginLayoutParams params;

            // 将每个TagView添加到页面上
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View tagView = mAdapter.getView(this, i, mAdapter.getItem(i));
                tagView.setDuplicateParentStateEnabled(true);

                TagContainer container = new TagContainer(getContext());
                if (null != tagView.getLayoutParams()) {
                    container.setLayoutParams(tagView.getLayoutParams());
                } else {
                    params = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(DensityUtils.dp2px(getContext(), 4), DensityUtils.dp2px(getContext(), 4), DensityUtils.dp2px(getContext(), 4), DensityUtils.dp2px(getContext(), 4));
                    container.setLayoutParams(params);
                }
                params = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                tagView.setLayoutParams(params);
                tagView.setClickable(false);
                ViewUtils.removeParent(tagView);
                container.addView(tagView);
                addView(container);

                mTagContainerList.add(container);

                // 刷新时选中本来已经选中的tag
                if (mAdapter.getCheckedList().contains(mAdapter.getItem(i))) {
                    container.setChecked(true);
                }

                // 选中适配器中手动设置的tag
                if (mAdapter.isChecked(i, mAdapter.getItem(i))) {
                    container.setChecked(true);
                }
            }

            // 给每个tagView添加单击事件，用来触发状态改变
            for (int i = 0; i < mTagContainerList.size(); i++) {
                final TagContainer container = mTagContainerList.get(i);
                final int position = i;

                container.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mAdapter) {
                            TagContainer tagView = (TagContainer) v;
                            T data = mAdapter.getItem(position);

                            if (mIsSingle) {
                                //  单选
                                mAdapter.getCheckedList().clear();
                                mCheckedList.clear();
                                for (TagContainer tc : mTagContainerList) {
                                    if (tc != tagView) {
                                        // 把其它tagView设置为未选中
                                        tc.setChecked(false);
                                    } else if (mSingleMode == 0) {
                                        // 如果是radio风格的单选，并且当前点击的tagView不是已经选中的tagView，才改变状态
                                        if (!tagView.isChecked()) {
                                            tc.setChecked(true);
                                            mAdapter.getCheckedList().add(data);
                                            mCheckedList.add(position);
                                            // 回调外部接口
                                            if (null != mListener) {
                                                mListener.onChanged(tagView.isChecked(), position, data, mAdapter.getCheckedList());
                                            }
                                        }
                                    } else {
                                        tc.setChecked(!tagView.isChecked());
                                        if (tagView.isChecked()) {
                                            mAdapter.getCheckedList().add(data);
                                            mCheckedList.add(position);
                                        } else {
                                            mAdapter.getCheckedList().remove(data);
                                            mCheckedList.remove((Integer) position);
                                        }
                                        // 回调外部接口
                                        if (null != mListener) {
                                            mListener.onChanged(tagView.isChecked(), position, data, mAdapter.getCheckedList());
                                        }
                                    }
                                }
                            } else {
                                // 多选
                                // 这里 isChecked() 返回的是点击时还未改变的状态
                                if (mMaxCount > -1 && mAdapter.getCheckedList().size() >= mMaxCount && !tagView.isChecked()) {
                                    // 达到最大限制，不允许再继续选
                                    return;
                                }
                                // 切换状态
                                tagView.toggle();

                                // 这里 isChecked() 返回的是改变后的新状态
                                if (tagView.isChecked() && !mAdapter.getCheckedList().contains(data)) {
                                    mAdapter.getCheckedList().add(data);
                                    mCheckedList.add(position);
                                } else if (!tagView.isChecked() && mAdapter.getCheckedList().contains(data)) {
                                    mAdapter.getCheckedList().remove(data);
                                    mCheckedList.remove((Integer) position);
                                }
                                // 回调外部接口
                                if (null != mListener) {
                                    mListener.onChanged(tagView.isChecked(), position, data, mAdapter.getCheckedList());
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 当数据改变刷新适配器时回调
     */
    @Override
    public void onDataChanged() {
        mTagContainerList.clear();
        mCheckedList.clear();
        refreshAdapter();
    }

    /**
     * 获取选择结果集
     *
     * @return 选择结果集
     */
    public List<T> getCheckedList() {
        return null != mAdapter ? mAdapter.getCheckedList() : null;
    }

    /**
     * dp转换成px
     *
     * @param dpValue dp
     * @return px
     */
    private int dp2px(float dpValue) {
        return DensityUtils.dp2px(getContext(), dpValue);
    }

    /**
     * 保存页面状态
     *
     * @return 页面状态参数
     */
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("key_default", super.onSaveInstanceState());
        StringBuffer sb = new StringBuffer();
        for (int index : mCheckedList) {
            sb.append("|").append(index);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        bundle.putString("checked_index", sb.toString());
        return bundle;
    }

    /**
     * 恢复页面状态
     *
     * @param state 页面状态参数
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String checkedIndex = bundle.getString("checked_index");
            if (!TextUtils.isEmpty(checkedIndex) && null != mAdapter) {
                mCheckedList.clear();
                mAdapter.getCheckedList().clear();

                String[] indexArr = checkedIndex.split("\\|");
                for (String index : indexArr) {
                    int pos = Integer.valueOf(index);
                    mCheckedList.add(pos);
                    mAdapter.getCheckedList().add(mAdapter.getItem(pos));
                }
                refreshAdapter();
            }
            super.onRestoreInstanceState(bundle.getParcelable("key_default"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * 设置状态改变监听器
     *
     * @param listener 状态改变监听
     */
    public void setOnCheckChangedListener(OnCheckChangedListener<T> listener) {
        mListener = listener;
    }

    /**
     * 状态改变监听
     *
     * @param <T> tag数据类型
     */
    public interface OnCheckChangedListener<T> {

        /**
         * 当状态改变时回调
         *
         * @param checked  是否被选中
         * @param position 当前索引
         * @param data     当前tag
         * @param dataList 已被选中的tag数据集合
         */
        void onChanged(boolean checked, int position, T data, List<T> dataList);
    }
}
