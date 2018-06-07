package com.yhy.widget.core.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Builder mBuilder;
    private Handler mHandler;
    private Runnable mRunnable;
    private Dialog mDialog;
    private int[] mAnchorLocation;

    /**
     * 构造方法
     *
     * @param builder 构造器
     */
    private InputDialogView(Builder builder) {
        mBuilder = builder;
        mHandler = new Handler();
        mAnchorLocation = new int[2];
    }

    /**
     * 显示弹窗
     */
    public void show() {
        mDialog = new Dialog(mBuilder.context, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setContentView(R.layout.widget_input_dialog);

        final LinearLayout llInputArea = mDialog.findViewById(R.id.ll_input_area);
        final EditText etInput = mDialog.findViewById(R.id.et_input);
        final TextView tvPublish = mDialog.findViewById(R.id.tv_publish);
        final View vUnderline = mDialog.findViewById(R.id.v_underline);

        // 先将发表按钮和下划线设置成不可用状态
        tvPublish.setEnabled(false);
        vUnderline.setEnabled(false);

        llInputArea.setBackgroundColor(mBuilder.bgColor);

        etInput.setHint(mBuilder.hint);
        etInput.setText(mBuilder.content);
        etInput.setTextColor(mBuilder.contentColor);
        etInput.setTextSize(mBuilder.contentSize);

        tvPublish.setText(mBuilder.publish);
        tvPublish.setTextColor(mBuilder.publishColor);
        tvPublish.setTextSize(mBuilder.publishSize);

        tvPublish.setBackgroundResource(mBuilder.stateSelectorRes);
        vUnderline.setBackgroundResource(mBuilder.stateSelectorRes);

        mDialog.setCancelable(true);

        mDialog.findViewById(R.id.ll_input_area_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                //取消滚动
                cancelScroll();

                if (mBuilder.listener != null) {
                    mBuilder.listener.onDismiss();
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
                vUnderline.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mBuilder.listener) {
                    mBuilder.listener.onPublish(InputDialogView.this, etInput.getText().toString().trim());
                }
            }
        });
        mDialog.show();

        if (null != mBuilder.anchor) {
            mBuilder.anchor.getLocationOnScreen(mAnchorLocation);
        }
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (null != mBuilder.listener) {
                    int[] position = new int[2];
                    // 输入框距离屏幕顶部（不包括状态栏）的长度
                    llInputArea.getLocationOnScreen(position);
                    int width = 0;
                    int height = 0;
                    // 计算偏移量，以便进行相关操作，如：点击某个view时，让该view刚好在输入框上面
                    if (null != mBuilder.anchor) {
                        width = mBuilder.anchor.getWidth();
                        height = mBuilder.anchor.getHeight();
                    }
                    int offsetX = mAnchorLocation[0] + width - position[0];
                    int offsetY = mAnchorLocation[1] + height - position[1];

                    mBuilder.listener.onShow(offsetX, offsetY, position);
                }
            }
        };

        mHandler.postDelayed(mRunnable, 400);
    }

    /**
     * 取消滚动
     */
    private void cancelScroll() {
        if (null != mRunnable) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    /**
     * 销毁弹窗
     */
    public void dismiss() {
        if (null != mDialog) {
            mDialog.dismiss();
        }
    }

    /**
     * 构造器
     */
    public static class Builder {
        // 上下文
        private Context context;
        // 提示文字
        private CharSequence hint;
        // 文本内容
        private CharSequence content;
        // 文本颜色，默认：@color/textPrimary
        private int contentColor;
        // 文本大小，默认：12sp
        private float contentSize;
        // 发表按钮文本
        private CharSequence publish;
        // 发表按钮字体颜色，默认：#ffffff
        private int publishColor;
        // 发表按钮字体大小，默认：14sp
        private float publishSize;
        // 背景颜色，默认：@color/windowBackground
        private int bgColor;
        // 发表按钮和下划线选择器，默认：@drawable/bg_input_dialog_enable_selector
        private int stateSelectorRes;
        // 用来作参考的view，如果弹出后需要显示在某个view的下边，那就传对应的view即可
        private View anchor;
        // 监听事件
        private OnInputDialogListener listener;

        /**
         * 构造器
         *
         * @param context 上下文
         */
        public Builder(Context context) {
            this.context = context;
            this
                    .hint("说点儿什么吧...")
                    .content("")
                    .contentColorRes(R.color.textPrimary)
                    .contentSize(12)
                    .publish("发表")
                    .publishColor(Color.WHITE)
                    .publishSize(14)
                    .bgColorRes(R.color.windowBackground)
                    .stateSelectorRes(R.drawable.bg_input_dialog_enable_selector);
        }

        /**
         * 提示文字
         *
         * @param hint 提示文字
         * @return 当前构造器对象
         */
        public Builder hint(CharSequence hint) {
            this.hint = hint;
            return this;
        }

        /**
         * 文本内容
         *
         * @param content 文本内容
         * @return 当前构造器对象
         */
        public Builder content(CharSequence content) {
            this.content = content;
            return this;
        }

        /**
         * 文本颜色
         *
         * @param color 文本颜色
         * @return 当前构造器对象
         */
        public Builder contentColor(int color) {
            this.contentColor = color;
            return this;
        }

        /**
         * 文本颜色资源id
         *
         * @param res 资源id
         * @return 当前构造器对象
         */
        public Builder contentColorRes(int res) {
            return contentColor(context.getResources().getColor(res));
        }

        /**
         * 文本字体大小，单位：sp
         *
         * @param size 字体大小
         * @return 当前构造器对象
         */
        public Builder contentSize(float size) {
            this.contentSize = size;
            return this;
        }

        /**
         * 发表按钮文本
         *
         * @param publish 发表按钮文本
         * @return 当前构造器对象
         */
        public Builder publish(CharSequence publish) {
            this.publish = publish;
            return this;
        }

        /**
         * 发表按钮文本颜色
         *
         * @param color 文本颜色
         * @return 当前构造器对象
         */
        public Builder publishColor(int color) {
            this.publishColor = color;
            return this;
        }

        /**
         * 发表按钮文本颜色资源id
         *
         * @param res 资源id
         * @return 当前构造器对象
         */
        public Builder publishColorRes(int res) {
            return publishColor(context.getResources().getColor(res));
        }

        /**
         * 发表按钮文本字体大小，单位：sp
         *
         * @param size 字体带下
         * @return 当前构造器对象
         */
        public Builder publishSize(float size) {
            this.publishSize = size;
            return this;
        }

        /**
         * 背景颜色
         *
         * @param color 背景颜色
         * @return 当前构造器对象
         */
        public Builder bgColor(int color) {
            this.bgColor = color;
            return this;
        }

        /**
         * 背景颜色资源id
         *
         * @param res 资源id
         * @return 当前构造器对象
         */
        public Builder bgColorRes(int res) {
            return bgColor(context.getResources().getColor(res));
        }

        /**
         * 状态选择器资源id
         *
         * @param res 资源id
         * @return 当前构造器对象
         */
        public Builder stateSelectorRes(int res) {
            this.stateSelectorRes = res;
            return this;
        }

        /**
         * 参考view
         *
         * @param anchor 用来当作参考对象的view
         * @return 当前构造器对象
         */
        public Builder anchor(View anchor) {
            this.anchor = anchor;
            return this;
        }

        /**
         * 监听器
         *
         * @param listener 监听器
         * @return 当前构造器对象
         */
        public Builder listener(OnInputDialogListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 构建弹窗对象
         *
         * @return 弹窗对象
         */
        public InputDialogView build() {
            return new InputDialogView(this);
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
        void onPublish(InputDialogView dialog, CharSequence content);

        /**
         * 当输入框显示时回调
         *
         * @param offsetX  水平方向偏移量
         * @param offsetY  竖直方向偏移量
         * @param position 弹窗显示的位置
         */
        void onShow(int offsetX, int offsetY, int[] position);

        /**
         * 当输入框消失时回调
         */
        void onDismiss();
    }
}
