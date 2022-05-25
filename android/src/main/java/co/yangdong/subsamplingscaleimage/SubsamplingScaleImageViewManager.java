package co.yangdong.subsamplingscaleimage;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import javax.annotation.Nullable;

public class SubsamplingScaleImageViewManager extends SimpleViewManager<SubsamplingScaleImageView> {
    public static final String REACT_CLASS = "SubsamplingScaleImageView";
    static final String REACT_ON_LOAD_START_EVENT = "onSubsamplingScaleImageLoadStart";
    static final String REACT_ON_LOAD_ERROR_EVENT = "onSubsamplingScaleImageLoadError";
    static final String REACT_ON_LOAD_EVENT = "onSubsamplingScaleImageLoad";
    static final String REACT_ON_LOAD_END_EVENT = "onSubsamplingScaleImageLoadEnd";
    static final String REACT_ON_LOAD_CLEARED_EVENT = "onSubsamplingScaleImageLoadCleared";
    static final String REACT_ON_SCALE_CHANGED_EVENT = "onSubsamplingScaleImageScaleChanged";
    static final String REACT_ON_CENTER_CHANGED_EVENT = "onSubsamplingScaleImageCenterChanged";

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

    @androidx.annotation.Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(REACT_ON_LOAD_START_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_START_EVENT))
                .put(REACT_ON_LOAD_ERROR_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_ERROR_EVENT))
                .put(REACT_ON_LOAD_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_EVENT))
                .put(REACT_ON_LOAD_END_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_END_EVENT))
                .put(REACT_ON_LOAD_CLEARED_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_CLEARED_EVENT))
                .put(REACT_ON_SCALE_CHANGED_EVENT, MapBuilder.of("onScaleChanged", REACT_ON_SCALE_CHANGED_EVENT))
                .put(REACT_ON_CENTER_CHANGED_EVENT, MapBuilder.of("onCenterChanged", REACT_ON_CENTER_CHANGED_EVENT))
                .build();
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "source")
    public void setSrc(SubsamplingScaleImageView view, @Nullable ReadableMap source) {
        final SubsamplingScaleImageSource imageSource = SubsamplingScaleImageViewConverter.getImageSource(view.getContext(), source);

        if (requestManager != null && imageSource.getUri().toString().length() != 0) {
            view.setOnStateChangedListener(new SubsamplingScaleImageView.OnStateChangedListener() {
                @Override
                public void onScaleChanged(float newScale, int origin) {
                    WritableMap event = Arguments.createMap();
                    event.putDouble("newScale", newScale);
                    event.putDouble("origin", origin);
                    ReactContext reactContext = (ReactContext) view.getContext();
                    int viewId = view.getId();
                    reactContext.getJSModule(RCTEventEmitter.class)
                            .receiveEvent(viewId, REACT_ON_SCALE_CHANGED_EVENT, event);
                }

                @Override
                public void onCenterChanged(PointF newCenter, int origin) {
                    WritableMap event = Arguments.createMap();
                    WritableMap center = Arguments.createMap();
                    center.putDouble("x", newCenter.x);
                    center.putDouble("y", newCenter.y);
                    event.putMap("newCenter", center);
                    event.putDouble("origin", origin);
                    ReactContext reactContext = (ReactContext) view.getContext();
                    int viewId = view.getId();
                    reactContext.getJSModule(RCTEventEmitter.class)
                            .receiveEvent(viewId, REACT_ON_CENTER_CHANGED_EVENT, event);
                }
            });

            requestManager.load(imageSource.getSourceForLoad())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onLoadStarted(@androidx.annotation.Nullable Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            ReactContext reactContext = (ReactContext) view.getContext();
                            int viewId = view.getId();
                            reactContext.getJSModule(RCTEventEmitter.class)
                                    .receiveEvent(viewId, REACT_ON_LOAD_START_EVENT, new WritableNativeMap());
                        }

                        @Override
                        public void onLoadFailed(@androidx.annotation.Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            ReactContext reactContext = (ReactContext) view.getContext();
                            int viewId = view.getId();
                            reactContext.getJSModule(RCTEventEmitter.class)
                                    .receiveEvent(viewId, REACT_ON_LOAD_END_EVENT, new WritableNativeMap());
                            reactContext.getJSModule(RCTEventEmitter.class)
                                    .receiveEvent(viewId, REACT_ON_LOAD_ERROR_EVENT, new WritableNativeMap());
                        }

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @androidx.annotation.Nullable Transition<? super Bitmap> transition) {
                            view.setImage(ImageSource.cachedBitmap(resource), new ImageViewState(0, new PointF(0, 0), 0));
                            WritableMap event = Arguments.createMap();
                            event.putDouble("width", resource.getWidth());
                            event.putDouble("height", resource.getHeight());
                            ReactContext reactContext = (ReactContext) view.getContext();
                            int viewId = view.getId();
                            reactContext.getJSModule(RCTEventEmitter.class)
                                    .receiveEvent(viewId, REACT_ON_LOAD_EVENT, event);
                            reactContext.getJSModule(RCTEventEmitter.class)
                                    .receiveEvent(viewId, REACT_ON_LOAD_END_EVENT, new WritableNativeMap());
                        }

                        @Override
                        public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {
                            ReactContext reactContext = (ReactContext) view.getContext();
                            int viewId = view.getId();
                            reactContext.getJSModule(RCTEventEmitter.class)
                                    .receiveEvent(viewId, REACT_ON_LOAD_CLEARED_EVENT, new WritableNativeMap());
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
    @ReactProp(name = "minScale")
    public void setMinScale(SubsamplingScaleImageView view, int minScale) {
        view.setMinScale(minScale);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "maxScale")
    public void setMaxScale(SubsamplingScaleImageView view, int maxScale) {
        view.setMaxScale(maxScale);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "minimumScaleType")
    public void setMinimumScaleType(SubsamplingScaleImageView view, int minimumScaleType) {
        view.setMinimumScaleType(minimumScaleType);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "panEnabled")
    public void setPanEnabled(SubsamplingScaleImageView view, boolean panEnabled) {
        view.setPanEnabled(panEnabled);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "eagerLoadingEnabled")
    public void setEagerLoadingEnabled(SubsamplingScaleImageView view, boolean eagerLoadingEnabled) {
        view.setEagerLoadingEnabled(eagerLoadingEnabled);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "animateScale")
    public void animateScale(SubsamplingScaleImageView view, float animateScale) {
        view.animateScale(animateScale);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "animateCenter")
    public void animateCenter(SubsamplingScaleImageView view, ReadableMap animateCenter) {
        view.animateCenter(new PointF((float) animateCenter.getDouble("x"), (float) animateCenter.getDouble("y")));
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "animateScaleAndCenter")
    public void animateScaleAndCenter(SubsamplingScaleImageView view, ReadableMap options) {
        if (options != null && options.hasKey("animateScale") && options.hasKey("animateCenter")) {
            float animateScale = (float) options.getDouble("animateScale");
            ReadableMap animateCenter = options.getMap("animateCenter");
            if (animateCenter != null && animateCenter.hasKey("x") && animateCenter.hasKey("y")) {
                PointF pointF = new PointF((float) animateCenter.getDouble("x"), (float) animateCenter.getDouble("y"));
                view.animateScaleAndCenter(animateScale, pointF);
            }
        }
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "doubleTapZoomDpi")
    public void setDoubleTapZoomDpi(SubsamplingScaleImageView view, int dpi) {
        view.setDoubleTapZoomDpi(dpi);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "doubleTapZoomDuration")
    public void setDoubleTapZoomDuration(SubsamplingScaleImageView view, int durationMs) {
        view.setDoubleTapZoomDuration(durationMs);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "doubleTapZoomStyle")
    public void setDoubleTapZoomStyle(SubsamplingScaleImageView view, int durationMs) {
        view.setDoubleTapZoomStyle(durationMs);
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
