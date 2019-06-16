package com.zac4j.system.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

/**
 * Common useful methods.
 *
 * @author: zac
 * @date: 2019-06-16
 */
public class Utils {

    public static void getScreenSize(Activity activity, int[] size) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
