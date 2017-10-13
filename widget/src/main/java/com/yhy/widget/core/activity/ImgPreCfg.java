package com.yhy.widget.core.activity;

import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:01
 * version: 1.0.0
 * desc   :
 */
public class ImgPreCfg<T> implements Serializable {
    private static ImgLoader loader;
    private int mIvX;
    private int mIvY;
    private int mIvWidth;
    private int mIvHeight;
    private List<T> mModelList;
    private int mCurrent;

    /**
     * 构造函数
     *
     * @param imgView 点击的ImageView
     * @param model   图片路径
     */
    public ImgPreCfg(ImageView imgView, T model) {
        List<T> temp = new ArrayList<>();
        temp.add(model);

        this.mModelList = temp;
        this.mCurrent = 0;
        calculateIv(imgView);
    }

    /**
     * 构造函数
     *
     * @param imgView   点击的ImageView
     * @param modelList 图片路径集合
     */
    public ImgPreCfg(ImageView imgView, List<T> modelList) {
        this(imgView, modelList, 0);
    }

    /**
     * 构造函数
     *
     * @param imgView   点击的ImageView
     * @param modelList 图片路径集合
     * @param current   当前选中的索引
     */
    public ImgPreCfg(ImageView imgView, List<T> modelList, int current) {
        this.mModelList = modelList;
        this.mCurrent = current;
        calculateIv(imgView);
    }

    /**
     * 获取图片加载器
     *
     * @return 图片加载器
     */
    public ImgLoader getLoader() {
        return loader;
    }

    /**
     * 获取图片路径集合
     *
     * @return 图片路径集合
     */
    public List<T> getModelList() {
        return mModelList;
    }

    /**
     * 设置当前条目索引
     *
     * @param current 当前条目索引
     */
    public void setCurrent(int current) {
        this.mCurrent = current;
    }

    /**
     * 获取当前条目索引
     *
     * @return 当前条目索引
     */
    public int getCurrent() {
        return mCurrent;
    }

    /**
     * 获取原图片控件X坐标
     *
     * @return 原图片控件X坐标
     */
    public int getIvX() {
        return mIvX;
    }

    /**
     * 获取原图片控件Y坐标
     *
     * @return 原图片控件Y坐标
     */
    public int getIvY() {
        return mIvY;
    }

    /**
     * 获取原图片控件宽度
     *
     * @return 原图片控件宽度
     */
    public int getIvWidth() {
        return mIvWidth;
    }

    /**
     * 获取原图片控件高度
     *
     * @return 原图片控件高度
     */
    public int getIvHeight() {
        return mIvHeight;
    }

    /**
     * 初始化，建议在Application中
     *
     * @param loader 图片加载器
     */
    public static void init(ImgLoader loader) {
        ImgPreCfg.loader = loader;
    }

    /**
     * 计算点击的ImageView的大小
     *
     * @param iv 点击的ImageView
     */
    private void calculateIv(ImageView iv) {
        if (null != iv) {
            // 如果传入ImageView，就按该ImageView计算
            mIvWidth = iv.getWidth();
            mIvHeight = iv.getHeight();

            int[] points = new int[2];
            iv.getLocationInWindow(points);
            mIvX = points[0];
            mIvY = points[1];
        } else {
            // 如果ImageView为空，就宽高设置为0
            mIvWidth = 0;
            mIvHeight = 0;
            // 并且把起点坐标设置0
            mIvX = 0;
            mIvY = 0;
        }
    }

    /**
     * 图片加载器
     */
    public interface ImgLoader {

        /**
         * 加载图片的方法
         *
         * @param iv        图片控件
         * @param model     图片路径
         * @param pbLoading 加载进度条，默认不显示
         * @param <T>       路径泛型参数
         */
        <T> void load(ImageView iv, T model, ProgressBar pbLoading);
    }
}
