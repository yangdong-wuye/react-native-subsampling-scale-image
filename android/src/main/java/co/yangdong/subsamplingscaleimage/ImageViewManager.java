package co.yangdong.subsamplingscaleimage;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

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

public class ImageViewManager extends SimpleViewManager<ImageView> {
    public static final String REACT_CLASS = "ImageView";
    static final String REACT_ON_LOAD_START_EVENT = "onImageLoadStart";
    static final String REACT_ON_LOAD_ERROR_EVENT = "onImageLoadError";
    static final String REACT_ON_LOAD_EVENT = "onImageLoad";
    static final String REACT_ON_LOAD_END_EVENT = "onImageLoadEnd";
    static final String REACT_ON_LOAD_CLEARED_EVENT = "onImageLoadCleared";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected ImageView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new ImageView(reactContext);
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

    @Override
    public void onDropViewInstance(@NonNull ImageView view) {
        super.onDropViewInstance(view);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "source")
    public void setSource(ImageView view, @Nullable ReadableMap source) {
        final ImageSource imageSource = ImageSourceConverter.getImageSource(view.getContext(), source);
        view.loadImage(imageSource.getSourceForLoad(), new ImageLoadListener() {
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

    }

    @SuppressWarnings("unused")
    @ReactProp(name = "minimumScaleType")
    public void setMaxScale(ImageView view, int minimumScaleType) {
        view.setMinimumScaleType(minimumScaleType);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "scaleType")
    public void setScaleType(ImageView view, int scaleType) {
        view.setScaleType(scaleType);
    }
}
