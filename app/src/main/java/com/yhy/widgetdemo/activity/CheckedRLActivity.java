package com.yhy.widgetdemo.activity;

import android.view.View;
import android.widget.TextView;

import com.yhy.widget.layout.checked.CheckedRelativeLayout;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.utils.ToastUtils;

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
    protected void initEvent() {
//        tvTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.shortT("点击了TextView");
//            }
//        });

        crlTest.setOnCheckedChangeListener(new CheckedRelativeLayout.OnCheckedChangeListener() {
            @Override
            public void onChanged(CheckedRelativeLayout crl, boolean isChecked) {
                ToastUtils.shortT("isChecked = " + isChecked);
            }
        });
    }
}
