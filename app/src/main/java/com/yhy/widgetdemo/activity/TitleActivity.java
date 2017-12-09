package com.yhy.widgetdemo.activity;

import android.view.View;

import com.yhy.widget.core.title.TitleBar;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;

/**
 * Created by HongYi Yan on 2017/4/6 23:54.
 */
public class TitleActivity extends BaseActivity {

    private TitleBar tbTest1;
    private TitleBar tbTest2;

    @Override
    protected int getLayout() {
        return R.layout.activity_title;
    }

    @Override
    protected void initView() {
        tbTest1 = $(R.id.tb_test_1);
        tbTest2 = $(R.id.tb_test_2);
    }

    @Override
    protected void initData() {
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
