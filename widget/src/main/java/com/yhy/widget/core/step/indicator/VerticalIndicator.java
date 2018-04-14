package com.yhy.widget.core.step.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yhy.widget.core.step.VerticalStepView;
import com.yhy.widget.core.step.adapter.StepAdapter;
import com.yhy.widget.core.step.entity.StepAble;
import com.yhy.widget.core.step.entity.StepNode;
import com.yhy.widget.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 10:57
 * version: 1.0.0
 * desc   :
 */
public class VerticalIndicator<T extends StepAble> extends View implements StepAdapter.OnDataChangedListener {
    private int mWidth = ViewUtils.dp2px(getContext(), 48);
    private int mHeight;
    private float mCenterX = (float) mWidth / 2.0f;
    private float mRadius = ViewUtils.dp2px(getContext(), 8);
    private float mDashLineWidth = ViewUtils.dp2px(getContext(), 1);
    private float mSolidLineWidth = ViewUtils.dp2px(getContext(), 2);
    private boolean mReverse = false;
    private boolean mAlignMiddle = true;

    private VerticalStepView<T> mStepView;
    private StepAdapter<T> mAdapter;

    private final List<StepNode> mNodeList = new ArrayList<>();
    private DashPathEffect mEffects;

    private Paint mLinePaint;
    private Paint mRoundPaint;

    public VerticalIndicator(Context context) {
        this(context, null);
    }

    public VerticalIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#00beaf"));
        mLinePaint.setStyle(Paint.Style.STROKE);

        mRoundPaint = new Paint();
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setColor(Color.WHITE);
        mRoundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            mWidth = Math.min(mWidth, MeasureSpec.getSize(widthMeasureSpec));
        }

        setDimension();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mNodeList.isEmpty()) {
            return;
        }

        drawDashLine(canvas);
        drawSolidLine(canvas);
        drawRound(canvas);
    }

    private void drawRound(Canvas canvas) {
        for (StepNode node : mNodeList) {
            if (node.status == StepAble.Status.COMPLETE) {
                mRoundPaint.setColor(Color.parseColor("#00beaf"));
            } else if (node.status == StepAble.Status.ATTENTION) {
                mRoundPaint.setColor(Color.YELLOW);
            } else {
                mRoundPaint.setColor(Color.GRAY);
            }
            canvas.drawCircle(node.center.x, node.center.y, node.radius, mRoundPaint);
        }
    }

    private void drawSolidLine(Canvas canvas) {
        mLinePaint.setStrokeWidth(mSolidLineWidth);
        mLinePaint.setPathEffect(null);
        drawLine(canvas, false);
    }

    private void drawDashLine(Canvas canvas) {
        mLinePaint.setStrokeWidth(mDashLineWidth);
        mLinePaint.setPathEffect(mEffects);
        drawLine(canvas, true);
    }

    private void drawLine(Canvas canvas, boolean isDash) {
        StepNode start = isDash ? mNodeList.get(0) : mReverse ? attentionStep() : firstCompleteStep();
        StepNode end = isDash ? mNodeList.get(mNodeList.size() - 1) : mReverse ? lastCompleteStep() : attentionStep();

        if (null == start || null == end) {
            return;
        }

        Path path = new Path();
        path.moveTo(start.center.x, start.center.y);
        path.lineTo(end.center.x, end.center.y);

        canvas.drawPath(path, mLinePaint);
    }

    @Override
    public void onDataChanged() {
        if (null != mStepView) {
            mHeight = mStepView.getHeight();
            setDimension();
            refresh();
        }
    }

    public void setupStepView(VerticalStepView<T> view) {
        mStepView = view;
        mAdapter = mStepView.getAdapter();
    }

    public void reverse(boolean reverse) {
        mReverse = reverse;
    }

    private void refresh() {
        if (null == mAdapter) {
            return;
        }

        refreshNodeList();
        invalidate();
    }

    private void refreshNodeList() {
        mNodeList.clear();
        List<T> dataList = mAdapter.getDataList();

        StepAble.Status status;
        StepNode.NodeType type;

        View item;
        StepNode node;
        int height = 0;
        float centerY = 0;
        ViewGroup.MarginLayoutParams params;

        for (int i = 0; i < dataList.size(); i++) {
            status = dataList.get(i).getStatus();
            type = i == 0 ? StepNode.NodeType.START : i == dataList.size() - 1 ? StepNode.NodeType.END : StepNode.NodeType.MIDDLE;

            item = mStepView.getStepItem(i);
            if (null == item) {
                continue;
            }
            params = (ViewGroup.MarginLayoutParams) item.getLayoutParams();

            if (mAlignMiddle) {
                centerY = height + (item.getMeasuredHeight() + params.topMargin + params.bottomMargin) / 2.0f;
            } else {
                centerY = height + mRadius;
            }
            node = new StepNode();
            node.center = new PointF(mCenterX, centerY);
            node.radius = mRadius;
            node.status = status;
            node.nodeType = type;

            mNodeList.add(node);
            height += item.getMeasuredHeight() + params.topMargin + params.bottomMargin;
        }
    }

    private StepNode firstCompleteStep() {
        for (StepNode node : mNodeList) {
            if (node.status == StepAble.Status.COMPLETE) {
                return node;
            }
        }
        return null;
    }

    private StepNode attentionStep() {
        for (StepNode node : mNodeList) {
            if (node.status == StepAble.Status.ATTENTION) {
                return node;
            }
        }
        return null;
    }

    private StepNode lastCompleteStep() {
        for (int i = mNodeList.size() - 1; i >= 0; i--) {
            if (mNodeList.get(i).status == StepAble.Status.COMPLETE) {
                return mNodeList.get(i);
            }
        }
        return null;
    }

    private void setDimension() {
        setMeasuredDimension(mWidth, mHeight);
    }
}
