package com.yhy.widgetdemo.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yhy.widgetdemo.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-08 10:06
 * version: 1.0.0
 * desc   :
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        initEvent();
    }

    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void initEvent();

    public <T extends View> T $(int id) {
        return findViewById(id);
    }
}
