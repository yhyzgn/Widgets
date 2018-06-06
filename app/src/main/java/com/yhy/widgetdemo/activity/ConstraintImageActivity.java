package com.yhy.widgetdemo.activity;

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

    @Override
    protected int getLayout() {
        return R.layout.activity_constraint_image_view;
    }

    @Override
    protected void initView() {
        civWidth = $(R.id.civ_width);
        civHeight = $(R.id.civ_height);
    }

    @Override
    protected void initData() {
        ImgUtils.load(this, civWidth, ImgUrls.getAImgUrl());
        ImgUtils.load(this, civHeight, ImgUrls.getAImgUrl());
    }

    @Override
    protected void initEvent() {
    }
}
