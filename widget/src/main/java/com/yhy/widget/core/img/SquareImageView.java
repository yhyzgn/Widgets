package com.yhy.widget.core.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-07 16:28
 * version: 1.0.0
 * desc   : 方形ImageView
 */
public class SquareImageView extends AppCompatImageView {
    private int mSize;
    private Bitmap mBtnImg;
    private int mBtnSize;
    private int mBtnColor;

    private int mDownX, mDownY;
    private OnBtnClickListener mListener;

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView);
        if (ta.hasValue(R.styleable.SquareImageView_siv_btn_img)) {
            mBtnImg = d2b(ta.getDrawable(R.styleable.SquareImageView_siv_btn_img));
        }
        mBtnSize = ta.getDimensionPixelSize(R.styleable.SquareImageView_siv_btn_size, 0);
        mBtnColor = ta.getColor(R.styleable.SquareImageView_siv_btn_color, Color.TRANSPARENT);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置参考值长宽相等，都设置为宽度
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mBtnImg) {
            drawBtn(canvas);
        }
    }

    private void drawBtn(Canvas canvas) {
        Rect src = new Rect(0, 0, mBtnImg.getWidth(), mBtnImg.getHeight());
        Rect dst = new Rect(mSize - mBtnSize, 0, mSize, mBtnSize);

        Paint paint = new Paint();
        paint.setColor(mBtnColor);
        paint.setAntiAlias(true);

        canvas.drawRect(dst, paint);

        canvas.drawBitmap(mBtnImg, src, dst, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (upX >= mSize - mBtnSize && upX <= mSize && upY >= 0 && upY <= mBtnSize && Math.abs(upX - mDownX) < mBtnSize / 2 && Math.abs(upY - mDownY) < mBtnSize / 2) {
                    // 点击了删除按钮
                    if (null != mListener) {
                        mListener.onClick(this);
                    }
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 由于宽高相等，所以任意一个值都行，这里以宽度为准
        mSize = getMeasuredWidth();
        // 检查并设置按钮默认宽度
        if (null != mBtnImg && mBtnSize == 0) {
            mBtnSize = mSize / 6;
        }
    }

    private Bitmap d2b(Drawable drawable) {
        if (null != drawable) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
        return null;
    }

    public void setOnDeleteListener(OnBtnClickListener listener) {
        mListener = listener;
    }

    public interface OnBtnClickListener {
        void onClick(SquareImageView siv);
    }
}
