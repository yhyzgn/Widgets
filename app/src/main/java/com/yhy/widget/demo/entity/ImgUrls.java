package com.yhy.widget.demo.entity;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-01-08 13:14
 * version: 1.0.0
 * desc   :
 */
public class ImgUrls {
    private static final Random RAND = new Random(System.currentTimeMillis());
    private static final List<String> IMG_URL_LIST = new ArrayList<>();

    public static void init(Context context) {
        try {
            InputStream is = context.getAssets().open("img-url.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            List<String> tempList = reader.lines().collect(Collectors.toList());
            if (!tempList.isEmpty()) {
                IMG_URL_LIST.addAll(tempList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAImgUrl() {
        if (IMG_URL_LIST.size() == 0) {
            throw new IllegalStateException("请先初始化 ImgUrls 类");
        }
        int index = RAND.nextInt(IMG_URL_LIST.size());
        return IMG_URL_LIST.get(index);
    }
}
