package com.yhy.widget.demo.entity;

import java.util.Random;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-01-08 13:14
 * version: 1.0.0
 * desc   :
 */
public class ImgUrls {
    private static final Random RAND = new Random(System.currentTimeMillis());

    final static String[] ICON_ARR = {
            "https://meitulu.me/p/3134-1b4f79.jpg",
            "https://meitulu.me/p/3134-9bde7a.jpg",
            "https://meitulu.me/p/3006-894c88.jpg",
            "https://meitulu.me/p/3142-52de53.jpg",
            "https://meitulu.me/p/3142-96436b.jpg",
            "https://meitulu.me/p/7517-5eb28d.jpg",
            "https://meitulu.me/p/7517-9a7a79.jpg",
            "https://meitulu.me/p/7517-3392dd.jpg",
            "https://meitulu.me/p/7517-f70f41.jpg",
            "https://meitulu.me/p/7517-960972.jpg",
            "https://meitulu.me/p/7517-e8fd3c.jpg",
            "https://meitulu.me/p/7517-ab23c9.jpg",
            "https://meitulu.me/p/7517-15bdf1.jpg",
            "https://meitulu.me/p/7517-09e8e0.jpg",
            "https://meitulu.me/p/3201-9ecd1a.jpg",
            "https://meitulu.me/p/3201-3af46f.jpg",
            "https://meitulu.me/p/3201-f9b756.jpg",
            "https://meitulu.me/p/3183-8b212f.jpg",
            "https://meitulu.me/p/3183-862f9a.jpg",
            "https://meitulu.me/p/3139-840f7a.jpg",
            "https://meitulu.me/p/3139-09c40c.jpg",
            "https://meitulu.me/p/3139-b65dc6.jpg",
            "https://meitulu.me/p/3139-750e03.jpg",
            "https://meitulu.me/p/3139-d05604.jpg",
            "https://meitulu.me/p/3139-6a8eee.jpg",
            "https://meitulu.me/p/3139-a59494.jpg",
            "https://meitulu.me/p/3139-014ff1.jpg",
            "https://meitulu.me/p/3139-c62697.jpg",

            "https://upload-images.jianshu.io/upload_images/5809200-a99419bb94924e6d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "https://upload-images.jianshu.io/upload_images/5809200-736bc3917fe92142.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "https://upload-images.jianshu.io/upload_images/5809200-7fe8c323e533f656.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "https://upload-images.jianshu.io/upload_images/5809200-c12521fbde6c705b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "https://upload-images.jianshu.io/upload_images/5809200-caf66b935fd00e18.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "https://upload-images.jianshu.io/upload_images/5809200-48dd99da471ffa3f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "https://upload-images.jianshu.io/upload_images/5809200-4de5440a56bff58f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "https://upload-images.jianshu.io/upload_images/5809200-03bbbd715c24750e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"

    };

    public static String getAImgUrl() {
        return ICON_ARR[RAND.nextInt(ICON_ARR.length)];
    }
}
