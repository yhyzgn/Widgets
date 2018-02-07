package com.yhy.widget.core.preview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.yhy.widget.R;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:01
 * version: 1.0.0
 * desc   : 图片适配器
 */
public class PreImgAdapter extends PagerAdapter implements OnPhotoTapListener {
    private Context mCtx;
    private ImgPreCfg mCfg;
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
    }

    @Override
    public int getCount() {
        return mCfg.getModelList().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
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
     * 获取当前条目的ImageView
     *
     * @return 当前条目的ImageView
     */
    public ImageView getPrimaryImgView() {
        return (ImageView) getPrimaryItem().findViewById(R.id.pv_img);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.widget_item_pager_per_img, null);
        PhotoView pvImg = view.findViewById(R.id.pv_img);
        ProgressBar pbLoading = view.findViewById(R.id.pb_loading);

        //设置点击事件
        pvImg.setOnPhotoTapListener(this);

        //加载图片
        if (null == ImgPreHelper.getInstance().getLoader()) {
            throw new IllegalStateException("Must set ImgLoader at first.");
        }
        ImgPreHelper.getInstance().getLoader().load(pvImg, mCfg.getModelList().get(position), pbLoading);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ((PreImgActivity) mCtx).finishWithAnim();
    }
}
