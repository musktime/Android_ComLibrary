package com.musk.fuzhu.core;

import java.util.List;
import com.assistant.library.Huawei;
import com.assistant.library.Leshi;
import com.assistant.library.Nexus;
import com.assistant.library.Thumb;
import com.assistant.library.Vivo;
import com.assistant.library.WeiXin;
import com.assistant.library.Xiaomi;
import com.assistant.master.TestActivity;
import com.assistant.master.utils.MLog;
import com.fuzhu.master.R;
import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyAccessibility extends AccessibilityService {

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		processAccessibilityEnvent(event);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * process event
	 * 
	 * @param event
	 */
	private void processAccessibilityEnvent(AccessibilityEvent event) {
		if (event == null || event.getSource() == null) {
		} else {
			processAutoStart(event);
		}
	}

	String targetAppName = "360卫士";

	@SuppressWarnings("unused")
	private void processAutoStart(AccessibilityEvent event) {
		try {
			if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
				if (true) {
					if (event.getPackageName().equals("com.tencent.mobileqq")) {
						try {
							MLog.i("--tencent--");
							AccessibilityNodeInfo rootNode = getRootInActiveWindow();
							Thumb.do_Slide(rootNode);

							rootNode = getRootInActiveWindow();
							Thumb.do_Profile(rootNode);

							rootNode = getRootInActiveWindow();
							Thumb.do_Thumbs(rootNode);

							rootNode = getRootInActiveWindow();
							Thumb.do_ThumbList(rootNode);
						} catch (Exception e) {
							e.printStackTrace();
							MLog.i("==process==Ex>>" + e.getMessage());
						}
					}
				}
				/**
				 * ---------------------乐视机型------------------------
				 */
				// 通知管理、悬浮窗、关闭系统锁屏、自启动
				if (false) {
					// 设备管理器【设置主页进入】
					Leshi.enable_DeviceManage(event, this);
					Leshi.test(event);
				}
				if (false) {
					// 自启动测试 【管家主页进入】
					Leshi.enable_AutoStart(event, this);
				}
				if (false) {
					// 测试微信-列表-文章-内容获取
					if (event.getPackageName().equals("com.tencent.mm")) {
						if (WeiXin.flag_step1) {
							AccessibilityNodeInfo node = getRootInActiveWindow();
							Log.i("musk", "==node==" + node.getClassName());
							for (int i = 0; i < node.getChildCount(); i++) {
								AccessibilityNodeInfo content = node
										.getChild(i);
								Log.i("musk",
										"==content==" + content.getClassName());

								if (content.getClassName().equals(
										"android.widget.TextView")) {
									if (content.getText() == null
											& content.isClickable()) {
										boolean clickTv = content
												.performAction(AccessibilityNodeInfo.ACTION_CLICK);
										if (clickTv) {
											WeiXin.flag_step1 = false;
										}
									}
								}
							}
						}
						if (WeiXin.flag_step1) {
							return;
						}
						if (WeiXin.flag_step2) {
							AccessibilityNodeInfo node = getRootInActiveWindow();
							Log.i("musk", "==node==" + node.getClassName());
							// 保存ImageButton节点
							AccessibilityNodeInfo saveNode = null;
							for (int i = 0; i < node.getChildCount(); i++) {
								AccessibilityNodeInfo content = node
										.getChild(i);
								Log.i("musk",
										"==content==" + content.getClassName());
								if (content.getClassName().equals(
										"android.widget.ImageButton")) {
									saveNode = content;
								}
								if (content.getClassName().equals(
										"android.widget.TextView")) {
									if (content.getText() != null) {
										if ("文章".equals(content.getText()
												.toString())) {
											if (saveNode != null) {
												boolean clickBut = saveNode
														.performAction(AccessibilityNodeInfo.ACTION_CLICK);
												if (clickBut) {
													WeiXin.flag_step2 = false;
												}
											}
										}
									}
								}
							}
						}
						if (WeiXin.flag_step2) {
							return;
						}
						if (WeiXin.flag_step3) {// com.tencent.smtt.webkit.WebView
							List<AccessibilityNodeInfo> Frames = event
									.getSource()
									.findAccessibilityNodeInfosByViewId(
											"com.tencent.mm:id/ajp");
							for (int i = 0; i < Frames.size(); i++) {
								AccessibilityNodeInfo frame = Frames.get(i);
								if (frame.getClassName().equals(
										"android.widget.FrameLayout")) {
									for (int j = 0; j < frame.getChildCount(); j++) {
										AccessibilityNodeInfo web = frame
												.getChild(j);
										if (web.getClassName()
												.equals("com.tencent.smtt.webkit.WebView")) {
											web.performAction(
													AccessibilityNodeInfo.ACTION_CLICK,
													new Bundle());
										}
									}
								}
							}
						}
						if (WeiXin.flag_step3) {
							return;
						}
					}
				}
				/**
				 * ---------------------原生Nexus 5----------------------
				 */
				if (false) {
					/**
					 * 设备管理
					 */
					if (event.getPackageName().equals("com.android.settings")) {
						Nexus.enable_DeviceManage(event, this);
						Intent in = new Intent(this, TestActivity.class);
						in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(in);
					}
				}
				/**
				 * --------------------vivo机型------------------------ -------
				 */
				if (event.getPackageName().equals("com.iqoo.secure")) {
					if (false) {
						/**
						 * 自启动[从i管家开始跳转，不是从设置跳转]
						 */
						Vivo.enable_autoStart(event, this);
					}
				}
				/**
				 * 设备管理 【VIVO Y29L】【Android 4.4.4】【FunTouch_OS 2.0】 设置列表
				 * android:id/list
				 */
				if (event.getPackageName().equals("com.android.settings")) {
					if (false) {
						/**
						 * 设备管理器
						 */
						Vivo.enable_DeviceManage(event, this);
					}
				}
				/**
				 * --------------------小米机型--------------------
				 */
				if (event.getPackageName().equals("com.miui.securitycenter")) {
					/**
					 * 测试自启动管理点击， 如果不在第一页中，则需要滚动查找
					 */
					if (false) {
						// 自启动列表的滚动问题
						Xiaomi.test_ziqidong_listScroll(event);
					}
					if (false) {
						/**
						 * 自启动
						 */
						Xiaomi.enable_AutoStart_FormManager(event, this);
					}
				}
				/**
				 * 针对小米 设置页面 com.android.settings/.MainSettings
				 */
				if (event.getPackageName().equals("com.android.settings")) {

					// 点击设备管理
					if (false) {
						Xiaomi.enable_DeviceManage(event, this);
					}
					// 通知管理
					if (false) {
						Xiaomi.go_page_tongzhiguanli(event,
								this.getString(R.string.app_name));
					}

					/**
					 * 测试我自己写的开关控件
					 */
					if (false) {
						if (event.getPackageName().equals(
								"com.phonecleaner.master")) {
							List<AccessibilityNodeInfo> checks = event
									.getSource()
									.findAccessibilityNodeInfosByViewId(
											"com.phonecleaner.master:id/checkBox");
							AccessibilityNodeInfo check = checks.get(0);
							boolean isOk = check
									.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							Log.i("musk", "====check click====" + isOk);
						}
					}

					/**
					 * --------------------华为机型--------------------
					 */

					if (event.getPackageName().equals("com.android.settings")) {
						if (false) {
							// 1.设备管理器
							Huawei.enable_DeviceManage(event, this);
						}
						if (false) {
							// 2.自启动管理

						}
					}
				}
			}
		} catch (Exception e) {
			Log.e("musk", "==exception==" + e.getMessage());
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------
	@Override
	public void onInterrupt() {
		Toast.makeText(this, "====AccessibilityService onInterrupt====",
				Toast.LENGTH_SHORT).show();
	}

	// ---------可选-------
	@Override
	protected void onServiceConnected() {
		super.onServiceConnected();
		Toast.makeText(this, "====AccessibilityService onServiceConnected====",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		Toast.makeText(this, "====AccessibilityService onRebind====",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		super.unbindService(conn);
		Toast.makeText(this, "====AccessibilityService unbindService====",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "====AccessibilityService onDestroy====",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "====AccessibilityService onUnbind====",
				Toast.LENGTH_SHORT).show();
		return super.onUnbind(intent);
	}
}