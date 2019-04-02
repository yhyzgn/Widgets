package com.yhy.widgetdemo.activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yhy.widget.core.recycler.div.RvDivider;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.adapter.RvTestAdapter;

import java.util.ArrayList;
import java.util.List;

public class RvActivity extends BaseActivity {
    private final List<String> TEST_STR_LIST = new ArrayList<>();
    private RecyclerView rvContent;
    private RvDivider mDivider;

    @Override
    protected int getLayout() {
        return R.layout.activity_rv;
    }

    @Override
    protected void initView() {
        rvContent = findViewById(R.id.rv_content);
    }

    @Override
    protected void initData() {
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

    @Override
    protected void initEvent() {

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
