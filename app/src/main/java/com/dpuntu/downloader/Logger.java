package com.dpuntu.downloader;

import android.util.Log;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

public class Logger {
    private static final String TAG = "downloader ";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void v(String msg) {
        Log.v(TAG, msg);
    }
}
