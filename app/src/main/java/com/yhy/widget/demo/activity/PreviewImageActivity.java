package com.yhy.widget.demo.activity;

import android.view.View;
import android.widget.ImageView;

import com.yhy.widget.core.preview.ImgPreCfg;
import com.yhy.widget.core.preview.PreImgActivity;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;
import com.yhy.widget.demo.entity.ImgUrls;

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
                ImgPreCfg<String> cfg = new ImgPreCfg<>((ImageView) v, ImgUrls.getAImgUrl());
//                cfg.setDownloadable(false);
                PreImgActivity.preview(PreviewImageActivity.this, cfg);
            }
        });
    }

    private void preview(ImageView iv) {
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            urlList.add(ImgUrls.getAImgUrl());
        }
        ImgPreCfg<String> cfg = new ImgPreCfg<>(iv, urlList, 1);
//        cfg.setDownloadable(false);
//        cfg.setDownloadIconId(R.mipmap.ic_def_download);
        PreImgActivity.preview(this, cfg);
    }
}
