package com.yhy.widgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yhy.widget.sb.SwitchButton;

public class SwitchButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_button);

        SwitchButton switchButton = (SwitchButton) findViewById(R.id.switch_button);

        switchButton.onOrOff(true);
        switchButton.isOn();
        switchButton.toggle();     //switch state
        switchButton.toggle(false);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation
        switchButton.setOnStateChangeListener(new SwitchButton.OnStateChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isOn) {
                Toast.makeText(SwitchButtonActivity.this, "isOn = " + isOn, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
