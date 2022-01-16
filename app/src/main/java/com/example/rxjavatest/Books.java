package com.example.rxjavatest;


import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

import java.util.Arrays;
import java.util.Random;

public class Books implements ObservableOnSubscribe<String> {
    private static final String TAG = Books.class.getCanonicalName();
    private ObservableEmitter<String> mEmitter;


    @Override
        public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
        mEmitter = emitter;
        Log.v(TAG, "书已注册成被观察者");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updataStory(){
        String [] storyNames = new String[]{"斗破苍穹", "将夜", "仙域"};
        Random random = new Random();
        String name = storyNames[random.nextInt(3)];
        Log.v(TAG, String.format("小说%s更新",name));
        mEmitter.onNext(name);
    }

    public ObservableEmitter<String> getEmitter(){
        if (mEmitter != null){
            return mEmitter;
        }
        return null;
    }
}
