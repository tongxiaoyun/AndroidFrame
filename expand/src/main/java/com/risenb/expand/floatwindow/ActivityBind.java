package com.risenb.expand.floatwindow;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/13
 * 描    述：activity绑定 用于窗口绑定
 * 修订历史：
 * ================================================
 */
public class ActivityBind {

    private static ActivityBind instance;


    private ActivityBind() {
    }

    public static ActivityBind getInstance() {
        if (instance == null) {
            instance = new ActivityBind();
        }
        return instance;
    }

    /**
     * 显示
     */
    public void showFloat(Context context, String path) {
        Intent service = new Intent(context, FloatWindowService.class);
        service.setAction(FloatWindowService.FLOAT_SHOW_WINDOW);
        service.putExtra(FloatWindowService.VIDEO_URL, path);
        context.startService(service);
    }


    /**
     * 隐藏
     */
    public void dismissFloat(Context context) {
        Intent service = new Intent(context, FloatWindowService.class);
        service.setAction(FloatWindowService.FLOAT_DISMISS_WINDOW);
        context.startService(service);
    }

    /**
     * 生命周期
     */
    public void onResume(Context context) {
        Intent service = new Intent(context, FloatWindowService.class);
        service.setAction(FloatWindowService.FLOAT_ACTIVITY_ONRESUME);
        context.startService(service);
    }

    /**
     * 生命周期
     */
    public void onStop(Context context) {
        Intent service = new Intent(context, FloatWindowService.class);
        service.setAction(FloatWindowService.FLOAT_ACTIVITY_ONSTOP);
        context.startService(service);
    }


    /**
     * 多功能键被按
     */
    public void funPress(Context context) {
        Intent service = new Intent(context, FloatWindowService.class);
        service.setAction(FloatWindowService.FLOAT_FUN);
        context.startService(service);
    }

}

