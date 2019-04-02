package com.yhy.widget.core.preview;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhy.widget.R;
import com.yhy.widget.core.pager.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.viewpager.widget.ViewPager;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:01
 * version: 1.0.0
 * desc   : 图片预览页面
 */
public class PreImgActivity extends AppCompatActivity implements ViewTreeObserver.OnPreDrawListener {
    private static final long ANIMATE_DURATION = 200;
    private static final int REQ_CODE_PERMISSION = 1000;

    private ImgPreCfg mCfg;
    private RelativeLayout rlRoot;
    private HackyViewPager vpImg;
    private RelativeLayout rlFooter;
    private TextView tvItem;
    private ImageView ivDownload;
    private int mScreenWidth;
    private int mScreenHeight;
    private PreImgAdapter mAdapter;
    private int mImgHeight;
    private int mImgWidth;
    private boolean isAnimating;

    /**
     * 供外部调用的静态方法入口
     *
     * @param activity 触发调用的Activity
     * @param cfg      配置参数
     */
    public static void preview(Activity activity, ImgPreCfg cfg) {
        if (null == activity || null == cfg) {
            throw new IllegalArgumentException("Both of 2 arguments can not be null.");
        }
        Intent intent = new Intent(activity, PreImgActivity.class);
        intent.putExtra("cfg", cfg);

        //正式启动预览图片的Activity
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置透明状态栏
        setStatusBar();

        setContentView(R.layout.widget_activity_pre_img);

        rlRoot = findViewById(R.id.rl_root);
        vpImg = findViewById(R.id.vp_img);
        rlFooter = findViewById(R.id.rl_footer);
        tvItem = findViewById(R.id.tv_item);
        ivDownload = findViewById(R.id.iv_download);

        calculateScreenSize();

        mCfg = (ImgPreCfg) getIntent().getSerializableExtra("cfg");

        mAdapter = new PreImgAdapter(this, mCfg);
        vpImg.setAdapter(mAdapter);
        vpImg.setCurrentItem(mCfg.getCurrent());
        vpImg.getViewTreeObserver().addOnPreDrawListener(this);

        vpImg.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCfg.setCurrent(position);
                tvItem.setText(String.format(getString(R.string.pre_img_current_item), mCfg.getCurrent() + 1, mCfg.getModelList().size()));
            }
        });

        tvItem.setText(String.format(getString(R.string.pre_img_current_item), mCfg.getCurrent() + 1, mCfg.getModelList().size()));

        // 只有预览网络图片时才显示下载按钮
        ivDownload.setImageResource(mCfg.getDownloadIconId());
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndDownload();
            }
        });
    }

    private void checkPermissionAndDownload() {
        // 检查SD卡读取权限
        List<String> permissions = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(PreImgActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(PreImgActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

        if (null != permissions && !permissions.isEmpty()) {
            ActivityCompat.requestPermissions(PreImgActivity.this, permissions.toArray(new String[permissions.size()]), REQ_CODE_PERMISSION);
        } else {
            // 已授权
            ImgPreHelper.ImgDownloader downloader = ImgPreHelper.getInstance().getDownloader();
            if (null != downloader) {
                Object model = mCfg.getModelList().get(vpImg.getCurrentItem());
                // 只有预览网络图片时才能下载
                if (model instanceof String && isNetImg((String) model)) {
                    downloader.download(PreImgActivity.this, (String) model, ImgPreHelper.getInstance().getOnDownloadListener());
                    return;
                }
                throw new IllegalStateException("Unknown resource : [" + model + "]");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_PERMISSION) {
            checkPermissionAndDownload();
        }
    }

    /**
     * 是否是网络图片
     *
     * @return 是否是网络图片
     */
    private boolean isNetImg(String model) {
        return model.startsWith("http:") || model.startsWith("https:") || model.startsWith("ftp:");
    }

    /**
     * 在绘制前执行动画
     *
     * @return 如果返回false的话，刚开始出现的那一帧画面会被跳过，但是我们需要显示这第一帧，所以要返回true
     */
    @Override
    public boolean onPreDraw() {
        if (isAnimating) {
            return false;
        }
        rlRoot.getViewTreeObserver().removeOnPreDrawListener(this);
        final View view = mAdapter.getPrimaryItem();
        final ImageView ivImg = mAdapter.getPrimaryImgView();
        calculateIvSize(ivImg);

        final float vx = mCfg.getIvWidth() * 1.0f / mImgWidth;
        final float vy = mCfg.getIvHeight() * 1.0f / mImgHeight;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long playTime = animation.getCurrentPlayTime();
                float fraction = duration > 0 ? (float) playTime / duration : 1f;
                if (fraction > 1) fraction = 1;
                view.setTranslationX(evaluateFloat(fraction, mCfg.getIvX() + mCfg.getIvWidth() / 2 - ivImg.getWidth() / 2, 0));
                view.setTranslationY(evaluateFloat(fraction, mCfg.getIvY() + mCfg.getIvHeight() / 2 - ivImg.getHeight() / 2, 0));

                view.setScaleX(evaluateFloat(fraction, vx, 1));
                view.setScaleY(evaluateFloat(fraction, vy, 1));

                view.setAlpha(evaluateFloat(fraction, 0f, 1.0f));

                rlRoot.setBackgroundColor(evaluateArgb(fraction, Color.TRANSPARENT, Color.BLACK));
            }
        });
        addIntoListener(animator);
        animator.setDuration(ANIMATE_DURATION);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();

        //返回true，表示第一帧就开始绘制，否则会被跳过
        return true;
    }

    /**
     * 带动画结束Activity
     */
    public void finishWithAnim() {
        if (isAnimating) {
            return;
        }
        rlRoot.getViewTreeObserver().removeOnPreDrawListener(this);
        final View view = mAdapter.getPrimaryItem();
        final ImageView ivImg = mAdapter.getPrimaryImgView();
        calculateIvSize(ivImg);

        final float vx = mCfg.getIvWidth() * 1.0f / mImgWidth;
        final float vy = mCfg.getIvHeight() * 1.0f / mImgHeight;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long playTime = animation.getCurrentPlayTime();
                float fraction = duration > 0 ? (float) playTime / duration : 1f;
                if (fraction > 1) fraction = 1;
                view.setTranslationX(evaluateFloat(fraction, 0, mCfg.getIvX() + mCfg.getIvWidth() / 2 - ivImg.getWidth() / 2));
                view.setTranslationY(evaluateFloat(fraction, 0, mCfg.getIvY() + mCfg.getIvHeight() / 2 - ivImg.getHeight() / 2));

                view.setScaleX(evaluateFloat(fraction, 1, vx));
                view.setScaleY(evaluateFloat(fraction, 1, vy));

                view.setAlpha(evaluateFloat(fraction, 1.0f, 0f));

                rlRoot.setBackgroundColor(evaluateArgb(fraction, Color.BLACK, Color.TRANSPARENT));
            }
        });
        addOutListener(animator);
        animator.setDuration(ANIMATE_DURATION);
        animator.start();
    }

    @Override
    public void onBackPressed() {
        finishWithAnim();
    }

    /**
     * 进场动画过程监听
     */
    private void addIntoListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rlRoot.setBackgroundColor(getResources().getColor(R.color.alphaBlack));
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tvItem.setVisibility(mCfg.getModelList().size() > 1 ? View.VISIBLE : View.GONE);
                ivDownload.setVisibility(mCfg.isDownloadable() ? View.VISIBLE : View.GONE);
                rlFooter.setVisibility(tvItem.getVisibility() == View.VISIBLE || ivDownload.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 退场动画过程监听
     */
    private void addOutListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rlRoot.setBackgroundColor(0x0);
                rlFooter.setVisibility(View.GONE);
                tvItem.setVisibility(View.GONE);
                ivDownload.setVisibility(View.GONE);
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void setStatusBar() {
        Window window = getWindow();
        //清除系统提供的默认保护色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置系统UI的显示方式
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //添加属性可以自定义设置系统工具栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * 计算ImageView大小
     *
     * @param ivImg ImageView
     */
    private void calculateIvSize(ImageView ivImg) {
        // 获取真实大小
        Drawable drawable = ivImg.getDrawable();
        if (null != drawable) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            // 计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
            float w = mScreenWidth * 1.0f / intrinsicWidth;
            float h = mScreenHeight * 1.0f / intrinsicHeight;
            if (h > w) h = w;
            else w = h;

            // 得出当宽高至少有一个充满的时候图片对应的宽高
            mImgWidth = (int) (intrinsicWidth * w);
            mImgHeight = (int) (intrinsicHeight * h);
        }
    }

    private void calculateScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    /**
     * Integer 估值器
     *
     * @param fraction   当前比例
     * @param startValue 开始值
     * @param endValue   终点值
     * @return 当前值
     */
    private Integer evaluateInt(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

    /**
     * Float 估值器
     *
     * @param fraction   当前比例
     * @param startValue 开始值
     * @param endValue   终点值
     * @return 当前值
     */
    private Float evaluateFloat(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * Argb 估值器
     *
     * @param fraction   当前比例
     * @param startValue 开始值
     * @param endValue   终点值
     * @return 当前值
     */
    private int evaluateArgb(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24//
                | (startR + (int) (fraction * (endR - startR))) << 16//
                | (startG + (int) (fraction * (endG - startG))) << 8//
                | (startB + (int) (fraction * (endB - startB)));
    }
}
