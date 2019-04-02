package com.yhy.widgetdemo.activity;

import android.view.View;
import android.widget.TextView;

import com.yhy.widget.core.dialog.status.StatusDialogManager;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-06-06 17:53
 * version: 1.0.0
 * desc   :
 */
public class LoadingDialogActivity extends BaseActivity {

    private TextView tvLoading;
    private StatusDialogManager manager;

    @Override
    protected int getLayout() {
        return R.layout.activity_loading_dialog;
    }

    @Override
    protected void initView() {
        tvLoading = $(R.id.tv_loading);

        manager = StatusDialogManager.get(LoadingDialogActivity.this).create();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        tvLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                manager.loading("加载中，哈哈...");
//                manager.success();
//                manager.failed();
                manager.error();
            }
        });
    }
}
