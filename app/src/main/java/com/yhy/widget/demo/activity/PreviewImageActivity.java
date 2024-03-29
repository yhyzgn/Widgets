package com.yhy.widget.demo.activity;

import android.widget.ImageView;

import com.yhy.widget.core.preview.ImgPreCfg;
import com.yhy.widget.core.preview.PreImgActivity;
import com.yhy.widget.core.preview.PreviewModel;
import com.yhy.widget.demo.R;
import com.yhy.widget.demo.activity.base.BaseActivity;
import com.yhy.widget.demo.entity.ImgUrls;
import com.yhy.widget.demo.utils.ImgUtils;

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
        ImgUtils.load(this, ivB, "/storage/emulated/0/Download/103340rc8v3xuddce27y8w.jpg");
    }

    @Override
    protected void initEvent() {
        ivA.setOnClickListener(v -> preview(ivA));

        ivB.setOnClickListener(v -> {
            ImgPreCfg cfg = new ImgPreCfg(v, new PreviewModel().setUrl(ImgUrls.getAImgUrl()).setType(PreviewModel.TYPE_IMAGE));
//                cfg.setDownloadable(false);
            PreImgActivity.preview(PreviewImageActivity.this, cfg);
        });
    }

    private void preview(ImageView iv) {
        List<PreviewModel> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(new PreviewModel().setName("图片" + (i + 1)).setUrl(ImgUrls.getAImgUrl()).setType(PreviewModel.TYPE_IMAGE));
        }
        list.add(new PreviewModel().setName("GG").setUrl("http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4").setType(PreviewModel.TYPE_VIDEO).setThumbnail(ImgUrls.getAImgUrl()));
        list.add(new PreviewModel().setName("图片--1234").setUrl(ImgUrls.getAImgUrl()).setType(PreviewModel.TYPE_IMAGE));
        list.add(new PreviewModel().setName("SIN_TEL").setUrl("http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4").setType(PreviewModel.TYPE_VIDEO).setThumbnail(ImgUrls.getAImgUrl()));
        list.add(new PreviewModel().setName("图片--34234").setUrl(ImgUrls.getAImgUrl()).setType(PreviewModel.TYPE_IMAGE));
        list.add(new PreviewModel().setName("试试").setUrl("/storage/emulated/0/Download/视频_2022-5-1_18-56-3.mp4").setType(PreviewModel.TYPE_VIDEO).setThumbnail("/storage/emulated/0/Download/v2-2af194dc7206f39c80bbb209bafbae28_r.jpg"));
        ImgPreCfg cfg = new ImgPreCfg(iv, list, 8);
//        cfg.setDownloadable(false);
//        cfg.setDownloadIconId(R.mipmap.ic_def_download);
        PreImgActivity.preview(this, cfg);
    }
}
