package com.yhy.widget.demo.status;

import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yhy.widget.layout.status.StatusLayout;
import com.yhy.widget.layout.status.listener.OnStatusRetryListener;
import com.yhy.widget.demo.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 15:19
 * version: 1.0.0
 * desc   :
 */
public class DefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_def);

        final StatusLayout slContent = (StatusLayout) findViewById(R.id.sl_content);

        findViewById(R.id.btn_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slContent.showLoading();
            }
        });

        findViewById(R.id.btn_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slContent.showSuccess();
            }
        });

        findViewById(R.id.btn_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slContent.showError();
            }
        });

        findViewById(R.id.btn_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slContent.showEmpty();
            }
        });

        slContent.setOnStatusRetryListener(new OnStatusRetryListener() {
            @Override
            public void onRetry() {
                Toast.makeText(DefActivity.this, "重试加载...", Toast.LENGTH_SHORT).show();
                new Thread() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        slContent.showSuccess();
                    }
                }.start();
            }
        });
    }
}
