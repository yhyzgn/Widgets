package com.yhy.widget.core.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-01-10 13:21
 * version: 1.0.0
 * desc   : 渐变动画的TextView
 */
public class GradientTextView extends AppCompatTextView {

    private int[] mColorArr;
    private int mSpeed;

    private int mWidth;
    private Paint mPaint;
    private LinearGradient mGradient;
    private Matrix mMatrix;
    private int mTranslateWidth;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        int colorRes = ta.getResourceId(R.styleable.GradientTextView_gtv_text_color_list, R.array.color_arr_gradient_text_view);
        if (colorRes > 0) {
            mColorArr = getResources().getIntArray(colorRes);
        } else {
            mColorArr = new int[]{Color.BLACK, Color.BLACK};
        }
        mSpeed = ta.getInt(R.styleable.GradientTextView_gtv_speed_millions, 200);
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
        }
        mPaint = getPaint();
        mGradient = new LinearGradient(-mWidth, 0, 0, 0, mColorArr, null, Shader.TileMode.CLAMP);
        mPaint.setShader(mGradient);
        mPaint.setColor(Color.BLACK);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mMatrix) {
            return;
        }

        mTranslateWidth += mWidth / (mColorArr.length * 2);
        if (mTranslateWidth > mWidth * 2) {
            mTranslateWidth -= mWidth * 2;
        }
        mMatrix.setTranslate(mTranslateWidth, 0);
        mGradient.setLocalMatrix(mMatrix);
        // 每隔 n 时长刷新颜色，达到渐变动画效果
        postInvalidateDelayed(mSpeed);
    }
}
