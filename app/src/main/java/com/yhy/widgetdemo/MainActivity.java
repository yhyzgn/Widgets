package com.yhy.widgetdemo;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yhy.widgetdemo.activity.AdvActivity;
import com.yhy.widgetdemo.activity.CheckBoxActivity;
import com.yhy.widgetdemo.activity.CheckedRLActivity;
import com.yhy.widgetdemo.activity.CheckedTVActivity;
import com.yhy.widgetdemo.activity.ConstraintImageActivity;
import com.yhy.widgetdemo.activity.ETVActivity;
import com.yhy.widgetdemo.activity.FlowLayoutActivity;
import com.yhy.widgetdemo.activity.GradientTextViewActivity;
import com.yhy.widgetdemo.activity.InputDialogActivity;
import com.yhy.widgetdemo.activity.LineTextViewActivity;
import com.yhy.widgetdemo.activity.PickViewActivity;
import com.yhy.widgetdemo.activity.PreviewImageActivity;
import com.yhy.widgetdemo.activity.RIVActivity;
import com.yhy.widgetdemo.activity.RvActivity;
import com.yhy.widgetdemo.activity.SettingsActivity;
import com.yhy.widgetdemo.activity.SliderActivity;
import com.yhy.widgetdemo.activity.SquareIVActivity;
import com.yhy.widgetdemo.activity.StatusDialogActivity;
import com.yhy.widgetdemo.activity.StepActivity;
import com.yhy.widgetdemo.activity.SwitchButtonActivity;
import com.yhy.widgetdemo.activity.TagFlowActivity;
import com.yhy.widgetdemo.activity.TitleActivity;
import com.yhy.widgetdemo.activity.WebHybridActivity;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.status.StatusActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final AbsListView.LayoutParams PARAMS = new AbsListView.LayoutParams(AbsListView
            .LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
    private static final List<String> WIDGET_NAME_LIST = new ArrayList<>();
    private static final List<Class> WIDGET_CLASS_LIST = new ArrayList<>();

    private ListView lvWidget;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        lvWidget = findViewById(R.id.lv_widget);
    }

    public void initData() {
        WIDGET_NAME_LIST.clear();
        WIDGET_CLASS_LIST.clear();

        WIDGET_NAME_LIST.add("AdvView");
        WIDGET_NAME_LIST.add("TitleBar");
        WIDGET_NAME_LIST.add("RvDivider");
        WIDGET_NAME_LIST.add("ExpandTextView");
        WIDGET_NAME_LIST.add("RoundCircleImageView");
        WIDGET_NAME_LIST.add("SwitchButton");
        WIDGET_NAME_LIST.add("SettingsItemView");
        WIDGET_NAME_LIST.add("PreImgActivity");
        WIDGET_NAME_LIST.add("PickView");
        WIDGET_NAME_LIST.add("StatusLayout");
        WIDGET_NAME_LIST.add("SliderLayout");
        WIDGET_NAME_LIST.add("CheckedTextView");
        WIDGET_NAME_LIST.add("SquareImageView");
        WIDGET_NAME_LIST.add("CheckedRelativeLayout");
        WIDGET_NAME_LIST.add("FlowLayout");
        WIDGET_NAME_LIST.add("TagFlowLayout");
        WIDGET_NAME_LIST.add("StepView");
        WIDGET_NAME_LIST.add("HybridBridge");
        WIDGET_NAME_LIST.add("ConstraintImageView");
        WIDGET_NAME_LIST.add("CheckBox");
        WIDGET_NAME_LIST.add("LineTextView");
        WIDGET_NAME_LIST.add("GradientTextView");
        WIDGET_NAME_LIST.add("StatusDialog");
        WIDGET_NAME_LIST.add("InputDialog");

        WIDGET_CLASS_LIST.add(AdvActivity.class);
        WIDGET_CLASS_LIST.add(TitleActivity.class);
        WIDGET_CLASS_LIST.add(RvActivity.class);
        WIDGET_CLASS_LIST.add(ETVActivity.class);
        WIDGET_CLASS_LIST.add(RIVActivity.class);
        WIDGET_CLASS_LIST.add(SwitchButtonActivity.class);
        WIDGET_CLASS_LIST.add(SettingsActivity.class);
        WIDGET_CLASS_LIST.add(PreviewImageActivity.class);
        WIDGET_CLASS_LIST.add(PickViewActivity.class);
        WIDGET_CLASS_LIST.add(StatusActivity.class);
        WIDGET_CLASS_LIST.add(SliderActivity.class);
        WIDGET_CLASS_LIST.add(CheckedTVActivity.class);
        WIDGET_CLASS_LIST.add(SquareIVActivity.class);
        WIDGET_CLASS_LIST.add(CheckedRLActivity.class);
        WIDGET_CLASS_LIST.add(FlowLayoutActivity.class);
        WIDGET_CLASS_LIST.add(TagFlowActivity.class);
        WIDGET_CLASS_LIST.add(StepActivity.class);
        WIDGET_CLASS_LIST.add(WebHybridActivity.class);
        WIDGET_CLASS_LIST.add(ConstraintImageActivity.class);
        WIDGET_CLASS_LIST.add(CheckBoxActivity.class);
        WIDGET_CLASS_LIST.add(LineTextViewActivity.class);
        WIDGET_CLASS_LIST.add(GradientTextViewActivity.class);
        WIDGET_CLASS_LIST.add(StatusDialogActivity.class);
        WIDGET_CLASS_LIST.add(InputDialogActivity.class);

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

    @Override
    protected void initEvent() {
        lvWidget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WIDGET_CLASS_LIST.get(position));
                startActivity(intent);
            }
        });
    }
}
