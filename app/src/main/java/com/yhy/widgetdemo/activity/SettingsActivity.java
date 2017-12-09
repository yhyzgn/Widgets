package com.yhy.widgetdemo.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.yhy.widget.core.settings.SettingsItemView;
import com.yhy.widget.core.toggle.SwitchButton;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    private SettingsItemView sivSwitch;

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initView() {
        sivSwitch = (SettingsItemView) findViewById(R.id.siv_switch);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        sivSwitch.setOnSwitchStateChangeListener(new SettingsItemView.OnSwitchStateChangeListener() {
            @Override
            public void onStateChanged(SettingsItemView siv, SwitchButton sb, boolean isOn) {
                toast(siv.getName() + " :: isOn = " + isOn);
            }
        });
    }
}
