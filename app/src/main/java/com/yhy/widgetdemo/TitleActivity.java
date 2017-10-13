package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yhy.widget.title.TitleBar;

/**
 * Created by HongYi Yan on 2017/4/6 23:54.
 */
public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        TitleBar tbTest1 = $(R.id.tb_test_1);
        TitleBar tbTest2 = $(R.id.tb_test_2);

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

    public <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
