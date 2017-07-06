package com.risenb.expandtest;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.VideoView;

import com.risenb.expand.floatwindow.FloatWindowService;
import com.risenb.expand.floatwindow.interfaces.LayoutInitCallback;
import com.risenb.expand.m;

import java.util.List;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/9
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MyApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        m.getInstance().setFloatView(LayoutInflater.from(this).inflate(R.layout.float_window, null));
        m.getInstance().setCallback(new LayoutInitCallback() {
            @Override
            public void initView(View v, String path) {
                VideoView vv = (VideoView) v.findViewById(R.id.vv);
                vv.setVideoURI(Uri.parse(path));
                vv.start();
            }

            @Override
            public void reStart(View v, String path) {
                VideoView vv = (VideoView) v.findViewById(R.id.vv);
                vv.setVideoURI(Uri.parse(path));
                vv.start();
            }

            @Override
            public void stop(View v) {
                VideoView vv = (VideoView) v.findViewById(R.id.vv);
                vv.pause();

            }

            @Override
            public void destory(View v) {

            }
        });
    }


}
