package com.yhy.widget.core.preview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yhy.widget.R;

import java.util.HashMap;
import java.util.Map;

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
    private final Map<Integer, StandardGSYVideoPlayer> mPlayerMap;
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

    public StandardGSYVideoPlayer getPlayer(int position) {
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
        StandardGSYVideoPlayer svpPlayer = getPrimaryItem().findViewById(R.id.svp_player);
        return pvImg.getVisibility() == View.VISIBLE || null == svpPlayer.getThumbImageView() || !(svpPlayer.getThumbImageView() instanceof ImageView) ? pvImg : (ImageView) svpPlayer.getThumbImageView();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.widget_item_pager_per_img, null);
        PhotoView pvImg = view.findViewById(R.id.pv_img);
        StandardGSYVideoPlayer svpPlayer = view.findViewById(R.id.svp_player);
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
            svpPlayer.setVisibility(View.GONE);

            ImgPreHelper.getInstance().getLoader().load(pvImg, pm.getUrl(), pbLoading);
        } else {
            pvImg.setVisibility(View.INVISIBLE);
            svpPlayer.setVisibility(View.VISIBLE);

            ImageView ivThumb = new ImageView(mCtx);
            ivThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            svpPlayer.setThumbImageView(ivThumb);
            ViewGroup.LayoutParams lp = ivThumb.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = (int) dpToPx(mCtx, 240);
            ivThumb.setLayoutParams(lp);
            svpPlayer.getThumbImageViewLayout().setVisibility(View.VISIBLE);
            svpPlayer.setThumbPlay(true);

            svpPlayer.setUp(pm.getUrl(), true, pm.getName());
            svpPlayer.setTag(pm.getThumbnail());
            svpPlayer.setAutoFullWithSize(true);
            svpPlayer.setShowFullAnimation(true);
            svpPlayer.setReleaseWhenLossAudio(true);
            svpPlayer.getTitleTextView().setVisibility(View.GONE);
            svpPlayer.getBackButton().setVisibility(View.GONE);
            //设置全屏按键功能
            svpPlayer.getFullscreenButton().setOnClickListener(v -> svpPlayer.startWindowFullscreen(mCtx, false, true));

            ImgPreHelper.getInstance().getLoader().load(ivThumb, !TextUtils.isEmpty(pm.getThumbnail()) ? pm.getThumbnail() : R.mipmap.img_def_loading, pbLoading);
        }
        mPlayerMap.put(position, svpPlayer);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit = Dimension.DP) int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
