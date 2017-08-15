package com.musk.lib.apk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by musk on 2017/8/15.
 */

public class Apk {
    /**
     * Get installed apk name and judge whether the target apk has installed
     *
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        List<String> myapp_list = new ArrayList<String>();
        myapp_list.clear();
        try {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> appList = pm
                    .getInstalledPackages((PackageManager.GET_UNINSTALLED_PACKAGES));

            for (PackageInfo info : appList) {
                ApplicationInfo applicationInfo = info.applicationInfo;
                myapp_list.add(applicationInfo.packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (myapp_list.contains(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    public static PackageInfo getPackageInfoFromApkFile(Context context,
                                                        String apkFilePath) {
        return context.getPackageManager()
                .getPackageArchiveInfo(apkFilePath, 0);
    }

    public static String getPackageNameFromApkFile(Context context,
                                                   String apkFilePath) {
        if (apkFilePath == null || "".equals(apkFilePath)) {
            return null;
        }

        File apkFile = new File(apkFilePath);
        if (!apkFile.exists() || !apkFile.canRead()) {
            return null;
        }
        PackageInfo packageArchiveInfo = getPackageInfoFromApkFile(context,
                apkFilePath);

        if (packageArchiveInfo != null) {
            return packageArchiveInfo.packageName;
        }

        return null;
    }

    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * Saved the time of the Ad
     */

    public static void openApp(Context context, String PackageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage(PackageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }
}
