package com.yhy.widget.helper;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 图片矩阵变换助手
 * <p>
 * Created on 2022-10-04 03:20
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ImageViewScaleMatrixHelper {

    private final ImageView mImageView;
    private final Matrix mMatrix;

    private ImageViewScaleMatrixHelper(ImageView imageView) {
        mImageView = imageView;
        mMatrix = imageView.getImageMatrix();
    }

    public static ImageViewScaleMatrixHelper with(ImageView imageView) {
        return new ImageViewScaleMatrixHelper(imageView);
    }

    public Matrix apply() {
        if (null == mImageView) {
            return mMatrix;
        }
        return fitScaleTranslate(mImageView.getDrawable());
    }

    private Matrix fitScaleTranslate(Drawable drawable) {
        if (null == drawable || null == mMatrix) {
            return mMatrix;
        }
        mMatrix.reset();

        ImageView.ScaleType scaleType = mImageView.getScaleType();

        float width = mImageView.getWidth() * 1.0f;
        float height = mImageView.getHeight() * 1.0f;

        float dWidth = drawable.getIntrinsicWidth() * 1.0f;
        float dHeight = drawable.getIntrinsicHeight() * 1.0f;

        switch (scaleType) {
            case FIT_START:
                fitStart(width, height, dWidth, dHeight);
                break;
            case FIT_XY:
                fitXY(width, height, dWidth, dHeight);
                break;
            case FIT_CENTER:
                fitCenter(width, height, dWidth, dHeight);
                break;
            case FIT_END:
                fitEnd(width, height, dWidth, dHeight);
                break;
            case CENTER:
                center(width, height, dWidth, dHeight);
                break;
            case CENTER_INSIDE:
                centerInside(width, height, dWidth, dHeight);
                break;
            case CENTER_CROP:
                centerCrop(width, height, dWidth, dHeight);
                break;
            case MATRIX:
                // 使用 Matrix 绘制图片。
                // Do nothing at present.
        }
        return mMatrix;
    }

    /**
     * {@link ImageView.ScaleType#FIT_START}
     * <p>
     * 保持图片的宽高比，对图片进行X和Y方向缩放，直到一个方向铺满 {@code ImageView} 。
     * <p>
     * 缩放后的图片与 {@code ImageView} 左上角对齐进行显示。
     *
     * @param w  控件宽度
     * @param h  控件高度
     * @param dw 图片宽度
     * @param dh 图片高度
     */
    private void fitStart(float w, float h, float dw, float dh) {
        float percentWidth = w / dw;
        float percentHeight = h / dh;
        float minPercent = Math.min(percentWidth, percentHeight);
        mMatrix.setScale(minPercent, minPercent);
        mMatrix.postTranslate(0, 0);
    }

    /**
     * {@link ImageView.ScaleType#FIT_XY}
     * <p>
     * 对 {@code X} 和 {@code Y} 方向独立缩放，直到图片铺满 {@code ImageView} 。
     * <p>
     * 这种方式可能会改变图片原本的宽高比，导致图片拉伸变形。
     *
     * @param w  控件宽度
     * @param h  控件高度
     * @param dw 图片宽度
     * @param dh 图片高度
     */
    private void fitXY(float w, float h, float dw, float dh) {
        float percentWidth = w / dw;
        float percentHeight = h / dh;
        mMatrix.setScale(percentWidth, percentHeight);
        mMatrix.postTranslate(0, 0);
    }

    /**
     * {@link ImageView.ScaleType#FIT_CENTER}
     * <p>
     * 保持图片的宽高比，对图片进行X和Y方向缩放，直到一个方向铺满 {@code ImageView} 。
     * <p>
     * 缩放后的图片居中显示在 {@code ImageView} 中。
     *
     * @param w  控件宽度
     * @param h  控件高度
     * @param dw 图片宽度
     * @param dh 图片高度
     */
    private void fitCenter(float w, float h, float dw, float dh) {
        float percentWidth = w / dw;
        float percentHeight = h / dh;
        float minPercent = Math.min(percentWidth, percentHeight);
        int targetWidth = Math.round(minPercent * dw);
        int targetHeight = Math.round(minPercent * dh);
        mMatrix.setScale(minPercent, minPercent);
        mMatrix.postTranslate((w - targetWidth) * 0.5f, (h - targetHeight) * 0.5f);
    }

    /**
     * {@link ImageView.ScaleType#FIT_END}
     * <p>
     * 保持图片的宽高比，对图片进行X和Y方向缩放，直到一个方向铺满 {@code ImageView} 。
     * <p>
     * 缩放后的图片与 {@code ImageView} 右下角对齐进行显示。
     *
     * @param w  控件宽度
     * @param h  控件高度
     * @param dw 图片宽度
     * @param dh 图片高度
     */
    private void fitEnd(float w, float h, float dw, float dh) {
        float percentWidth = w / dw;
        float percentHeight = h / dh;
        float minPercent = Math.min(percentWidth, percentHeight);
        int targetWidth = Math.round(minPercent * dw);
        int targetHeight = Math.round(minPercent * dh);
        mMatrix.setScale(minPercent, minPercent);
        mMatrix.postTranslate(w - targetWidth * 1.0f, h - targetHeight * 1.0f);
    }

    /**
     * {@link ImageView.ScaleType#CENTER}
     * <p>
     * 图片居中显示在 {@code ImageView} 中，不对图片进行缩放。
     *
     * @param w  控件宽度
     * @param h  控件高度
     * @param dw 图片宽度
     * @param dh 图片高度
     */
    private void center(float w, float h, float dw, float dh) {
        mMatrix.postTranslate((w - dw) * 0.5f, (h - dh) * 0.5f);
    }

    /**
     * {@link ImageView.ScaleType#CENTER_CROP}
     * <p>
     * 保持图片的宽高比，等比例对图片进行 {@code X} 和 {@code Y} 方向缩放，直到每个方向都大于等于 {@code ImageView} 对应的尺寸。
     * <p>
     * 缩放后的图片居中显示在 {@code ImageView} 中，超出部分做裁剪处理。
     *
     * @param w  控件宽度
     * @param h  控件高度
     * @param dw 图片宽度
     * @param dh 图片高度
     */
    private void centerCrop(float w, float h, float dw, float dh) {
        float percentWidth = w / dw;
        float percentHeight = h / dh;
        float maxPercent = Math.max(percentWidth, percentHeight);

        int targetWidth = Math.round(maxPercent * dw);
        int targetHeight = Math.round(maxPercent * dh);
        mMatrix.setScale(maxPercent, maxPercent);
        mMatrix.postTranslate((w - targetWidth) * 0.5f, (h - targetHeight) * 0.5f);
    }

    /**
     * {@link ImageView.ScaleType#CENTER_INSIDE}
     * <p>
     * 当 {@code 图片宽度 <= ImageView宽度 && 图片高度 <= ImageView高度} 时应用，不执行缩放，居中显示在 {@code ImageView} 中。
     * <p>
     * 其余情况按 {@code ScaleType.FIT_CENTER} 处理。
     *
     * @param w  控件宽度
     * @param h  控件高度
     * @param dw 图片宽度
     * @param dh 图片高度
     */
    private void centerInside(float w, float h, float dw, float dh) {
        if (dw <= w && dh <= h) {
            mMatrix.setScale(1.0f, 1.0f);
            mMatrix.setTranslate((w - dw) * 0.5f, (h - dh) * 0.5f);
        } else {
            fitCenter(w, h, dw, dh);
        }
    }
}
