package com.unipi.msc.raiseupandroid.Tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.unipi.msc.raiseupandroid.R;

public class ImageUtils {
    public static void loadProfileToImageView(Context c, String url, ImageView imageView){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_profile);

        Glide.with(c)
                .load(url)
                .apply(options)
                .into(imageView);

    }
}
