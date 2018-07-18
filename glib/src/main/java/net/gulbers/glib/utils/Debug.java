package net.gulbers.glib.utils;

import android.util.Log;

/**
 * Created by sgultom on 3/6/2017.
 */

public class Debug {
    /**
     * Log error
     *
     * @param isDebug   debug option true or false
     * @param tag log TAG
     * @param message log message
     */
    public static void e(boolean isDebug, String tag, String message) {
        if (isDebug) {
            if (message.length() > 3000) {
                int chunkCount = message.length() / 3000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 3000 * (i + 1);
                    if (max >= message.length()) {
                        Log.e(tag, ">>> " + message.substring(3000 * i));
                    } else {
                        Log.e(tag, ">>> " + message.substring(3000 * i, max));
                    }
                }
            } else {
                Log.e(tag, ">>> " + message);
            }
        }
    }

    public static void d(boolean isDebug, String tag, String message) {
        if (isDebug) {
            Log.d(tag, ">>> " + message);
        }
    }

    public static void i(boolean isDebug, String tag, String message) {
        if (isDebug) {
            Log.i(tag, ">>> " + message);
        }
    }

    public static void w(boolean isDebug, String tag, String message) {
        if (isDebug) {
            Log.w(tag, ">>> " + message);
        }
    }
}
