package com.tik.android.component.libcommon;

import android.util.Log;

import java.util.Locale;

public class LogUtil {
    private static boolean ENABLE_DEBUG = true;
    private static boolean ENABLE_ERROR = true;

    public static String TAG = "tik.android";

    public static void setTAG(String tag) {
        TAG = tag;
    }

    public static void enableDebug(boolean enable) {
        ENABLE_DEBUG = enable;
    }

    public static boolean isEnableDebug() {
        return ENABLE_DEBUG;
    }

    public static void enableError(boolean enable) {
        ENABLE_ERROR = enable;
    }

    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('.') + 1);
                break;
            }
        }
        if (callingClass.contains("$")) {
            int end = callingClass.lastIndexOf("$");
            callingClass = callingClass.substring(0, end);
        }
        return callingClass + " : ";
    }

    private static String buildMessage(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String caller = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                caller = trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread()
                .getId(), caller, msg);
    }

    public static void v(String msg) {
        if (ENABLE_DEBUG) {
            Log.v(getTag() + TAG, buildMessage(msg));
        }
    }

    public static void d(String msg) {
        if (ENABLE_DEBUG) {
            // 测试阶段临时修改为 Error 类型，因为测试使用 Release手机测试，无法输出Debug 类型日志。
            if (BuildConfig.DEBUG) {
                Log.e(getTag() + TAG, buildMessage(msg));
            } else {
                Log.d(getTag() + TAG, buildMessage(msg));
            }
        }
    }

    public static void i(String msg) {
        if (ENABLE_DEBUG) {
            Log.i(getTag() + TAG, buildMessage(msg));
        }
    }

    public static void w(String msg) {
        if (ENABLE_DEBUG) {
            Log.w(getTag() + TAG, buildMessage(msg));
        }
    }

    public static void e(String msg) {
        if (ENABLE_ERROR) {//Different between other.
            Log.e(getTag() + TAG, buildMessage(msg));
        }
    }

    public static void e(String msg, Throwable e) {
        if (ENABLE_ERROR) {//Different between other.
            Log.e(getTag() + TAG, buildMessage(msg), e);
        }
    }

    public static void printTraceStack(String msg) {
        if (ENABLE_DEBUG) {
            Exception e = new Exception();
            StackTraceElement[] steArray = e.getStackTrace();
            Log.e(TAG, "---------------- Stack Trace ---------------");
            Log.e(TAG, "" + msg);
            for (StackTraceElement ste : steArray) {
                Log.e(TAG, ste.toString());
            }
        }
    }

    public static long currentTimeMillis() {
        long time = System.currentTimeMillis();// ms
        return time;
    }

    public static void duration(String msg, long start, long end) {
        Log.e(TAG, buildMessageThread(msg) + " , duration: " + (end - start) + "ms");
    }

    private static String buildMessageThread(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String caller = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                caller = trace[i].getMethodName();
                break;
            }
        }
        Thread thread = Thread.currentThread();
        return String.format(Locale.US, "%s (%s): %s",
                thread.toString(),
                caller,
                msg);
    }

}
