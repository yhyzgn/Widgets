package com.yhy.widget.pv;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2016-08-30 15:39
 * version: 1.0.0
 * desc   : 滚动选择器
 */
public class PickerView<T> extends View {

    public static final String TAG = "PickerView";

    // text之间间距和minTextSize之比
    public static final float MARGIN_ALPHA = 2.8f;

    // 自动回滚到中间的速度
    public static final float SPEED = 2;

    private List<T> mDataList;

    // 选中的位置，这个位置是mDataList的中心位置，一直不变
    private int mCurrentSelected;
    private Paint mPaint;

    // 字体最大大小20sp
    private float mMaxTextSize = sp2px(getContext(), 20);
    // 字体最小大小14sp
    private float mMinTextSize = sp2px(getContext(), 14);

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 100;

    private int mColorText = 0xe84c3d;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private Timer timer;
    private PvTimerTask mTask;
    private ItemProvider<T> mItemProvider;
    private OnSelectListener<T> mSelectListener;

    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            invalidate();
        }

    };

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            // 从布局文件获取字体大小，如果想自动调整字体大小，就必须在布局文件给定0sp的字体大小
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PickerViewAttrs);

            mMaxTextSize = ta.getDimensionPixelSize(R.styleable.PickerViewAttrs_pv_max_text_size, (int) mMaxTextSize);
            mMinTextSize = ta.getDimensionPixelSize(R.styleable.PickerViewAttrs_pv_min_text_size, (int) mMinTextSize);
            mColorText = ta.getColor(R.styleable.PickerViewAttrs_pv_text_color, mColorText);

            ta.recycle();
            init();
        }
    }

    /**
     * 设置选中事件
     *
     * @param listener 选中事件
     * @return 当前实例
     */
    public PickerView<T> setOnSelectListener(OnSelectListener<T> listener) {
        mSelectListener = listener;
        return this;
    }

    private void performSelect() {
        if (mSelectListener != null && mDataList.size() > 0) {
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
        }
    }

    /**
     * 设置数据
     *
     * @param datas    数据集合
     * @param provider 数据提供者
     * @return 当前对象
     */
    public PickerView<T> setData(List<T> datas, ItemProvider<T> provider) {
        mDataList = datas;
        mItemProvider = provider;

        mCurrentSelected = datas.size() / 2;
        invalidate();

        return this;
    }

    /**
     * 选择选中的item的index
     *
     * @param selected 选中的索引
     * @return 当前实例
     */
    public PickerView<T> setSelected(int selected) {
        mCurrentSelected = selected;
        int distance = mDataList.size() / 2 - mCurrentSelected;
        if (distance < 0)
            for (int i = 0; i < -distance; i++) {
                moveHeadToTail();
                mCurrentSelected--;
            }
        else if (distance > 0)
            for (int i = 0; i < distance; i++) {
                moveTailToHead();
                mCurrentSelected++;
            }
        // 如果没有找到默认选中的内容项时，需要把它默认选中的内容返回
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new PvTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
        invalidate();

        return this;
    }

    /**
     * 选择选中的内容
     *
     * @param selectItem 选中的内容
     * @return 当前实例
     */
    public PickerView<T> setSelected(String selectItem) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).equals(selectItem)) {
                setSelected(i);
                return this;
            }
        }
        // 如果没有找到默认选中的内容项时，需要把它默认选中的内容返回
        // performSelect();
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new PvTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);

        return this;
    }

    private void moveHeadToTail() {
        T head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        T tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        if (mMaxTextSize <= 0) {
            mMaxTextSize = mViewHeight / 4.0f;
        }
        if (mMinTextSize <= 0) {
            mMinTextSize = mMaxTextSize / 2f;
        }
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<T>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        if (null != mItemProvider) {
            String itemData = mItemProvider.getItem(mDataList.get(mCurrentSelected), mCurrentSelected);
            canvas.drawText(itemData, x, baseline, mPaint);
        }
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen);
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        if (null != mItemProvider) {
            int index = mCurrentSelected + type * position;
            String itemData = mItemProvider.getItem(mDataList.get(index), index);
            canvas.drawText(itemData, (float) (mViewWidth / 2.0), baseline, mPaint);
        }
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {
        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new PvTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    /**
     * sp转px
     *
     * @param context 上下文对象
     * @param spVal   sp值
     * @return px值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context
                .getResources().getDisplayMetrics());
    }

    class PvTimerTask extends TimerTask {
        Handler handler;

        public PvTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public interface ItemProvider<T> {
        String getItem(T data, int position);
    }

    public interface OnSelectListener<T> {
        void onSelect(T data);
    }
}
