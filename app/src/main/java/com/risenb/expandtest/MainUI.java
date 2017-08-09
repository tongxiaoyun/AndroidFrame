package com.risenb.expandtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;

import com.risenb.expand.floatwindow.ActivityBind;
import com.risenb.expand.imagepick.PhotoPicker;
import com.risenb.expand.imagepick.picker.Load;
import com.risenb.expand.swipeback.base.SwipeBackUI;

import java.util.ArrayList;

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


    private ArrayList<String> courseFile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_l);
        getAppDetailSettingIntent();
    }

    public void banner(View v) {
        startActivity(new Intent(MainUI.this, BannerUI.class));
    }

    public void floatWindow(View v) {
        startActivity(new Intent(MainUI.this, FloatWindowUI.class));
    }

    public void doctoment(View v) {
    }

    public void imagePick(View v) {
        PhotoPicker.init(new GlideImageLoader(), null);
        courseFile.remove("");
        Load load = PhotoPicker.load()
                .showCamera(true)
                .gridColumns(3);
        load.multi().maxPickSize(9).selectedPaths(courseFile).start(this);

    }


    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
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
