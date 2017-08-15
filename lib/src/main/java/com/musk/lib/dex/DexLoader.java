package com.musk.lib.dex;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by musk on 2017/5/16.
 */

public class DexLoader {
    private static DexLoader mInstance = null;
    private DexClassLoader dxLoader = null;

    private DexLoader() {
    }

    public static DexLoader createLoader() {
        if (mInstance == null) {
            synchronized (DexLoader.class) {
                mInstance = new DexLoader();
            }
        }
        return mInstance;
    }

    public void setLoadEnvironment(Context context, String dxPath) {
        File outPath = context.getDir("dxcache", Context.MODE_PRIVATE);
        dxLoader = new DexClassLoader(dxPath, outPath.getAbsolutePath(), null, context.getClassLoader());
    }

    /**
     * load Class
     *
     * @param classPath
     * @return
     */
    private Class loadClass(String classPath) {
        try {
            return dxLoader.loadClass(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * load instance
     *
     * @param classPath
     * @return
     */
    private Object loadInstance(String classPath) {
        try {
            Class clazz = loadClass(classPath);
            return clazz == null ? null : clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * invoke static method without return
     *
     * @param classPath
     * @param methodName
     * @param paramTypes
     */
    public void invokeStaticMethodNoReturn(String classPath, String methodName, Class[] paramTypes, Object[] params) {
        try {
            Class clazz = loadClass(classPath);
            Method method = clazz.getMethod(methodName, paramTypes);
            method.invoke(clazz, params);
            Log.i("musk", "invoke:ok\t" + methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.i("musk", "ex:\t" + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.i("musk", "ex:\t" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("musk", "ex:\t" + e.getMessage());
        }
    }

    /**
     * invoke static method with return
     *
     * @param classPath
     * @param methodName
     * @param paramTypes
     */
    public Object invokeStaticMethodWithReturn(String classPath, String methodName, Class[] paramTypes, Object[] params) {
        try {
            Class clazz = loadClass(classPath);
            Method method = clazz.getMethod(methodName, paramTypes);
            Log.i("musk", "invoke:ok\t" + methodName);
            return method.invoke(clazz, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.i("musk", "ex:\t" + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.i("musk", "ex:\t" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("musk", "ex:\t" + e.getMessage());
        }
        return null;
    }

    /**
     * invoke instance method no return
     *
     * @param classPath
     * @param methodName
     * @param paramTypes
     * @param params
     */
    public void invokeNonstaticMethodNoReturn(String classPath, String methodName, Class[] paramTypes, Object[] params) {
        try {
            Class clazz = loadClass(classPath);
            Object obj = clazz.newInstance();
            Method method = clazz.getMethod(methodName, paramTypes);
            Log.i("musk", "invoke:" + methodName);
            method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.i("musk", "ex:" + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.i("musk", "ex:" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("musk", "ex:" + e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.i("musk", "ex:" + e.getMessage());
        }
    }

    /**
     * invoke instance method no return
     *
     * @param classPath
     * @param methodName
     * @param paramTypes
     * @param params
     */
    public Object invokeNonstaticMethodWithReturn(String classPath, String methodName, Class[] paramTypes, Object[] params) {
        try {
            Class clazz = loadClass(classPath);
            Object obj = clazz.newInstance();
            Method method = clazz.getMethod(methodName, paramTypes);
            return method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}