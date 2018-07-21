package com.yhy.widgetdemo.activity;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.yhy.widget.core.title.TitleBar;
import com.yhy.widget.utils.WidgetCoreUtils;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;

/**
 * Created by HongYi Yan on 2017/4/6 23:54.
 */
public class TitleActivity extends BaseActivity {

    private TitleBar tbTest1;
    private TitleBar tbTest2;
    private TitleBar tbTest3;
    private TitleBar tbTest4;
    private TitleBar tbTest5;

    @Override
    protected int getLayout() {
        return R.layout.activity_title;
    }

    @Override
    protected void initView() {
        tbTest1 = $(R.id.tb_test_1);
        tbTest2 = $(R.id.tb_test_2);
        tbTest3 = $(R.id.tb_test_3);
        tbTest4 = $(R.id.tb_test_4);
        tbTest5 = $(R.id.tb_test_5);
    }

    @Override
    protected void initData() {
        setPadding(tbTest4);
        setPadding(tbTest5);
    }

    private void setPadding(final TitleBar titleBar) {
        titleBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int paddingTop = WidgetCoreUtils.dp2px(TitleActivity.this, 24);
                titleBar.setPadding(0, paddingTop, 0, 0);

                ViewGroup.LayoutParams params = titleBar.getLayoutParams();
                if (null == params) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.width = titleBar.getMeasuredWidth();
                params.height = titleBar.getMeasuredHeight() + paddingTop;
                titleBar.setLayoutParams(params);

                titleBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    protected void initEvent() {
        tbTest2.setOnTitleBarListener(new TitleBar.OnTitleBarListener() {
            @Override
            public void titleClick(View view) {
                toast("点击了标题");
            }

            @Override
            public void leftIconClick(View view) {
                toast("返回");
                finish();
            }

            @Override
            public void leftTextClick(View view) {
                toast("左边文本");
            }

            @Override
            public void rightIconClick(View view) {
                toast("右边图标");
            }

            @Override
            public void rightTextClick(View view) {
                toast("右边文本");
            }
        });
    }
}
