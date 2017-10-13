package com.yhy.widgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.yhy.widget.exptext.ExpandTextView;

public class ETVActivity extends AppCompatActivity {

    private ExpandTextView etvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etv);

        etvContent = (ExpandTextView) findViewById(R.id.etv_content);

        etvContent.mapViewId(R.id.tv_content, R.id.tv_expand);

        etvContent.setText("哈哈哈哈哈哈啊哈哈哈哈");

        etvContent.setText("测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本");

        etvContent.setOnExpandStateChangeListener(new ExpandTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                Toast.makeText(ETVActivity.this, isExpanded ? "展开了" : "收起了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
