package com.risenb.expandtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;

import com.risenb.expand.floatwindow.ActivityBind;
import com.risenb.expand.swipeback.base.SwipeBackUI;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/28
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MainUI extends SwipeBackUI {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_l);
    }

    public void banner(View v) {
        startActivity(new Intent(MainUI.this, BannerUI.class));
    }

    public void floatWindow(View v) {
        startActivity(new Intent(MainUI.this, FloatWindowUI.class));
    }

    public void doctoment(View v) {
        startActivity(new Intent(MainUI.this, DoctomentUI.class));
    }

    public void imagePick(View v) {

    }

    //  弹窗生命周期
    @Override
    protected void onResume() {
        super.onResume();
        ActivityBind.getInstance().onResume(MainUI.this);
    }

    //  弹窗生命周期
    @Override
    protected void onStop() {
        super.onStop();
        ActivityBind.getInstance().onStop(MainUI.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            ActivityBind.getInstance().funPress(MainUI.this);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityBind.getInstance().dismissFloat(MainUI.this);
    }


}
