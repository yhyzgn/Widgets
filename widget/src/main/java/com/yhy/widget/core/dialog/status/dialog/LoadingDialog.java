package com.yhy.widget.core.dialog.status.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yhy.widget.R;
import com.yhy.widget.core.dialog.status.abs.AbsStatusDialog;
import com.yhy.widget.utils.WidgetCoreUtils;

import androidx.annotation.NonNull;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-01-06 14:37
 * version: 1.0.0
 * desc   : 加载中进度弹窗
 */
public class LoadingDialog extends AbsStatusDialog {

    public LoadingDialog(@NonNull Context context) {
        this(context, "Loading...");
    }

    public LoadingDialog(@NonNull Context context, CharSequence text) {
        super(context, text);
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
        ProgressBar pbLoading = new ProgressBar(getContext());
        pbLoading.setLayoutParams(new ViewGroup.LayoutParams(WidgetCoreUtils.dp2px(getContext(), 60), WidgetCoreUtils.dp2px(getContext(), 60)));
        pbLoading.setIndeterminateDrawable(getContext().getResources().getDrawable(R.drawable.icon_status_dialog_loading));
        pbLoading.setIndeterminate(true);
        return pbLoading;
    }
}
