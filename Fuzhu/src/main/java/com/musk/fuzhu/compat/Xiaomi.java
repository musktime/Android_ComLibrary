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
public class Xiaomi {

	public static boolean flag_step1 = true;
	public static boolean flag_step2 = true;
	public static boolean flag_step3 = true;
	public static boolean flag_step4 = true;
	public static boolean flag_step5 = true;

	public static void init() {
		flag_step1 = true;
		flag_step2 = true;
		flag_step3 = true;
		flag_step4 = true;
		flag_step5 = true;
	}

	// ------------------------------自启动点击---------------------------
	public static void enable_AutoStart_FormManager(AccessibilityEvent event,
			Context context) {
		if (flag_step1) {
			if (go_page_ziqidongList(event,
					context.getString(R.string.app_name))) {
				flag_step1 = false;
			}
		}
		if (flag_step1) {
			return;
		}
		if (flag_step2) {
			if (go_enbale_ziqidong(event)) {
				flag_step2 = false;
			}
		}
		if (flag_step2) {
			return;
		}
	}

	public static void enable_AutoStart_FromSetting(AccessibilityEvent event,
			Context context) {

	}

	/**
	 * 一、注意：这里是从手机管家启动的，不是从设置页面进入自启动列表
	 * 
	 * @param event
	 * @param targetName
	 * @return
	 */
	private static boolean go_page_ziqidongList(AccessibilityEvent event,
			String targetName) {
		// 小米4 android 6.0.1 MUI 8.0.2.0
		// 自启动列表id com.miui.securitycenter:id/list_view"
		// 小米4 android 4.4.4 MUI 6.7.1.0
		// 自启动列表id有改变com.miui.securitycenter:id/auto_start_list
		List<AccessibilityNodeInfo> next_node = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.miui.securitycenter:id/auto_start_list");
		Log.i("musk", "----next_node---" + next_node.size());
		for (int i = 0; i < next_node.size(); i++) {
			AccessibilityNodeInfo listview = next_node.get(i);
			Log.i("musk", "----listview---" + listview.getClassName());
			if (listview.getClassName().equals("android.widget.ListView")) {
				int visibleCount = listview.getChildCount() - 1;
				boolean isFound = false;
				for (int m = 0; m < listview.getChildCount(); m++) {
					AccessibilityNodeInfo items = listview.getChild(m);
					if (null != items) {
						Log.i("musk", "--items--" + items.getClassName());
						Log.i("musk", "--items--" + items.getChildCount());
						for (int j = 0; j < items.getChildCount(); j++) {
							AccessibilityNodeInfo item = items.getChild(j);
							if (item.getClassName().equals(
									"android.widget.TextView")) {
								Log.i("musk", "--item--" + j + "--"
										+ (null == item.getText()));
								if (null != item.getText()) {
									Log.i("musk",
											"--item--"
													+ j
													+ "--"
													+ (item.getText()
															.toString().trim()));
								}
							}
							if (item.getClassName().equals(
									"android.widget.TextView")) {
								// 小米4 android 4.4.4 MUI 6.7.1.0
								// 有一个TextView.getText()返回null
								if (null != item.getText()) {
									if (targetName.equals(item.getText()
											.toString().trim())) {
										isFound = true;
										Log.i("musk",
												"------------isFound ok-------------");
									}
								}
							}
							if (isFound) {
								if (item.getClassName().equals(
										"android.widget.CheckBox")) {
									if (item.isEnabled()) {
										// checkBox的clickAble属性是false;checkAble属性是true,点击无效
										boolean ok = item
												.performAction(AccessibilityNodeInfo.ACTION_CLICK);
										Log.i("musk", "--click item--" + ok);
										return ok;
									}
								}
							}
							Log.i("musk", "--isFound--" + isFound);
							if (!isFound) {
								if (m > visibleCount) {
									if (m % visibleCount == 0) {
										Log.i("musk",
												"--ACTION_SCROLL_FORWARD--");
										boolean ok = listview
												.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
										Log.i("musk", "--scroll--" + ok);
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
	 * 二、激活自启动
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_enbale_ziqidong(AccessibilityEvent event) {
		// 进入下级页面点击 com.miui.securitycenter:id/list_view
		List<AccessibilityNodeInfo> listNodes = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.miui.securitycenter:id/list_view");
		for (int i = 0; i < listNodes.size(); i++) {
			AccessibilityNodeInfo listview = listNodes.get(i);
			if (listview.getClassName().equals("android.widget.ListView")) {
				// boolean isFind = false;
				for (int k = 0; k < listview.getChildCount(); k++) {
					AccessibilityNodeInfo item = listview.getChild(k);
					Log.i("musk", "===item===" + item.getClassName());
					if (item != null) {
						for (int j = 0; j < item.getChildCount(); j++) {
							AccessibilityNodeInfo part = item.getChild(j);
							if (part.getClassName().equals(
									"android.widget.CheckBox")
									&& !part.isChecked()) {
								boolean clickCheck = part
										.performAction(AccessibilityNodeInfo.ACTION_CLICK);
								Log.i("musk", "===clickCheck===" + clickCheck);
								// switch类型的开关控件单独点击：只能改变控件状态，最終是無效的
								return clickCheck;
							}
						}
					}
				}
			}
		}
		return false;
	}

	// ---------------------------设备管理器------------------------------
	public static void enable_DeviceManage(AccessibilityEvent event,
			Context context) {
		Log.i("max", "==enable_DeviceManage==");
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
			if (go_page_enable(event, context)) {
				flag_step4 = false;
			}
		}
		if (flag_step4) {
			return;
		}
		// if (flag_step5) {
		// if (go_page_gengduoshezhi(event)) {
		// flag_step5 = false;
		// }
		// }
		// if (flag_step5) {
		// return;
		// }
	}

	/**
	 * 点击设置的更多应用android:id/list
	 * 
	 * @param event
	 * @return
	 */
	private static boolean go_page_gengduoshezhi(AccessibilityEvent event) {
		// 查找点击设置某一项
		return Access.clickListviewItemByText(event, "android:id/list", "更多设置",
				"go_page_gengduoyingyong");
	}

	/**
	 * @param event
	 * @return
	 */
	private static boolean go_page_anquan(AccessibilityEvent event) {
		return Access.clickListviewItemByText(event, "android:id/list", "系统安全",
				"go_page_anquan");
	}

	/**
	 * @param event
	 * @return
	 */
	private static boolean go_page_shebeiguanli(AccessibilityEvent event) {
		return Access.clickListviewItemByText(event, "android:id/list", "设备管理器",
				"go_page_anquan");
	}

	/**
	 * @param event
	 * @param context
	 * @return
	 */
	private static boolean go_page_enable(AccessibilityEvent event,
			Context context) {
		return Access.clickListviewItemByText(event, "android:id/list",
				context.getString(R.string.app_name), "go_page_enable");
	}

	// ------------------------------自启动管理【从设置页面开始的】--------------------------------
	public static void go_enable(AccessibilityEvent event, Context context) {
		go_page_shezhi(event);
	}

	/**
	 * step1
	 */

	private static boolean go_page_shezhi(AccessibilityEvent event) {

		return false;
	}

	// ---------------------------Other------------------------------
	/**
	 * 测试点击通知管理
	 * 
	 * @param event
	 * @param targetName
	 * @return
	 */
	public static boolean go_page_tongzhiguanli(AccessibilityEvent event,
			String targetName) {
		List<AccessibilityNodeInfo> titles = event.getSource()
				.findAccessibilityNodeInfosByViewId("miui:id/action_bar_title");
		boolean isNext = false;
		for (int i = 0; i < titles.size(); i++) {
			AccessibilityNodeInfo title = titles.get(i);
			if (title != null) {
				if (title.getClassName().equals("android.widget.TextView")) {
					Log.i("musk", "==Mi==title====" + title.getText());
					if (title.getText().equals("通知使用权")) {
						isNext = true;
					}
				}
			}
		}
		if (isNext) {
			List<AccessibilityNodeInfo> listviews = event.getSource()
					.findAccessibilityNodeInfosByViewId("android:id/list");
			for (int i = 0; i < listviews.size(); i++) {
				AccessibilityNodeInfo listview = listviews.get(i);
				if (listview.getClassName().equals("android.widget.ListView")) {
					Log.i("musk", "=Mi=listview==" + listview.getClassName());
					boolean isMe = false;
					for (int j = 0; j < listview.getChildCount(); j++) {
						AccessibilityNodeInfo item = listview.getChild(j);
						if (item != null) {
							Log.i("musk", "===Mi=item==" + item.getClassName());
							if (item.getClassName().equals(
									"android.widget.LinearLayout")) {
								for (int k = 0; k < item.getChildCount(); k++) {
									AccessibilityNodeInfo part = item
											.getChild(k);
									if (part != null) {
										Log.i("musk",
												"===通知=part=="
														+ part.getClassName());
										if (part.getClassName().equals(
												"android.widget.TextView")) {
											Log.i("musk",
													"==" + j + "==通知=part=="
															+ part.getText());
											if (part.getText() != null) {
												if (targetName.equals(part
														.getText().toString())) {
													isMe = true;
													item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
												}
												if (!isMe) {
													if (j >= 7) {
														if (j % 7 == 0) {
															listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
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
				}
			}
		}
		// boolean isMyAllow = false;
		List<AccessibilityNodeInfo> alertTitles = event.getSource()
				.findAccessibilityNodeInfosByText("miui:id/alertTitle");
		for (int i = 0; i < alertTitles.size(); i++) {
			AccessibilityNodeInfo title = alertTitles.get(i);
			if (null != title) {
				if (title.getClassName().equals("")) {

				}
			}
		}
		event.getSource().findAccessibilityNodeInfosByText("允许");
		return false;
	}

	/**
	 * 测试列表的滚动问题
	 * 
	 * @param event
	 * @return
	 */
	public static boolean test_ziqidong_listScroll(AccessibilityEvent event) {
		String whichItem = "头条视频";
		Log.i("musk", "==测试自启动管理点击==");
		// 设置列表的id
		// MUI_7.1.2.0
		// com.miui.securitycenter:id/auto_start_list
		// MUI_8.0.2.0 com.miui.securitycenter:id/list_view
		List<AccessibilityNodeInfo> nodes = event.getSource()
				.findAccessibilityNodeInfosByViewId(
						"com.miui.securitycenter:id/list_view");
		if (nodes == null) {
			return false;
		}
		for (int i = 0; i < nodes.size(); i++) {
			AccessibilityNodeInfo listview = nodes.get(i);
			Log.i("musk", "==listview==" + i);
			if (listview.getClassName().equals("android.widget.ListView")) {
				int itemCount = listview.getChildCount() - 1;
				boolean ok = false;
				Log.i("musk", "==getChildCount==" + listview.getChildCount());
				for (int j = 0; j < listview.getChildCount(); j++) {
					AccessibilityNodeInfo item = listview.getChild(j);
					if (item != null) {
						for (int k = 0; k < item.getChildCount(); k++) {
							AccessibilityNodeInfo part = item.getChild(k);
							if (part.getClassName().equals(
									"android.widget.TextView")) {
								if (part.getText() != null) {
									Log.i("musk",
											"==" + j + "==part=="
													+ part.getText());
									if (part.getText().toString()
											.equals(whichItem)) {
										ok = true;
										item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
									}
								}
							}
						}
					}
					if (!ok) {
						if (j >= itemCount) {
							if (j % itemCount == 0) {
								listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
							}
						}
					}
				}
			}
		}
		return false;
	}
}