package co.yangdong.subsamplingscaleimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.react.views.view.ReactViewGroup;
import com.github.chrisbanes.photoview.PhotoView;


public class ScaleImageView extends ReactViewGroup {
    private final SubsamplingScaleImageView longImage;
    private final PhotoView simpleImage;
    private AlphaAnimation mShowAnimation;
    private float maxScale = 3;
    private float minScale = 1;
    private int minimumScaleType; // only longImage
    private int scaleType; // only simpleImage
    private Object source;
    private ImageLoadListener imageLoadListener;
    private boolean zoomEnabled = true;
    private final RequestBuilder<Bitmap> requestManager;

    public ScaleImageView(Context context) {
        super(context);

        requestManager = Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .onlyRetrieveFromCache(false)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL)
                .placeholder(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        longImage = new SubsamplingScaleImageView(context);
        longImage.setVisibility(View.GONE);
        longImage.setLayoutParams(lp);

        simpleImage = new PhotoView(context);
        simpleImage.setVisibility(View.GONE);
        simpleImage.setLayoutParams(lp);

        this.setLayoutParams(lp);
        this.addView(longImage);
        this.addView(simpleImage);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (longImage != null) {
            measureContentView(longImage, widthMeasureSpec, heightMeasureSpec);
        }

        if (simpleImage != null) {
            measureContentView(simpleImage, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean flag, int i, int j, int k, int l) {
        if (longImage != null) {
            longImage.layout(0, 0, longImage.getMeasuredWidth(), longImage.getMeasuredHeight());
        }

        if (simpleImage != null) {
            simpleImage.layout(0, 0, simpleImage.getMeasuredWidth(), simpleImage.getMeasuredHeight());
        }

    }

    private void measureContentView(View child,
                                    int parentWidthMeasureSpec,
                                    int parentHeightMeasureSpec) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                0, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                0, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }


    public void loadImage() {
        Context context = this.getContext();
        if (ImageUtil.isValidRequest(context)) {
            return;
        }

        requestManager
                .load(source)
                .into(new CustomViewTarget<View, Bitmap>(this) {
                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        imageLoadListener.onLoadCleared(placeholder);
                    }

                    @Override
                    protected void onResourceLoading(@Nullable Drawable placeholder) {
                        super.onResourceLoading(placeholder);
                        longImage.setVisibility(View.GONE);
                        simpleImage.setVisibility(View.GONE);
                        imageLoadListener.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        imageLoadListener.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();
                        boolean eqLongImage = ImageUtil.isLongImg(imageWidth, imageHeight);

                        longImage.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                        simpleImage.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);

                        if (eqLongImage) {
                            setVisibleAnimation(longImage);
                            longImage.setMinScale(minScale);
                            longImage.setMaxScale(maxScale);
                            longImage.setMinimumScaleType(minimumScaleType);
                            longImage.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_FIXED);
                            longImage.setZoomEnabled(zoomEnabled);
                            longImage.setPanEnabled(true);
                            longImage.setQuickScaleEnabled(true);
                            longImage.setImage(ImageSource.cachedBitmap(resource),
                                    new ImageViewState(0, new PointF(0, 0), 0));
                        } else {
                            setVisibleAnimation(simpleImage);
                            simpleImage.setMinimumScale(minScale);
                            simpleImage.setMaximumScale(maxScale);
                            simpleImage.setZoomable(zoomEnabled);
                            simpleImage.setScaleType(ImageUtil.getImageScaleType(scaleType));
                            simpleImage.setImageBitmap(resource);
                        }
                        imageLoadListener.onLoad(imageWidth, imageHeight, eqLongImage);
                    }
                });
    }

    protected void setShowAnimation( View view, int duration) {
        if (null == view || duration < 0)
        {
            return;
        }
        if (null != mShowAnimation)
        {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }

    protected void setVisibleAnimation(View view) {
        setShowAnimation(view, 300);
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    public void setMinimumScaleType(int minimumScaleType) {
        this.minimumScaleType = minimumScaleType;
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public void setImageLoadListener(ImageLoadListener listener) {
        this.imageLoadListener = listener;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }
}
