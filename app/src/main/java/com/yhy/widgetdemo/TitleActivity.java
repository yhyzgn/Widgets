package com.yhy.widgetdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yhy.widget.titb.TextTitleBar;
import com.yhy.widget.titb.abs.AbsNormalTitleBar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HongYi Yan on 2017/4/6 23:54.
 */
public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        TextTitleBar ttbTitle = (TextTitleBar) findViewById(R.id.ttb_title);
        ttbTitle.support(this);

        ttbTitle.setCircleImgAdapter(new AbsNormalTitleBar.CircleImgAdapter() {
            @Override
            public boolean leftCiv(CircleImageView leftCiv) {
                leftCiv.setImageResource(R.mipmap.ic_avatar);
                leftCiv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TitleActivity.this, "左", Toast.LENGTH_SHORT).show();
                    }
                });
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
    }
}
