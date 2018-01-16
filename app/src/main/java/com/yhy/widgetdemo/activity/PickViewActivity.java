package com.yhy.widgetdemo.activity;

import com.yhy.widget.core.picker.PickerView;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.entity.TestEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-08 19:58
 * version: 1.0.0
 * desc   :
 */
public class PickViewActivity extends BaseActivity {

    private PickerView<TestEntity> pvTest;

    @Override
    protected int getLayout() {
        return R.layout.activity_pick_view;
    }

    @Override
    protected void initView() {
        pvTest = findViewById(R.id.pv_test);
    }

    @Override
    protected void initData() {
        List<TestEntity> testList = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            testList.add(new TestEntity(i, "Data " + i));
        }

        pvTest.setData(testList, new PickerView.ItemProvider<TestEntity>() {
            @Override
            public String getItem(TestEntity data, int position) {
                return data.name;
            }
        }).setOnSelectListener(new PickerView.OnSelectListener<TestEntity>() {
            @Override
            public void onSelect(TestEntity data) {
                toast(data.name);
            }
        });
    }

    @Override
    protected void initEvent() {

    }
}
