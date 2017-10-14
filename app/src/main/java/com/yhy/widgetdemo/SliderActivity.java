package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yhy.widget.layout.slider.SlideLayout;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-10-14 9:31
 * version: 1.0.0
 * desc   :
 */
public class SliderActivity extends AppCompatActivity {

    private SlideLayout slSlide;
    private TextView tvMenu;
    private ViewPager vpContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        slSlide = (SlideLayout) findViewById(R.id.sl_slide);
        tvMenu = (TextView) findViewById(R.id.tv_menu);
        vpContent = (ViewPager) findViewById(R.id.vp_content);

        vpContent.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = new ImageView(SliderActivity.this);
                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (position == 0) {
                    iv.setImageResource(R.mipmap.img_pager_1);
                } else if (position == 1) {
                    iv.setImageResource(R.mipmap.img_pager_2);
                } else if (position == 2) {
                    iv.setImageResource(R.mipmap.img_pager_3);
                } else {
                    iv.setImageResource(R.mipmap.img_pager_4);
                }
                container.addView(iv);
                return iv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slSlide.close();
            }
        });

        slSlide.setOnSlideEnableWatcher(new SlideLayout.OnSlideEnableWatcher() {
            @Override
            public boolean shouldEnable() {
                //第二页禁用侧边栏
                return vpContent.getCurrentItem() != 1;
            }
        });

        slSlide.setOnStateChangeListener(new SlideLayout.OnStateChangeListener() {
            @Override
            public void onOpened() {
                tvMenu.setText("已打开");
            }

            @Override
            public void onClosed() {
                tvMenu.setText("已关闭");
            }

            @Override
            public void onDragging(float percent, int dx, int total) {
                tvMenu.setText("比例：" + percent);
            }
        });
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
