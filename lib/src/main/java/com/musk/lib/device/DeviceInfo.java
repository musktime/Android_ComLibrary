package com.musk.lib.device;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceInfo {
	public String uid = "";
	public String imei = "";
	public String mac = "";
	public String imsi = "";
	public String os_version = "";
	public String brand = "";
	public String model = "";
	public String resolution = "";

	public int screen_width = 0;
	public int screen_height = 0;
	public String lan_ip = "";
	public String wlan_ip = "";
	public String androidid = "";
	public String vendor = "";

	public static DeviceInfo info = null;

	public static void Refresh(Context context) {
		if (info == null) {
			info = new DeviceInfo();
		}
		try {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			DisplayMetrics displayMetrics = new DisplayMetrics();
			display.getMetrics(displayMetrics);
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			int ip = wifi.getConnectionInfo().getIpAddress();

			info.androidid=Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			info.screen_width = displayMetrics.widthPixels;
			info.screen_height = displayMetrics.heightPixels;
			info.resolution = String.valueOf(info.screen_width) + "x"
					+ String.valueOf(info.screen_height);
			info.vendor = android.os.Build.MANUFACTURER;
			info.brand = android.os.Build.BRAND;
			info.model = android.os.Build.MODEL;
			info.imei = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			info.imsi = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE))
					.getSubscriberId();
			info.mac = wifi.getConnectionInfo().getMacAddress();
			info.os_version = String.valueOf(android.os.Build.VERSION.SDK_INT);
			info.lan_ip = info.wlan_ip = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF)
					+ "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DeviceInfo GetInfo(Context context) {
		if (info == null) {
			Refresh(context);
		}
		return info;
	}

}
