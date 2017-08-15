package com.musk.fuzhu.compat;

import com.assistant.master.utils.MLog;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Thumb {

	public static boolean step1 = false;
	public static boolean step2 = false;
	public static boolean step3 = false;
	public static boolean step4 = false;

	public static void init() {
		step1 = true;
		step2 = true;
		step3 = true;
		step4 = true;

		click_Slide = false;
		click_Profile = false;
		click_Thumbs = false;
		click_Thumblist = false;
	}

	static boolean click_Slide = false;
	static boolean click_Profile = false;
	static boolean click_Thumbs = false;
	static boolean click_Thumblist = false;

	/**
	 * step1----Slide
	 * 
	 * @param event
	 *            com.tencent.mobileqq:id/conversation_head
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean go_page_Slide(AccessibilityNodeInfo rootNode,
			boolean isContinue) {
		MLog.i("==go_page_Slide==" + isContinue);
		if (!isContinue) {
			MLog.i("==go_page_Slide=return=");
			return true;
		}
		if (rootNode != null) {
			int childCount = rootNode.getChildCount();
			if (rootNode.getChildCount() > 0) {
				for (int i = 0; i < childCount; i++) {
					AccessibilityNodeInfo node = rootNode.getChild(i);
					if (click_Slide) {
						go_page_Slide(node, false);
					} else {
						go_page_Slide(node, true);
					}
				}
			} else {
				if ("帐户及设置".equals(rootNode.getContentDescription())) {
					if (rootNode.isClickable()) {
						click_Slide = rootNode
								.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						MLog.i("thumblist_click>>" + click_Slide);
					}
				}
			}
		}
		return false;
	}

	/**
	 * step2---profile
	 * 
	 * @param event
	 *            com.tencent.mobileqq:id/name
	 * @return
	 */
	public static boolean go_page_Profile(AccessibilityNodeInfo rootNode,
			boolean isContinue) {
		if (!isContinue) {
			return true;
		}
		MLog.i("go_page_Profile");
		if (rootNode != null) {
			int childCount = rootNode.getChildCount();
			if (rootNode.getChildCount() > 0) {
				if ("android.widget.RelativeLayout".equals(rootNode
						.getClassName())) {
					if (rootNode.getChildCount() == 2 && rootNode.isClickable()
							&& rootNode.isFocused()) {
						click_Profile = rootNode
								.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						MLog.i("profile_click >>" + click_Profile);
					}
				}
				for (int i = 0; i < childCount; i++) {
					AccessibilityNodeInfo node = rootNode.getChild(i);
					if (click_Profile) {
						go_page_Profile(node, false);
					} else {
						go_page_Profile(node, true);
					}
				}
			}
		}
		return false;
	}

	/**
	 * step3---thumbs
	 * 
	 * @param event
	 *            com.tencent.mobileqq:id/name
	 * @return
	 */
	public static boolean go_page_thumbs(AccessibilityNodeInfo rootNode,
			boolean isContinue) {
		if (!isContinue) {
			return true;
		}
		MLog.i("go_page_thumbs");
		if (rootNode != null) {
			int childCount = rootNode.getChildCount();
			if (rootNode.getChildCount() > 0) {
				if ("android.widget.RelativeLayout".equals(rootNode
						.getClassName())) {
					if (rootNode.getChildCount() == 2 && rootNode.isClickable()) {
						click_Thumbs = rootNode
								.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						MLog.i("==thumbs_click>>" + click_Thumbs);
					}
				}
				for (int i = 0; i < childCount; i++) {
					AccessibilityNodeInfo node = rootNode.getChild(i);
					if (click_Thumbs) {
						go_page_thumbs(node, false);
					} else {
						go_page_thumbs(node, true);
					}
				}
			} else {
				if (rootNode.getContentDescription() != null) {
					if (rootNode.getContentDescription().toString()
							.contains("次赞")) {
						click_Thumbs = rootNode
								.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						MLog.i("goZan");
					}
				}
			}
		}
		return false;
	}

	public static boolean go_page_thumbList(AccessibilityNodeInfo rootNode,
			boolean isContinue) {
		if (!isContinue) {
			return true;
		}
		MLog.i("go_page_thumbList");
		if (rootNode != null) {
			int childCount = rootNode.getChildCount();
			if (rootNode.getChildCount() > 0) {
				for (int i = 0; i < childCount; i++) {
					AccessibilityNodeInfo node = rootNode.getChild(i);
					if (click_Thumblist) {
						go_page_thumbList(node, false);
					} else {
						go_page_thumbList(node, true);
					}
				}
			} else {
				if (rootNode.getContentDescription() != null) {
					if (rootNode.getContentDescription().toString().equals("赞")) {
						MLog.i("clickZan");
						for (int i = 0; i < 1; i++) {
							click_Thumblist = rootNode
									.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean do_Slide(AccessibilityNodeInfo rootNode) {
		return go_page_Slide(rootNode, true);
	}

	public static boolean do_Profile(AccessibilityNodeInfo rootNode) {
		return go_page_Profile(rootNode, true);
	}

	public static boolean do_Thumbs(AccessibilityNodeInfo rootNode) {
		return go_page_thumbs(rootNode, true);
	}

	public static boolean do_ThumbList(AccessibilityNodeInfo rootNode) {
		return go_page_thumbList(rootNode, true);
	}

}
