package com.yhy.widget.demo.activity;

import android.view.View;
import android.widget.TextView;

import com.yhy.widget.layout.checked.CheckedRelativeLayout;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-08 10:04
 * version: 1.0.0
 * desc   :
 */
public class CheckedRLActivity extends BaseActivity {

    private TextView tvTest;
    private CheckedRelativeLayout crlTest;

    @Override
    protected int getLayout() {
        return R.layout.activity_checked_relative_layout;
    }

    @Override
    protected void initView() {
        crlTest = $(R.id.crl_test);
        tvTest = $(R.id.tv_test);

        crlTest.setChecked(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击了TextView");
            }
        });

        crlTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击");
            }
        });

        crlTest.setOnCheckedChangeListener(new CheckedRelativeLayout.OnCheckedChangeListener() {
            @Override
            public void onChanged(CheckedRelativeLayout crl, boolean isChecked) {
                toast("isChecked = " + isChecked);
            }
        });
    }
}
