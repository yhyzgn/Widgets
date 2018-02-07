package com.yhy.widget.core.preview;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-09-21 14:01
 * version: 1.0.0
 * desc   : 图片预览器配置参数
 */
public class ImgPreCfg<T> implements Serializable {
    private static final long serialVersionUID = 671615371513873774L;

    private boolean mDownloadable = ImgPreHelper.getInstance().isDownloadable();
    private int mDownloadIconId = ImgPreHelper.getInstance().getDownloadIconId();

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
        calculateIv(imgView);
        this.mCurrent = 0;
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
        calculateIv(imgView);
        this.mCurrent = current;
    }

    /**
     * 构造函数
     *
     * @param ivX      图片x坐标
     * @param ivY      图片y坐标
     * @param ivWidth  图片宽度
     * @param ivHeight 图片高度
     * @param model    图片路径
     */
    public ImgPreCfg(int ivX, int ivY, int ivWidth, int ivHeight, T model) {
        List<T> temp = new ArrayList<>();
        temp.add(model);

        this.mModelList = temp;
        this.mIvX = ivX;
        this.mIvY = ivY;
        this.mIvWidth = ivWidth;
        this.mIvHeight = ivHeight;
        this.mCurrent = 0;
    }

    /**
     * 构造函数
     *
     * @param ivX       图片x坐标
     * @param ivY       图片y坐标
     * @param ivWidth   图片宽度
     * @param ivHeight  图片高度
     * @param modelList 图片路径集合
     */
    public ImgPreCfg(int ivX, int ivY, int ivWidth, int ivHeight, List<T> modelList) {
        this(ivX, ivY, ivWidth, ivHeight, modelList, 0);
    }

    /**
     * 构造函数
     *
     * @param ivX       图片x坐标
     * @param ivY       图片y坐标
     * @param ivWidth   图片宽度
     * @param ivHeight  图片高度
     * @param modelList 图片路径集合
     * @param current   当前选中的索引
     */
    public ImgPreCfg(int ivX, int ivY, int ivWidth, int ivHeight, List<T> modelList, int current) {
        this.mModelList = modelList;
        this.mCurrent = 0;
        this.mIvX = ivX;
        this.mIvY = ivY;
        this.mIvWidth = ivWidth;
        this.mIvHeight = ivHeight;
        this.mCurrent = current;
    }

    /**
     * 是否可以下载图片
     *
     * @param enable 是否可下载
     * @return 当前对象
     */
    public ImgPreCfg<T> setDownloadable(boolean enable) {
        this.mDownloadable = enable;
        return this;
    }

    /**
     * 下载按钮图标
     *
     * @param iconId 图标id
     * @return 当前对象
     */
    public ImgPreCfg<T> setDownloadIconId(int iconId) {
        this.mDownloadIconId = iconId;
        return this;
    }

    /**
     * 获取是否可下载
     *
     * @return 是否可下载
     */
    public boolean isDownloadable() {
        return mDownloadable;
    }

    /**
     * 获取下载按钮图标id
     *
     * @return 图标id
     */
    public int getDownloadIconId() {
        return mDownloadIconId;
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
}
