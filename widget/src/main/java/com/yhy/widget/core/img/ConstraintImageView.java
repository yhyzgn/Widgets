package com.yhy.widget.core.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yhy.widget.R;

import java.util.regex.Pattern;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-06-06 14:42
 * version: 1.0.0
 * desc   : 按比例约束宽高的ImageView
 */
public class ConstraintImageView extends AppCompatImageView {
    private Reference mReference;
    private String mOriginalRatio;
    private float mRealRatio;

    public ConstraintImageView(Context context) {
        this(context, null);
    }

    public ConstraintImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConstraintImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ConstraintImageView);
        int reference = ta.getInt(R.styleable.ConstraintImageView_civ_reference, Reference.WIDTH.code);
        mReference = reference == 0 ? Reference.WIDTH : Reference.HEIGHT;
        mOriginalRatio = ta.getString(R.styleable.ConstraintImageView_civ_ratio);
        ta.recycle();

        calculateRatio();
    }

    /**
     * 计算宽高比
     */
    private void calculateRatio() {
        if (!TextUtils.isEmpty(mOriginalRatio)) {
            mOriginalRatio = mOriginalRatio.replaceAll(" ", "");
            if (Pattern.matches("^[1-9][0-9]*:[1-9][0-9]*$", mOriginalRatio)) {
                String[] ratioText = mOriginalRatio.split(":");
                float width = Float.valueOf(ratioText[0]);
                float height = Float.valueOf(ratioText[1]);
                mRealRatio = width / height;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRealRatio > 0) {
            float width = getMeasuredWidth();
            float height = getMeasuredHeight();

            float realWidth = width;
            float realHeight = height;
            if (mReference == Reference.WIDTH) {
                // 以宽度为基准，按约束比例计算高度
                realHeight = width / mRealRatio;
            } else {
                // 以高度为基准，按约束比例计算宽度
                realWidth = height * mRealRatio;
            }
            setMeasuredDimension((int) realWidth, (int) realHeight);
        }
    }

    /**
     * 参考方式
     */
    public enum Reference {
        // 以宽为准；以高为准
        WIDTH(0), HEIGHT(1);

        int code;

        /**
         * 构造行数
         *
         * @param code 标识码
         */
        Reference(int code) {
            this.code = code;
        }
    }
}
