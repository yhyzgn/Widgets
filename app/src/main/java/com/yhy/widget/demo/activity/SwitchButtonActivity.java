package com.yhy.widget.demo.activity;

import com.yhy.widget.core.toggle.SwitchButton;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;

public class SwitchButtonActivity extends BaseActivity {

    private SwitchButton switchButton;

    @Override
    protected int getLayout() {
        return R.layout.activity_switch_button;
    }

    @Override
    protected void initView() {
        switchButton = (SwitchButton) findViewById(R.id.switch_button);

        switchButton.onOrOff(true);
        switchButton.isOn();
        switchButton.toggle();     //switch state
        switchButton.toggle(false);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        switchButton.setOnStateChangeListener(new SwitchButton.OnStateChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isOn) {
                toast("isOn = " + isOn);
            }
        });
    }
}
