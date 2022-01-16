package com.example.rxjavatest;

import android.content.Context;
import android.util.Log;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import io.grpc.InternalChannelz;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = ExampleInstrumentedTest.class.getCanonicalName();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.rxjavatest", appContext.getPackageName());
    }

    public static void test(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8888).usePlaintext().build();
        ProtoTestGrpc.ProtoTestBlockingStub protoTestStub = ProtoTestGrpc.newBlockingStub(channel);
        com.example.rxjavatest.Test.STRING str = com.example.rxjavatest.Test.STRING.newBuilder().setText("测试成功").build();
        com.example.rxjavatest.Test.INT result = protoTestStub.testMethod(str);
        Log.v(TAG, String.format("result = %d", result.getText()));
    }

    public static void main(String...args){

    }
}