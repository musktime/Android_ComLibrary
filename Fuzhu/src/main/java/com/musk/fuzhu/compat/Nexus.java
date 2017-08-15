package com.musk.fuzhu.compat;

import java.util.List;

import com.fuzhu.master.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Nexus {
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

	// ----------------------------------------------------------
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
			if (go_page_shebeilist(event, context.getString(R.string.app_name))) {
				flag_step3 = false;
			}
		}
		if (flag_step3) {
			return;
		}
		if (flag_step4) {
			if (go_enable_shebei(event, context.getString(R.string.app_name))) {
				flag_step4 = false;
			}
		}
		if (Nexus.flag_step4) {
			return;
		}
	}

	/**
	 * 转到安全页面
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_anquan(AccessibilityEvent event) {
		methordName = "go_page_anquan";
		List<AccessibilityNodeInfo> sets = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.android.settings:id/dashboard");
		if (sets == null) {
			return false;
		}
		Log.i("musk", "==" + methordName + "==");
		for (int i = 0; i < sets.size(); i++) {
			if (sets.get(i).getClassName().equals("android.widget.ScrollView")) {
				// find ScrollView
				AccessibilityNodeInfo scrollView = sets.get(i);
				boolean isFind = false;
				int visiCount = scrollView.getChildCount() - 1;
				Log.i("musk",
						"==scrollView.getChildCount=="
								+ scrollView.getChildCount());
				for (int j = 0; j < scrollView.getChildCount(); j++) {
					AccessibilityNodeInfo item = scrollView.getChild(j);
					// find title
					if (item != null) {
						if (item.getClassName().equals(
								"android.widget.TextView")
								&& "个人".equals(item.getText())) {
							Log.i("musk", "==item==" + item.getText());
						}
						// find FlameLayout
						if (item.getClassName().equals(
								"android.widget.FrameLayout")) {
							Log.i("musk", "==item==" + item.getClassName());
							for (int k = 0; k < item.getChildCount(); k++) {
								AccessibilityNodeInfo part = item.getChild(k);
								Log.i("musk", "==part==" + part.getClassName());
								Log.i("musk", "==part==" + part.getText());
								if (part.getClassName().equals(
										"android.widget.TextView")) {
									if ("安全".equals(part.getText())) {
										Log.i("musk", "==安全==");
										isFind = true;
										Log.i("musk", "==我在这个索引处点击的==" + j);
										return item
												.performAction(AccessibilityNodeInfo.ACTION_CLICK);
									}
								} else {
									if (!isFind) {
										if (j >= visiCount) {
											if (j % visiCount == 0) {
												Log.i("musk", "==scroll==");
												Log.i("musk", "==我在这个索引处滑动的=="
														+ j);
												scrollView
														.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
											}
										}
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
	 * 转到设备管理ListView/TextView + LinearLayout
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_shebeiguanli(AccessibilityEvent event) {
		// 安全界面进入设备管理器 android:id/list
		List<AccessibilityNodeInfo> secureList = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int j = 0; j < secureList.size(); j++) {
			boolean isFind = false;
			AccessibilityNodeInfo seList = secureList.get(j);
			if (seList.getClassName().equals("android.widget.ListView")) {
				for (int k = 0; k < seList.getChildCount(); k++) {
					AccessibilityNodeInfo item = seList.getChild(k);
					if (item != null) {
						Log.i("musk", "==item==" + item.getText());
						if (item.getClassName().equals(
								"android.widget.TextView")) {
							if ("设备管理".equals(item.getText())) {
								isFind = true;
							}
						}
						if (item.getClassName().equals(
								"android.widget.LinearLayout")) {
							if (isFind) {
								return item
										.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 设备管理选择页面 android:id/list
	 * 
	 * @param event
	 * @param targetName
	 * @return
	 */
	private static boolean go_page_shebeilist(AccessibilityEvent event,
			String targetName) {

		List<AccessibilityNodeInfo> devicesNode = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int i = 0; i < devicesNode.size(); i++) {
			AccessibilityNodeInfo deviceList = devicesNode.get(i);
			boolean isFindDevice = false;
			if (deviceList.getClassName().equals("android.widget.ListView")) {
				for (int j = 0; j < deviceList.getChildCount(); j++) {
					AccessibilityNodeInfo deviceItem = deviceList.getChild(j);
					if (deviceItem.getClassName().equals(
							"android.widget.LinearLayout")) {
						for (int k = 0; k < deviceItem.getChildCount(); k++) {
							AccessibilityNodeInfo item = deviceItem.getChild(k);
							if (item != null) {
								Log.i("musk",
										"==item——className=="
												+ item.getClassName());
								if (item.getClassName().equals(
										"android.widget.TextView")) {
									if (targetName.equals(item.getText())) {
										isFindDevice = true;
										Log.i("musk", "==findDevice==");
									}
								}
								if (isFindDevice) {
									if (item.getClassName().equals(
											"android.widget.CheckBox")) {
										// 点击容器
										boolean clickDevice = deviceItem
												.performAction(AccessibilityNodeInfo.ACTION_CLICK);
										Log.i("musk", "==clickDevice=="
												+ clickDevice);
										return clickDevice;
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
	 * 选择设备管理器开启
	 * 
	 * @param event
	 * @param targetName
	 * @return
	 */
	private static boolean go_enable_shebei(AccessibilityEvent event,
			String targetName) {// 选择激活设备管理
								// com.android.settings:id/active_layout
		List<AccessibilityNodeInfo> enableAct = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.android.settings:id/active_layout");
		for (int i = 0; i < enableAct.size(); i++) {
			AccessibilityNodeInfo enableDevice = enableAct.get(i);
			Log.i("musk", "==enableDevice==" + enableDevice.getClassName());
			if (enableDevice.getClassName().equals(
					"android.widget.LinearLayout")) {
				for (int j = 0; j < enableDevice.getChildCount(); j++) {
					AccessibilityNodeInfo deviceChild = enableDevice
							.getChild(j);
					Log.i("musk",
							"==deviceChild==" + deviceChild.getClassName());
					if (deviceChild.getClassName().equals(
							"android.widget.Button")) {
						if ("激活".equals(deviceChild.getText())) {
							boolean isClickOk = deviceChild
									.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							return isClickOk;
						}
					}
					if (deviceChild.getClassName().equals(
							"android.widget.Button")) {
						if ("取消激活".equals(deviceChild.getText())) {
							return true;
						}
					}
					if (deviceChild.getClassName().equals(
							"android.widget.Button")) {
						if ("取消".equals(deviceChild.getText())) {

						}
					}
				}
			}
		}
		return false;
	}
}
