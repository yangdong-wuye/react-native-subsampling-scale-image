package co.yangdong.reactnativelongimage;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import javax.annotation.Nullable;

public class LongImageViewManager extends SimpleViewManager<SubsamplingScaleImageView> {
    public static final String REACT_CLASS = "LongImageView";

    @Nullable
    private RequestBuilder<Bitmap> requestManager = null;

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected SubsamplingScaleImageView createViewInstance(@NonNull ThemedReactContext reactContext) {
        if (isValidContextForGlide(reactContext)) {
            requestManager = Glide.with(reactContext).asBitmap();
        }
        return new SubsamplingScaleImageView(reactContext);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "source")
    public void setSrc(SubsamplingScaleImageView view, @Nullable ReadableMap source) {
        if (requestManager != null && source != null && source.hasKey("uri") && !TextUtils.isEmpty(source.getString("uri"))) {
            requestManager.load(source.getString("uri"))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onLoadStarted(@androidx.annotation.Nullable Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                        }

                        @Override
                        public void onLoadFailed(@androidx.annotation.Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @androidx.annotation.Nullable Transition<? super Bitmap> transition) {
                            view.setImage(ImageSource.cachedBitmap(resource));
                        }

                        @Override
                        public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "quickScaleEnabled")
    public void setQuickScaleEnabled(SubsamplingScaleImageView view, boolean quickScaleEnabled) {
        view.setQuickScaleEnabled(quickScaleEnabled);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "zoomEnabled")
    public void setZoomEnabled(SubsamplingScaleImageView view, boolean zoomEnabled) {
        view.setZoomEnabled(zoomEnabled);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "minimumScale")
    public void setMinimumScale(SubsamplingScaleImageView view, int minimumScale) {
        view.setMinimumScaleType(minimumScale);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "panEnabled")
    public void setPanEnabled(SubsamplingScaleImageView view, boolean panEnabled) {
        view.setPanEnabled(panEnabled);
    }

    @Override
    public void onDropViewInstance(@NonNull SubsamplingScaleImageView view) {
        super.onDropViewInstance(view);
    }

    private static boolean isValidContextForGlide(final Context context) {
        Activity activity = getActivityFromContext(context);

        if (activity == null) {
            return false;
        }

        return !isActivityDestroyed(activity);
    }

    private static Activity getActivityFromContext(final Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }

        if (context instanceof ThemedReactContext) {
            final Context baseContext = ((ThemedReactContext) context).getBaseContext();
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }

            if (baseContext instanceof ContextWrapper) {
                final ContextWrapper contextWrapper = (ContextWrapper) baseContext;
                final Context wrapperBaseContext = contextWrapper.getBaseContext();
                if (wrapperBaseContext instanceof Activity) {
                    return (Activity) wrapperBaseContext;
                }
            }
        }

        return null;
    }

    private static boolean isActivityDestroyed(Activity activity) {
        return activity.isDestroyed() || activity.isFinishing();
    }
}
