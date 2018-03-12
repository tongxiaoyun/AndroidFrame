package com.risenb.expand.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2018/3/12
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class Log {
    public static boolean debug = false;

    public Log() {
    }


    public static void i(String TAG, String msg) {
        if (debug) {
            android.util.Log.i(TAG, "[" + getFileLineMethod() + "]" + msg);
        }

    }

    public static void i(String msg) {
        if (debug) {
            android.util.Log.i("UI", getFileLineMethod() + msg);
        }

    }

    public static void d(String TAG, String method, String msg) {
        android.util.Log.d(TAG, "[" + method + "]" + msg);
    }

    public static void d(String TAG, String msg) {
        if (debug) {
            android.util.Log.d(TAG, "[" + getFileLineMethod() + "]" + msg);
        }

    }

    public static void d(String msg) {
        if (debug) {
            android.util.Log.d(_FILE_(), "[" + getLineMethod() + "]" + msg);
        }

    }

    public static void e(String msg) {
        if (debug) {
            android.util.Log.e("UI", getFileLineMethod() + msg);
        }

    }

    public static void e(String TAG, String msg, Exception e) {
        if (debug) {
            android.util.Log.e(TAG, msg, e);
        }

    }

    public static void e(String TAG, String msg) {
        if (debug) {
            android.util.Log.e(TAG, getFileLineMethod() + msg);
        }

    }

    public static String getFileLineMethod() {
        StackTraceElement traceElement = (new Exception()).getStackTrace()[2];
        StringBuffer toStringBuffer = (new StringBuffer("[")).append(traceElement.getFileName()).append(" | ").append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("]");
        return toStringBuffer.toString();
    }

    public static String getLineMethod() {
        StackTraceElement traceElement = (new Exception()).getStackTrace()[2];
        StringBuffer toStringBuffer = (new StringBuffer("[")).append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("]");
        return toStringBuffer.toString();
    }

    public static String _FILE_() {
        StackTraceElement traceElement = (new Exception()).getStackTrace()[2];
        return traceElement.getFileName();
    }

    public static String _FUNC_() {
        StackTraceElement traceElement = (new Exception()).getStackTrace()[1];
        return traceElement.getMethodName();
    }

    public static int _LINE_() {
        StackTraceElement traceElement = (new Exception()).getStackTrace()[1];
        return traceElement.getLineNumber();
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }

    public static void mem() {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long total = runtime.totalMemory();
        long max = runtime.maxMemory();
        runtime.gc();
        e("内存 = " + (max >> 20) + "M" + " 已用 = " + memTrim(total) + " 可用 = " + memTrim(max - total));
    }

    public static String memTrim(long num) {
        boolean max = true;
        String str = "";
        if (num > 0L) {
            str = String.valueOf(num % 1024L);
            num >>= 10;
        }

        if (num > 0L) {
            str = num % 1024L + "," + str;
            num >>= 10;
        }

        if (num > 0L) {
            str = num + "," + str;
        }

        return str;
    }
}
