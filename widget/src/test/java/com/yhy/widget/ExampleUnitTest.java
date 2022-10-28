package com.yhy.widget;

import static org.junit.Assert.assertEquals;

import com.danikula.videocache.file.Md5FileNameGenerator;
import com.yhy.widget.component.downloader.FilenameGenerator;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testFilename() {
        String filename = "https://upload-images.jianshu.io/upload_images/5809200-a99419bb94924e6d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
        System.out.println(new Md5FileNameGenerator().generate(filename));
        System.out.println(FilenameGenerator.generate(filename));
        System.out.println(FilenameGenerator.generate("https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7a5971ea24504d2b89179e33499f4dff~tplv-k3u1fbpfcp-zoom-in-crop-mark:4536:0:0:0.awebp"));

        System.out.println(isInternetFile(filename));
        System.out.println(isInternetFile("http://p3-juejin.byteimg.com/storage/emulated/0/Download/视频_2022-5-1_18-56-3.mp4"));
        System.out.println(isInternetFile("ftps://p3-juejin.byteimg.com/storage/emulated/0/Download/视频_2022-5-1_18-56-3.mp4"));
        System.out.println(isInternetFile("ftp://p3-juejin.byteimg.com/storage/emulated/0/Download/视频_2022-5-1_18-56-3.mp4"));
        System.out.println(isInternetFile("storage/emulated/0/Download/视频_2022-5-1_18-56-3.mp4"));
        System.out.println(isInternetFile("/storage/emulated/0/Download/视频_2022-5-1_18-56-3.mp4"));
        System.out.println(isInternetFile("file:///storage/emulated/0/Download/视频_2022-5-1_18-56-3.mp4"));
    }

    private boolean isInternetFile(String url) {
        return url.matches("^((http)|(ftp)s?://).+");
    }
}