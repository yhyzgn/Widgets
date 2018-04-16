package com.yhy.widget.core.step.indicator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yhy.widget.core.step.StepView;
import com.yhy.widget.core.step.adapter.StepAdapter;
import com.yhy.widget.core.step.entity.StepAble;
import com.yhy.widget.core.step.entity.StepNode;
import com.yhy.widget.utils.DensityUtils;
import com.yhy.widget.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 10:57
 * version: 1.0.0
 * desc   : 步骤指示器
 */
public class StepIndicator<T extends StepAble> extends View implements StepAdapter.OnDataChangedListener {
    // 指示器宽度，默认48dp
    private int mWidth = DensityUtils.dp2px(getContext(), 48);
    // 指示器高度，默认48dp
    private int mHeight = DensityUtils.dp2px(getContext(), 48);
    // 指示器中点x坐标
    private float mCenterX;
    // 指示器中点y坐标
    private float mCenterY;
    // 显示方向，默认垂直方向
    private Orientation mOrientation = Orientation.VERTICAL;
    // 是否反转，默认为false，即正序显示
    private boolean mReverse = false;
    // 是否对齐itemView的中间位置，默认为true；false时则对其itemView的上边或者左边
    private boolean mAlignMiddle = true;
    // 节点圆的半径，默认8dp
    private float mRadius = DensityUtils.dp2px(getContext(), 8);
    // 虚线宽度，默认1dp
    private float mDashLineWidth = DensityUtils.dp2px(getContext(), 1);
    // 实线宽度，默认2dp
    private float mSolidLineWidth = DensityUtils.dp2px(getContext(), 2);
    // 虚线颜色，默认#00beaf
    private int mDashLineColor = Color.parseColor("#00beaf");
    // 实线颜色，默认#00beaf
    private int mSolidLineColor = Color.parseColor("#00beaf");
    // 完成节点圆的颜色，默认#00beaf
    private int mCompleteColor = Color.parseColor("#00beaf");
    // 当前节点圆的颜色，默认#ff7500
    private int mCurrentColor = Color.parseColor("#ff7500");
    // 默认节点圆的颜色，默认#dcdcdc
    private int mDefaultColor = Color.parseColor("#dcdcdc");
    // 完成节点的图标
    private Bitmap mCompleteIcon;
    // 当前节点的图标
    private Bitmap mCurrentIcon;
    // 默认节点的图标
    private Bitmap mDefaultIcon;

    private StepView<T> mStepView;
    private StepAdapter<T> mAdapter;

    private final List<StepNode> mNodeList = new ArrayList<>();
    private DashPathEffect mEffects;

    private Paint mLinePaint;
    private Paint mRoundPaint;

    public StepIndicator(Context context) {
        this(context, null);
    }

    public StepIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mRoundPaint = new Paint();
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mOrientation == Orientation.VERTICAL) {
            // 垂直方向，就测量宽度，高度需要在设置StepView时由其高度决定
            if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
                mWidth = Math.min(mWidth, MeasureSpec.getSize(widthMeasureSpec));
            }
        } else {
            // 水平方向，就测量高度，宽度需要在设置StepView时由其宽度决定
            if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
                mHeight = Math.min(mHeight, MeasureSpec.getSize(heightMeasureSpec));
            }
        }

        // 中点的x，y坐标
        mCenterX = (float) mWidth / 2.0f;
        mCenterY = (float) mHeight / 2.0f;

        // 设置大小
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
        drawRoundIcon(canvas);
    }

    /**
     * 绘制节点图标
     *
     * @param canvas 当前画布
     */
    private void drawRoundIcon(Canvas canvas) {
        Rect src;
        RectF dest;
        Bitmap icon;
        for (StepNode node : mNodeList) {
            icon = null;
            if (null != mCompleteIcon && node.status == StepAble.Status.COMPLETE) {
                icon = mCompleteIcon;
            } else if (null != mCurrentIcon && node.status == StepAble.Status.CURRENT) {
                icon = mCurrentIcon;
            } else if (null != mDefaultIcon) {
                icon = mDefaultIcon;
            }
            if (null == icon) {
                continue;
            }
            src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
            dest = new RectF(node.center.x - mRadius, node.center.y - mRadius, node.center.x + mRadius, node.center.y + mRadius);
            canvas.drawBitmap(icon, src, dest, null);
        }
    }

    /**
     * 绘制节点圆
     *
     * @param canvas 当前画布
     */
    private void drawRound(Canvas canvas) {
        for (StepNode node : mNodeList) {
            if (node.status == StepAble.Status.COMPLETE) {
                mRoundPaint.setColor(mCompleteColor);
            } else if (node.status == StepAble.Status.CURRENT) {
                mRoundPaint.setColor(mCurrentColor);
            } else {
                mRoundPaint.setColor(mDefaultColor);
            }
            canvas.drawCircle(node.center.x, node.center.y, node.radius, mRoundPaint);
        }
    }

    /**
     * 绘制实线
     *
     * @param canvas 当前画布
     */
    private void drawSolidLine(Canvas canvas) {
        mLinePaint.setColor(mSolidLineColor);
        mLinePaint.setStrokeWidth(mSolidLineWidth);
        mLinePaint.setPathEffect(null);
        drawLine(canvas, false);
    }

    /**
     * 绘制虚线
     *
     * @param canvas 当前画布
     */
    private void drawDashLine(Canvas canvas) {
        mLinePaint.setColor(mDashLineColor);
        mLinePaint.setStrokeWidth(mDashLineWidth);
        mLinePaint.setPathEffect(mEffects);
        drawLine(canvas, true);
    }

    /**
     * 画线条
     *
     * @param canvas 当前画布
     * @param isDash 是否时虚线
     */
    private void drawLine(Canvas canvas, boolean isDash) {
        /*
        开始节点：
                虚线： 第一个节点
                实线：
                    正序： 第一个完成节点
                    逆序： 当前状态的节点
         结束节点：
                虚线： 最后一个节点
                实线：
                    正序： 当前状态的节点
                    逆序： 最后一个完成状态的节点
         */
        StepNode start = isDash ? mNodeList.get(0) : mReverse ? currentStep() : firstCompleteStep();
        StepNode end = isDash ? mNodeList.get(mNodeList.size() - 1) : mReverse ? lastCompleteStep() : currentStep();

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
            // 当数据发生改变时被回调
            // 将指示器高度或者宽度与StepView匹配
            if (mOrientation == Orientation.VERTICAL) {
                // 垂直显示，需要匹配高度
                mHeight = mStepView.getHeight();
            } else {
                // 水平显示，需要匹配宽度
                mWidth = mStepView.getWidth();
            }
            // 设置大小
            setDimension();
            // 刷新
            refresh();
        }
    }

    /**
     * 设置指示器对应的StepView
     *
     * @param view StepView
     */
    public void setupStepView(StepView<T> view) {
        mStepView = view;
        mAdapter = mStepView.getAdapter();
    }

    /**
     * 设置显示方向，默认为垂直方向
     *
     * @param orientation 显示方向
     * @return 当前对象
     */
    public StepIndicator<T> orientation(Orientation orientation) {
        mOrientation = orientation;
        return this;
    }

    /**
     * 设置是否反转，默认为false
     *
     * @param reverse 是否反转
     * @return 当前对象
     */
    public StepIndicator<T> reverse(boolean reverse) {
        mReverse = reverse;
        return this;
    }

    /**
     * 设置节点圆的半径，默认为8dp
     *
     * @param dp 半径
     * @return 当前对象
     */
    public StepIndicator<T> radius(float dp) {
        mRadius = DensityUtils.dp2px(getContext(), dp);
        return this;
    }

    /**
     * 设置完成状态节点圆颜色，默认#00feaf
     *
     * @param color 颜色值
     * @return 当前对象
     */
    public StepIndicator<T> completeColor(int color) {
        mCompleteColor = color;
        return this;
    }

    /**
     * 设置完成状态节点圆颜色，默认#00feaf
     *
     * @param resId 颜色资源id
     * @return 当前对象
     */
    public StepIndicator<T> completeColorRes(int resId) {
        return completeColor(getResources().getColor(resId));
    }

    /**
     * 设置当前状态节点圆颜色，默认#ff7500
     *
     * @param color 颜色值
     * @return 当前对象
     */
    public StepIndicator<T> currentColor(int color) {
        mCurrentColor = color;
        return this;
    }

    /**
     * 设置当前状态节点圆颜色，默认#ff7500
     *
     * @param resId 颜色资源id
     * @return 当前对象
     */
    public StepIndicator<T> currentColorRes(int resId) {
        return currentColor(getResources().getColor(resId));
    }

    /**
     * 设置默认状态节点圆颜色，默认#dcdcdc
     *
     * @param color 颜色值
     * @return 当前对象
     */
    public StepIndicator<T> defaultColor(int color) {
        mDefaultColor = color;
        return this;
    }

    /**
     * 设置默认状态节点圆颜色，默认#dcdcdc
     *
     * @param resId 颜色资源id
     * @return 当前对象
     */
    public StepIndicator<T> defaultColorRes(int resId) {
        return defaultColor(getResources().getColor(resId));
    }

    /**
     * 设置完成状态节点图标
     *
     * @param bitmap 图标
     * @return 当前对象
     */
    public StepIndicator<T> completeIcon(Bitmap bitmap) {
        mCompleteIcon = bitmap;
        return this;
    }

    /**
     * 设置完成状态节点图标
     *
     * @param drawable 图标
     * @return 当前对象
     */
    public StepIndicator<T> completeIcon(Drawable drawable) {
        return completeIcon(ViewUtils.drawableToBitmap(drawable));
    }

    /**
     * 设置完成状态节点图标
     *
     * @param resId 图标资源id
     * @return 当前对象
     */
    public StepIndicator<T> completeIcon(int resId) {
        return completeIcon(getResources().getDrawable(resId));
    }

    /**
     * 设置当前状态节点图标
     *
     * @param bitmap 图标
     * @return 当前对象
     */
    public StepIndicator<T> currentIcon(Bitmap bitmap) {
        mCurrentIcon = bitmap;
        return this;
    }

    /**
     * 设置当前状态节点图标
     *
     * @param drawable 图标
     * @return 当前对象
     */
    public StepIndicator<T> currentIcon(Drawable drawable) {
        return currentIcon(ViewUtils.drawableToBitmap(drawable));
    }

    /**
     * 设置当前状态节点图标
     *
     * @param resId 图标资源id
     * @return 当前对象
     */
    public StepIndicator<T> currentIcon(int resId) {
        return currentIcon(getResources().getDrawable(resId));
    }

    /**
     * 设置默认状态节点图标
     *
     * @param bitmap 图标
     * @return 当前对象
     */
    public StepIndicator<T> defaultIcon(Bitmap bitmap) {
        mDefaultIcon = bitmap;
        return this;
    }

    /**
     * 设置默认状态节点图标
     *
     * @param drawable 图标
     * @return 当前对象
     */
    public StepIndicator<T> defaultIcon(Drawable drawable) {
        return defaultIcon(ViewUtils.drawableToBitmap(drawable));
    }

    /**
     * 设置默认状态节点图标
     *
     * @param resId 图标资源id
     * @return 当前对象
     */
    public StepIndicator<T> defaultIcon(int resId) {
        return defaultIcon(getResources().getDrawable(resId));
    }

    /**
     * 设置虚线颜色，默认为#00beaf
     *
     * @param color 颜色值
     * @return 当前对象
     */
    public StepIndicator<T> dashLineColor(int color) {
        mDashLineColor = color;
        return this;
    }

    /**
     * 设置虚线颜色
     *
     * @param resId 颜色资源id
     * @return 当前对象
     */
    public StepIndicator<T> dashLineColorRes(int resId) {
        return dashLineColor(getResources().getColor(resId));
    }

    /**
     * 设置虚线宽度，默认为1dp
     *
     * @param dp 宽度
     * @return 当前对象
     */
    public StepIndicator<T> dashLineWidth(float dp) {
        mDashLineWidth = DensityUtils.dp2px(getContext(), dp);
        return this;
    }

    /**
     * 设置实线颜色，默认为#00beaf
     *
     * @param color 颜色值
     * @return 当前对象
     */
    public StepIndicator<T> solidLineColor(int color) {
        mSolidLineColor = color;
        return this;
    }

    /**
     * 设置实线颜色
     *
     * @param resId 颜色资源id
     * @return 当前对象
     */
    public StepIndicator<T> solidLineColorRes(int resId) {
        return solidLineColor(getResources().getColor(resId));
    }

    /**
     * 设置实线宽度，默认为2dp
     *
     * @param dp 宽度
     * @return 当前对象
     */
    public StepIndicator<T> solidLineWidth(float dp) {
        mSolidLineWidth = DensityUtils.dp2px(getContext(), dp);
        return this;
    }

    /**
     * 设置是否让节点对齐itemView的中间位置，默认为true
     *
     * @param align 是否对齐
     * @return 当前对象
     */
    public StepIndicator<T> alignMiddle(boolean align) {
        mAlignMiddle = align;
        return this;
    }

    /**
     * 刷新
     */
    private void refresh() {
        if (null == mAdapter) {
            return;
        }

        // 刷新节点列表
        refreshNodeList();

        // 重绘
        invalidate();
    }

    /**
     * 刷新节点列表
     */
    private void refreshNodeList() {
        mNodeList.clear();
        List<T> dataList = mAdapter.getDataList();

        StepAble.Status status;
        StepNode.NodeType type;

        View item;
        StepNode node;
        int width = 0;
        int height = 0;
        float centerX;
        float centerY;
        ViewGroup.MarginLayoutParams params;

        for (int i = 0; i < dataList.size(); i++) {
            status = dataList.get(i).getStatus();
            type = i == 0 ? StepNode.NodeType.START : i == dataList.size() - 1 ? StepNode.NodeType.END : StepNode.NodeType.MIDDLE;

            item = mStepView.getStepItem(i);
            if (null == item) {
                continue;
            }
            params = (ViewGroup.MarginLayoutParams) item.getLayoutParams();

            if (mOrientation == Orientation.VERTICAL) {
                // 垂直显示
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
            } else {
                // 水平显示
                if (mAlignMiddle) {
                    centerX = width + (item.getMeasuredWidth() + params.leftMargin + params.rightMargin) / 2.0f;
                } else {
                    centerX = width + mRadius;
                }
                node = new StepNode();
                node.center = new PointF(centerX, mCenterY);
                node.radius = mRadius;
                node.status = status;
                node.nodeType = type;
                mNodeList.add(node);
                width += item.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
        }
    }

    /**
     * 获取第一个完成状态的节点
     *
     * @return 第一个完成状态的节点
     */
    private StepNode firstCompleteStep() {
        for (StepNode node : mNodeList) {
            if (node.status == StepAble.Status.COMPLETE) {
                return node;
            }
        }
        return null;
    }

    /**
     * 获取当前状态的节点
     *
     * @return 当前状态的节点
     */
    private StepNode currentStep() {
        for (StepNode node : mNodeList) {
            if (node.status == StepAble.Status.CURRENT) {
                return node;
            }
        }
        return null;
    }

    /**
     * 获取最后一个完成状态的节点
     *
     * @return 最后一个完成状态的节点
     */
    private StepNode lastCompleteStep() {
        for (int i = mNodeList.size() - 1; i >= 0; i--) {
            if (mNodeList.get(i).status == StepAble.Status.COMPLETE) {
                return mNodeList.get(i);
            }
        }
        return null;
    }

    /**
     * 设置大小
     */
    private void setDimension() {
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 方向枚举
     */
    public enum Orientation {
        // 垂直，水平
        VERTICAL(0), HORIZONTAL(1);

        // 方向
        public int orientation;

        Orientation(int orientation) {
            this.orientation = orientation;
        }
    }
}
