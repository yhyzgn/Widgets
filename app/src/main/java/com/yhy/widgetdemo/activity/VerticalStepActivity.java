package com.yhy.widgetdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yhy.widget.core.step.VerticalStepView;
import com.yhy.widget.core.step.abs.StepView;
import com.yhy.widget.core.step.adapter.StepAdapter;
import com.yhy.widget.core.step.entity.StepAble;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.entity.TestStep;
import com.yhy.widgetdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerticalStepActivity extends AppCompatActivity {

    private VerticalStepView<TestStep> vsvTest;

    private final List<TestStep> mStepList = new ArrayList<>();
    private VerticalAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view_vertical);

        vsvTest = findViewById(R.id.vsv_test);

        loadData();

        mAdapter = new VerticalAdapter();
        vsvTest.setAdapter(mAdapter);

        vsvTest.setOnItemClickListener(new StepView.OnItemClickListener<TestStep>() {
            @Override
            public void onItemClick(StepView<TestStep> parent, int position, TestStep data) {
                ToastUtils.shortT(data.text);
//                mStepList.remove(data);
//                mAdapter.notifiedDataChanged();
            }
        });
    }

    private void loadData() {
        mStepList.clear();

        mStepList.add(new TestStep("您已提交定单，等待系统确认"));
        mStepList.add(new TestStep("您的商品需要从外地调拨，我们会尽快处理，请耐心等待"));
        mStepList.add(new TestStep("您的订单已经进入亚洲第一仓储中心1号库准备出库"));
        mStepList.add(new TestStep("您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中"));
        mStepList.add(new TestStep("您的订单已打印完毕"));
        mStepList.add(new TestStep("您的订单已拣货完成"));
        mStepList.add(new TestStep("扫描员已经扫描"));
        mStepList.add(new TestStep("打包成功"));
        mStepList.add(new TestStep("您的订单在京东【华东外单分拣中心】发货完成，准备送往京东【北京通州分拣中心】"));
        mStepList.add(new TestStep("您的订单在京东【北京通州分拣中心】分拣完成"));
        mStepList.add(new TestStep("您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京中关村大厦站】"));
        mStepList.add(new TestStep("您的订单在京东【北京中关村大厦站】验货完成，正在分配配送员"));
        mStepList.add(new TestStep("配送员【包牙齿】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦", StepAble.Status.ATTENTION));
        mStepList.add(new TestStep("感谢你在京东购物，欢迎你下次光临！", StepAble.Status.DEFAULT));

        Collections.reverse(mStepList);
    }

    private class VerticalAdapter extends StepAdapter<TestStep> {
        public VerticalAdapter() {
            super(mStepList);
        }

        @Override
        public View getItem(StepView<TestStep> stepView, int position, TestStep data) {
            View view = LayoutInflater.from(VerticalStepActivity.this).inflate(R.layout.item_step_vertical, null);
            TextView tvTest = view.findViewById(R.id.tv_test);
            tvTest.setText(data.text);
            return view;
        }
    }
}
