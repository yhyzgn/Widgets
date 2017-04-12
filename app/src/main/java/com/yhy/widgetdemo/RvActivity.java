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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);

        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.addItemDecoration(new RvDivider(this, 2, R.color.colorPrimary, RvDivider
                .TYPE_CONTENT));

        TEST_STR_LIST.add("Test 1");
        TEST_STR_LIST.add("Test 2");
        TEST_STR_LIST.add("Test 3");
        TEST_STR_LIST.add("Test 4");
        TEST_STR_LIST.add("Test 5");
        TEST_STR_LIST.add("Test 6");
        TEST_STR_LIST.add("Test 7");

        horizontal();
    }

    private void horizontal() {
//        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
//                false));

        rvContent.setLayoutManager(new GridLayoutManager(this, 3));

        rvContent.setAdapter(new RvTestAdapter(this, TEST_STR_LIST));
    }
}
