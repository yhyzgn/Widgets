package com.yhy.widget.demo.activity;

import android.view.View;
import android.widget.TextView;

import com.yhy.widget.core.checked.CheckBox;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-06-06 16:45
 * version: 1.0.0
 * desc   :
 */
public class CheckBoxActivity extends BaseActivity {

    private CheckBox cbDef;
    private CheckBox cbTest;
    private CheckBox cbCancel;
    private TextView tvCancel;

    @Override
    protected int getLayout() {
        return R.layout.activity_check_box;
    }

    @Override
    protected void initView() {
        cbDef = $(R.id.cb_def);
        cbTest = $(R.id.cb_test);
        cbCancel = $(R.id.cb_cancel);
        tvCancel = $(R.id.tv_cancel);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        cbDef.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CheckBox checkBox, boolean isChecked) {
                toast("第一个是否选中：" + isChecked);
            }
        });

        cbTest.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CheckBox checkBox, boolean isChecked) {
                toast("第二个是否选中：" + isChecked);
            }
        });

        cbCancel.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CheckBox checkBox, boolean isChecked) {
                toast("第三个是否选中：" + isChecked);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换
                cbCancel.toggle();
            }
        });
    }
}
