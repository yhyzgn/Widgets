package com.yhy.widgetdemo.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import com.yhy.widget.core.img.ConstraintImageView;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.entity.ImgUrls;
import com.yhy.widgetdemo.utils.ImgUtils;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-06-06 15:16
 * version: 1.0.0
 * desc   :
 */
public class ConstraintImageActivity extends BaseActivity {

    private ConstraintImageView civWidth;
    private ConstraintImageView civHeight;
    private ConstraintImageView civA;
    private ConstraintImageView civB;
    private ConstraintImageView civC;
    private ConstraintImageView civD;
    private ConstraintImageView civTest;
    private ConstraintImageView civTestRatio;

    @Override
    protected int getLayout() {
        return R.layout.activity_constraint_image_view;
    }

    @Override
    protected void initView() {
        civWidth = $(R.id.civ_width);
        civHeight = $(R.id.civ_height);
        civA = findViewById(R.id.civ_a);
        civB = findViewById(R.id.civ_b);
        civC = findViewById(R.id.civ_c);
        civD = findViewById(R.id.civ_d);
        civTest = findViewById(R.id.civ_test);
        civTestRatio = findViewById(R.id.civ_test_ratio);
    }

    @Override
    protected void initData() {
        ImgUtils.load(this, civWidth, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civHeight, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civA, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civB, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civC, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civD, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civTest, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civTestRatio, ImgUrls.getAImgUrl());
    }

    @Override
    protected void initEvent() {
    }
}
