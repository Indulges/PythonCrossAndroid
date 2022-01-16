package com.example.rxjavatest;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.rxjavatest.Impl.PythonCrossAndroidImpl;
import io.grpc.*;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.NettyServerProvider;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Books books = new Books();
//        Observable<String> observable = Observable.create(books);
//        observable.observeOn(Schedulers.io());
//        observable.subscribeOn(AndroidSchedulers.mainThread());
//        observable.subscribe(new Reader());
//        for (int i = 0; i <= 10; i ++ ){
//            books.updataStory();
//        }
//        Observable.just("1", "2").observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Reader());
//        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Exception {
//                long times = emitter.requested();
//            }
//        }, BackpressureStrategy.BUFFER);
//        flowable.observeOn(Schedulers.io());
//
//        flowable.subscribeOn(AndroidSchedulers.mainThread());
//        flowable.subscribe(new FlowableSubscriber<String>() {
//            @Override
//            public void onSubscribe(@NonNull Subscription s) {
//                s.request(100);
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//        ManagedChannelBuilder managedChannelBuilder = ManagedChannelBuilder.forAddress("192.168.1.1", 8088).usePlaintext();
//        ManagedChannel channel = managedChannelBuilder.build();
//        GrpcTest grpcTest = new GrpcTest();
//        grpcTest.bindService();
//        startGrpcService();
//        grpcMethodTest();
//        mHandler.obtainMessage(4).sendToTarget();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startGrpcService();
            }
        }).start();
//        grpcMethodTest();
    }




    public void startGrpcService(){
        try {
            ServerRegistry.getDefaultRegistry().register(new NettyServerProvider());
            //port > 0 port < 65535
            NettyServerBuilder nettyServerBuilder = NettyServerBuilder.forAddress(new InetSocketAddress("localhost", 8888));
            Server server = nettyServerBuilder.addService(new PythonCrossAndroidImpl()).build();
            server.start();
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void grpcMethodTest(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8888).usePlaintext().build();
        ProtoTestGrpc.ProtoTestBlockingStub protoTestStub = ProtoTestGrpc.newBlockingStub(channel);
//        Test.STRING str = Test.STRING.newBuilder().setText("测试成功").build();
//        Test.INT result = protoTestStub.testMethod(str);
//        Log.v(TAG, String.format("result = %d", result.getText()));
    }
}