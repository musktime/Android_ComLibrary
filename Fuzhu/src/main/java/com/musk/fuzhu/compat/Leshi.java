package com.musk.fuzhu.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Leshi {
	public static boolean flag_step1 = true;
	public static boolean flag_step2 = true;
	public static boolean flag_step3 = true;
	public static boolean flag_step4 = true;
	public static boolean flag_step5 = true;

	// private static String methordName = "";

	public static void init() {
		flag_step1 = true;
		flag_step2 = true;
		flag_step3 = true;
		flag_step4 = true;
		flag_step5 = true;
	}

	// ---------------------------------自启动点击测试【从管家主页进入】-------------------------------------
	public static void enable_AutoStart(AccessibilityEvent event,
			Context context) {
		if (Leshi.flag_step1) {
			if (Leshi.go_page_MainGuanjia(event)) {
				Leshi.flag_step1 = false;
			}
		}
		if (Leshi.flag_step1) {
			return;
		}
		if (Leshi.flag_step2) {
			if (Leshi.go_page_ziqidong(event)) {
				Leshi.flag_step2 = false;
			}
		}
		if (Leshi.flag_step2) {
			return;
		}
		if (Leshi.flag_step3) {
			if (Leshi.go_enable(event)) {
				Leshi.flag_step3 = false;
			}
		}
		if (Leshi.flag_step3) {
			return;
		}
	}

	/**
	 * 
	 * 一、乐视手机管家--安全隐私界面
	 * 
	 * @param event
	 * @return
	 */
	public static boolean go_page_MainGuanjia(AccessibilityEvent event) {
		// com.letv.android.supermanager:id/menuItemListView
		return Access.clickListviewItemByText(event,
				"com.letv.android.supermanager:id/menuItemListView", "安全隐私",
				null);
	}

	/**
	 * 二、自启动界面
	 * 
	 * @param event
	 * @return
	 */
	public static boolean go_page_ziqidong(AccessibilityEvent event) {
		// com.letv.android.supermanager:id/menuItemListView
		return Access.clickListviewItemByText(event,
				"com.letv.android.supermanager:id/menuItemListView", "自启动管理",
				null);
	}

	/**
	 * 三、开启某应用的自启动，例如神州租车
	 * 
	 * @param event
	 * @return
	 */
	public static boolean go_enable(AccessibilityEvent event) {
		// android:id/list
		return Access.clickListviewItemByText(event, "android:id/list", "神州租车", null);
	}

	// ---------------------------------自启动【从设置主页进入】-------------------------------------
	public static void test(AccessibilityEvent event) {
		Log.i("musk", "==start test==");
		Access.clickListviewItemByText(event, "android:id/list", "隐私授权", "隐私授权");
		// Access.access_ListView(event, "android:id/list", "应用使用权管理",
		// "应用使用权管理");
		// Access.access_ListView(event, "android:id/list", "身体传感器", "身体传感器");
		// Access.access_ListView(event, "android:id/list", "支付宝", "支付宝");
		Log.i("musk", "==end test==");
	}

	// ---------------------------------设备管理器【从设置主页进入】-------------------------------------
	public static void enable_DeviceManage(AccessibilityEvent event,
			Context context) {
		Log.i("musk", "==start enable_DeviceManage==");
		if (flag_step1) {
			if (go_page_zhiwen(event)) {
				flag_step1 = false;
			}
		}
		if (flag_step1) {
			return;
		}
		if (flag_step2) {
			if (go_page_qitaanquan(event)) {
				flag_step2 = false;
			}
		}
		if (flag_step2) {
			return;
		}
		if (flag_step3) {
			if (go_page_shebeiguanli(event)) {
				flag_step3 = false;
			}
		}
		if (flag_step3) {
			return;
		}
		if (flag_step4) {
			if (go_page_shebeilist(event)) {
				flag_step4 = false;
			}
		}
		if (flag_step4) {
			return;
		}
		Log.i("musk", "==end enable_DeviceManage==");
	}

	/**
	 * 一、进入指纹与密码页面
	 * 
	 * @param event
	 * @return
	 */
	public static boolean go_page_zhiwen(AccessibilityEvent event) {
		// android:id/list
		return Access.clickListviewItemByText(event, "android:id/list", "指纹和密码", null);
	}

	/**
	 * 二、进入其他安全选项页面
	 * 
	 * @param event
	 * @return
	 */
	public static boolean go_page_qitaanquan(AccessibilityEvent event) {
		// android:id/list
		return Access.clickListviewItemByText(event, "android:id/list", "其他安全选项",
				"go_page_qitaanquan");
	}

	/**
	 * 三、进入设备管理器
	 * 
	 * @param event
	 * @return
	 */
	public static boolean go_page_shebeiguanli(AccessibilityEvent event) {
		// android:id/list
		return Access.clickListviewItemByText(event, "android:id/list", "设备管理器", null);
	}

	/**
	 * 四、进入设备列表
	 * 
	 * @param event
	 * @return
	 */
	public static boolean go_page_shebeilist(AccessibilityEvent event) {
		// android:id/list
		return Access.clickListviewItemByText(event, "android:id/list", "手机管家", null);
	}
}
