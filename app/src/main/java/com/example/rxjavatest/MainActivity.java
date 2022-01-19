package com.example.rxjavatest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.rxjavatest.Impl.PythonCrossAndroidImpl;
import com.example.rxjavatest.Impl.UiServiceImpl;
import com.example.rxjavatest.Impl.accessibility.EntryAccessibilityService;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private AlertDialog mAccessibilitySettingDialog;
    private AlertDialog mPermissionDialog;
    private static final int REQUESTCODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Books books = new Books();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startGrpcService();
            }
        }).start();
        checkAccessibilitySetting();
    }


    public void checkAccessibilitySetting(){
        Log.v(TAG, "openAccessibilitySetting");
        if (!EntryAccessibilityService.isAccessibilityServiceON(this)){
            mAccessibilitySettingDialog = new AlertDialog.Builder(this).
                    setMessage("您需要开启辅助服务以保证脚本正常运行").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog(mAccessibilitySettingDialog);
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog(mAccessibilitySettingDialog);
                        }
                    }).create();
            mAccessibilitySettingDialog.show();
        }
    }

    //关闭对话框
    private void cancelPermissionDialog(AlertDialog alertDialog) {
        alertDialog.cancel();
    }

    public void startGrpcService(){
        try {
            ServerRegistry.getDefaultRegistry().register(new NettyServerProvider());
            //port > 0 port < 65535
            NettyServerBuilder nettyServerBuilder = NettyServerBuilder.forAddress(new InetSocketAddress("localhost", 8888));
            Server server = nettyServerBuilder
                    .addService(new PythonCrossAndroidImpl())
                    .addService(new UiServiceImpl())
                    .build();
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