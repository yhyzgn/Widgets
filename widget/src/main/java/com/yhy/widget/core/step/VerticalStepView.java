package com.yhy.widget.core.step;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yhy.widget.R;
import com.yhy.widget.core.step.abs.StepView;
import com.yhy.widget.core.step.adapter.StepAdapter;
import com.yhy.widget.core.step.entity.StepAble;
import com.yhy.widget.core.step.indicator.VerticalIndicator;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 10:58
 * version: 1.0.0
 * desc   :
 */
public class VerticalStepView<T extends StepAble> extends LinearLayout implements StepView<T> {

    private VerticalIndicator<T> viIndicator;
    private LinearLayout llStep;
    private StepAdapter<T> mAdapter;
    private OnItemClickListener<T> mOnItemClickListener;

    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_step_view_vertical, this);
        viIndicator = view.findViewById(R.id.vi_indicator);
        llStep = view.findViewById(R.id.ll_step);
    }

    public void setAdapter(StepAdapter<T> adapter) {
        mAdapter = adapter;
        mAdapter.bindStepView(this);
        viIndicator.setupStepView(this);
        refresh();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    private void refresh() {
        if (null == mAdapter) {
            return;
        }

        checkData();

        llStep.removeAllViews();
        int stepCount = mAdapter.getCount();
        View item;
        for (int i = 0; i < stepCount; i++) {
            item = mAdapter.getItem(this, i, mAdapter.getDataList().get(i));

            if (null != item) {
                final int index = i;
                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnItemClickListener) {
                            mOnItemClickListener.onItemClick(VerticalStepView.this, index, mAdapter.getDataList().get(index));
                        }
                    }
                });
                llStep.addView(item);
            }
        }
    }

    private void checkData() {
        int attentionCount = attentionCount();
        if (attentionCount > 1) {
            // 最多只能包含一条“注意”状态的条目
            throw new IllegalStateException("The number of attention item is one in '" + this + "' at most.");
        }

        StepAble first = mAdapter.getDataList().get(0);
        StepAble last = mAdapter.getDataList().get(mAdapter.getDataList().size() - 1);

        if (first.getStatus() == StepAble.Status.COMPLETE && last.getStatus() == StepAble.Status.COMPLETE) {
            // 首尾均已完成
        } else if ((first.getStatus() == StepAble.Status.DEFAULT || first.getStatus() == StepAble.Status.ATTENTION) && last.getStatus() == StepAble.Status.COMPLETE) {
            // 逆序
            viIndicator.reverse(true);
        } else if (first.getStatus() == StepAble.Status.COMPLETE && (last.getStatus() == StepAble.Status.DEFAULT || last.getStatus() == StepAble.Status.ATTENTION)) {
            // 正序
            viIndicator.reverse(false);
        }
    }

    private int attentionCount() {
        int count = 0;
        for (StepAble step : mAdapter.getDataList()) {
            if (step.getStatus() == StepAble.Status.ATTENTION) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 刷新指示器
        viIndicator.onDataChanged();
    }

    @Override
    public final void onDataChanged() {
        refresh();
    }

    @Override
    public StepAdapter<T> getAdapter() {
        return mAdapter;
    }

    @Override
    public View getStepItem(int position) {
        return llStep.getChildAt(position);
    }
}
