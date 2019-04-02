package com.yhy.widget.core.dialog.status.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.yhy.widget.R;
import com.yhy.widget.core.dialog.status.abs.AbsUnableCancelStatusDialog;

import androidx.annotation.NonNull;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2019-04-02 16:55
 * version: 1.0.0
 * desc   : 成功状态弹窗提示
 */
public class SuccessDialog extends AbsUnableCancelStatusDialog {

    public SuccessDialog(@NonNull Context context) {
        super(context);
    }

    public SuccessDialog(@NonNull Context context, CharSequence text) {
        super(context, text);
    }

    public SuccessDialog(@NonNull Context context, CharSequence text, long duration) {
        super(context, text, duration);
    }

    @Override
    protected View statusView() {
        return defaultView();
    }

    /**
     * 默认view
     *
     * @return 默认view
     */
    private View defaultView() {
        ImageView img = new ImageView(getContext());
        img.setImageResource(R.mipmap.icon_status_dialog_success);
        return img;
    }
}
