package com.yhy.widget.adv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HongYi Yan onOrOff 2017/4/7 13:52.
 */
public class TextAdvAdapter extends AdvAdapter<String> {
    private Context mCtx;
    private List<String> mDataList;
    private ViewGroup.LayoutParams mParams;

    public TextAdvAdapter(Context ctx, List<String> list) {
        super(ctx, list);
        mCtx = ctx;
        mDataList = list;
        mParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
    }

    @Override
    public View getView(int position) {
        return createTextView(getItem(position));
    }

    protected TextView createTextView(String text) {
        TextView tv = new TextView(mCtx);
        tv.setLayoutParams(mParams);
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tv.setText(text);
        tv.setTextColor(Color.DKGRAY);
        tv.setTextSize(14);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        return tv;
    }
}
