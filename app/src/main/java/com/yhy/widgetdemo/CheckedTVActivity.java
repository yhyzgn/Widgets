package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yhy.widget.core.checked.CheckedTextView;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-07 17:44
 * version: 1.0.0
 * desc   :
 */
public class CheckedTVActivity extends AppCompatActivity {

    private CheckedTextView ctvDef;
    private CheckedTextView ctvPrev;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_textview);

        ctvDef = findViewById(R.id.ctv_def);
        ctvPrev = findViewById(R.id.ctv_prev);

        ctvDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("我的click被阻止了");
            }
        });

        ctvDef.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toast("我的longClick被阻止了");
                return true;
            }
        });

        ctvDef.setOnCheckedChangeListener(new CheckedTextView.OnCheckedChangeListener() {
            @Override
            public void onChanged(CheckedTextView ctv, boolean isChecked) {
                toast("isChecked = " + isChecked);
            }
        });

        ctvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("我的click未被阻止");
            }
        });

        ctvPrev.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toast("我的longClick未被阻止");
                return true;
            }
        });

        ctvPrev.setOnCheckedChangeListener(new CheckedTextView.OnCheckedChangeListener() {
            @Override
            public void onChanged(CheckedTextView ctv, boolean isChecked) {
                toast("isChecked = " + isChecked);
            }
        });
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
