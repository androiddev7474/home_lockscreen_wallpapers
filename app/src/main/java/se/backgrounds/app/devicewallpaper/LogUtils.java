package se.backgrounds.app.devicewallpaper;

import android.util.Log;

/**
 * Created by stude on 2018-02-24.
 */

public class LogUtils {

    public static void debug(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void debug(String message) {
        if (BuildConfig.DEBUG) {
            System.err.print(message);
        }
    }
}
