package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yhy.widget.pv.PickerView;
import com.yhy.widgetdemo.emtity.TestEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-08 19:58
 * version: 1.0.0
 * desc   :
 */
public class PickViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_view);

        PickerView<TestEntity> pvTest = (PickerView<TestEntity>) findViewById(R.id.pv_test);

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
                Toast.makeText(PickViewActivity.this, data.name, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
