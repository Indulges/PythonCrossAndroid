package com.example.rxjavatest.Impl;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.example.rxjavatest.BaseTypeOuterClass;
import com.example.rxjavatest.PythonCrossAndroidOuterClass;
import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ClassHanlder extends AbstratMethodHandler{
    private static final String TAG = ClassHanlder.class.getCanonicalName();
    private static ClassHanlder sClassHander;

    public static ClassHanlder getInstance(){
        if (sClassHander == null){
            synchronized (ClassHanlder.class){
                if (sClassHander == null){
                    sClassHander = new ClassHanlder();
                }
            }
        }
        return sClassHander;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public BaseTypeOuterClass.Responder findClass(BaseTypeOuterClass.Requester requester){
        try {
            Class clazz = null;
            String className = requester.getClassName();
            if (mClassMap.containsKey(className)){
                clazz = mClassMap.get(className);
            }else {
                clazz = Class.forName(className);
                mClassMap.put(className, clazz);
            }
            if (clazz != null){
                return getResponder(clazz, null);
            }
            return getResponder(clazz, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return getResponder(null, e);
        }
    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public BaseTypeOuterClass.Responder invokeMethod(BaseTypeOuterClass.Requester requester){
//        Object[] args = null;
//        Object object = null;
//        if (requester.getArgsCount() > 0){
//            args = getArgsArrays(requester.getArgsList());
//        }
//        if (requester.getMethodName() == null){
//            Log.e(TAG, "method name is null");
//            return getResponder(null, new Exception("method name is null"));
//        }
//        if (mObjectMap.containsKey(requester.getObjectHashCode())){
//            object = mObjectMap.get(requester.getObjectHashCode());
//            //非静态方法
//            object.getClass().getDeclaredMethod(requester.getMethodName(), arg);
//        }else {
//            //静态方法
//            return getResponder(null, new Exception("Please crete instance first"));
//        }
//
//
//        Method[] methods = (Method[]) Arrays.stream(object.getClass().getDeclaredMethods()).filter(item -> item.getName().equals(requester.getMethodName())).toArray();
//        if (methods.length == 1){
//
//        }else if (methods.length == 0){
//
//        }else {
//            return getResponder(null, new Exception(String.format("not found this method %s %S", requester.getCrequester.getMethodName())));
//        }
//
//
//        //
//    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public BaseTypeOuterClass.Responder invokeStaticMethod(BaseTypeOuterClass.Requester requester){
        Object[] args = null;
        Object[] finallyArgs = null;
        Method targetMethod = null;
        if (requester.getArgsCount() > 0){
            args =  argsInitialization(requester.getArgsList());
        }
        if (requester.getMethodName() == null){
            Log.e(TAG, "method name is null");
            return getResponder(null, new Exception("method name is null"));
        }
        Class clazz = findClass(requester.getClassName());
        if (clazz == null){
            return getResponder(null, new Exception("class not found"));
        }
        //多个方法
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(item -> item.getName().equals(requester.getMethodName())).collect(Collectors.toList());
        if (methods.size() == 0){
            return getResponder(null, new Exception(String.format("not found this method %s %s", requester.getClassName(), requester.getMethodName())));
        }else {
            for (Method method : methods){
                if ((args == null && method.getParameterCount() == 0) ||
                        (method.getParameterCount() == args.length)){
                    method.setAccessible(true);
                    if (args != null){//有参
                        Object[] newArgs = argsTypeConversion(method.getParameterTypes(), args);
                        if (newArgs == null && methods.size() == 1){
                            return getResponder(null, new Exception("args cast fail"));
                        }else if (newArgs == null){//转化失败，但还有其他方法，所以跳过当前方法
                            Log.e(TAG, "cast fail");
                            continue;
                        }else {
                            finallyArgs = newArgs;
                            targetMethod = method;
                            break;
                        }
                    }else {//无参
                        targetMethod = method;
                        break;
                    }
                }
            }
            try {
                Object result = targetMethod.invoke(null, finallyArgs);
                mObjectMap.put(result.hashCode(), result);
                return getResponder(result, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return getResponder(null, e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return getResponder(null, e);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean hasAttribute(BaseTypeOuterClass.Requester requester){
        Log.v(TAG, "hasAttribute");
         Class<?> clazz = findClass(requester.getClassName());
         if (clazz != null){
             Field[] fields = clazz.getDeclaredFields();
             if (fields.length > 0){
                 String attributeName = requester.getAttributeName();
                 Log.v(TAG, String.format("find attributeName %s", attributeName));
                 List<Field> fieldList = Arrays.stream(fields).filter(field -> field.getName().equals(requester.getAttributeName())).collect(Collectors.toList());
                 if (fieldList.size() > 0){
                     return true;
                 }else {
                     Log.v(TAG, "fieldList size is zero");
                 }
             }
         }else {
             Log.e(TAG, "class is null");
         }
         return false;
    }

    public Object[] argsTypeConversion(Class<?>[] classes, Object[] args){
        ArrayList<Object> newArrays = new ArrayList<>();
        if (classes.length != args.length){
            Log.e(TAG, String.format("args count error， class count is %d, but arg count is %d", classes.length, args.length));
            return null;
        }else {
            try {
                for (int i = 0;classes.length > 0 && i < classes.length; i ++){
                    Log.v(TAG, String.format("class is %s args is %s", classes[i].getCanonicalName(), args[i].getClass().getCanonicalName()));
                    if (classes[i].getCanonicalName().equals(args[i].getClass().getCanonicalName())){
                        newArrays.add(args[i]);
                    }else {
                        if (classes[i].equals(int.class) && args[i].getClass().equals(Integer.class)){
                            newArrays.add(args[i]);
                        }else {
                            newArrays.add(classes[i].cast(args[i]));
                        }
                    }
                }
                return newArrays.toArray();
            }catch (ClassCastException e){
                e.printStackTrace();
                return null;
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public Object[] argsInitialization(List<BaseTypeOuterClass.ANY> list){
        List<Object> argslist = new ArrayList<>();
        List<Class> argsClassList = new ArrayList<>();
        list.stream().forEach(any -> {
            switch (any.getAnyTypeCase()){
                case RESPONDER:
                    if (any.getResponder().getIsClass()){
                        //
//                        解Class，解Object

                    }
                case DOUBLE:
                    argslist.add(any.getDouble());
                    argsClassList.add(double.class);
                    break;
                case FLOAT:
                    argslist.add(any.getFloat());
                    argsClassList.add(float.class);
                    break;
                case INT32:
                    argslist.add(any.getInt32());
                    argsClassList.add(int.class);
                    break;
                case INT64:
                    argslist.add(any.getInt64());
                    argsClassList.add(long.class);
                    break;
                case UINT32:
                    argslist.add(any.getUint32());
                    argsClassList.add(int.class);
                    break;
                case UINT64:
                    argslist.add(any.getUint64());
                    argsClassList.add(long.class);
                    break;
                case SINT32:
                    argslist.add(any.getSint32());
                    argsClassList.add(int.class);
                    break;
                case SINT64:
                    argslist.add(any.getSint64());
                    argsClassList.add(long.class);
                    break;
                case FIXED32:
                    argslist.add(any.getFixed32());
                    argsClassList.add(int.class);
                    break;
                case FIXED64:
                    argslist.add(any.getFixed64());
                    argsClassList.add(long.class);
                    break;
                case BOOL:
                    argslist.add(any.getBool());
                    argsClassList.add(boolean.class);
                    break;
                case STRING:
                    argslist.add(any.getString());
                    argsClassList.add(String.class);
                    break;
                case BYTES:
                    argslist.add(any.getBytes());
                    argsClassList.add(ByteString.class);
                    break;
            }
        });
        return argslist.toArray();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public BaseTypeOuterClass.Responder getResponder(Object obj, Exception e){
        BaseTypeOuterClass.Responder.Builder builder = BaseTypeOuterClass.Responder.newBuilder();
        if (obj == null){
            builder.setError("obj or class is null or method return null");
            return builder.build();
        }
        if (e != null){
            StringBuffer stringBuffer = new StringBuffer();
            Arrays.stream(e.getStackTrace()).forEach(item -> {
                stringBuffer.append(item.toString());
            });
            builder.setError(stringBuffer.toString());
            return builder.build();
        }
        if (obj.getClass().equals(Class.class)){
            builder.setIsClass(true);
            builder.setClassName(((Class<?>) obj).getCanonicalName());
        }else {
            builder.setClassName(obj.getClass().getCanonicalName());
            builder.setIsClass(false);
            builder.setObjectHashCode(obj.hashCode());
        }
        return builder.build();
    }


}
