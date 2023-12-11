package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.unipi.msc.raiseupandroid.R;

public class ImageUtils {
    public static void loadProfileToImageView(Activity a, String url, ImageView imageView){
        RequestOptions options = new RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_profile);
        LazyHeaders headers = new LazyHeaders.Builder()
            .addHeader("Authorization", UserUtils.loadBearerToken(a))
            .build();
        try {
            Glide.with(a)
                    .load(new GlideUrl(url, headers))
                    .apply(options)
                    .into(imageView);
        }catch (Exception ignore){}
    }
}
