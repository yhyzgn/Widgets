package com.yhy.widget.demo.activity;

import android.widget.TextView;

import com.yhy.widget.core.exptext.ExpandTextView;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;

public class ETVActivity extends BaseActivity {

    private ExpandTextView etvContent;

    @Override
    protected int getLayout() {
        return R.layout.activity_etv;
    }

    @Override
    protected void initView() {
        etvContent = findViewById(R.id.etv_content);
        etvContent.mapViewId(R.id.tv_content, R.id.tv_expand);
    }

    @Override
    protected void initData() {
        etvContent.setText("哈哈哈哈哈哈啊哈哈哈哈");
        etvContent.setText("测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本");
    }

    @Override
    protected void initEvent() {
        etvContent.setOnExpandStateChangeListener(new ExpandTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                toast(isExpanded ? "展开了" : "收起了");
            }
        });
    }
}
