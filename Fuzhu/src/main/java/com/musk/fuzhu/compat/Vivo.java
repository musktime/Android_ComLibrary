package com.musk.fuzhu.compat;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.fuzhu.master.R;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Vivo {
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

	// -----------------------------------自启动管理--------------------------------------------
	public static void enable_autoStart(AccessibilityEvent event,
			Context context) {
		if (flag_step1) {
			if (go_page_IGuanjia(event)) {
				flag_step1 = false;
			}
		}
		if (flag_step1) {
			return;
		}
		if (flag_step2) {
			if (go_page_ziqidongguanli(event)) {
				flag_step2 = false;
			}
		}
		if (flag_step3) {
			if (go_enable_ziqidong(event, context.getString(R.string.app_name))) {
				flag_step3 = false;
			}
		}
		if (flag_step3) {
			return;
		}
	}

	/**
	 * 一、进入i管家主页
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_IGuanjia(AccessibilityEvent event) {
		// 进入i管家主界面
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.iqoo.secure:id/gridView");
		if (nodes == null) {
			return false;
		}
		for (int i = 0; i < nodes.size(); i++) {
			AccessibilityNodeInfo gridView = nodes.get(i);
			// find gridView
			if (gridView.getClassName().equals("android.widget.GridView")) {
				for (int j = 0; j < gridView.getChildCount(); j++) {
					// find item
					AccessibilityNodeInfo linearlayout = gridView.getChild(j);
					if (j == 2) {
						boolean clickLine = linearlayout
								.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						Log.i("musk", "--clickLine--" + clickLine);
						return clickLine;
					}
					linearlayout.recycle();
				}
			}
			gridView.recycle();
		}
		return false;
	}

	/**
	 * 二、进入自启动管理
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_ziqidongguanli(AccessibilityEvent event) {
		// 进入自启动管理界面
		List<AccessibilityNodeInfo> lists = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int i = 0; i < lists.size(); i++) {
			AccessibilityNodeInfo listview = lists.get(i);
			if (listview.getClassName().equals("android.widget.ListView")) {
				Log.i("musk",
						"--listview.getChildCount--" + listview.getChildCount());
				for (int j = 0; j < listview.getChildCount(); j++) {
					AccessibilityNodeInfo Relativity = listview.getChild(j);
					for (int k = 0; k < Relativity.getChildCount(); k++) {
						AccessibilityNodeInfo part = Relativity.getChild(k);
						if (part.getClassName().equals(
								"android.widget.TextView")) {
							if ("自启动管理".equals(part.getText())) {
								return Relativity
										.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							}
						}
						part.recycle();
					}
					Relativity.recycle();
				}
			}
			listview.recycle();
		}
		return false;
	}

	/**
	 * 三、开启某应用自启动
	 * 
	 * @param event
	 * @param targetName
	 * @return
	 */
	private static boolean go_enable_ziqidong(AccessibilityEvent event,
			String targetName) {
		// 开启自启动 com.iqoo.secure:id/section_list_view

		List<AccessibilityNodeInfo> startLists = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.iqoo.secure:id/section_list_view");
		Log.i("musk", "==section_list_view--");
		for (int i = 0; i < startLists.size(); i++) {
			AccessibilityNodeInfo listview = startLists.get(i);
			// find listView
			if (listview.getClassName().equals("android.widget.ListView")) {
				// find 开关
				List<AccessibilityNodeInfo> checks = listview
						.findAccessibilityNodeInfosByViewId("com.iqoo.secure:id/forbid_btn");
				// find item layout
				int visiCount = listview.getChildCount() - 1;
				for (int j = 0; j < listview.getChildCount(); j++) {
					AccessibilityNodeInfo line = listview.getChild(j);
					Log.i("musk", "==line--" + line.getClassName());
					// find item part
					for (int k = 0; k < line.getChildCount(); k++) {
						AccessibilityNodeInfo part = line.getChild(k);
						if (part.isClickable()) {
							Log.i("musk",
									"==part click--" + part.getClassName());
						}
						Log.i("musk", "==part--" + part.getClassName());
						Log.i("musk", "==part--" + part.getText());
						if (part.getClassName().equals(
								"android.widget.TextView")) {
							if (targetName.equals(part.getText())) {
								if (checks.get(j).getClassName()
										.equals("android.view.View")) {
									boolean ok = checks.get(j).performAction(
											AccessibilityNodeInfo.ACTION_CLICK);
									Log.i("musk", "==line.ok--" + ok);
									return ok;
								}
							}
						} else {
							if (j >= visiCount) {
								if (j % visiCount == 0) {
									listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	// -----------------------------------设备管理器--------------------------------------------

	public static void enable_DeviceManage(AccessibilityEvent event,
			Context context) {
		if (flag_step1) {
			if (go_page_gengduoshezhi(event)) {
				flag_step1 = false;
			}
		}
		if (flag_step1) {
			return;
		}
		if (flag_step2) {
			if (go_page_anquan(event)) {
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
			if (go_page_enable(context, event)) {
				flag_step4 = false;
			}
		}
		if (flag_step4) {
			return;
		}
		if (flag_step5) {
			if (go_enable_shebei(context, event)) {
				flag_step5 = false;
			}
		}
		if (flag_step5) {
			return;
		}
	}

	/**
	 * 一、进入设置列表 android:id/list设备管理器
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_gengduoshezhi(AccessibilityEvent event) {
		methordName = "go_page_gengduoshezhi";
		List<AccessibilityNodeInfo> list = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int i = 0; i < list.size(); i++) {
			AccessibilityNodeInfo listview = list.get(i);
			// 获取一屏可见item的数量
			int visibleCount = listview.getChildCount() - 1;
			if (listview.getClassName().equals("android.widget.ListView")) {
				boolean isFind = false;
				for (int k = 0; k < listview.getChildCount(); k++) {
					AccessibilityNodeInfo item = listview.getChild(k);
					isFind = false;
					Log.i("musk", "==" + methordName + "==" + k
							+ "==item.className==" + item.getClassName());
					for (int l = 0; l < item.getChildCount(); l++) {
						AccessibilityNodeInfo part = item.getChild(l);
						Log.i("musk",
								"==part==" + methordName + "=="
										+ part.getClassName());
						if (part.getClassName().equals(
								"android.widget.TextView")) {
							Log.i("musk", "==" + methordName + "==" + k
									+ "==part==" + part.getText());
							if ("更多设置".equals(part.getText().toString())) {
								isFind = true;
								return item
										.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							}
						}
					}
					if (!isFind) {
						if (k >= visibleCount) {
							if (k % visibleCount == 0) {
								boolean b = listview
										.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
								Log.i("musk", "==scroll==" + methordName + "=="
										+ b);
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 二、进入安全页面 android:id/list
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_anquan(AccessibilityEvent event) {
		methordName = "go_page_anquan";
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int i = 0; i < nodes.size(); i++) {
			AccessibilityNodeInfo listview = nodes.get(i);
			if (listview.getClassName().equals("android.widget.ListView")) {
				for (int j = 0; j < listview.getChildCount(); j++) {
					AccessibilityNodeInfo item = listview.getChild(j);
					for (int l = 0; l < item.getChildCount(); l++) {
						AccessibilityNodeInfo part = item.getChild(l);
						if (part.getClassName().equals(
								"android.widget.TextView")) {
							Log.i("musk", "===" + methordName + "==part=="
									+ part.getText().toString());
							if ("安全".equals(part.getText().toString())) {
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
	 * 三、进入设备管理页面
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_shebeiguanli(AccessibilityEvent event) {
		methordName = "go_page_shebeiguanli";
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		for (int i = 0; i < nodes.size(); i++) {
			AccessibilityNodeInfo item = nodes.get(i);
			for (int j = 0; j < item.getChildCount(); j++) {
				AccessibilityNodeInfo rela = item.getChild(j);
				if (rela != null) {
					Log.i("musk",
							"==" + methordName + "==" + rela.getClassName());
					for (int k = 0; k < rela.getChildCount(); k++) {
						AccessibilityNodeInfo part = rela.getChild(k);
						if (part.getClassName().equals(
								"android.widget.TextView")) {
							Log.i("musk", "==" + methordName + "==part=="
									+ part.getText().toString());
							if ("设备管理器".equals(part.getText().toString())) {
								boolean clickShebei = rela
										.performAction(AccessibilityNodeInfo.ACTION_CLICK);
								Log.i("musk", "==" + methordName
										+ "==clickShebei==" + clickShebei);
								if (clickShebei) {
									return true;
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
	 * 四、进入设备激活页面
	 * 
	 * @param context
	 * @param event
	 * @return
	 */
	private static boolean go_page_enable(Context context,
			AccessibilityEvent event) {
		methordName = "go_page_enable";
		Log.i("musk", "==" + methordName + "==");
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId("android:id/list");
		if (nodes != null) {
			for (int i = 0; i < nodes.size(); i++) {
				AccessibilityNodeInfo listview = nodes.get(i);
				if (listview.getClassName().equals("android.widget.ListView")) {
					for (int j = 0; j < listview.getChildCount(); j++) {
						AccessibilityNodeInfo item = listview.getChild(j);
						boolean isFind = false;
						boolean isEnbale = true;
						if (item.getClassName().equals(
								"android.widget.LinearLayout")) {
							for (int k = 0; k < item.getChildCount(); k++) {
								AccessibilityNodeInfo part = item.getChild(k);

								if (part.getClassName().equals(
										"android.widget.TextView")) {
									Log.i("musk", "==" + methordName
											+ "==part==" + part.getText());
									if (context.getString(R.string.app_name)
											.equals(part.getText().toString())) {
										isFind = true;
									}
								}
								if ("android.widget.CheckBox".equals(part
										.getClassName())) {
									Log.i("musk", "==" + methordName
											+ "==part==" + part.isChecked());
									isEnbale = part.isChecked();
								}
								if (isFind & !isEnbale) {
									return item
											.performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
	 * 五、激活某应用的设备管理
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_enable_shebei(Context context,
			AccessibilityEvent event) {
		methordName = "go_Enable_Shebei";
		Log.i("musk", "==" + methordName + "==");
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.android.settings:id/admin_name");
		if (nodes == null) {
			return false;
		}
		Log.i("musk", "==" + methordName + "==" + nodes.size());
		for (int i = 0; i < nodes.size(); i++) {
			AccessibilityNodeInfo name = nodes.get(i);
			if (name.getClassName().equals("android.widget.TextView")) {
				Log.i("musk", "==" + methordName + "==name==" + name.getText());
				if (context.getString(R.string.app_name).equals(
						name.getText().toString())) {
					List<AccessibilityNodeInfo> buts = event.getSource()
							.findAccessibilityNodeInfosByText("激活");
					if (buts != null) {
						for (int j = 0; j < buts.size(); j++) {
							AccessibilityNodeInfo but = buts.get(j);
							Log.i("musk",
									"==" + methordName + "==but=="
											+ but.getClassName());
							if (but.getClassName().equals(
									"android.widget.Button")) {
								return but
										.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							}
						}
					}
				}
			}
		}
		return false;
	}
}
