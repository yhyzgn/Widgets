package com.yhy.widgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yhy.widget.core.activity.ImgPreCfg;
import com.yhy.widget.core.activity.PreImgActivity;

import java.util.ArrayList;
import java.util.List;

public class PreviewImageActivity extends AppCompatActivity {

    private ImageView ivA;
    private ImageView ivB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);

        ivA = (ImageView) findViewById(R.id.iv_a);
        ivB = (ImageView) findViewById(R.id.iv_b);

        ivA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview(ivA);
            }
        });

        ivB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview(ivB);
            }
        });
    }

    private void preview(ImageView iv) {
        List<String> urlList = new ArrayList<>();
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg");
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/7b2a0fdbb23c9e63586b7ff6798dbebb_magazine_web_m.jpg");
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/c9c47160b46fceab5afd24dea7f216e6_magazine_web_m.jpg");
        urlList.add("http://img.youguoquan.com/uploads/magazine/content/fd986a6e0d5fa3a4485e5ce28f40b2ad_magazine_web_m.jpg");
        ImgPreCfg cfg = new ImgPreCfg(iv, urlList, 1);
        PreImgActivity.preview(this, cfg);
    }
}
