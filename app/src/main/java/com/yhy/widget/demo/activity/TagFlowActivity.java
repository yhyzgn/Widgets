package com.yhy.widget.demo.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yhy.widget.layout.flow.tag.TagFlowAdapter;
import com.yhy.widget.layout.flow.tag.TagFlowLayout;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;
import com.yhy.widget.demo.entity.TestEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-09 11:34
 * version: 1.0.0
 * desc   :
 */
public class TagFlowActivity extends BaseActivity {
    private final List<TestEntity> mTestList = new ArrayList<>();

    private TagFlowLayout<TestEntity> tflDef;
    private TagFlowLayout<TestEntity> tflLimit;
    private TagFlowLayout<TestEntity> tflSingleRadio;
    private TagFlowLayout<TestEntity> tflSingleCheck;
    private Adapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_tag_flow;
    }

    @Override
    protected void initView() {
        tflDef = $(R.id.tfl_def);
        tflLimit = $(R.id.tfl_limit);
        tflSingleRadio = $(R.id.tfl_single_radio);
        tflSingleCheck = $(R.id.tfl_single_check);
    }

    @Override
    protected void initData() {
        mTestList.add(new TestEntity(0, "张三张三张三张三张三"));
        mTestList.add(new TestEntity(1, "李四"));
        mTestList.add(new TestEntity(2, "大胖子"));
        mTestList.add(new TestEntity(3, "尼古拉斯"));
        mTestList.add(new TestEntity(4, "哈"));
        mTestList.add(new TestEntity(5, "大胖子"));
        mTestList.add(new TestEntity(6, "尼古拉斯"));
        mTestList.add(new TestEntity(7, "哈"));

        mAdapter = new Adapter(mTestList);
        mAdapter.setChecked(0);
        tflDef.setAdapter(mAdapter);

        tflLimit.setAdapter(new Adapter(mTestList));
        tflSingleRadio.setAdapter(new Adapter(mTestList).setChecked(1));
        tflSingleCheck.setAdapter(new Adapter(mTestList).setChecked(2));
    }

    @Override
    protected void initEvent() {
        tflDef.setOnCheckChangedListener(new TagFlowLayout.OnCheckChangedListener<TestEntity>() {
            @Override
            public void onChanged(boolean checked, int position, TestEntity data, List<TestEntity> dataList) {
                toast(dataList);
                mTestList.add(new TestEntity(9, "嘻嘻嘻" + position));
                mAdapter.notifyDataChanged();
            }
        });

        tflLimit.setOnCheckChangedListener(new TagFlowLayout.OnCheckChangedListener<TestEntity>() {
            @Override
            public void onChanged(boolean checked, int position, TestEntity data, List<TestEntity> dataList) {
                toast(dataList);
            }
        });

        tflSingleRadio.setOnCheckChangedListener(new TagFlowLayout.OnCheckChangedListener<TestEntity>() {
            @Override
            public void onChanged(boolean checked, int position, TestEntity data, List<TestEntity> dataList) {
                toast(dataList);
            }
        });

        tflSingleCheck.setOnCheckChangedListener(new TagFlowLayout.OnCheckChangedListener<TestEntity>() {
            @Override
            public void onChanged(boolean checked, int position, TestEntity data, List<TestEntity> dataList) {
                toast(dataList);
            }
        });
    }

    private void toast(List<TestEntity> dataList) {
        StringBuffer sb = new StringBuffer();
        for (TestEntity te : dataList) {
            sb.append("-").append(te.name);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        toast(sb.toString());
    }

    private class Adapter extends TagFlowAdapter<TestEntity> {
        public Adapter(List<TestEntity> dataList) {
            super(dataList);
        }

        @Override
        public View getView(TagFlowLayout parent, int position, TestEntity data) {
            TextView tv = (TextView) LayoutInflater.from(TagFlowActivity.this).inflate(R.layout.item_tag_flow, null);
            tv.setText(data.name);
            return tv;
        }
    }
}
