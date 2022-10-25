package com.yhy.widget.core.preview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.yhy.widget.R;

import java.util.HashMap;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:01
 * version: 1.0.0
 * desc   : 图片适配器
 */
public class PreImgAdapter extends PagerAdapter {
    private final Context mCtx;
    private final ImgPreCfg mCfg;
    private final Map<Integer, Jzvd> mPlayerMap;
    private View mPrimaryItem;

    /**
     * 构造函数
     *
     * @param ctx 上下文
     * @param cfg 配置参数
     */
    public PreImgAdapter(Context ctx, ImgPreCfg cfg) {
        mCtx = ctx;
        mCfg = cfg;
        mPlayerMap = new HashMap<>();
    }

    public Jzvd getPlayer(int position) {
        return mPlayerMap.get(position);
    }

    @Override
    public int getCount() {
        return mCfg.getModelList().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        mPrimaryItem = (View) object;
    }

    /**
     * 获取当前条目的View
     *
     * @return 当前条目的View
     */
    public View getPrimaryItem() {
        return mPrimaryItem;
    }

    /**
     * 获取当前条目的 View
     *
     * @return 当前条目的 View
     */
    public ImageView getPrimaryImgView() {
        ImageView pvImg = getPrimaryItem().findViewById(R.id.pv_img);
        JzvdStd jsPlayer = getPrimaryItem().findViewById(R.id.jz_video_player);
        return pvImg.getVisibility() == View.VISIBLE ? pvImg : jsPlayer.posterImageView;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.widget_item_pager_per_img, null);
        PhotoView pvImg = view.findViewById(R.id.pv_img);
        JzvdStd jsPlayer = view.findViewById(R.id.jz_video_player);
        ProgressBar pbLoading = view.findViewById(R.id.pb_loading);

        //设置点击事件
        view.setOnClickListener(v -> ((PreImgActivity) mCtx).finishWithAnim());
        pvImg.setOnPhotoTapListener((v, x, y) -> ((PreImgActivity) mCtx).finishWithAnim());
        pvImg.setOnOutsidePhotoTapListener(v -> ((PreImgActivity) mCtx).finishWithAnim());

        PreviewModel pm = mCfg.getModelList().get(position);

        //加载图片
        if (null == ImgPreHelper.getInstance().getLoader()) {
            throw new IllegalStateException("Must set ImgLoader at first.");
        }

        if (pm.getType() == PreviewModel.TYPE_IMAGE) {
            pvImg.setVisibility(View.VISIBLE);
            jsPlayer.setVisibility(View.GONE);

            ImgPreHelper.getInstance().getLoader().load(pvImg, pm.getUrl(), pbLoading);
        } else {
            pvImg.setVisibility(View.GONE);
            jsPlayer.setVisibility(View.VISIBLE);

            jsPlayer.setUp(pm.getUrl(), pm.getName());
            ImgPreHelper.getInstance().getLoader().load(jsPlayer.posterImageView, !TextUtils.isEmpty(pm.getThumbnail()) ? pm.getThumbnail() : R.mipmap.img_def_loading, pbLoading);
        }
        mPlayerMap.put(position, jsPlayer);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
