package com.yhy.widgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yhy.widget.rv.div.RvDivider;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.adapter.RvTestAdapter;

import java.util.ArrayList;
import java.util.List;

public class RvActivity extends AppCompatActivity {
    private final List<String> TEST_STR_LIST = new ArrayList<>();
    private RecyclerView rvContent;
    private RvDivider mDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);

        rvContent = (RecyclerView) findViewById(R.id.rv_content);

        TEST_STR_LIST.add("Test 1");
        TEST_STR_LIST.add("Test 2");
        TEST_STR_LIST.add("Test 3");
        TEST_STR_LIST.add("Test 4");
        TEST_STR_LIST.add("Test 5");
        TEST_STR_LIST.add("Test 6");
        TEST_STR_LIST.add("Test 7");

        mDivider = new RvDivider.Builder(this)
                .widthDp(30)
                .color(getResources().getColor(R.color.colorPrimary))
                .type(RvDivider.DividerType.TYPE_WITH_START_END)
                .build();

//        horizontal();

//        vertical();

        gridview();
    }

    private void gridview() {
        rvContent.setLayoutManager(new GridLayoutManager(this, 4));

        rvContent.setAdapter(new RvTestAdapter(this, TEST_STR_LIST));
        rvContent.addItemDecoration(mDivider);
    }

    private void vertical() {
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvContent.setAdapter(new RvTestAdapter(this, TEST_STR_LIST));
        rvContent.addItemDecoration(mDivider);
    }

    private void horizontal() {
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvContent.setAdapter(new RvTestAdapter(this, TEST_STR_LIST));
        rvContent.addItemDecoration(mDivider);
    }
}
