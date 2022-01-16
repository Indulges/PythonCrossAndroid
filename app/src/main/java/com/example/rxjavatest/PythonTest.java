package com.example.rxjavatest;

import android.util.Log;

public class PythonTest {
    private static final String TAG = PythonTest.class.getCanonicalName();
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int age = 10;
    public String name = "张山";

    public static String pythonTest(){
        Log.v(TAG, "调用方法pythonTest");
        return "调用方法pythonTest";
    }

    public static String pythonTest1(String test, int num){
        Log.v(TAG, String.format("调用方法pythonTest test = %s  num = %d", test, num));
        return "调用方法pythonTest1";
    }
}
