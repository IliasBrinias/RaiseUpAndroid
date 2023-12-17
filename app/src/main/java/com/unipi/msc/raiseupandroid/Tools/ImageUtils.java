package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.unipi.msc.raiseupandroid.R;

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
