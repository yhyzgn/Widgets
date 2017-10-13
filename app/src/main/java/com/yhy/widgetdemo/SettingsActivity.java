package com.yhy.widgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yhy.widget.core.toggle.SwitchButton;
import com.yhy.widget.core.settings.SettingsItemView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsItemView sivSwitch = (SettingsItemView) findViewById(R.id.siv_switch);
        sivSwitch.setOnSwitchStateChangeListener(new SettingsItemView.OnSwitchStateChangeListener() {
            @Override
            public void onStateChanged(SettingsItemView siv, SwitchButton sb, boolean isOn) {
                Toast.makeText(SettingsActivity.this, siv.getName() + " :: isOn = " + isOn, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
