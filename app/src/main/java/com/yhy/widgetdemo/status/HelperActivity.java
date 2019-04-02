package com.yhy.widgetdemo.status;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yhy.widget.layout.status.StatusLayout;
import com.yhy.widget.layout.status.helper.StaLayoutHelperBuilder;
import com.yhy.widget.layout.status.listener.OnStatusRetryListener;
import com.yhy.widgetdemo.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-13 15:19
 * version: 1.0.0
 * desc   :
 */
public class HelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_helper);

        final StatusLayout slContent = (StatusLayout) findViewById(R.id.sl_content);

//        StaLayoutHelper helper = new StaLayoutHelper(slContent);
//        helper.setLoadingLayout(getLoadingView());
//        slContent.setHelper(helper);

        StaLayoutHelperBuilder builder = new StaLayoutHelperBuilder.Builder(slContent).setLoadingLayout(getLoadingView()).build();
        slContent.setHelper(builder.getHelper());

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
                Toast.makeText(HelperActivity.this, "重试加载...", Toast.LENGTH_SHORT).show();
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

    private View getLoadingView() {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setText("Helper中定义的加载中");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18);
        tv.setTextColor(Color.RED);
        // 设置tag为loading
        tv.setTag(StatusLayout.Status.LOADING.getStatus());
        return tv;
    }
}
