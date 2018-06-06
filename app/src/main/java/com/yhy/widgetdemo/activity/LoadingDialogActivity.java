package com.yhy.widgetdemo.activity;

import android.view.View;
import android.widget.TextView;

import com.yhy.widget.core.dialog.LoadingDialog;
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
    private LoadingDialog mDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_loading_dialog;
    }

    @Override
    protected void initView() {
        tvLoading = $(R.id.tv_loading);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        tvLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new LoadingDialog(LoadingDialogActivity.this, "加载中...");
                mDialog.show();

                // 3秒后消失
                tvLoading.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                    }
                }, 3000);
            }
        });
    }
}
