package com.yhy.widget.core.dialog.status.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
        Drawable drawable = getContext().getResources().getDrawable(R.mipmap.icon_status_dialog_loading);
        // 创建动画
        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        // 动画插值器
        rotate.setInterpolator(lin);
        // 设置动画持续时间
        rotate.setDuration(1000);
        // 设置重复次数
        rotate.setRepeatCount(-1);
        // 动画执行完后是否停留在执行完的状态
        rotate.setFillAfter(true);
        // 执行前的等待时间
        rotate.setStartOffset(10);

        pbLoading.setIndeterminateDrawable(drawable);
        pbLoading.setIndeterminate(true);
        pbLoading.startAnimation(rotate);
        return pbLoading;
    }
}
