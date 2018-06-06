package com.yhy.widget.core.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-01-06 14:37
 * version: 1.0.0
 * desc   : 加载中进度弹窗
 */
public class LoadingDialog extends AlertDialog {

    private TextView tvPdText;
    private CharSequence mText;

    public LoadingDialog(Context context) {
        this(context, R.style.Theme_DialogStyle_Transparent, "Loading...");
    }

    public LoadingDialog(Context context, CharSequence text) {
        this(context, R.style.Theme_DialogStyle_Transparent, text);
    }

    public LoadingDialog(Context context, int theme, CharSequence text) {
        super(context, theme);
        mText = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.widget_loading_dialog, null);
        tvPdText = view.findViewById(R.id.tv_loading);
        tvPdText.setText(mText);

        setContentView(view);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    /**
     * 设置文本
     *
     * @param text 文本
     * @return 当前弹窗对象
     */
    public LoadingDialog setText(CharSequence text) {
        mText = text;
        //在这里设置文本内容会导致空指针，需要在show方法中设置文本
        //tvPdText.setText(mText);
        return this;
    }

    /**
     * 设置是否可取消
     *
     * @param cancelable             返回键取消
     * @param canceledOnTouchOutside 点击外部取消
     * @return 当前弹窗对象
     */
    public LoadingDialog setCancelable(boolean cancelable, boolean canceledOnTouchOutside) {
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
        tvPdText.setText(mText);
    }
}
