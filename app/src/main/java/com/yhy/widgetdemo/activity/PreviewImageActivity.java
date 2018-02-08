package com.yhy.widgetdemo.activity;

import android.view.View;
import android.widget.ImageView;

import com.yhy.widget.core.preview.ImgPreCfg;
import com.yhy.widget.core.preview.PreImgActivity;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PreviewImageActivity extends BaseActivity {

    private ImageView ivA;
    private ImageView ivB;

    @Override
    protected int getLayout() {
        return R.layout.activity_preview_image;
    }

    @Override
    protected void initView() {
        ivA = findViewById(R.id.iv_a);
        ivB = findViewById(R.id.iv_b);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        ivA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview(ivA);
            }
        });

        ivB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgPreCfg<String> cfg = new ImgPreCfg<>((ImageView) v, "http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg");
                PreImgActivity.preview(PreviewImageActivity.this, cfg);
            }
        });
    }

    private void preview(ImageView iv) {
        List<String> urlList = new ArrayList<>();
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg");
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/7b2a0fdbb23c9e63586b7ff6798dbebb_magazine_web_m.jpg");
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/c9c47160b46fceab5afd24dea7f216e6_magazine_web_m.jpg");
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/fd986a6e0d5fa3a4485e5ce28f40b2ad_magazine_web_m.jpg");
        ImgPreCfg<String> cfg = new ImgPreCfg<>(iv, urlList, 1);
        PreImgActivity.preview(this, cfg);
    }
}
