package com.yhy.widgetdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yhy.widget.titb.TextTitleBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final AbsListView.LayoutParams PARAMS = new AbsListView.LayoutParams(AbsListView
            .LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
    private static final List<String> WIDGET_NAME_LIST = new ArrayList<>();
    private static final List<Class> WIDGET_CLASS_LIST = new ArrayList<>();

    private TextTitleBar ttbTitle;
    private ListView lvWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttbTitle = (TextTitleBar) findViewById(R.id.ttb_title);
        lvWidget = (ListView) findViewById(R.id.lv_widget);

        ttbTitle.support(this);

        initData();

        initAdapter();

        initListener();
    }

    private void initListener() {
        lvWidget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WIDGET_CLASS_LIST.get(position));
                startActivity(intent);
            }
        });
    }

    private void initAdapter() {
        lvWidget.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return WIDGET_NAME_LIST.size();
            }

            @Override
            public String getItem(int position) {
                return WIDGET_NAME_LIST.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(MainActivity.this);
                tv.setTextSize(18);
                tv.setLayoutParams(PARAMS);
                tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                tv.setPadding(16, 36, 16, 36);
                tv.setText(getItem(position));
                return tv;
            }
        });
    }

    private void initData() {
        WIDGET_NAME_LIST.clear();
        WIDGET_CLASS_LIST.clear();

        WIDGET_NAME_LIST.add("AdvView");
        WIDGET_NAME_LIST.add("TitleBar");
        WIDGET_NAME_LIST.add("RvDivider");
        WIDGET_NAME_LIST.add("ExpandTextView");
        WIDGET_NAME_LIST.add("RoundCircleImageView");
        WIDGET_NAME_LIST.add("LoadingView");
        WIDGET_NAME_LIST.add("SwitchButton");
        WIDGET_NAME_LIST.add("SettingsItemView");
        WIDGET_NAME_LIST.add("PreImgActivity");

        WIDGET_CLASS_LIST.add(AdvActivity.class);
        WIDGET_CLASS_LIST.add(TitleActivity.class);
        WIDGET_CLASS_LIST.add(RvActivity.class);
        WIDGET_CLASS_LIST.add(ETVActivity.class);
        WIDGET_CLASS_LIST.add(RIVActivity.class);
        WIDGET_CLASS_LIST.add(LoadingActivity.class);
        WIDGET_CLASS_LIST.add(SwitchButtonActivity.class);
        WIDGET_CLASS_LIST.add(SettingsActivity.class);
        WIDGET_CLASS_LIST.add(PreviewImageActivity.class);
    }
}
