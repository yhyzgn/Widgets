package com.yhy.widget.core.dialog.status.abs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import androidx.annotation.NonNull;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2019-04-02 17:11
 * version: 1.0.0
 * desc   : 禁止取消的抽象类弹窗
 */
public abstract class AbsUnableCancelStatusDialog extends AbsStatusDialog {
    // 默认显示时间为3s
    private static final long DURATION = 3000;

    public AbsUnableCancelStatusDialog(@NonNull Context context) {
        this(context, "Text");
    }

    public AbsUnableCancelStatusDialog(@NonNull Context context, CharSequence text) {
        this(context, text, DURATION);
    }

    public AbsUnableCancelStatusDialog(@NonNull Context context, CharSequence text, final long duration) {
        super(context, text);
        // 显示后设置回调，定时隐藏
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AbsUnableCancelStatusDialog.this.dismiss();
                    }
                }, duration);
            }
        });
    }

    @Override
    public final AbsStatusDialog setCancelable(boolean cancelable, boolean canceledOnTouchOutside) {
        return super.setCancelable(false, false);
    }

    @Override
    public final void setCancelable(boolean flag) {
        super.setCancelable(false);
    }

    @Override
    public final void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(false);
    }
}
