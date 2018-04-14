package com.yhy.widget.core.step.abs;

import android.view.View;

import com.yhy.widget.core.step.adapter.StepAdapter;
import com.yhy.widget.core.step.entity.StepAble;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 11:07
 * version: 1.0.0
 * desc   :
 */
public interface StepView<T extends StepAble> extends StepAdapter.OnDataChangedListener {

    StepAdapter<T> getAdapter();

    View getStepItem(int position);

    interface OnItemClickListener<T extends StepAble> {

        void onItemClick(StepView<T> parent, int position, T data);
    }
}
