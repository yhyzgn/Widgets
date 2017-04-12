package com.yhy.widget.rv.div;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by HongYi Yan on 2017/4/12 12:49.
 */
public class RvDivider extends RecyclerView.ItemDecoration {
    public static final int TYPE_CONTENT = 12;
    public static final int TYPE_BORDER = 14;
    private Context mCtx;
    private int mDividerWidth;
    private Paint mPaint;
    private int mColor;
    private int mType;

    public RvDivider(Context ctx) {
        this(ctx, 1);
    }

    public RvDivider(Context ctx, int dividerWidth) {
        this(ctx, dividerWidth, android.R.color.background_light);
    }

    public RvDivider(Context ctx, int dividerWidth, @ColorRes int colorResId) {
        this(ctx, dividerWidth, colorResId, TYPE_CONTENT);
    }

    public RvDivider(Context ctx, int dividerWidth, @ColorRes int colorResId, int type) {
        mCtx = ctx;
        mDividerWidth = dividerWidth;
        mColor = mCtx.getResources().getColor(colorResId);
        mType = type;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dividerWidth);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            //线性布局
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int orientation = llm.getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                //水平方向排列，绘制item右侧线条位置
                drawLinearHorizontal(c, parent);
            } else {
                //垂直方向排列，绘制item下边线条位置
                drawLinearVertical(c, parent);
            }
        } else {
            //GridView布局
            drawGrid(c, parent);
        }
    }

    private void drawGrid(Canvas c, RecyclerView parent) {
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        int parentLeft = parent.getPaddingLeft();
        int parentRight = parent.getWidth() - parent.getPaddingRight();

        GridLayoutManager glm = (GridLayoutManager) parent.getLayoutManager();

        int childCount = parent.getChildCount();
        int spanCount = glm.getSpanCount();
        int rowCount = spanCount % childCount == 0 ? childCount / spanCount : childCount /
                spanCount + 1;

        View child = null;
        RecyclerView.LayoutParams params = null;

        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getTop() + params.topMargin;
            int bottom = child.getHeight() - child.getPaddingBottom();
            int left = child.getLeft() + params.leftMargin;
            int right = child.getWidth() - child.getPaddingRight();

            int topX = child.getWidth() + mDividerWidth;
        }
    }

    private void drawLinearHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        View child = null;
        RecyclerView.LayoutParams params = null;
        int left, right;
        for (int i = 0; i < childCount; i++) {
            if (i == childCount - 1 && mType == TYPE_CONTENT) {
                //如果只绘制内容中的分割线，最后一个item就不再绘制右边线
                break;
            }

            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (i == 0 && mType == TYPE_BORDER) {
                //如果要绘制边框边线，第一个item就绘制左边线
                left = child.getLeft() + params.leftMargin;
                right = left + mDividerWidth;
                c.drawRect(left, top, right, bottom, mPaint);
            }

            //绘制其余分割线
            left = child.getRight() + params.rightMargin;
            right = left + mDividerWidth;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawLinearVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        View child = null;
        RecyclerView.LayoutParams params = null;
        int top, bottom;
        for (int i = 0; i < childCount; i++) {
            if (i == childCount - 1 && mType == TYPE_CONTENT) {
                break;
            }

            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (i == 0 && mType == TYPE_BORDER) {
                top = child.getTop() + params.topMargin;
                bottom = top + mDividerWidth;
                c.drawRect(left, top, right, bottom, mPaint);
            }

            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDividerWidth;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            //线性布局
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int orientation = llm.getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                //水平方向排列，空出item右侧线条位置
                outRect.set(0, 0, mDividerWidth, 0);
            } else {
                //垂直方向排列，空出item下边线条位置
                outRect.set(0, 0, 0, mDividerWidth);
            }
        } else if (lm instanceof GridLayoutManager) {
            //GridView布局
            outRect.set(0, 0, mDividerWidth, mDividerWidth);
        } else {
        }
    }
}
