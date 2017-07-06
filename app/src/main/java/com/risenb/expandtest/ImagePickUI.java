package com.risenb.expandtest;

import android.view.KeyEvent;

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
public class ImagePickUI extends SwipeBackUI {

    @Override
    protected void onCreate() {
        super.onCreate();
    }


    //  弹窗生命周期
    @Override
    protected void onResume() {
        super.onResume();
        ActivityBind.getInstance().onResume(ImagePickUI.this);
    }

    //  弹窗生命周期
    @Override
    protected void onStop() {
        super.onStop();
        ActivityBind.getInstance().onStop(ImagePickUI.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            ActivityBind.getInstance().funPress(ImagePickUI.this);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
