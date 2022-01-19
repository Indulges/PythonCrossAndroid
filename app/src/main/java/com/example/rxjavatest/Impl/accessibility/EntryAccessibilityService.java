package com.example.rxjavatest.Impl.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import com.example.rxjavatest.MyApplication;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EntryAccessibilityService extends AccessibilityService {
    private static final String TAG = EntryAccessibilityService.class.getCanonicalName();
    private static EntryAccessibilityService sAccessibilitySampleService;

    public static EntryAccessibilityService getAccessibilityService(){
        if (sAccessibilitySampleService != null){
            return sAccessibilitySampleService;
        }else {
            Log.e(TAG, "sAccessibilitySampleService is null, plese check the AccessibilitySampleService");
            isAccessibilityServiceON(MyApplication.getContext());
            return null;
        }

    }

    @Override
    protected void onServiceConnected() {
        // 通过代码可以动态配置，但是可配置项少一点
        Log.v(TAG, "无障碍服务启动");
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        accessibilityServiceInfo.notificationTimeout = 0;
        accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
        setServiceInfo(accessibilityServiceInfo);
        Toast.makeText(this, "无障碍服务启动", Toast.LENGTH_LONG).show();
        String test = android.provider.Settings.Secure.getString(MyApplication.getContext().getContentResolver(),
                android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        Log.v(TAG, " enabledServicesBuilder.toString() = " + test);
        sAccessibilitySampleService = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "无障碍服务解除绑定");
        return super.onUnbind(intent);
    }

    public static boolean isAccessibilityServiceON(Context context){
        final String enabledServicesSetting = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null) {
            return false;
        }
        final Set<ComponentName> enabledServices = new HashSet<>();
        final TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);
        while (colonSplitter.hasNext()) {
            final String componentNameString = colonSplitter.next();
            String name = MyApplication.getContext().getPackageName() + '/' + EntryAccessibilityService.class.getName();
            if (componentNameString.equals(name)){
                return true;
            }
        }
        return false;
    }

}
