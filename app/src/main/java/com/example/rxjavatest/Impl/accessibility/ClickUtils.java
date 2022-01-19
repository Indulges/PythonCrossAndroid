package com.example.rxjavatest.Impl.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.RequiresApi;
import com.example.rxjavatest.MyApplication;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClickUtils {
    private static final String TAG = ClickUtils.class.getSimpleName();
    private static final String ACCESSIBILITY_SERVICE_PATH = EntryAccessibilityService.class.getCanonicalName();
    private AccessibilityService mService;
    private static ClickUtils sClickUtils = null;

    public static ClickUtils getInstance(){
        if (sClickUtils == null){
            synchronized (ClickUtils.class){
                if (sClickUtils == null){
                    sClickUtils = new ClickUtils();
                }
            }
        }
        return sClickUtils;
    }


    /**
     * 判断是否有辅助功能权限
     *
     * @return
     */
    public static boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(MyApplication.getContext().getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        String packageName = MyApplication.getContext().getPackageName();
        final String serviceStr = packageName + "/" + ACCESSIBILITY_SERVICE_PATH;
        Log.v(TAG, "serviceStr: " + serviceStr);
        if (accessibilityEnabled == 1) {
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
            String settingValue = Settings.Secure.getString(MyApplication.getContext().getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    if (accessabilityService.equalsIgnoreCase(serviceStr)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 判断辅助服务是否正在运行
     */
    public static boolean isServiceRunning(AccessibilityService service) {
        if (service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();
        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }
        return true;
    }

    /**
     * 打开辅助服务的设置
     */
    public void openAccessibilityServiceSettings(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 自动点击按钮
     *
     * @param event
     * @param nodeText 按钮文本
     */
    public void handleEvent(AccessibilityEvent event, String nodeText) {
        List<AccessibilityNodeInfo> unintall_nodes = event.getSource().findAccessibilityNodeInfosByText(nodeText);
        if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < unintall_nodes.size(); i++) {
                node = unintall_nodes.get(i);
                if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }


    /**
     * 获取text
     */
    public String getNodeText(String id) {
        List<AccessibilityNodeInfo> unintall_nodes = mService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(id);
        if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
            return unintall_nodes.get(0).getText().toString().trim();
        }
        return null;
    }

    /**
     * 获取text
     */
    public String getNodeText(AccessibilityNodeInfo nodeInfo, String id) {
        List<AccessibilityNodeInfo> unintall_nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
            return unintall_nodes.get(0).getText().toString().trim();
        }
        return null;
    }


    private ClickUtils() {
        mService = EntryAccessibilityService.getAccessibilityService();
    }


    //通过id查找
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (nodeInfo == null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    //通过id查找
    public AccessibilityNodeInfo findNodeInfosById(String resId) {
        if (mService.getRootInActiveWindow() != null) {
            List<AccessibilityNodeInfo> list = mService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    //通过id查找
    public List<AccessibilityNodeInfo> findNodeListInfosById(String resId) {
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        if (mService.getRootInActiveWindow() != null) {
            List<AccessibilityNodeInfo> list = mService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(resId);
            if (list != null) {
                result.addAll(list);
            }
        }
        return result;
    }

    //通过id查找 ,第i个组件
    public AccessibilityNodeInfo findNodeInfosById(String resId, int index) {
        List<AccessibilityNodeInfo> list = mService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(resId);
        if (list != null && list.size() > index) {
            return list.get(index);
        }
        return null;
    }

    //返回指定位置的node
    public AccessibilityNodeInfo findNodeInfosByIdAndPosition(AccessibilityNodeInfo nodeInfo, String resId, int position) {
        if (nodeInfo == null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                return list.get(i);
            }
        }
        return null;
    }

    //通过某个文本查找
    public AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    //通过某个文本查找
    public AccessibilityNodeInfo findNodeInfosByText(String text) {
        if (mService.getRootInActiveWindow() != null) {
            List<AccessibilityNodeInfo> list = mService.getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
        return null;
    }

    //通过ClassName查找
    public AccessibilityNodeInfo findNodeInfosByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        for (int i = 0; nodeInfo != null && i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
            if (node != null) {
                if (className.equals(node.getClassName())) {
                    return node;
                } else if (node.getChildCount() > 0) {
                    findNodeInfosByClassName(node, className);
                }
            }
        }
        return null;
    }

    //通过ClassName查找
    public List<AccessibilityNodeInfo> findNodeInfoListByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (TextUtils.isEmpty(className)) {
            return Collections.EMPTY_LIST;
        }
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        for (int i = 0; nodeInfo != null && i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
            if (node != null && className.equals(node.getClassName())) {
                result.add(node);
            }
        }
        return result;
    }

    //通过ClassName查找
    public AccessibilityNodeInfo findNodeInfosByClassName(String className) {
        return findNodeInfosByClassName(mService.getRootInActiveWindow(), className);
    }

    /**
     * 找父组件
     */
    public AccessibilityNodeInfo findParentNodeInfosByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (nodeInfo == null) {
            return null;
        }
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        if (className.equals(nodeInfo.getClassName())) {
            return nodeInfo;
        }
        return findParentNodeInfosByClassName(nodeInfo.getParent(), className);
    }

    private static final Field sSourceNodeField;

    static {
        Field field = null;
        try {
            field = AccessibilityNodeInfo.class.getDeclaredField("mSourceNodeId");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sSourceNodeField = field;
    }

    public long getSourceNodeId(AccessibilityNodeInfo nodeInfo) {
        if (sSourceNodeField == null) {
            return -1;
        }
        try {
            return sSourceNodeField.getLong(nodeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getViewIdResourceName(AccessibilityNodeInfo nodeInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return nodeInfo.getViewIdResourceName();
        }
        return null;
    }

    //返回HOME界面
    public void performHome() {
        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    //返回
    public void performBack() {
        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }



    /**
     * 点击事件
     */
    public void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    /**
     * 点击事件
     */
    public void performClick(String id) {
        performClick(findNodeInfosById(id));
    }

    //长按事件
    public void performLongClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
    }

    //move 事件
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void performMoveDown(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        nodeInfo.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN.getId());
    }


    //ACTION_SCROLL_FORWARD 事件
    public boolean perform_scroll_forward(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    //ACTION_SCROLL_BACKWARD 后退事件
    public boolean perform_scroll_backward(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    //粘贴
    @TargetApi(18)
    public void performPaste(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
    }

    //设置editview text
    @TargetApi(21)
    public void performSetText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return;
        }
        CharSequence className = nodeInfo.getClassName();
        if ("android.widget.EditText".equals(className)) {//||"android.widget.TextView".equals(className)
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo
                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean clickByPoint(int x, int y){
        AccessibilityNodeInfo root = mService.getRootInActiveWindow();
        Rect rect = new Rect();
        root.getBoundsInParent(rect);
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription gestureDescription = new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 1, 1)).build();
        boolean result = mService.dispatchGesture(gestureDescription, null, null);
        Log.v(TAG, String.format("点击坐标：%d, %d, %s", x, y, result));
        return result;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean clickByRect(Rect rect){
        return clickByPoint(rect.centerX(), rect.centerY());
    }
}
