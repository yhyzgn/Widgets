package com.yhy.widgetdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yhy.widget.core.activity.ImgPreCfg;
import com.yhy.widget.core.activity.PreImgActivity;
import com.yhy.widget.core.img.SquareImageView;
import com.yhy.widget.core.recycler.div.RvDivider;
import com.yhy.widgetdemo.utils.ImgUtils;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-07 17:44
 * version: 1.0.0
 * desc   :
 */
public class SquareIVActivity extends AppCompatActivity {
    private final String mUrl = "http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg";
    private SquareImageView sivDef;
    private SquareImageView sivAttrs;
    private RecyclerView rvCiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_imageview);

        sivDef = findViewById(R.id.siv_def);
        sivAttrs = findViewById(R.id.siv_attrs);
        rvCiv = findViewById(R.id.rv_civ);

        rvCiv.setLayoutManager(new GridLayoutManager(this, 4));

        ImgUtils.load(this, sivDef, mUrl);
        sivDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgPreCfg cfg = new ImgPreCfg((ImageView) view, mUrl);
                PreImgActivity.preview(SquareIVActivity.this, cfg);
            }
        });

        ImgUtils.load(this, sivAttrs, mUrl);
        sivAttrs.setOnBtnClickListener(new SquareImageView.OnBtnClickListener() {
            @Override
            public void onClick(SquareImageView siv) {
                Toast.makeText(SquareIVActivity.this, "删除图片", Toast.LENGTH_SHORT).show();
            }
        });
        sivAttrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgPreCfg cfg = new ImgPreCfg((ImageView) view, mUrl);
                PreImgActivity.preview(SquareIVActivity.this, cfg);
            }
        });

        rvCiv.setAdapter(new RvAdapter());
        rvCiv.addItemDecoration(new RvDivider.Builder(this).color(Color.TRANSPARENT).widthDp(2.0f).build());
    }

    private class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

        @Override
        public RvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SquareIVActivity.this).inflate(R.layout.item_square_iv_rv, null);
            return new RvAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RvAdapter.ViewHolder holder, final int position) {
            ImgUtils.load(holder.itemView.getContext(), (SquareImageView) holder.itemView, mUrl);

            ((SquareImageView) holder.itemView).setOnBtnClickListener(new SquareImageView.OnBtnClickListener() {
                @Override
                public void onClick(SquareImageView siv) {
                    Toast.makeText(SquareIVActivity.this, "删除第" + position + "张图片", Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImgPreCfg cfg = new ImgPreCfg((ImageView) view, mUrl);
                    PreImgActivity.preview(SquareIVActivity.this, cfg);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
