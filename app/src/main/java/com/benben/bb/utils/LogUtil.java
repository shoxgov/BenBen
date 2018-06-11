package com.benben.bb.utils;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "BENBEN";

    public static void d(String log) {
        Log.d(TAG, log);
    }

    public static void d(String tag, String log) {
        Log.d(tag, log);
    }
}
