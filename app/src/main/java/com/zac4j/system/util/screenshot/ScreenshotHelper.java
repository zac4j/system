package com.zac4j.system.util.screenshot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import com.zac4j.system.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND;
import static com.zac4j.system.util.ReflectUtils.getFieldValue;
import static com.zac4j.system.util.Utils.isEmpty;

/**
 * This implementation comes from Falcon library -> https://github.com/jraska/Falcon.
 * positive is its screenshot result contain current shown dialog;
 * limit is can't take screenshot if the view is Android {@link SurfaceView} or Google Maps etc, and
 * use dangerous method reflection to get screen info.
 *
 * @author: zac
 * @date: 2019-06-16
 */
public class ScreenshotHelper {

    /**
     * Take screenshot of given activity.
     */
    public static Bitmap takeScreenshot(Activity activity) {
        int[] screenSize = new int[2];
        Utils.getScreenSize(activity, screenSize);

        final Bitmap bitmap = Bitmap.createBitmap(screenSize[0], screenSize[1],
            Bitmap.Config.ARGB_8888);
        drawViews(getScreenViews(activity), bitmap);

        return bitmap;
    }

    /**
     * Draw the views in the given bitmap.
     */
    private static void drawViews(final List<ViewConfig> configs, final Bitmap bitmap) {
        for (ViewConfig config : configs) {
            drawView(config, bitmap);
        }
    }

    private static void drawView(ViewConfig config, Bitmap bitmap) {
        if ((config.mLayoutParams.flags & FLAG_DIM_BEHIND) == FLAG_DIM_BEHIND) {
            Canvas dimCanvas = new Canvas(bitmap);

            int alpha = (int) (255 * config.mLayoutParams.dimAmount);
            dimCanvas.drawARGB(alpha, 0, 0, 0);
        }

        Canvas canvas = new Canvas(bitmap);
        canvas.translate(config.mWindowFrame.left, config.mWindowFrame.top);
        config.mView.draw(canvas);
    }

    /**
     * Get current activity views attach info.
     *
     * @param activity given Activity to take its screenshot.
     */
    private static List<ViewConfig> getScreenViews(Activity activity) {
        Object globalWindowManager;

        globalWindowManager = getFieldValue("mGlobal", activity.getWindowManager());
        Object rootsObj = getFieldValue("mRoots", globalWindowManager);
        Object paramsObj = getFieldValue("mParams", globalWindowManager);

        Object[] roots;
        WindowManager.LayoutParams[] params;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            roots = ((List) rootsObj).toArray();

            List<WindowManager.LayoutParams> paramsList =
                (List<WindowManager.LayoutParams>) paramsObj;
            params = paramsList.toArray(new WindowManager.LayoutParams[paramsList.size()]);
        } else {
            roots = (Object[]) rootsObj;
            params = (WindowManager.LayoutParams[]) paramsObj;
        }

        List<ViewConfig> viewConfigs = fetchViewConfigs(roots, params);

        if (isEmpty(viewConfigs)) {
            return Collections.emptyList();
        }

        return viewConfigs;
    }

    /**
     * Fetch root view's property by given the {@link WindowManager} property.
     */
    private static List<ViewConfig> fetchViewConfigs(Object[] roots,
        WindowManager.LayoutParams[] params) {
        List<ViewConfig> configs = new ArrayList<>();

        int length = roots.length;
        for (int i = 0; i < length; i++) {
            Object root = roots[i];

            View view = (View) getFieldValue("mView", root);

            if (view == null) {
                continue;
            }

            if (!view.isShown()) {
                continue;
            }

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            int left = location[0];
            int top = location[1];
            Rect area = new Rect(left, top, left + view.getWidth(), top + view.getHeight());

            configs.add(new ViewConfig(view, area, params[i]));
        }

        return configs;
    }

    /**
     * View's essential property.
     */
    static class ViewConfig {

        final View mView;

        private final Rect mWindowFrame;

        private final WindowManager.LayoutParams mLayoutParams;

        public ViewConfig(View view, Rect windowFrame, WindowManager.LayoutParams layoutParams) {
            mView = view;
            mWindowFrame = windowFrame;
            mLayoutParams = layoutParams;
        }

        boolean isDialogType() {
            return mLayoutParams.type == WindowManager.LayoutParams.TYPE_APPLICATION;
        }

        boolean isActivityType() {
            return mLayoutParams.type == WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
        }

        IBinder getWindowToken() {
            return mLayoutParams.token;
        }

        Context getContext() {
            return mView.getContext();
        }
    }
}
