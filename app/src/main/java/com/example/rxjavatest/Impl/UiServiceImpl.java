package com.example.rxjavatest.Impl;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.example.rxjavatest.BaseTypeOuterClass;
import com.example.rxjavatest.Impl.accessibility.EntryAccessibilityService;
import com.example.rxjavatest.UiServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;

import static android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK;

public class UiServiceImpl extends UiServiceGrpc.UiServiceImplBase {
    private static final String TAG = UiServiceImpl.class.getCanonicalName();

    @Override
    public void clickByText(BaseTypeOuterClass.STRING request, StreamObserver<BaseTypeOuterClass.BOOLEAN> responseObserver) {
        AccessibilityNodeInfo rootAccessibilityNodeInfo = EntryAccessibilityService.getAccessibilityService().getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodeInfos = rootAccessibilityNodeInfo.findAccessibilityNodeInfosByText(request.getText());
        if (nodeInfos.size() == 0){
            Log.v(TAG, String.format("未找到包含%s的控件", request.getText()));
            responseObserver.onNext(BaseTypeOuterClass.BOOLEAN.newBuilder().setValue(false).build());
            responseObserver.onCompleted();
        }else {
            boolean result = nodeInfos.get(0).performAction(ACTION_CLICK);
            responseObserver.onNext(BaseTypeOuterClass.BOOLEAN.newBuilder().setValue(result).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void clickHome(BaseTypeOuterClass.VOID request, StreamObserver<BaseTypeOuterClass.BOOLEAN> responseObserver) {
        boolean result = EntryAccessibilityService.getAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        responseObserver.onNext(BaseTypeOuterClass.BOOLEAN.newBuilder().setValue(result).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickBack(BaseTypeOuterClass.VOID request, StreamObserver<BaseTypeOuterClass.BOOLEAN> responseObserver) {
        boolean result = EntryAccessibilityService.getAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        responseObserver.onNext(BaseTypeOuterClass.BOOLEAN.newBuilder().setValue(result).build());
        responseObserver.onCompleted();
    }
}
