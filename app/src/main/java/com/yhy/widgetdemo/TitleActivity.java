package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yhy.widget.titb.TextTitleBar;

/**
 * Created by HongYi Yan on 2017/4/6 23:54.
 */
public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        TextTitleBar ttbTitle = (TextTitleBar) findViewById(R.id.ttb_title);
        ttbTitle.init(this);
    }
}
