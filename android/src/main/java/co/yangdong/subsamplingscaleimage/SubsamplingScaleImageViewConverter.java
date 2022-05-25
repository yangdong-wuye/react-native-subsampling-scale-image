package co.yangdong.subsamplingscaleimage;

import android.content.Context;

import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

public class SubsamplingScaleImageViewConverter {
    static SubsamplingScaleImageSource getImageSource(Context context, ReadableMap source) {
        return new SubsamplingScaleImageSource(context, source.getString("uri"), getHeaders(source));
    }

    static Headers getHeaders(ReadableMap source) {
        Headers headers = Headers.DEFAULT;

        if (source.hasKey("headers")) {
            ReadableMap headersMap = source.getMap("headers");
            ReadableMapKeySetIterator iterator = headersMap.keySetIterator();
            LazyHeaders.Builder builder = new LazyHeaders.Builder();

            while (iterator.hasNextKey()) {
                String header = iterator.nextKey();
                String value = headersMap.getString(header);

                builder.addHeader(header, value);
            }

            headers = builder.build();
        }

        return headers;
    }
}
