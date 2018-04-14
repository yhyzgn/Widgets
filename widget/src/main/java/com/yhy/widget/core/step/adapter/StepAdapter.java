package com.yhy.widget.core.step.adapter;

import android.view.View;

import com.yhy.widget.core.step.abs.StepView;
import com.yhy.widget.core.step.entity.StepAble;

import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 11:04
 * version: 1.0.0
 * desc   :
 */
public abstract class StepAdapter<T extends StepAble> {

    protected List<T> mDataList;

    protected StepView<T> mView;

    private OnDataChangedListener mListener;

    public StepAdapter(List<T> dataList) {
        mDataList = dataList;
    }

    public int getCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    public abstract View getItem(StepView<T> stepView, int position, T data);

    public void bindStepView(StepView<T> stepView) {
        mView = stepView;
        mListener = mView;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void notifiedDataChanged() {
        mListener.onDataChanged();
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }
}
