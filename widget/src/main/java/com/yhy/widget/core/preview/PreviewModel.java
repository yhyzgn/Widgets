package com.yhy.widget.core.preview;

import java.io.Serializable;

/**
 * Created on 2022-10-25 21:45
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class PreviewModel implements Serializable {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    private String name;
    private String url;
    private int type;
    private String thumbnail;

    public String getName() {
        return name;
    }

    public PreviewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PreviewModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getType() {
        return type;
    }

    public PreviewModel setType(int type) {
        if (type != TYPE_IMAGE && type != TYPE_VIDEO) {
            throw new IllegalArgumentException("未知的预览文件类型");
        }
        this.type = type;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public PreviewModel setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
}
