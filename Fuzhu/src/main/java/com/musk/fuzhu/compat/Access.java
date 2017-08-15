package com.musk.fuzhu.compat;

import java.util.List;

import com.musk.fuzhu.log.MLog;

import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class Access {

    private static String methordName = "";

    public static void touchViewLayers(AccessibilityNodeInfo rootNode) {
        MLog.i("==touchView==");
        if (rootNode != null) {
            int childCount = rootNode.getChildCount();
            if (rootNode.getChildCount() > 0) {
                MLog.i("nodeContainer==getClassName>>"
                        + rootNode.getClassName());
                MLog.i("nodeContainer==getWindowId>>" + rootNode.getWindowId());
                MLog.i("nodeContainer==getViewIdResourceName>>"
                        + rootNode.getViewIdResourceName());
                MLog.i("nodeContainer==getContentDescription>>"
                        + rootNode.getContentDescription());
                MLog.i("nodeContainer==isClickable>>" + rootNode.isClickable());
                for (int i = 0; i < childCount; i++) {
                    AccessibilityNodeInfo node = rootNode.getChild(i);
                    MLog.i("---------->>>第" + i + "个Container>>>----------");
                    touchViewLayers(node);
                }
            } else {
                MLog.i("----------【我是node】----------");
                MLog.i("node==getClassName>>" + rootNode.getClassName());
                MLog.i("node==getWindowId>>" + rootNode.getWindowId());
                MLog.i("node==getViewIdResourceName>>"
                        + rootNode.getViewIdResourceName());
                MLog.i("node==getContentDescription>>"
                        + rootNode.getContentDescription());
                MLog.i("node==isClickable>>" + rootNode.isClickable());
            }
        }
    }

    /**
     * 通过text定位点击
     *
     * @param event
     * @return
     */
    public static boolean clickNodeByText(AccessibilityEvent event, String text) {
        List<AccessibilityNodeInfo> nodes = event.getSource()
                .findAccessibilityNodeInfosByText(text);
        if (nodes == null || nodes.size() == 0) {
            return false;
        }
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo info = nodes.get(i);
            if (info.isClickable()) {
                return info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        return false;
    }

    /**
     * 通过id定位点击
     *
     * @param event
     * @param nodeId
     * @return
     */
    public static boolean clickNodeById(AccessibilityEvent event, String nodeId) {
        List<AccessibilityNodeInfo> nodes = event.getSource()
                .findAccessibilityNodeInfosByViewId(nodeId);
        if (nodes == null || nodes.size() == 0) {
            MLog.i("stop here");
            return false;
        }
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo info = nodes.get(i);
            MLog.i("info.isClickable" + info.isClickable());
            MLog.i("info.isClickable" + info.isClickable());
            if (info.isClickable()) {
                return info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        return false;
    }

    /**
     * 适合于ListView/LinearLayout/TextView + *****
     *
     * @param event
     * @param listviewId
     * @param itemDes
     * @return
     */
    public static boolean clickListviewItemByDes(AccessibilityEvent event,
                                                 String listviewId, String itemDes, int clickCounter,
                                                 String upmethordName) {
        Log.i("musk", "==start " + upmethordName + "==");
        try {
            if (upmethordName == null || upmethordName.length() <= 0) {
                methordName = "access_ListView";
            } else {
                methordName = upmethordName;
            }
            List<AccessibilityNodeInfo> nodes = event.getSource()
                    .findAccessibilityNodeInfosByViewId(listviewId);
            if (nodes == null) {
                return false;
            }
            for (int i = 0; i < nodes.size(); i++) {
                AccessibilityNodeInfo listview = nodes.get(i);
                boolean isStart = (listview.getClassName().equals(
                        "android.widget.ListView") || listview.getClassName()
                        .equals("android.widget.AbsListView"));
                if (isStart) {
                    Log.i("musk", "==" + methordName + "==getChildCount="
                            + listview.getChildCount());
                    for (int j = 0; j < listview.getChildCount(); j++) {
                        AccessibilityNodeInfo item = listview.getChild(j);
                        boolean isFind = false;
                        if (item != null) {
                            Log.i("musk", "==" + methordName + "==" + j + "=="
                                    + item.getClassName());
                            for (int k = 0; k < item.getChildCount(); k++) {
                                AccessibilityNodeInfo part = item.getChild(k);
                                if (part != null) {
                                    if (part.getContentDescription() != null) {
                                        if (itemDes.equals(part
                                                .getContentDescription()
                                                .toString())) {
                                            Log.i("musk", "==" + methordName
                                                    + "==isFind==" + j);
                                            boolean clickItem = false;
                                            for (int l = 0; l < clickCounter; l++) {
                                                clickItem = item
                                                        .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
                                            Log.i("musk",
                                                    "==item=="
                                                            + item.getClassName());
                                            Log.i("musk",
                                                    "==item=="
                                                            + item.isClickable());
                                            Log.i("musk", "==clickItem=="
                                                    + clickItem);
                                            return clickItem;
                                        }
                                    }

                                }
                            }
                        }
                        if (!isFind) {
                            int size = listview.getChildCount() - 1;
                            if (j >= size) {
                                if (j % size == 0) {
                                    boolean scroll = listview
                                            .performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                    Log.i("musk", "==" + methordName + "==" + j
                                            + "=scroll=" + scroll);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("musk", "==" + methordName + "==exception==" + e.getMessage());
        }
        Log.i("musk", "==end " + upmethordName + "==");
        return false;
    }

    /**
     * 适合于ListView/LinearLayout/TextView + *****
     *
     * @param event
     * @param listviewId
     * @param itemText
     * @return
     */
    public static boolean clickListviewItemByText(AccessibilityEvent event,
                                                  String listviewId, String itemText, String upmethordName) {
        Log.i("musk", "==start " + upmethordName + "==");
        try {
            if (upmethordName == null || upmethordName.length() <= 0) {
                methordName = "access_ListView";
            } else {
                methordName = upmethordName;
            }
            List<AccessibilityNodeInfo> nodes = event.getSource()
                    .findAccessibilityNodeInfosByViewId(listviewId);
            if (nodes == null) {
                return false;
            }
            for (int i = 0; i < nodes.size(); i++) {
                AccessibilityNodeInfo listview = nodes.get(i);
                if (listview.getClassName().equals("android.widget.ListView")) {
                    Log.i("musk", "==" + methordName + "==getChildCount="
                            + listview.getChildCount());
                    for (int j = 0; j < listview.getChildCount(); j++) {
                        AccessibilityNodeInfo item = listview.getChild(j);
                        boolean isFind = false;
                        if (item != null) {
                            Log.i("musk", "==" + methordName + "==" + j + "=="
                                    + item.getClassName());
                            for (int k = 0; k < item.getChildCount(); k++) {
                                AccessibilityNodeInfo part = item.getChild(k);
                                if (part != null) {
                                    if (part.getClassName().equals(
                                            "android.widget.TextView")) {
                                        if (part.getText() != null) {
                                            if (itemText.equals(part.getText()
                                                    .toString())) {
                                                Log.i("musk", "=="
                                                        + methordName
                                                        + "==isFind==" + j);
                                                boolean clickItem = item
                                                        .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                Log.i("musk",
                                                        "==item=="
                                                                + item.getClassName());
                                                Log.i("musk",
                                                        "==item=="
                                                                + item.isClickable());
                                                Log.i("musk", "==clickItem=="
                                                        + clickItem);
                                                return clickItem;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (!isFind) {
                            int size = listview.getChildCount() - 1;
                            if (j >= size) {
                                if (j % size == 0) {
                                    boolean scroll = listview
                                            .performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                    Log.i("musk", "==" + methordName + "==" + j
                                            + "=scroll=" + scroll);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("musk", "==" + methordName + "==exception==" + e.getMessage());
        }
        Log.i("musk", "==end " + upmethordName + "==");
        return false;
    }

    /**
     * 适合于ScrollView/FrameLayout/TextView
     *
     * @param event
     * @param scrollViewId
     * @param subTitle
     * @param itemName
     * @return
     */
    public static boolean clickScrollviewItemByText(AccessibilityEvent event,
                                                    String scrollViewId, String subTitle, String itemName) {
        List<AccessibilityNodeInfo> sets = event.getSource()
                .findAccessibilityNodeInfosByViewId(scrollViewId);
        if (sets == null) {
            return false;
        }
        for (int i = 0; i < sets.size(); i++) {
            AccessibilityNodeInfo scrollView = sets.get(i);
            if (scrollView.getClassName().equals("android.widget.ScrollView")) {
                // find ScrollView
                boolean isFind = false;
                int visibleCount = scrollView.getChildCount() - 1;
                Log.i("musk",
                        "==scrollView.getChildCount=="
                                + scrollView.getChildCount());
                for (int j = 0; j < scrollView.getChildCount(); j++) {
                    AccessibilityNodeInfo item = scrollView.getChild(j);
                    // find title
                    if (item != null) {
                        if (item.getClassName().equals(
                                "android.widget.TextView")
                                && subTitle.equals(item.getText())) {
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
                                    if (part.getText() != null) {
                                        if (itemName.equals(part.getText()
                                                .toString())) {
                                            Log.i("musk", "==安全==");
                                            isFind = true;
                                            Log.i("musk", "==我在这个索引处点击的==" + j);
                                            return item
                                                    .performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        }
                                    }
                                } else {
                                    if (!isFind) {
                                        if (j >= visibleCount) {
                                            if (j % visibleCount == 0) {
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
}
