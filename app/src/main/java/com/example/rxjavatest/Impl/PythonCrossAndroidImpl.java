package com.example.rxjavatest.Impl;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.example.rxjavatest.BaseTypeOuterClass;
import com.example.rxjavatest.PythonCrossAndroidGrpc;
import com.example.rxjavatest.PythonCrossAndroidOuterClass;
import io.grpc.stub.StreamObserver;

public class PythonCrossAndroidImpl extends PythonCrossAndroidGrpc.PythonCrossAndroidImplBase {
    private static final String TAG = PythonCrossAndroidImpl.class.getCanonicalName();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void invokeMethod(BaseTypeOuterClass.Requester request, StreamObserver<BaseTypeOuterClass.Responder> responseObserver) {
        long startTime = System.currentTimeMillis();
        BaseTypeOuterClass.Responder responder = ClassHanlder.getInstance().invokeStaticMethod(request);
        responseObserver.onNext(responder);
        responseObserver.onCompleted();
        long endTime = System.currentTimeMillis();
        Log.v(TAG, String.format("耗时:%d毫秒", endTime - startTime));
    }
}
