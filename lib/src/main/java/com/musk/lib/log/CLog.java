package com.musk.lib.log;

import android.util.Log;

public class CLog {
    public static final boolean LOGEnable = true;

    public static void i(String msg) {
        if (LOGEnable) {
            Log.i("musk", msg);
        }
    }
}
