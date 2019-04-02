package com.yhy.widget.core.dialog.status.abs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhy.widget.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2019-04-02 15:41
 * version: 1.0.0
 * desc   : 状态弹窗
 */
public abstract class AbsStatusDialog extends AlertDialog {

    private TextView tvText;
    private CharSequence mText;

    protected AbsStatusDialog(@NonNull Context context) {
        this(context, R.style.Theme_DialogStyle_Transparent);
    }

    protected AbsStatusDialog(@NonNull Context context, int themeResId) {
        this(context, themeResId, "Loading...");
    }

    protected AbsStatusDialog(@NonNull Context context, CharSequence text) {
        this(context, R.style.Theme_DialogStyle_Transparent, text);
    }

    protected AbsStatusDialog(@NonNull Context context, int themeResId, CharSequence text) {
        super(context, themeResId);
        mText = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.widget_status_dialog, null);
        RelativeLayout rlContainer = view.findViewById(R.id.rl_status_container);
        tvText = view.findViewById(R.id.tv_status_text);

        rlContainer.removeAllViews();
        rlContainer.addView(statusView());
        tvText.setText(mText);

        setContentView(view);

        if (null != getWindow()) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(params);
        }
    }

    /**
     * 设置文本
     *
     * @param text 文本
     * @return 当前弹窗对象
     */
    public AbsStatusDialog setText(CharSequence text) {
        mText = text;
        //在这里设置文本内容会导致空指针，需要在show方法中设置文本
        //tvText.setText(mText);
        return this;
    }

    /**
     * 设置是否可取消
     *
     * @param cancelable             返回键取消
     * @param canceledOnTouchOutside 点击外部取消
     * @return 当前弹窗对象
     */
    public AbsStatusDialog setCancelable(boolean cancelable, boolean canceledOnTouchOutside) {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        return this;
    }

    @Override
    public void show() {
        if (isShowing()) {
            return;
        }
        super.show();
        //设置文本需要在show方法中，show之后设置，否则会导致空指针
        tvText.setText(mText);
    }

    protected abstract View statusView();
}
