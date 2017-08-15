package com.musk.fuzhu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.musk.fuzhu.compat.Thumb;
import com.musk.fuzhu.core.MyAccessibility;

public class TestActivity extends Activity {
    public boolean isOPened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
    }

    public void clickBut(View v) {
        if (!isAccessibilitySettingsOn()) {
            Intent in = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(in);
        }
        Thumb.init();
        Intent intent = new Intent();
        intent.setClassName("com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.SplashActivity");
        startActivity(intent);
        // 测试微信列表抓取文章或列表标题
        // 测试手机为乐视
        // intent.setClassName("com.tencent.mm",
        // "com.tencent.mm.ui.LauncherUI");
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // WeiXin.init();

        // 乐视-手机管家
        // intent.setClassName("com.letv.android.supermanager",
        // "com.letv.android.supermanager.activity.SuperManagerActivity");
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Leshi.init();

        // for 华为 【自启动管理界面】
        // intent.setClassName("com.huawei.systemmanager",
        // "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        // Huawei.init();

        // for 小米 【自启动管理界面】
        // intent.setClassName("com.miui.securitycenter",
        // "com.miui.permcenter.autostart.AutoStartManagementActivity");
        // Xiaomi.init();

        // for viVo 【管家主界面】
        // intent.setClassName("com.iqoo.secure",
        // "com.iqoo.secure.MainActivity");
        // Vivo.init();

        // for neXus 5 【设置主界面】
        // intent.setClassName("com.android.settings",
        // "com.android.settings.Settings");
        // Nexus.init();
        // Xiaomi.init();
        // Leshi.init();
        // Vivo.init();

        // 测试自己的开关点击 com.test.test.TestActivity
        // intent.setClassName("com.phonecleaner.master",
        // "com.test.test.TestActivity");

        // 测试通知使用权管理
        // com.android.settings/.Settings$NotificationAccessSettingsActivity
        // intent.setAction("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");

        // 华为设置主界面 com.android.settings/.HWSettings
        // intent.setClassName("com.android.settings",
        // "com.android.settings.HWSettings");
        // Huawei.init();

        // 判断会有4个状态，0默认 1可用 2禁止 3user disable
        // ComponentName mComponentName = new ComponentName("com.fuzhu.master",
        // "com.test.test.BootReceiver");
        // xx就是软件名字，然后后面就是一般用来接收开机完成广播的组件名称。
        // int a =
        // getPackageManager().getComponentEnabledSetting(mComponentName);
        // Log.i("musk", "==start flag==" + a);
    }

    static final String BOOT_START_PERMISSION = "android.permission.RECEIVE_BOOT_COMPLETED";

    // ComponentName mComponentName = new ComponentName("com.xx","com.xx.receivers.BootReceiver");
    // xx就是软件名字，然后后面就是一般用来接收开机完成广播的组件名称。
    // int a = getPackageManager().getComponentEnabledSetting(mComponentName);
    public List<Map<String, Object>> fetch_installed_apps(Context context) {
        List<ApplicationInfo> packages = context.getPackageManager()
                .getInstalledApplications(0);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(
                packages.size());
        Iterator<ApplicationInfo> appInfoIterator = packages.iterator();

        while (appInfoIterator.hasNext()) {
            ApplicationInfo app = (ApplicationInfo) appInfoIterator.next();
            // 查找安装的package是否有开机启动权限
            if (PackageManager.PERMISSION_GRANTED == context
                    .getPackageManager().checkPermission(BOOT_START_PERMISSION,
                            app.packageName)) {
                String label = context.getPackageManager()
                        .getApplicationLabel(app).toString();
                Drawable appIcon = context.getPackageManager()
                        .getApplicationIcon(app);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", label);
                map.put("desc", app.packageName);
                map.put("img", appIcon);
                Log.i("musk", "==map==" + map);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * To check if service is enabled
     *
     * @param
     * @return
     */
    private boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/"
                + MyAccessibility.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(this
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(this
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
        }
        return false;
    }
}