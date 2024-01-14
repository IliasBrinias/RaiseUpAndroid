package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.unipi.msc.riseupandroid.R;

public class ImageUtils {
    public static void loadProfileToImageView(Activity a, String url, ImageView imageView){
        if (url == null) return;
        if (imageView == null) return;
        LazyHeaders headers = new LazyHeaders.Builder()
            .addHeader("Authorization", UserUtils.loadBearerToken(a))
            .build();

        imageView.setImageResource(R.drawable.ic_profile);

        Glide.with(imageView.getContext())
            .load(new GlideUrl(RetrofitUtils.BASE_URL + url, headers))
            .error(R.drawable.ic_profile)
            .apply(RequestOptions.skipMemoryCacheOf(true))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .fitCenter()
            .into(imageView);
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),matrix, true);
    }
}
