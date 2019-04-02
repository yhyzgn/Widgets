package com.yhy.widgetdemo.activity;

import android.os.Handler;
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
public class StatusDialogActivity extends BaseActivity {

    private TextView tvLoading;
    private TextView tvSuccess;
    private TextView tvFailed;
    private TextView tvError;

    private StatusDialogManager mManager;

    @Override
    protected int getLayout() {
        return R.layout.activity_status_dialog;
    }

    @Override
    protected void initView() {
        tvLoading = $(R.id.tv_loading);
        tvSuccess = $(R.id.tv_success);
        tvFailed = $(R.id.tv_failed);
        tvError = $(R.id.tv_error);

        mManager = StatusDialogManager
                .with(this)
                .loadingText("正在加载...")
                .successText("成功啦")
                .failedText("失败咯")
                .errorText("出错啦")
                .enableLoadingCancel(true)
                .duration(3000)
                .create();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        tvLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.loading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mManager.dismiss();
                    }
                }, 10000);
            }
        });

        tvSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.success();
            }
        });

        tvFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.failed();
            }
        });

        tvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.error();
            }
        });
    }
}
