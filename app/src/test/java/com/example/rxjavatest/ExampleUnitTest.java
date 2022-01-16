package com.example.rxjavatest;

import android.util.Log;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = ExampleUnitTest.class.getCanonicalName();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        Log.v(TAG, "测试成功");
        System.out.println("测试成功");
    }

    @Test
    public void test(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8888).usePlaintext().build();
        ProtoTestGrpc.ProtoTestBlockingStub protoTestStub = ProtoTestGrpc.newBlockingStub(channel);
//        com.example.rxjavatest.Test.STRING str = com.example.rxjavatest.Test.STRING.newBuilder().setText("测试成功").build();
//        com.example.rxjavatest.Test.INT result = protoTestStub.testMethod(str);
        com.example.rxjavatest.Test.HelloRequest helloRequest = com.example.rxjavatest.Test.HelloRequest.newBuilder().setName("测试成功").build();
        protoTestStub.sayHello(helloRequest);
//        Log.v(TAG, String.format("result = %d", result.getText()));
    }
}