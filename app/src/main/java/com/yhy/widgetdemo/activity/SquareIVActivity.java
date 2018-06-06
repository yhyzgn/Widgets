package com.yhy.widgetdemo.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yhy.widget.core.preview.ImgPreCfg;
import com.yhy.widget.core.preview.PreImgActivity;
import com.yhy.widget.core.img.SquareImageView;
import com.yhy.widget.core.recycler.div.RvDivider;
import com.yhy.widgetdemo.R;
import com.yhy.widgetdemo.activity.base.BaseActivity;
import com.yhy.widgetdemo.entity.ImgUrls;
import com.yhy.widgetdemo.utils.ImgUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-07 17:44
 * version: 1.0.0
 * desc   :
 */
public class SquareIVActivity extends BaseActivity {
    private SquareImageView sivDef;
    private SquareImageView sivAttrs;
    private RecyclerView rvCiv;

    @Override
    protected int getLayout() {
        return R.layout.activity_square_imageview;
    }

    @Override
    protected void initView() {
        sivDef = findViewById(R.id.siv_def);
        sivAttrs = findViewById(R.id.siv_attrs);
        rvCiv = findViewById(R.id.rv_civ);

        rvCiv.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @Override
    protected void initData() {
        ImgUtils.load(this, sivDef, ImgUrls.getAImgUrl());

        ImgUtils.load(this, sivAttrs, ImgUrls.getAImgUrl());

        rvCiv.setAdapter(new RvAdapter());
        rvCiv.addItemDecoration(new RvDivider.Builder(this).color(Color.TRANSPARENT).widthDp(2.0f).build());
    }

    @Override
    protected void initEvent() {
        sivDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgPreCfg<String> cfg = new ImgPreCfg<>((ImageView) view, ImgUrls.getAImgUrl());
                PreImgActivity.preview(SquareIVActivity.this, cfg);
            }
        });

        sivAttrs.setOnBtnClickListener(new SquareImageView.OnBtnClickListener() {
            @Override
            public void onClick(SquareImageView siv) {
                Toast.makeText(SquareIVActivity.this, "删除图片", Toast.LENGTH_SHORT).show();
            }
        });
        sivAttrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgPreCfg<String> cfg = new ImgPreCfg<>((ImageView) view, ImgUrls.getAImgUrl());
                PreImgActivity.preview(SquareIVActivity.this, cfg);
            }
        });
    }

    private class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
        private final List<String> mImgUrls = new ArrayList<>();

        RvAdapter() {
            for (int i = 0; i < 7; i++) {
                mImgUrls.add(ImgUrls.getAImgUrl());
            }
        }

        @NonNull
        @Override
        public RvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SquareIVActivity.this).inflate(R.layout.item_square_iv_rv, null);
            return new RvAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RvAdapter.ViewHolder holder, int position) {
            ImgUtils.load(holder.itemView.getContext(), (SquareImageView) holder.itemView, mImgUrls.get(position));

            if (position == 1) {
                ((SquareImageView) holder.itemView).setBtn(0);
            }

            ((SquareImageView) holder.itemView).setOnBtnClickListener(new SquareImageView.OnBtnClickListener() {
                @Override
                public void onClick(SquareImageView siv) {
                    Toast.makeText(SquareIVActivity.this, "删除第" + holder.getAdapterPosition() + "张图片", Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImgPreCfg<String> cfg = new ImgPreCfg<>((ImageView) view, mImgUrls.get(holder.getAdapterPosition()));
                    PreImgActivity.preview(SquareIVActivity.this, cfg);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mImgUrls.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
