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
    private static final Random RAND = new Random();

    final static String[] ICON_ARR = {
            "http://img.youguoquan.com/uploads/magazine/content/4a5a82b75d606b6aa655a3d35cc4992b_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/ee4fc46fc0ca0b1272d5a9130c045cd5_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/20d77b6ccdbc0d12c5f42c1bfe51b211_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/9d8de6725819f37ac326603b671ee788_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/7ac9376449feabe6bdca62535a037d76_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/4297924d06bb1699cccf641fa461a3fb_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/a811c176420a20f8e035fc3679f19a10_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/f25f13f7b9a1dd3f1f95504060b24a03_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/d9c23953cf453550e53f62da99021e21_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/c5fe93b6fdfbd1e44ef93f0260c0ea34_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/7b2a0fdbb23c9e63586b7ff6798dbebb_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/78b3d95fcb669210d214202703eb3c82_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/94504e0e41d1852f5bf9b1da347261e4_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/c9c47160b46fceab5afd24dea7f216e6_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/b8c96c13d94405dcce496e2bcd7e67dc_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/9e3393bdaaf52c49e2837df2bd7973ad_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/bc00ef1595e75820718c1dad1483c962_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/3d3d6f738532fd96113898d0f04c1af3_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/91dfb9f7e10036547b73e3867e8a9ee6_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/b99de2be8f74c784f5f9bf7b51c85556_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/c4bad3d7b2cdfbe3575f48ca3893ef34_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/fd986a6e0d5fa3a4485e5ce28f40b2ad_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/e1bdef3c69698ba482417e117e590422_magazine_web_m.jpg",
            "http://img.youguoquan.com/uploads/magazine/content/a62e045853ac7237c70a1e989caef932_magazine_web_m.jpg"
    };

    public static String getAImgUrl() {
        return ICON_ARR[RAND.nextInt(ICON_ARR.length)];
    }
}
