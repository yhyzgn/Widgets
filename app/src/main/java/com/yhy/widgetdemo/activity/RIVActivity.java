package com.yhy.widgetdemo.activity;

import android.widget.ImageView;

import com.yhy.widget.core.img.round.CircleImageView;
import com.yhy.widget.core.img.round.RoundImageView;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.entity.ImgUrls;
import com.yhy.widgetdemo.utils.ImgUtils;

public class RIVActivity extends BaseActivity {

    private CircleImageView civAvatar;
    private RoundImageView rivA;
    private RoundImageView rivB;
    private RoundImageView rivC;
    private RoundImageView rivD;
    private RoundImageView rivTest;
    private ImageView ivTest;

    @Override
    protected int getLayout() {
        return R.layout.activity_riv;
    }

    @Override
    protected void initView() {
        civAvatar = findViewById(R.id.civ_avatar);
        rivA = findViewById(R.id.riv_a);
        rivB = findViewById(R.id.riv_b);
        rivC = findViewById(R.id.riv_c);
        rivD = findViewById(R.id.riv_d);
        rivTest = findViewById(R.id.riv_test);
        ivTest = findViewById(R.id.iv_test);
    }

    @Override
    protected void initData() {
        ImgUtils.load(this, civAvatar, ImgUrls.getAImgUrl());
        ImgUtils.load(this, rivA, ImgUrls.getAImgUrl());
        ImgUtils.load(this, rivB, ImgUrls.getAImgUrl());
        ImgUtils.load(this, rivC, ImgUrls.getAImgUrl());
        ImgUtils.load(this, rivD, ImgUrls.getAImgUrl());

        String testUrl = ImgUrls.getAImgUrl();
        ImgUtils.load(this, rivTest, testUrl);
        ImgUtils.load(this, ivTest, testUrl);
    }

    @Override
    protected void initEvent() {
    }
}
