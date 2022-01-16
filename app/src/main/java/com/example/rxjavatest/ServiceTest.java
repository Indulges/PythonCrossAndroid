package com.example.rxjavatest;

import android.util.Log;
import io.grpc.ServerBuilder;
import io.grpc.ServerProvider;

public class ServiceTest extends ServerProvider {
    private static final String TAG = ServiceTest.class.getCanonicalName();



    @Override
    protected boolean isAvailable() {
        return false;
    }

    @Override
    protected int priority() {
        return 0;
    }

    @Override
    protected ServerBuilder<?> builderForPort(int port) {
        return null;
    }
}
