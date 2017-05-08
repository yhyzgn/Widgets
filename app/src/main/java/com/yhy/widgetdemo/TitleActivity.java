package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yhy.widget.iv.CircleImageView;
import com.yhy.widget.titb.TextTitleBar;
import com.yhy.widget.titb.abs.AbsNormalTitleBar;
import com.yhy.widgetdemo.utils.ImgUtils;

/**
 * Created by HongYi Yan on 2017/4/6 23:54.
 */
public class TitleActivity extends AppCompatActivity {

    private String url = "http://img.youguoquan" +
            ".com/uploads/magazine/content/ee4fc46fc0ca0b1272d5a9130c045cd5_magazine_web_m.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        TextTitleBar ttbTitle = (TextTitleBar) findViewById(R.id.ttb_title);
        CircleImageView civTest = (CircleImageView) findViewById(R.id.civ_test);
        ImageView ivTest = (ImageView) findViewById(R.id.iv_test);

        ttbTitle.support(this);

        ttbTitle.setCircleImgAdapter(new AbsNormalTitleBar.CircleImgAdapter() {
            @Override
            public boolean leftCiv(CircleImageView leftCiv) {
                ImgUtils.load(TitleActivity.this, leftCiv, url);

//                Glide.with(TitleActivity.this)
//                        .load(url)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .error(R.mipmap.ic_launcher)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)//使用磁盘缓存
//                        .skipMemoryCache(true)//跳过内存缓存
//                        .crossFade()//渐变切换
//                        .centerCrop()
//                        .into(leftCiv);

//                Picasso.with(TitleActivity.this)
//                        .load(url)
//                        .resize(50, 50)
//                        .centerCrop()
//                        .into(leftCiv);

                return true;
            }

            @Override
            public boolean rightCiv(CircleImageView rightCiv) {
                rightCiv.setImageResource(R.mipmap.ic_avatar);
                rightCiv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TitleActivity.this, "右", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });

        ImgUtils.load(this, civTest, url);
        ImgUtils.load(this, ivTest, url);
    }
}
