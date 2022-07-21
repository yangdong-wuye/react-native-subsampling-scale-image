package co.yangdong.subsamplingscaleimage;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.transition.Transition;

public interface ImageLoadListener {
    void onLoadStarted(@Nullable Drawable placeholder);

    void onLoadFailed(@Nullable Drawable errorDrawable);

    void onLoad(int width, int height, boolean isLongImage);

    void onLoadCleared(@Nullable Drawable placeholder);
}
