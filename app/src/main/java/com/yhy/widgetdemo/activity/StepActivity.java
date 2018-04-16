package com.yhy.widgetdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yhy.widget.core.step.StepView;
import com.yhy.widget.core.step.adapter.StepAdapter;
import com.yhy.widget.core.step.entity.StepAble;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.entity.TestStep;
import com.yhy.widgetdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StepActivity extends BaseActivity {

    private StepView<TestStep> svHorizontal;
    private StepView<TestStep> svVertical;

    private final List<TestStep> mStepVerticalList = new ArrayList<>();
    private final List<TestStep> mStepHorizontalList = new ArrayList<>();

    private VerticalAdapter mVerticalAdapter;
    private HorizontalAdapter mHorizontalAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_step_view_vertical;
    }

    @Override
    protected void initView() {
        svHorizontal = findViewById(R.id.sv_horizontal);
        svVertical = findViewById(R.id.sv_vertical);
    }

    @Override
    protected void initData() {
        loadData();

        // 水平
        mHorizontalAdapter = new HorizontalAdapter();
        svHorizontal.setAdapter(mHorizontalAdapter);

        // 垂直
        mVerticalAdapter = new VerticalAdapter();
        svVertical.setAdapter(mVerticalAdapter);
    }

    @Override
    protected void initEvent() {
        svHorizontal.setOnItemClickListener(new StepView.OnItemClickListener<TestStep>() {
            @Override
            public void onItemClick(StepView<TestStep> parent, int position, TestStep data) {
                toast(data.text);
            }
        });

        svVertical.setOnItemClickListener(new StepView.OnItemClickListener<TestStep>() {
            @Override
            public void onItemClick(StepView<TestStep> parent, int position, TestStep data) {
                toast(data.text);
            }
        });
    }

    private void loadData() {
        mStepVerticalList.clear();
        mStepVerticalList.add(new TestStep("您已提交定单，等待系统确认"));
        mStepVerticalList.add(new TestStep("您的商品需要从外地调拨，我们会尽快处理，请耐心等待"));
        mStepVerticalList.add(new TestStep("您的订单已经进入亚洲第一仓储中心1号库准备出库"));
        mStepVerticalList.add(new TestStep("您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中"));
        mStepVerticalList.add(new TestStep("您的订单已打印完毕"));
        mStepVerticalList.add(new TestStep("您的订单已拣货完成"));
        mStepVerticalList.add(new TestStep("扫描员已经扫描"));
        mStepVerticalList.add(new TestStep("打包成功"));
        mStepVerticalList.add(new TestStep("您的订单在京东【华东外单分拣中心】发货完成，准备送往京东【北京通州分拣中心】"));
        mStepVerticalList.add(new TestStep("您的订单在京东【北京通州分拣中心】分拣完成"));
        mStepVerticalList.add(new TestStep("您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京中关村大厦站】"));
        mStepVerticalList.add(new TestStep("您的订单在京东【北京中关村大厦站】验货完成，正在分配配送员"));
        mStepVerticalList.add(new TestStep("配送员【哈哈哈】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦", StepAble.Status.CURRENT));
        mStepVerticalList.add(new TestStep("感谢你在京东购物，欢迎你下次光临！", StepAble.Status.DEFAULT));
        Collections.reverse(mStepVerticalList);

        mStepHorizontalList.clear();
        mStepHorizontalList.add(new TestStep("注册/登录"));
        mStepHorizontalList.add(new TestStep("完善个人信息"));
        mStepHorizontalList.add(new TestStep("开通VIP会员"));
        mStepHorizontalList.add(new TestStep("在线申请贷款"));
        mStepHorizontalList.add(new TestStep("提交个人资质信息"));
        mStepHorizontalList.add(new TestStep("交手续费", StepAble.Status.CURRENT));
        mStepHorizontalList.add(new TestStep("贷款成功", StepAble.Status.DEFAULT));
    }

    private class HorizontalAdapter extends StepAdapter<TestStep> {
        public HorizontalAdapter() {
            super(mStepHorizontalList);
        }

        @Override
        public View getItem(StepView<TestStep> stepView, int position, TestStep data) {
            View view = LayoutInflater.from(StepActivity.this).inflate(R.layout.item_step_horizontal, null);
            TextView tvTest = view.findViewById(R.id.tv_test);
            tvTest.setText(data.text);
            return view;
        }
    }

    private class VerticalAdapter extends StepAdapter<TestStep> {
        public VerticalAdapter() {
            super(mStepVerticalList);
        }

        @Override
        public View getItem(StepView<TestStep> stepView, int position, TestStep data) {
            View view = LayoutInflater.from(StepActivity.this).inflate(R.layout.item_step_vertical, null);
            TextView tvTest = view.findViewById(R.id.tv_test);
            tvTest.setText(data.text);
            if (data.getStatus() == StepAble.Status.COMPLETE) {
                tvTest.setTextColor(Color.parseColor("#00beaf"));
            } else if (data.getStatus() == StepAble.Status.CURRENT) {
                tvTest.setTextColor(Color.parseColor("#ff7500"));
            } else {
                tvTest.setTextColor(Color.parseColor("#dcdcdc"));
            }
            return view;
        }
    }
}
