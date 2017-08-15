package com.musk.fuzhu.compat;

import java.util.List;

import com.fuzhu.master.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Huawei {

	public static boolean flag_step1 = true;
	public static boolean flag_step2 = true;
	public static boolean flag_step3 = true;
	public static boolean flag_step4 = true;
	public static boolean flag_step5 = true;
	private static String methordName = "";

	public static void init() {
		flag_step1 = true;
		flag_step2 = true;
		flag_step3 = true;
		flag_step4 = true;
		flag_step5 = true;
	}

	// HWSettings

	public static void enable_DeviceManage(AccessibilityEvent event,
			Context context) {
		if (flag_step1) {
			if (go_page_anquan(event)) {
				flag_step1 = false;
			}
		}
		if (flag_step1) {
			return;
		}
		if (flag_step2) {
			if (go_page_shebeiguanli(event)) {
				flag_step2 = false;
			}
		}
		if (flag_step2) {
			return;
		}
		if (flag_step3) {
			if (go_page_enbale(context, event)) {
				flag_step3 = false;
			}
		}
		if (flag_step3) {
			return;
		}
		if (flag_step4) {
			if (go_enable(context, event)) {
				flag_step4 = false;
			}
		}
		if (flag_step4) {
			return;
		}
	}

	/**
	 * 转到安全界面
	 * 
	 * @param event
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean go_page_anquan(AccessibilityEvent event) {
		methordName = "go_page_anquan";
		Log.i("musk", "==" + methordName + "==");
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		AccessibilityNodeInfo listView = nodes.get(0);
		boolean isFind = false;
		if (listView.getClassName().equals("android.widget.ListView")) {
			for (int i = 0; i < listView.getChildCount(); i++) {
				AccessibilityNodeInfo item = listView.getChild(i);
				for (int j = 0; j < item.getChildCount(); j++) {
					AccessibilityNodeInfo part = item.getChild(j);
					Log.i("musk",
							"==" + methordName + "==part=="
									+ part.getClassName());
					if (part.getClassName().equals("android.widget.TextView")) {
						if (part.getText().toString().equals("安全")) {
							isFind = true;
							boolean clickItem = item
									.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							if (clickItem) {
								Log.i("musk", "==" + methordName + "==false=="
										+ part.getClassName());
								return true;
							}
						}
					}
				}
				if (!isFind) {
					if (i >= 12) {
						if (i % 12 == 0) {
							boolean b = listView
									.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
							Log.i("musk", "==Scroll==" + b);
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 转到设备管理器
	 * 
	 * @param event
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean go_page_shebeiguanli(AccessibilityEvent event) {
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int i = 0; i < nodes.size(); i++) {
			AccessibilityNodeInfo listView = nodes.get(i);
			if (listView.getClassName().equals("android.widget.ListView")) {
				for (int j = 0; j < listView.getChildCount(); j++) {
					AccessibilityNodeInfo item = listView.getChild(j);
					for (int k = 0; k < item.getChildCount(); k++) {
						AccessibilityNodeInfo part = item.getChild(k);
						if (part != null) {
							if (part.getClassName().equals(
									"android.widget.TextView")) {
								if (part.getText().toString().equals("设备管理器")) {
									boolean clickItem = item
											.performAction(AccessibilityNodeInfo.ACTION_CLICK);
									if (clickItem) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 转到激活页面
	 * 
	 * @param event
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean go_page_enbale(Context context,
			AccessibilityEvent event) {
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int i = 0; i < nodes.size(); i++) {
			AccessibilityNodeInfo listView = nodes.get(i);
			if (listView.getClassName().equals("android.widget.ListView")) {
				for (int j = 0; j < listView.getChildCount(); j++) {
					AccessibilityNodeInfo item = listView.getChild(j);
					boolean isFind = false;
					boolean isEnable = false;
					for (int k = 0; k < item.getChildCount(); k++) {
						AccessibilityNodeInfo part = item.getChild(k);
						if (part.getClassName().equals(
								"android.widget.TextView")) {
							if (part.getText()
									.toString()
									.equals(context
											.getString(R.string.app_name))) {
								isFind = true;
							}
							if (part.getText().toString().equals("激活")) {
								isEnable = true;
							} else {
								isEnable = false;
							}
						}
						if (isFind & !isEnable) {
							boolean clickItem = item
									.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							if (clickItem) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	// go_page_enbale com.android.settings:id/admin_name
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean go_enable(Context context, AccessibilityEvent event) {
		methordName = "go_enable";
		Log.i("musk", "==" + methordName + "==");
		List<AccessibilityNodeInfo> names = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.android.settings:id/admin_name");
		if (names != null) {
			Log.i("musk", "==ssssssss==" + names.size());
			for (int i = 0; i < names.size(); i++) {
				AccessibilityNodeInfo name = names.get(i);
				Log.i("musk", "==" + methordName + "==");
				if (name.getClassName().equals("android.widget.TextView")) {
					if (name.getText().toString()
							.equals(context.getString(R.string.app_name))) {
						List<AccessibilityNodeInfo> buts = event.getSource()
								.findAccessibilityNodeInfosByText("激活");
						for (int j = 0; j < buts.size(); j++) {
							AccessibilityNodeInfo but = buts.get(j);
							if (but.getClassName().equals(
									"android.widget.Button")) {
								boolean clickBut = but
										.performAction(AccessibilityNodeInfo.ACTION_CLICK);
								if (clickBut) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
}