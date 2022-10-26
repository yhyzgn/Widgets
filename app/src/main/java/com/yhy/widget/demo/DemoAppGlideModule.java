package com.yhy.widget.demo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created on 2022-10-26 11:01
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@GlideModule
public class DemoAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .set(GifOptions.DECODE_FORMAT, DecodeFormat.DEFAULT);
        builder.setDefaultRequestOptions(options);
    }
}
