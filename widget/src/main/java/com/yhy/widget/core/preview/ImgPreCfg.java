package com.yhy.widget.core.preview;

import android.view.View;

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
public class ImgPreCfg implements Serializable {
    private static final long serialVersionUID = 671615371513873774L;

    private boolean mDownloadable = ImgPreHelper.getInstance().isDownloadable();
    private int mDownloadIconId = ImgPreHelper.getInstance().getDownloadIconId();

    private final List<PreviewModel> mModelList = new ArrayList<>();

    private int mIvX;
    private int mIvY;
    private int mIvWidth;
    private int mIvHeight;
    private int mCurrent;

    /**
     * 构造函数
     *
     * @param view  点击的 View
     * @param model 预览条目
     */
    public ImgPreCfg(View view, PreviewModel model) {
        mModelList.clear();
        mModelList.add(model);
        calculateIv(view);
        mCurrent = 0;
    }

    /**
     * 构造函数
     *
     * @param view      点击的 View
     * @param modelList 预览条目集合
     */
    public ImgPreCfg(View view, List<PreviewModel> modelList) {
        this(view, modelList, 0);
    }

    /**
     * 构造函数
     *
     * @param view      点击的 View
     * @param modelList 预览条目集合
     * @param current   当前选中的索引
     */
    public ImgPreCfg(View view, List<PreviewModel> modelList, int current) {
        mModelList.clear();
        mModelList.addAll(modelList);
        calculateIv(view);
        mCurrent = current;
    }

    /**
     * 构造函数
     *
     * @param ivX      图片x坐标
     * @param ivY      图片y坐标
     * @param ivWidth  图片宽度
     * @param ivHeight 图片高度
     * @param model    预览条目
     */
    public ImgPreCfg(int ivX, int ivY, int ivWidth, int ivHeight, PreviewModel model) {
        mModelList.clear();
        mModelList.add(model);
        mIvX = ivX;
        mIvY = ivY;
        mIvWidth = ivWidth;
        mIvHeight = ivHeight;
        mCurrent = 0;
    }

    /**
     * 构造函数
     *
     * @param ivX       图片x坐标
     * @param ivY       图片y坐标
     * @param ivWidth   图片宽度
     * @param ivHeight  图片高度
     * @param modelList 预览条目合
     */
    public ImgPreCfg(int ivX, int ivY, int ivWidth, int ivHeight, List<PreviewModel> modelList) {
        this(ivX, ivY, ivWidth, ivHeight, modelList, 0);
    }

    /**
     * 构造函数
     *
     * @param ivX       图片x坐标
     * @param ivY       图片y坐标
     * @param ivWidth   图片宽度
     * @param ivHeight  图片高度
     * @param modelList 预览条目集合
     * @param current   当前选中的索引
     */
    public ImgPreCfg(int ivX, int ivY, int ivWidth, int ivHeight, List<PreviewModel> modelList, int current) {
        mModelList.clear();
        mModelList.addAll(modelList);
        mIvX = ivX;
        mIvY = ivY;
        mIvWidth = ivWidth;
        mIvHeight = ivHeight;
        mCurrent = current;
    }

    /**
     * 是否可以下载图片
     *
     * @param enable 是否可下载
     * @return 当前对象
     */
    public ImgPreCfg setDownloadable(boolean enable) {
        this.mDownloadable = enable;
        return this;
    }

    /**
     * 下载按钮图标
     *
     * @param iconId 图标id
     * @return 当前对象
     */
    public ImgPreCfg setDownloadIconId(int iconId) {
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
    public List<PreviewModel> getModelList() {
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
     * 计算点击的 View 的大小
     *
     * @param view 点击的 View
     */
    private void calculateIv(View view) {
        if (null != view) {
            // 如果传入ImageView，就按该ImageView计算
            mIvWidth = view.getWidth();
            mIvHeight = view.getHeight();

            int[] points = new int[2];
            view.getLocationInWindow(points);
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
