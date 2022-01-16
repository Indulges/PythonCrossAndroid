package com.example.rxjavatest.Impl;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.WeakHashMap;

public abstract class AbstratMethodHandler {
    private static final String TAG = AbstratMethodHandler.class.getCanonicalName();
    public WeakHashMap<Integer, Object> mObjectMap = new WeakHashMap<>();
    public WeakHashMap<String, Class> mClassMap = new WeakHashMap<>();


    public Class findClass(String className){
        if (mClassMap.containsKey(className)){
            Log.v(TAG, "使用缓存类");
            return mClassMap.get(className);
        }else {
            try {
                Class clazz = Class.forName(className);
                mClassMap.put(className, clazz);
                return clazz;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public Object classNecessary(Class<?> currentClass, Class<?> targetClass, Object object){
        if (currentClass.equals(targetClass)){
            return object;
        }
        if (currentClass.isAssignableFrom(targetClass)){
            return object;
        } else if (currentClass.equals(int.class) && targetClass.equals(Integer.class)){
            return object;
        } else if (currentClass.equals(long.class) && targetClass.equals(Long.class)){
            return object;
        } else if (currentClass.equals(char.class) && targetClass.equals(Byte.class)){

        }

    }
}
