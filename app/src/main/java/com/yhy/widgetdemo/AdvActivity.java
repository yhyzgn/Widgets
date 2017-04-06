package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yhy.widget.adv.TextAdvView;
import com.yhy.widget.adv.abs.AbsAdvView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HongYi Yan on 2017/4/6 20:24.
 */
public class AdvActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);

        TextAdvView tavView = (TextAdvView) findViewById(R.id.tav_view);

        //设置集合
        List<String> items = new ArrayList<>();
        items.add("这是第1个");
        items.add("这是第2个");
        items.add("这是很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的第3个");
        items.add("这是第4个");
        items.add("这是第5个");
        items.add("这是第6个");
        items.add("这是第7个");
        tavView.setOnItemClickListener(new AbsAdvView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv = (TextView) view;
                Toast.makeText(AdvActivity.this, tv.getText() + "::" + position, Toast
                        .LENGTH_SHORT).show();
            }
        });
        tavView.startWithTextList(items);
        tavView.setCurrentItem(2);
    }
}
