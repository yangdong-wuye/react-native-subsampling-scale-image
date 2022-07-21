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
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class ImageView extends FrameLayout {
    private final androidx.appcompat.widget.AppCompatImageView imageView; // 普通图片
    private final SubsamplingScaleImageView longImageView; // 长图
    private int scaleType = 0;
    private int minimumScaleType = SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;
    private final RequestBuilder<Bitmap> requestManager;
    private AlphaAnimation mShowAnimation;

    public ImageView(@NonNull Context context) {
        super(context);
        requestManager = Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .onlyRetrieveFromCache(false)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL)
                .placeholder(new ColorDrawable(Color.TRANSPARENT));

        imageView = new androidx.appcompat.widget.AppCompatImageView(context);
        longImageView = new SubsamplingScaleImageView(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        longImageView.setVisibility(View.GONE);
        longImageView.setLayoutParams(lp);

        imageView.setVisibility(View.GONE);
        imageView.setLayoutParams(lp);


        this.setLayoutParams(lp);
        this.addView(longImageView);
        this.addView(imageView);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (longImageView != null) {
            longImageView.measure(widthMeasureSpec, heightMeasureSpec);
        }

        if (imageView != null) {
            imageView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean flag, int i, int j, int k, int l) {
        if (longImageView != null) {
            longImageView.layout(0, 0, longImageView.getMeasuredWidth(), longImageView.getMeasuredHeight());
        }

        if (imageView != null) {
            imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        }
    }

    public void loadImage(@NonNull Object url, ImageLoadListener listener) {
        Context context = this.getContext();
        if (ImageUtil.isValidRequest(context)) {
            return;
        }

        requestManager.load(url)
                .into(new CustomViewTarget<View, Bitmap>(this) {
                    @Override
                    protected void onResourceLoading(@Nullable Drawable placeholder) {
                        super.onResourceLoading(placeholder);
                        longImageView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        listener.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        listener.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        setResource(resource, listener);
                    }


                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        listener.onLoadCleared(placeholder);
                    }
                });
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }

    public void setMinimumScaleType(int minimumScaleType) {
        this.minimumScaleType = minimumScaleType;
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

    protected void setResource(@Nullable Bitmap resource, ImageLoadListener listener) {
        if (resource != null) {
            int imageWidth = resource.getWidth();
            int imageHeight = resource.getHeight();

            boolean eqLongImage = ImageUtil.isLongImg(imageWidth, imageHeight);

            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);

            if (eqLongImage) {
                setVisibleAnimation(longImageView);
                longImageView.setPanEnabled(false);
                longImageView.setZoomEnabled(false);
                longImageView.setQuickScaleEnabled(false);
                longImageView.setMinimumScaleType(minimumScaleType);
                longImageView.setImage(ImageSource.cachedBitmap(resource),
                        new ImageViewState(0, new PointF(0, 0), 0));
            } else {
                setVisibleAnimation(imageView);
                imageView.setScaleType(ImageUtil.getImageScaleType(scaleType));
                imageView.setImageBitmap(resource);
            }
            listener.onLoad(imageWidth, imageHeight, eqLongImage);
        }
    }
}
