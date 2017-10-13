package com.yhy.widgetdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yhy.widget.core.adv.AdvView;
import com.yhy.widget.core.adv.adapter.SimpleAdvAdapter;
import com.yhy.widget.core.adv.adapter.TextAdvAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HongYi Yan onOrOff 2017/4/6 20:24.
 */
public class AdvActivity extends AppCompatActivity {

    private AdvView avSingle;
    private AdvView avMultiple;
    private List<String> mItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);

        avSingle = (AdvView) findViewById(R.id.av_view_single);
        avMultiple = (AdvView) findViewById(R.id.av_view_multiple);

        //设置集合
        mItems = new ArrayList<>();
        mItems.add("这是第1个");
        mItems.add("这是第2个");
        mItems.add("这是很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的第3个");
        mItems.add("这是第4个");
        mItems.add("这是第5个");
        mItems.add("这是第6个");
        mItems.add("这是第7个");

        // 单条目
        initSingle();

        // 多条目
        initMultiple();
    }

    private void initSingle() {
        avSingle.setAdapter(new TextAdvAdapter(this, mItems));

        avSingle.setOnItemClickListener(new AdvView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv = (TextView) view;
                Toast.makeText(AdvActivity.this, tv.getText() + "::" + position, Toast.LENGTH_SHORT).show();
            }
        });
        avSingle.setCurrentItem(2);
    }

    private void initMultiple() {
        SimpleAdvAdapter<String> avAdapter = new SimpleAdvAdapter<String>(this, mItems, 3) {
            @Override
            protected View getItemView(int position, String data) {
                TextView tv = new TextView(mCtx);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                tv.setText(data);
                tv.setTextColor(Color.DKGRAY);
                tv.setTextSize(14);
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);

                return tv;
            }
        };
        avAdapter.setOnItemClickListener(new SimpleAdvAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(SimpleAdvAdapter adapter, int position, String data) {
                Toast.makeText(AdvActivity.this, "position = " + position + ", " + data, Toast.LENGTH_SHORT).show();
            }
        });
        avMultiple.setAdapter(avAdapter);
    }
}
