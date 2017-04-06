package com.yhy.widget.adv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yhy.widget.R;
import com.yhy.widget.adv.abs.AbsAdvView;
import com.yhy.widget.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HongYi Yan on 2017/4/6 13:59.
 */
public class TextAdvView extends AbsAdvView {
    private List<? extends CharSequence> mTextList;

    private float mTextSize = 32;//px
    private int mTextColor = Color.BLACK;
    private int mWhere = -1;
    private TextUtils.TruncateAt mEllipsize = TextUtils.TruncateAt.END;

    public TextAdvView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TextAdvViewAttrs);
        mTextSize = ta.getDimension(R.styleable.TextAdvViewAttrs_tav_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.TextAdvViewAttrs_tav_text_color, mTextColor);
        mWhere = ta.getInteger(R.styleable.TextAdvViewAttrs_tav_text_ellipsize, mWhere);
        ta.recycle();

        if (mWhere > -1) {
            switch (mWhere) {
                case 0:
                    mEllipsize = TextUtils.TruncateAt.START;
                    break;
                case 1:
                    mEllipsize = TextUtils.TruncateAt.MIDDLE;
                    break;
                case 2:
                    mEllipsize = TextUtils.TruncateAt.END;
                    break;
                default:
                    mEllipsize = TextUtils.TruncateAt.END;
                    break;
            }
        }
    }

    public void startWithTextList(List<? extends CharSequence> textList) {
        startWithTextList(textList, 0);
    }

    public void startWithTextList(List<? extends CharSequence> textList, int index) {
        mTextList = textList;
        start();
        setCurrentItem(index);
    }

    @Override
    protected List<? extends View> getViewList() {
        if (null == mTextList || mTextList.isEmpty()) {
            return null;
        }
        List<TextView> tvList = new ArrayList<>();
        for (int i = 0; i < mTextList.size(); i++) {
            tvList.add(createTextView(mTextList.get(i)));
        }
        return tvList;
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(CharSequence text) {
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tv.setText(text);
        tv.setTextColor(mTextColor);
        tv.setTextSize(DensityUtils.px2sp(getContext(), mTextSize));
        tv.setSingleLine();
        tv.setEllipsize(mEllipsize);
        return tv;
    }
}
