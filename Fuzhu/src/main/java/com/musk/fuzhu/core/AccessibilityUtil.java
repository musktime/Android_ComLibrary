package com.musk.fuzhu.core;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import com.musk.fuzhu.R;

/**
 * Created by chenmengting on 2016/11/9.
 */
public class AccessibilityUtil {
    public static boolean isfind = false;
    Context context;

    public AccessibilityUtil(Context context) {
        this.context = context;
    }

    public void openZiqi(AccessibilityEvent event,
                         AccessibilityNodeInfo rootNodeInfo) {
        /*
         * isfind=false; count=0;
		 */

        List<AccessibilityNodeInfo> nodes1 = rootNodeInfo
                .findAccessibilityNodeInfosByViewId("com.miui.securitycenter:id/auto_start_list");

        Log.e("nodes1nodes1", nodes1.size() + "nodes1nodes1nodes1nodes1");
        for (int i = 0; i < nodes1.size(); i++) {
            if (isfind) {
                return;
            }
            AccessibilityNodeInfo nodeInfo = nodes1.get(i);
            Log.e("nodeInfonodeInfo", nodeInfo.getClassName()
                    + "nodeInfonodeInfo");
            if (nodeInfo.getClassName().equals("android.widget.ListView")) {
                Log.e("||||||||||||||||||", nodeInfo.getChildCount()
                        + "|||||||||||||||||||");
                for (int j = 0; j < nodeInfo.getChildCount(); j++) {
                    if (isfind) {
                        return;
                    }
                    AccessibilityNodeInfo listitem = nodeInfo.getChild(j);

                    if (listitem.getClassName().equals(
                            "android.widget.LinearLayout")) {
                        for (int k = 0; k < listitem.getChildCount(); k++) {
                            if (isfind) {
                                return;
                            }
                            AccessibilityNodeInfo childitem = listitem
                                    .getChild(k);
                            Log.e("childitemchilditem",
                                    childitem.getClassName()
                                            + "childitemchilditem");
                            /*
                             * if (childitem.getClassName().equals(
							 * "android.widget.LinearLayout")) {
							 * 
							 * for (int m=0;m<childitem.getChildCount();m++) {
							 * AccessibilityNodeInfo
							 * appNameNodeInfo=childitem.getChild(m);
							 * Log.e("appNameNodeInfoapp"
							 * ,appNameNodeInfo.getClassName
							 * ()+"appNameNodeInfoappNameNodeInfo");
							 */
                            if (childitem.getClassName().equals(
                                    "android.widget.TextView")) {
                                if (childitem.getText() != null) {
                                    Log.e("-------",
                                            childitem.getText().toString()
                                                    + "---"
                                                    + context
                                                    .getResources()
                                                    .getString(
                                                            R.string.app_name));
                                    Log.e("@@@@@@@@@@@@@@",
                                            childitem
                                                    .getText()
                                                    .toString()
                                                    .equals(context
                                                            .getResources()
                                                            .getString(
                                                                    R.string.app_name))
                                                    + "");
                                    if (childitem
                                            .getText()
                                            .toString()
                                            .equals(context.getResources()
                                                    .getString(
                                                            R.string.app_name))) {

										/*
                                         * if
										 * (listitem.getChild(k+1).getClassName
										 * ()
										 * .equals("android.widget.CheckBox")&&
										 * !listitem.getChild(k+1).isChecked())
										 * {
										 * Log.e("getClassName",listitem.getChild
										 * (k+1).getClassName().equals(
										 * "android.widget.CheckBox")+"");
										 * boolean clickDevice =
										 * listitem.getChild(k+1)
										 * .performAction(
										 * AccessibilityNodeInfo.ACTION_CLICK);
										 * Log.e("musk", "==clickDevice==" +
										 * clickDevice);
										 */
                                        /*
										 * nodeInfo.findFocus(j);
										 * 
										 * nodeInfo.focusSearch(j);
										 */
										/*
										 * if (clickDevice) {
										 */
                                        isfind = true;
                                        // }

                                        // }

                                    }
                                }
                            }

                            // }

                            // }

                        }

                    }

                    if (!isfind) {
                        int size = nodeInfo.getChildCount() - 1;
                        if (j >= size) {
                            if (j % size == 0) {
                                boolean scroll = nodeInfo
                                        .performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                Log.i("musk", "=Mi=" + j + "=scroll=" + scroll);
                            }
                        }
                    }

                }

            }
			/*
			 * if (!isfind&&nodes1.size()>0) {
			 * 
			 * boolean ok = nodes1.get(0)
			 * .performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD); //
			 * openZiqi(event);
			 * Log.e("ACTION_SCROLL_FORWARD","ACTION_SCROLL_FORWARD");
			 * //nodes1.get(0).performAction(AccessibilityNodeInfo); }
			 */
        }
    }

    public void xiaomiziqi(AccessibilityEvent event) {
        /**
         * 针对小米机型的测试代码
         */
        try {
            if (event.getPackageName().equals("com.miui.securitycenter")) {
                Log.i("musk", "==xiaomiziqi==");
                String whichItem = "锁屏设置";
                // com.miui.securitycenter:id/auto_start_list
                // com.miui.securitycenter:id/list_view
                List<AccessibilityNodeInfo> nodes = event.getSource()
                        .findAccessibilityNodeInfosByViewId(
                                "com.miui.securitycenter:id/auto_start_list");
                if (nodes == null) {
                    return;
                }
                for (int i = 0; i < nodes.size(); i++) {
                    AccessibilityNodeInfo listview = nodes.get(i);
                    Log.i("musk", "==listview==" + i);
                    if (listview.getClassName().equals(
                            "android.widget.ListView")) {
                        int itemCount = listview.getChildCount() - 1;
                        boolean ok = false;
                        Log.i("musk",
                                "==getChildCount==" + listview.getChildCount());
                        for (int j = 0; j < listview.getChildCount(); j++) {
                            AccessibilityNodeInfo item = listview.getChild(j);
                            if (item != null) {
                                for (int k = 0; k < item.getChildCount(); k++) {
                                    AccessibilityNodeInfo part = item
                                            .getChild(k);
                                    if (part.getClassName().equals(
                                            "android.widget.TextView")) {
                                        if (part.getText() != null) {
                                            Log.i("musk", "==" + j + "==part=="
                                                    + part.getText());
                                            if (part.getText().toString()
                                                    .equals(whichItem)) {
                                                Log.i("musk", "==find==");
                                                ok = true;
                                                // boolean clickItem = item
                                                // .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                // Log.i("musk", "==clickItem=="
                                                // + clickItem);
                                                // return;
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("musk", "==xiaomiziqi==Exception==" + e.getMessage());
        }
    }

    /**
     * 检查节点信息打开允许接收通知开关
     */
    private void checkNodeInfo(AccessibilityEvent event,
                               AccessibilityNodeInfo rootNodeInfo) {

		/* 聊天会话窗口，遍历节点匹配“领取红包”和"微信红包" */
		/*
		 * List<AccessibilityNodeInfo> nodes1 = this
		 * .findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]
		 * {"LockDemo"});
		 */

        // List<AccessibilityNodeInfo>
        // nodes1=rootNodeInfo.findAccessibilityNodeInfosByText("锁屏设置");
        List<AccessibilityNodeInfo> nodes1 = rootNodeInfo
                .findAccessibilityNodeInfosByViewId("android:id/list");
        Log.e("nodes1nodes1nodes1", nodes1.size() + "nodes1nodes1nodes1");
        for (int i = 0; i < nodes1.size(); i++) {
            if (isfind) {
                return;
            }
            AccessibilityNodeInfo accessibilityNodeInfo = nodes1.get(i);
            for (int j = 0; j < accessibilityNodeInfo.getChildCount(); j++) {
                if (isfind) {
                    return;
                }
                AccessibilityNodeInfo item = accessibilityNodeInfo.getChild(j);
                if (item != null) {
                    if (item.getClassName().equals(
                            "android.widget.LinearLayout")) {
                        for (int m = 0; m < item.getChildCount(); m++) {
                            if (isfind) {
                                return;
                            }
                            AccessibilityNodeInfo item2 = item.getChild(m);
                            if (item2.getClassName().equals(
                                    "android.widget.TextView")) {
                                if (!item2.getText().toString().equals("")) {

                                    if (item2
                                            .getText()
                                            .toString()
                                            .equals(context.getResources()
                                                    .getString(
                                                            R.string.app_name))) {
                                        // 点击容器
                                        Log.e("点击容器点击容器", "点击容器点击容器点击容器");
                                        boolean clickDevice = item
                                                .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        Log.e("musk", "==clickDevice=="
                                                + clickDevice);
                                        if (clickDevice) {
                                            isfind = true;
                                        }

                                    }
                                }

                            }

                        }
                    }
                }
                if (!isfind) {
                    int size = accessibilityNodeInfo.getChildCount() - 1;
                    if (j >= size) {
                        if (j % size == 0) {
                            boolean scroll = accessibilityNodeInfo
                                    .performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            Log.i("musk", "=Mi=" + j + "=scroll=" + scroll);
                        }
                    }
                }

            }

        }
        // 允许接收通知点击事件
        List<AccessibilityNodeInfo> btnnode = rootNodeInfo
                .findAccessibilityNodeInfosByViewId("android:id/button1");
        for (int k = 0; k < btnnode.size(); k++) {

            AccessibilityNodeInfo btn = btnnode.get(k);
            if (btn.getClassName().equals("android.widget.Button"))
                ;
            {
                if (btn.getText().toString().equals("允许")
                        || btn.getText().toString().equals("确定")) {
                    boolean btnclick = btn
                            .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e("musk", "==btnclick==" + btnclick);
                }

            }
        }

        // Log.e("nodes1nodes1nodes1","nodes1nodes1"+nodes1.size());
        // nodes1.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);

    }

    public void startXuanfu(AccessibilityEvent event,
                            AccessibilityNodeInfo rootNodeInfo) {

		/* 聊天会话窗口，遍历节点匹配“领取红包”和"微信红包" */
		/*
		 * List<AccessibilityNodeInfo> nodes1 = this
		 * .findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]
		 * {"LockDemo"});
		 */

        // List<AccessibilityNodeInfo>
        // nodes1=rootNodeInfo.findAccessibilityNodeInfosByText("锁屏设置");
        List<AccessibilityNodeInfo> nodes1 = rootNodeInfo
                .findAccessibilityNodeInfosByViewId("android:id/list");
        Log.e("nodes1nodes1nodes1", nodes1.size() + "nodes1nodes1nodes1");
        for (int i = 0; i < nodes1.size(); i++) {
            if (isfind) {
                return;
            }
            AccessibilityNodeInfo accessibilityNodeInfo = nodes1.get(i);
            Log.e("accessibilityNodeInfo", accessibilityNodeInfo.getClassName()
                    + "accessibilityNodeInfo");
            if (accessibilityNodeInfo.getClassName().equals(
                    "android.widget.ListView")) {
                for (int j = 0; j < accessibilityNodeInfo.getChildCount(); j++) {
                    if (isfind) {
                        return;
                    }
                    AccessibilityNodeInfo listviewItem = accessibilityNodeInfo
                            .getChild(j);
                    Log.e("listviewItemlistview", listviewItem.getClassName()
                            + "listviewItemlistviewItem");
                    if (listviewItem.getClassName().equals(
                            "android.widget.LinearLayout")) {
                        for (int m = 0; m < listviewItem.getChildCount(); m++) {
                            if (isfind) {
                                return;
                            }
                            AccessibilityNodeInfo childnode = listviewItem
                                    .getChild(m);
                            Log.e("childnodechildnode",
                                    childnode.getClassName()
                                            + "childnodechildnode");
							/*
							 * if (childnode.getClassName().equals(
							 * "android.widget.RelativeLayout")) { for (int
							 * n=0;n<childnode.getChildCount();n++) {
							 */
                            // AccessibilityNodeInfo
                            // textnode=childnode.getChild(n);
                            Log.e("textnodetextnode", childnode.getClassName()
                                    + "textnodetextnode");
                            if (childnode.getClassName().equals(
                                    "android.widget.TextView")) {
                                Log.e("textnodetextnode", childnode.getText()
                                        .toString() + "textnodetextnode");
                                if (childnode.getText().toString()
                                        .equals("显示悬浮窗")) {
                                    boolean btnclick = listviewItem
                                            .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    Log.e("musk", "==btnclick==" + btnclick);
                                    isfind = true;
                                    // return;
                                }
                            }
							/*
							 * } }
							 */
                        }
                    }
                    if (!isfind) {
                        int size = accessibilityNodeInfo.getChildCount() - 1;
                        if (j >= size) {
                            if (j % size == 0) {
                                boolean scroll = accessibilityNodeInfo
                                        .performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                Log.i("musk", "=Mi=" + j + "=scroll=" + scroll);
                            }
                        }
                    }
                }
            }
        }
        // 允许接收通知点击事件
        List<AccessibilityNodeInfo> btnnode = rootNodeInfo
                .findAccessibilityNodeInfosByViewId("android:id/text1");
        for (int k = 0; k < btnnode.size(); k++) {

            AccessibilityNodeInfo btn = btnnode.get(k);
            if (btn.getClassName().equals("android.widget.CheckedTextView"))
                ;
            {
                if (btn.getText().toString().equals("允许")) {
                    boolean btnclick = btn
                            .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e("musk", "==btnclick==" + btnclick);
                }

            }
        }

    }

    public void closeSystemLock(AccessibilityEvent event,
                                AccessibilityNodeInfo rootNodeInfo) {

        List<AccessibilityNodeInfo> nodes1 = rootNodeInfo
                .findAccessibilityNodeInfosByViewId("android:id/list");
        Log.e("nodes1nodes1nodes1", nodes1.size() + "nodes1nodes1nodes1");
        for (int i = 0; i < nodes1.size(); i++) {
            if (isfind) {
                return;
            }
            AccessibilityNodeInfo accessibilityNodeInfo = nodes1.get(i);
            Log.e("accessibilityNodeInfo", accessibilityNodeInfo.getClassName()
                    + "accessibilityNodeInfo");
            if (accessibilityNodeInfo.getClassName().equals(
                    "android.widget.ListView")) {
                for (int j = 0; j < accessibilityNodeInfo.getChildCount(); j++) {
                    if (isfind) {
                        return;
                    }
                    AccessibilityNodeInfo listviewItem = accessibilityNodeInfo
                            .getChild(j);
                    Log.e("listviewItemlistview", listviewItem.getClassName()
                            + "listviewItemlistviewItem");
                    if (listviewItem.getClassName().equals(
                            "android.widget.LinearLayout")) {
                        for (int m = 0; m < listviewItem.getChildCount(); m++) {
                            if (isfind) {
                                return;
                            }
                            AccessibilityNodeInfo childnode = listviewItem
                                    .getChild(m);
                            Log.e("childnodechildnode",
                                    childnode.getClassName()
                                            + "childnodechildnode");
							/*
							 * if (childnode.getClassName().equals(
							 * "android.widget.RelativeLayout")) { for (int
							 * n=0;n<childnode.getChildCount();n++) {
							 */
                            // AccessibilityNodeInfo
                            // textnode=childnode.getChild(n);
                            Log.e("textnodetextnode", childnode.getClassName()
                                    + "textnodetextnode");
                            if (childnode.getClassName().equals(
                                    "android.widget.TextView")) {
                                Log.e("textnodetextnode", childnode.getText()
                                        .toString() + "textnodetextnode");
                                if (childnode.getText().toString()
                                        .equals("直接进入系统")) {
                                    boolean btnclick = listviewItem
                                            .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    Log.e("musk", "==btnclick==" + btnclick);
                                    isfind = true;
                                    // return;
                                }
                            }
							/*
							 * } }
							 */
                        }
                    }
                }
            }
        }
    }

    public void huaWeiNotification(AccessibilityEvent event,
                                   AccessibilityNodeInfo rootNodeInfo) {

        if (event.getPackageName().equals("com.huawei.systemmanager")) {
            List<AccessibilityNodeInfo> nodeinfo = rootNodeInfo
                    .findAccessibilityNodeInfosByText(context.getResources()
                            .getString(R.string.app_name));
            Log.e("nodeinfonodeinfo", nodeinfo + "nodeinfonodeinfonodeinfo");
            if (nodeinfo.size() > 0) {
                for (int i = 0; i < nodeinfo.size(); i++) {
                    AccessibilityNodeInfo textviewnodeinfo = nodeinfo.get(i);
                    if (textviewnodeinfo.getClassName().equals(
                            "android.widget.TextView")) {

                    }
                }
            }
        }

    }
}
