package com.yhy.widget.core.step;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yhy.widget.R;
import com.yhy.widget.core.step.adapter.StepAdapter;
import com.yhy.widget.core.step.entity.StepAble;
import com.yhy.widget.core.step.indicator.StepIndicator;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 10:58
 * version: 1.0.0
 * desc   : 步骤控件
 */
public class StepView<T extends StepAble> extends LinearLayout implements StepAdapter.OnDataChangedListener {

    private LinearLayout llStepRoot;
    private StepIndicator<T> siStepIndicator;
    private LinearLayout llStepContainer;
    // 默认垂直方向
    private StepIndicator.Orientation mOrientation = StepIndicator.Orientation.VERTICAL;
    // 节点圆的半径，默认8dp
    private float mRadius = 8;
    // 虚线宽度，默认1dp
    private float mDashLineWidth = 1;
    // 实线宽度，默认2dp
    private float mSolidLineWidth = 2;
    // 虚线颜色，默认#00beaf
    private int mDashLineColor = Color.parseColor("#00beaf");
    // 实现颜色，默认#00beaf
    private int mSolidLineColor = Color.parseColor("#00beaf");
    // 完成节点颜色，默认#00beaf
    private int mCompleteColor = Color.parseColor("#00beaf");
    // 当前节点颜色，默认#ff7500
    private int mCurrentColor = Color.parseColor("#ff7500");
    // 默认节点颜色，默认#dcdcdc
    private int mDefaultColor = Color.parseColor("#dcdcdc");
    // 完成节点图标
    private Drawable mCompleteIcon;
    // 当前节点图标
    private Drawable mCurrentIcon;
    // 默认节点图标
    private Drawable mDefaultIcon;
    // 节点是否对齐item中间，默认true
    private boolean mAlignMiddle = true;

    private StepAdapter<T> mAdapter;
    private OnItemClickListener<T> mOnItemClickListener;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_step_view, this);
        llStepRoot = view.findViewById(R.id.ll_step_root);
        siStepIndicator = llStepRoot.findViewById(R.id.si_step_indicator);
        llStepContainer = llStepRoot.findViewById(R.id.ll_step_container);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        int orientation = ta.getInt(R.styleable.StepView_sv_orientation, mOrientation.orientation);
        mOrientation = orientation == 0 ? StepIndicator.Orientation.VERTICAL : StepIndicator.Orientation.HORIZONTAL;
        mCompleteColor = ta.getColor(R.styleable.StepView_sv_complete_color, mCompleteColor);
        mCurrentColor = ta.getColor(R.styleable.StepView_sv_current_color, mCurrentColor);
        mDefaultColor = ta.getColor(R.styleable.StepView_sv_default_color, mDefaultColor);
        mDashLineColor = ta.getColor(R.styleable.StepView_sv_dash_line_color, mDashLineColor);
        mSolidLineColor = ta.getColor(R.styleable.StepView_sv_solid_line_color, mSolidLineColor);
        mDashLineWidth = ta.getDimension(R.styleable.StepView_sv_dash_line_width, mDashLineWidth);
        mSolidLineWidth = ta.getDimension(R.styleable.StepView_sv_solid_line_width, mSolidLineWidth);
        mRadius = ta.getDimension(R.styleable.StepView_sv_radius, mRadius);
        mCompleteIcon = ta.getDrawable(R.styleable.StepView_sv_complete_icon);
        mCurrentIcon = ta.getDrawable(R.styleable.StepView_sv_current_icon);
        mDefaultIcon = ta.getDrawable(R.styleable.StepView_sv_default_icon);
        mAlignMiddle = ta.getBoolean(R.styleable.StepView_sv_align_middle, mAlignMiddle);
        ta.recycle();

        setAttrs();
    }

    private void setAttrs() {
        // 根布局，需要水平显示时根布局为垂直布局，反之同理
        llStepRoot.setOrientation(mOrientation == StepIndicator.Orientation.VERTICAL ? HORIZONTAL : VERTICAL);
        // item容器
        llStepContainer.setOrientation(mOrientation == StepIndicator.Orientation.VERTICAL ? VERTICAL : HORIZONTAL);
        // 设置指示器属性
        siStepIndicator
                .orientation(mOrientation)
                .radius(mRadius)
                .completeColor(mCompleteColor)
                .currentColor(mCurrentColor)
                .defaultColor(mDefaultColor)
                .completeIcon(mCompleteIcon)
                .currentIcon(mCurrentIcon)
                .defaultIcon(mDefaultIcon)
                .solidLineColor(mSolidLineColor)
                .dashLineColor(mDashLineColor)
                .solidLineWidth(mSolidLineWidth)
                .dashLineWidth(mDashLineWidth)
                .alignMiddle(mAlignMiddle);
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(StepAdapter<T> adapter) {
        mAdapter = adapter;
        mAdapter.bindStepView(this);
        // 设置指示器对应的StepView
        siStepIndicator.setupStepView(this);
        refresh();
    }

    /**
     * 设置条目点击事件
     *
     * @param listener 条目点击事件
     */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 刷新
     */
    private void refresh() {
        if (null == mAdapter) {
            return;
        }

        checkData();

        llStepContainer.removeAllViews();
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
                            mOnItemClickListener.onItemClick(StepView.this, index, mAdapter.getDataList().get(index));
                        }
                    }
                });
                llStepContainer.addView(item);
            }
        }
    }

    /**
     * 检查数据集
     * <p>
     * 并自动设置指示器是否反转
     */
    private void checkData() {
        int currentCount = currentCount();
        if (currentCount > 1) {
            // 最多只能包含一条“注意”状态的条目
            throw new IllegalStateException("The number of current item is one in '" + this + "' at most.");
        }

        StepAble first = mAdapter.getDataList().get(0);
        StepAble last = mAdapter.getDataList().get(mAdapter.getDataList().size() - 1);

        if (first.getStatus() == StepAble.Status.COMPLETE && last.getStatus() == StepAble.Status.COMPLETE) {
            // 首尾均已完成，检查中间步骤是否均已完成
            for (StepAble step : mAdapter.getDataList()) {
                if (step.getStatus() != StepAble.Status.COMPLETE) {
                    throw new IllegalStateException("The completed StepAble dataList ' " + mAdapter.getDataList() + " ' can not contains unfinished step.");
                }
            }
        } else if ((first.getStatus() == StepAble.Status.DEFAULT || first.getStatus() == StepAble.Status.CURRENT) && last.getStatus() == StepAble.Status.COMPLETE) {
            // 逆序
            siStepIndicator.reverse(true);
        } else if (first.getStatus() == StepAble.Status.COMPLETE && (last.getStatus() == StepAble.Status.DEFAULT || last.getStatus() == StepAble.Status.CURRENT)) {
            // 正序
            siStepIndicator.reverse(false);
        }
    }

    /**
     * 当前状态的数量
     *
     * @return 当前状态的数量
     */
    private int currentCount() {
        int count = 0;
        for (StepAble step : mAdapter.getDataList()) {
            if (step.getStatus() == StepAble.Status.CURRENT) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 刷新指示器
        siStepIndicator.onDataChanged();
    }

    @Override
    public final void onDataChanged() {
        refresh();
    }

    /**
     * 获取适配器
     *
     * @return 适配器
     */
    public StepAdapter<T> getAdapter() {
        return mAdapter;
    }

    /**
     * 获取itemView
     *
     * @param position 索引
     * @return itemView
     */
    public View getStepItem(int position) {
        return llStepContainer.getChildAt(position);
    }

    /**
     * 条目点击事件
     *
     * @param <T> 可步骤化实体类型
     */
    public interface OnItemClickListener<T extends StepAble> {

        /**
         * 当条目被点击时回调
         *
         * @param parent   StepView
         * @param position 索引
         * @param data     索引对应的数据
         */
        void onItemClick(StepView<T> parent, int position, T data);
    }
}
