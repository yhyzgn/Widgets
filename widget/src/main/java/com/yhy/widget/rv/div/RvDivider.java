package com.yhy.widget.rv.div;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yhy.widget.utils.DensityUtils;

/**
 * Created by HongYi Yan on 2017/4/12 12:49.
 */
public class RvDivider extends RecyclerView.ItemDecoration {
    //绘制方式，默认 只绘制内容区域，边界不绘制
    private static final int DIVIDER_TYPE = DividerType.TYPE_CENTER;
    //线条宽度，默认是 0.5dp
    private static final float DIVIDER_WIDTH = 0.5f;
    //线条颜色，默认是 #dddddd
    private static final int DIVIDER_COLOR = 0xffdddddd;
    //上下文对象
    private Context mCtx;
    //绘制方式
    private int mType;
    //线条宽度
    private int mWidthPx;
    //线条颜色
    private int mColor;
    //绘制线条的画笔
    private Paint mPaint;

    /**
     * 构造函数
     *
     * @param ctx 上下文对象
     */
    public RvDivider(Context ctx) {
        this(ctx, DIVIDER_COLOR, DIVIDER_WIDTH, DIVIDER_TYPE);
    }

    /**
     * 构造函数
     *
     * @param ctx   上下文对象
     * @param color 线条颜色
     */
    public RvDivider(Context ctx, int color) {
        this(ctx, color, DIVIDER_WIDTH, DIVIDER_TYPE);
    }

    /**
     * 构造函数
     *
     * @param ctx     上下文对象
     * @param color   线条颜色
     * @param widthDp 线条宽度
     */
    public RvDivider(Context ctx, int color, float widthDp) {
        this(ctx, color, widthDp, DIVIDER_TYPE);
    }

    /**
     * 构造函数
     *
     * @param ctx     上下文对象
     * @param color   线条颜色
     * @param widthDp 线条宽度
     * @param type    绘制类型
     */
    public RvDivider(Context ctx, int color, float widthDp, int type) {
        mCtx = ctx;
        mColor = color;
        mWidthPx = DensityUtils.dp2px(mCtx, widthDp);
        //判断type参数合法性
        if (type != DividerType.TYPE_CENTER && type != DividerType.TYPE_FILL) {
            throw new IllegalArgumentException("The argument ype must be one of the DividerType's member.");
        }
        mType = type;

        //创建画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mWidthPx);
    }

    /**
     * 获取绘制类型
     *
     * @return 线条绘制类型
     */
    public int getType() {
        return mType;
    }

    /**
     * 设置绘制类型
     *
     * @param type 绘制类型
     * @return 当前对象
     */
    public RvDivider setType(int type) {
        this.mType = type;
        return this;
    }

    /**
     * 获取线条宽度，单位dp
     *
     * @return 线条宽度
     */
    public float getWidthDp() {
        return DensityUtils.px2dp(mCtx, mWidthPx);
    }

    /**
     * 设置线条宽度，单位dp
     *
     * @param widthDp 线条宽度
     * @return 当前对象
     */
    public RvDivider setWidthDp(float widthDp) {
        this.mWidthPx = DensityUtils.dp2px(mCtx, widthDp);
        return this;
    }

    /**
     * 获取线条颜色
     *
     * @return 线条颜色
     */
    public int getColor() {
        return mColor;
    }

    /**
     * 设置线条颜色
     *
     * @param color 线条颜色
     * @return 当前对象
     */
    public RvDivider setColor(int color) {
        this.mColor = color;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof LinearLayoutManager && !(lm instanceof GridLayoutManager)) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int orientation = llm.getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                //绘制垂直排列的分割线
                drawVertical(c, parent);
            } else if (orientation == LinearLayoutManager.HORIZONTAL) {
                //绘制水平排列的分割线
                drawHorizontal(c, parent);
            }
        } else if (lm instanceof GridLayoutManager) {
            //绘制Grid分割线
            drawGrid(c, parent);
        }
    }

    private void drawGrid(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        GridLayoutManager glm = (GridLayoutManager) parent.getLayoutManager();
        //列数
        int spanCount = glm.getSpanCount();
        //子控件数量对列数求余
        int childMod = childCount % spanCount;
        //行数
        int rowCount = childMod == 0 ? childCount / spanCount : childCount / spanCount + 1;

        int left, right, top, bottom;

        View child = null;
        RecyclerView.LayoutParams params = null;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();

            //先绘制水平线分割线
            left = child.getLeft() + params.leftMargin;
            right = child.getRight() + params.rightMargin;

            //第一行，绘制上边线
            if (i < spanCount) {
                top = child.getTop() + params.topMargin - mWidthPx;
                bottom = top + mWidthPx;

                c.drawRect(left, top, right, bottom, mPaint);
            }

            //绘制每一行的下边线
            top = child.getBottom() + params.topMargin;
            bottom = top + mWidthPx;
            c.drawRect(left, top, right, bottom, mPaint);
            //水平分割线绘制结束

            //再绘制垂直分割线
            //因为此方法是绘制每个child的边界线，所以最终结果是每个child的角落上都是空白，此时就需要用水平分割线或者垂直分割线来填充。
            //这里就选垂直防线分割线来填充。
            //具体方法是将每个child的top值减小mWidth，并且将bottom值增大mWidth，否则最后一行还是会出现空白角落
            top = child.getTop() + params.topMargin - mWidthPx;
            bottom = child.getBottom() + params.bottomMargin + mWidthPx;

            //第一列，绘制左边线
            if (i % spanCount == 0) {
                left = child.getLeft() + params.leftMargin - mWidthPx;
                right = left + mWidthPx;
                c.drawRect(left, top, right, bottom, mPaint);
            }

            //绘制每一列的右边线
            left = child.getRight() + params.leftMargin;
            right = left + mWidthPx;
            c.drawRect(left, top, right, bottom, mPaint);

            //至此，绘制完成
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        int left, right;

        View child = null;
        RecyclerView.LayoutParams params = null;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (i == 0) {
                //如果是第一个元素，就绘制左边线
                //child.getLeft() + params.leftMargin 得到的是child的x起点坐标，需要在该x坐标之前绘制宽度为mWidth的线，所以需要减mWidth
                left = child.getLeft() + params.leftMargin - mWidthPx;
                right = left + mWidthPx;
                //绘制左边线
                c.drawRect(left, top, right, bottom, mPaint);
            }

            //再绘制每个元素的右边线
            left = child.getRight() + params.rightMargin;
            right = left + mWidthPx;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        int top, bottom;

        View child = null;
        RecyclerView.LayoutParams params = null;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (i == 0) {
                //如果是第一个元素，就绘制上边线
                //child.getTop() + params.topMargin 得到的是child的y起点坐标，需要在该y坐标之前绘制宽度为mWidth的线，所以需要减mWidth
                top = child.getTop() + params.topMargin - mWidthPx;
                bottom = top + mWidthPx;
                //绘制上边线
                c.drawRect(left, top, right, bottom, mPaint);
            }

            top = child.getBottom() + params.bottomMargin;
            bottom = top + mWidthPx;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (null == parent.getAdapter()) {
            throw new RuntimeException("You must set adapter for RecycleView before add ItemDecoration.");
        }
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
//        int childCount = parent.getChildCount();
        //以上方法获取到的childCount不准确，不过通过adapter获取到的itemCount倒是可用
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);

        if (lm instanceof LinearLayoutManager && !(lm instanceof GridLayoutManager)) {
            //线性布局
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int orientation = llm.getOrientation();

            if (mType == DividerType.TYPE_CENTER) {
                //如果只绘制中间区域，则不绘制开始和结束的边界线
                if (itemPosition < childCount - 1) {
                    //不是最后一个，就绘制下边线或者右边线
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        //竖直排列，不是最后一个就绘制下边线
                        outRect.set(0, 0, 0, mWidthPx);
                    } else if (orientation == LinearLayoutManager.HORIZONTAL) {
                        //水平排列，不是最后一个就绘制右边线
                        outRect.set(0, 0, mWidthPx, 0);
                    }
                }
            } else if (mType == DividerType.TYPE_FILL) {
                //不仅绘制内容区域，连边界一起绘制
                if (orientation == LinearLayoutManager.VERTICAL) {
                    //竖直排列，第一个绘制上边线和下边线
                    if (itemPosition == 0) {
                        outRect.top = mWidthPx;
                    }
                    outRect.bottom = mWidthPx;
                } else if (orientation == LinearLayoutManager.HORIZONTAL) {
                    //水平排列，第一个绘制左边线和右边线
                    if (itemPosition == 0) {
                        outRect.left = mWidthPx;
                    }
                    outRect.right = mWidthPx;
                }
            }
        } else if (lm instanceof GridLayoutManager) {
            //GridView布局
            GridLayoutManager glm = (GridLayoutManager) lm;
            //列数
            int spanCount = glm.getSpanCount();
            //子控件数量对列数求余
            int childMod = childCount % spanCount;
            //行数
            int rowCount = childMod == 0 ? childCount / spanCount : childCount / spanCount + 1;

            if (mType == DividerType.TYPE_FILL) {
                if (itemPosition < spanCount) {
                    //第一行
                    outRect.top = mWidthPx;
                }

                if (itemPosition % spanCount == 0) {
                    //第一列
                    outRect.left = mWidthPx;
                }

                //设置右边线和下边线
                outRect.right = mWidthPx;
                outRect.bottom = mWidthPx;
            }

            if (mType == DividerType.TYPE_CENTER) {
                //左边线和上边线直接不设置，默认为0
                //先设置右边线和下边线，再在不同的位置改变此设置
                outRect.right = mWidthPx;
                outRect.bottom = mWidthPx;

                //最后一列，右边线设置为0
                if (itemPosition % spanCount == spanCount - 1) {
                    outRect.right = 0;
                }

                //如果最后一行不完整，也将最后一个元素的右边线设置为0
//                if (childMod != 0) {
//                    if (itemPosition == childCount - 1) {
//                        outRect.right = 0;
//                    }
//                }

//                if (childMod == 0) {
//                    //如果余数为0，说明最后一行完整，将最后一整行下边线设为0
//                    if (itemPosition >= spanCount * (rowCount - 1)) {
//                        outRect.bottom = 0;
//                    }
//                } else {
//                    //余数不为0，就将最后余着的和上一行显示在余数后边的元素的下边线设为0
//                    if (itemPosition >= childCount - spanCount) {
//                        outRect.bottom = 0;
//                    }
//                }

                /***************************************************************/
                /********** 不再使用以上设置，直接将最后一行下边线设为0 **********/
                /***************************************************************/

                //最后一行，将下边线设置为0
                if (itemPosition >= spanCount * (rowCount - 1)) {
                    outRect.bottom = 0;
                }
            }
        }
    }

    /**
     * 线条绘制类型
     */
    public interface DividerType {
        //只绘制中间部分，边界不绘制
        int TYPE_CENTER = 2000;
        //包括边界线绘制
        int TYPE_FILL = 2001;
    }
}
