package com.yhy.widgetdemo.activity;

import android.view.View;

import com.yhy.widget.core.checked.CheckedTextView;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-07 17:44
 * version: 1.0.0
 * desc   :
 */
public class CheckedTVActivity extends BaseActivity {

    private CheckedTextView ctvDef;
    private CheckedTextView ctvPrev;

    @Override
    protected int getLayout() {
        return R.layout.activity_checked_textview;
    }

    @Override
    protected void initView() {
        ctvDef = findViewById(R.id.ctv_def);
        ctvPrev = findViewById(R.id.ctv_prev);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        ctvDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("我的click被阻止了");
            }
        });

        ctvDef.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toast("我的longClick被阻止了");
                return true;
            }
        });

        ctvDef.setOnCheckedChangeListener(new CheckedTextView.OnCheckedChangeListener() {
            @Override
            public void onChanged(CheckedTextView ctv, boolean isChecked) {
                toast("isChecked = " + isChecked);
            }
        });

        ctvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("我的click未被阻止");
            }
        });

        ctvPrev.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toast("我的longClick未被阻止");
                return true;
            }
        });

        ctvPrev.setOnCheckedChangeListener(new CheckedTextView.OnCheckedChangeListener() {
            @Override
            public void onChanged(CheckedTextView ctv, boolean isChecked) {
                toast("isChecked = " + isChecked);
            }
        });
    }
}
