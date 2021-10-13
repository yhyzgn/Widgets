package com.yhy.widget.demo.activity.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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
        initData();
        initEvent();
    }

    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    public <T extends View> T $(int id) {
        return findViewById(id);
    }

    public void toast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
