package co.yangdong.subsamplingscaleimage;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class SubsamplingScaleImageView extends com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView {
    private AlphaAnimation mShowAnimation;

    public SubsamplingScaleImageView(Context context) {
        super(context);
    }

    protected void setShowAnimation(View view, int duration) {
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

    protected void setVisibleAnimation() {
        setShowAnimation(this, 300);
    }
}
