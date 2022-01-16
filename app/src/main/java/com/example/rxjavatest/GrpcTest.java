package com.example.rxjavatest;

import android.util.Log;
import io.grpc.stub.StreamObserver;
import org.testng.annotations.Test;

public class GrpcTest extends ProtoTestGrpc.ProtoTestImplBase {
    private static final String TAG = GrpcTest.class.getCanonicalName();
//    @Override
//    public void sayHello(Test.HelloRequest request, StreamObserver<Test.HelloReply> responseObserver) {
//        String name = request.getName();
//        Log.v(TAG, "name = " + name);
//        Test.HelloReply helloReply = Test.HelloReply.newBuilder().setMessage("测试成功").build();
//        responseObserver.onNext(helloReply);
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void testMethod(Test.STRING request, StreamObserver<Test.INT> responseObserver) {
//        Log.v(TAG, "Text = " + request.getText());
//        responseObserver.onNext(Test.INT.newBuilder().setText(10).build());
//        responseObserver.onCompleted();
//    }
}
