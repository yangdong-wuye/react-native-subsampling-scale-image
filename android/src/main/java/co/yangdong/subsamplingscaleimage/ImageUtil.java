package co.yangdong.subsamplingscaleimage;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

public class ImageUtil {
    public static boolean isLongImg(int width, int height) {
        if (width <= 0 || height <= 0) {
            return false;
        }
        int newHeight = width * 3;
        return height > newHeight;
    }

    public static ImageView.ScaleType getImageScaleType(int scaleType) {
        ImageView.ScaleType[] scaleTypes = {
                ImageView.ScaleType.FIT_CENTER,
                ImageView.ScaleType.CENTER,
                ImageView.ScaleType.CENTER_INSIDE,
                ImageView.ScaleType.CENTER_CROP,
                ImageView.ScaleType.FIT_START,
                ImageView.ScaleType.FIT_END,
                ImageView.ScaleType.FIT_XY,
        };
        return scaleTypes[scaleType];
    }

    public static boolean isValidRequest(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return isDestroy(activity);
        } else if (context instanceof ContextWrapper){
            ContextWrapper contextWrapper = (ContextWrapper) context;
            if (contextWrapper.getBaseContext() instanceof Activity){
                Activity activity = (Activity) contextWrapper.getBaseContext();
                return isDestroy(activity);
            }
        }
        return false;
    }

    private static boolean isDestroy(Activity activity) {
        if (activity == null) {
            return true;
        }
        return activity.isFinishing() || activity.isDestroyed();
    }

    /**
     * 获取屏幕真实宽度
     *
     * @param context
     * @return
     */
    public static int getRealScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    /**
     * 获取屏幕真实高度
     *
     * @param context
     * @return
     */
    public static int getRealScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }
}
