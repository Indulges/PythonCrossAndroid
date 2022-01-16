package com.example.rxjavatest;

import android.util.Log;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class Reader implements Observer<String>{
    private static final String TAG = Reader.class.getCanonicalName();

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        Log.v(TAG, "onSubscribe");
    }

    @Override
    public void onNext(@NonNull String book) {
        Log.v(TAG, String.format("收到%s的更新",book));
    }



    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {
        Log.v(TAG, "取消订阅");
    }


}
