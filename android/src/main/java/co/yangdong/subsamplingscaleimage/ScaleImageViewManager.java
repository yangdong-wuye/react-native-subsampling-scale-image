package co.yangdong.subsamplingscaleimage;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.transition.Transition;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import javax.annotation.Nullable;

public class ScaleImageViewManager extends ViewGroupManager<ScaleImageView> {
    public static final String REACT_CLASS = "ScaleImageView";
    static final String REACT_ON_LOAD_START_EVENT = "onScaleImageLoadStart";
    static final String REACT_ON_LOAD_ERROR_EVENT = "onScaleImageLoadError";
    static final String REACT_ON_LOAD_EVENT = "onScaleImageLoad";
    static final String REACT_ON_LOAD_END_EVENT = "onScaleImageLoadEnd";
    static final String REACT_ON_LOAD_CLEARED_EVENT = "onScaleImageLoadCleared";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected ScaleImageView createViewInstance(@NonNull ThemedReactContext reactContext) {
        ScaleImageView view = new ScaleImageView(reactContext);
        view.setImageLoadListener(new ImageLoadListener() {
            @Override
            public void onLoadStarted(@androidx.annotation.Nullable Drawable placeholder) {
                ReactContext reactContext = (ReactContext) view.getContext();
                int viewId = view.getId();
                reactContext.getJSModule(RCTEventEmitter.class)
                        .receiveEvent(viewId, REACT_ON_LOAD_START_EVENT, new WritableNativeMap());
            }

            @Override
            public void onLoadFailed(@androidx.annotation.Nullable Drawable errorDrawable) {
                ReactContext reactContext = (ReactContext) view.getContext();
                int viewId = view.getId();
                reactContext.getJSModule(RCTEventEmitter.class)
                        .receiveEvent(viewId, REACT_ON_LOAD_END_EVENT, new WritableNativeMap());
                reactContext.getJSModule(RCTEventEmitter.class)
                        .receiveEvent(viewId, REACT_ON_LOAD_ERROR_EVENT, new WritableNativeMap());
            }

            @Override
            public void onLoad(int width, int height, boolean isLongImage) {
                WritableMap event = Arguments.createMap();
                event.putDouble("width", width);
                event.putDouble("height", height);
                event.putBoolean("isLongImage", isLongImage);
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
        return view;
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(REACT_ON_LOAD_START_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_START_EVENT))
                .put(REACT_ON_LOAD_ERROR_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_ERROR_EVENT))
                .put(REACT_ON_LOAD_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_EVENT))
                .put(REACT_ON_LOAD_END_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_END_EVENT))
                .put(REACT_ON_LOAD_CLEARED_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_CLEARED_EVENT))
                .build();
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "source")
    public void setSource(ScaleImageView view, @Nullable ReadableMap source) {
        final ImageSource imageSource = ImageSourceConverter.getImageSource(view.getContext(), source);
        view.setSource(imageSource.getSourceForLoad());
        view.loadImage();
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "zoomEnabled")
    public void setMinScale(ScaleImageView view, boolean zoomEnabled) {
        view.setZoomEnabled(zoomEnabled);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "minScale")
    public void setMinScale(ScaleImageView view, float minScale) {
        view.setMinScale(minScale);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "maxScale")
    public void setMaxScale(ScaleImageView view, float maxScale) {
        view.setMaxScale(maxScale);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "minimumScaleType")
    public void setMaxScale(ScaleImageView view, int minimumScaleType) {
        view.setMinimumScaleType(minimumScaleType);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "scaleType")
    public void setScaleType(ScaleImageView view, int scaleType) {
        view.setScaleType(scaleType);
    }
}
