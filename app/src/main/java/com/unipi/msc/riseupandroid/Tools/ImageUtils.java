package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.unipi.msc.riseupandroid.R;

public class ImageUtils {
    public static void loadProfileToImageView(Activity a, String url, ImageView imageView){
        LazyHeaders headers = new LazyHeaders.Builder()
            .addHeader("Authorization", UserUtils.loadBearerToken(a))
            .build();

        Glide.with(imageView.getContext())
            .load(new GlideUrl(RetrofitUtils.BASE_URL + url, headers))
            .error(R.drawable.ic_profile)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .fitCenter()
            .into(imageView);
    }
}
