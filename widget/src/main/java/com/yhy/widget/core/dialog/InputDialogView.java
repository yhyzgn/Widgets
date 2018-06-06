package com.yhy.widget.core.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-03-03 9:36
 * version: 1.0.0
 * desc   : 输入框弹窗
 */
public class InputDialogView {
    private static Handler handler = new Handler();
    private static Runnable runnable;

    /**
     * 弹出输入框
     *
     * @param ctx      上下文
     * @param hint     提示文字
     * @param listener 监听事件
     */
    public static void showInputView(Context ctx, String hint, final OnInputDialogListener listener) {
        final Dialog mDialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setContentView(R.layout.widget_input_dialog);

        final EditText etInput = mDialog.findViewById(R.id.et_input);
        final TextView tvPublish = mDialog.findViewById(R.id.tv_publish);

        etInput.setHint(hint);

        mDialog.setCancelable(true);

        mDialog.findViewById(R.id.ll_input_area_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                //取消滚动
                cancelScroll();

                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPublish.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPublish(mDialog, etInput.getText().toString().trim());
                }
            }
        });
        mDialog.show();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    int[] coord = new int[2];
                    mDialog.findViewById(R.id.ll_input_area).getLocationOnScreen(coord);
                    // 传入 输入框距离屏幕顶部（不包括状态栏）的长度
                    listener.onShow(coord);
                }
            }
        };

        handler.postDelayed(runnable, 800);
    }

    /**
     * 取消滚动
     */
    public static void cancelScroll() {
        if (null != runnable) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 输入框监听器
     */
    public interface OnInputDialogListener {
        /**
         * 点击发表按钮回调
         *
         * @param dialog  当前对话框
         * @param content 输入的内容
         */
        void onPublish(Dialog dialog, String content);

        /**
         * 当输入框显示时回调
         *
         * @param position 弹窗显示的位置
         */
        void onShow(int[] position);

        /**
         * 当输入框消失时回调
         */
        void onDismiss();
    }
}
