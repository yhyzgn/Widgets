package com.yhy.widget.component.downloader;

import android.text.TextUtils;

import com.yhy.widget.utils.WidgetCoreUtils;

/**
 * Created on 2022-10-28 16:21
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class FilenameGenerator {

    /**
     * 生成文件名
     *
     * @param filepath 原始路径
     * @return 文件名
     */
    public static String generate(String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            String md5 = WidgetCoreUtils.md5(filepath);
            String ext = WidgetCoreUtils.getExt(filepath);
            if (!TextUtils.isEmpty(ext)) {
                filepath = md5 + "." + ext;
            }
        }
        return filepath;
    }
}
