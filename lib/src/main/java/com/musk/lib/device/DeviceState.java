package com.musk.lib.device;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by musk on 2017/8/15.
 */

public class DeviceState {
    //W
    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //H
    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static boolean isServiceWork(Context mContext, String serviceName) {
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPortrait(Context context) {
        int w = getDeviceWidth(context);
        int h = getDeviceHeight(context);
        if (w < h) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isScreenPortrait(Context context) {
        Configuration mConfiguration = context.getResources()
                .getConfiguration();
        int ori = mConfiguration.orientation;

        if (ori == Configuration.ORIENTATION_PORTRAIT) {
            // 横屏
            return true;
        } else if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            // 竖屏
            return false;
        }
        return false;
    }

    /**
     * update percent of used memory
     *
     * @param context
     * @return
     */
    public static int upDateMemPercent(Context context) {
        int percent = 0;
        long memTotal = Math.abs(getTotalMemory(context));
        long memUsed = Math.abs(memTotal
                - Math.abs(getAvailableMemory(context)));

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMaximumIntegerDigits(2);
        numberFormat.setMinimumIntegerDigits(0);
        String result = numberFormat.format((float) memUsed / (float) memTotal
                * 100);
        try {
            percent = Integer.parseInt(result.trim());
        } catch (Exception e) {
        }
        return percent;
    }

    /**
     * get memory that can be used
     *
     * @param context
     * @return
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * get total memory value
     *
     * @param context
     * @return
     */
    @SuppressWarnings("resource")
    public static long getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            long tmp = 1;
            if (str2.length() <= 0) {
                return getAvailableMemory(context) * 3 / 2;
            }
            arrayOfString = str2.split("\\s+");

            if (arrayOfString.length <= 0) {
                return getAvailableMemory(context) * 3 / 2;
            }

            if (!arrayOfString[1].matches("^[0-9]*$")) {
                tmp = getAvailableMemory(context) * 3 / 2;
                arrayOfString[1] = String.valueOf(tmp);
            }
            initial_memory = Long.valueOf(arrayOfString[1].trim()).intValue() * 1024;
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return initial_memory;
    }
}
