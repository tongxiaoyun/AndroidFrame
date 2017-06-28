package com.risenb.expandtest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;


import com.risenb.expand.floatwindow.ActivityBind;
import com.risenb.expand.floatwindow.FloatWindowService;
import com.risenb.expand.floatwindow.interfaces.LayoutInitCallback;
import com.risenb.expand.m;
import com.risenb.expand.swipeback.base.SwipeBackUI;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/27
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class TestTwoUI extends SwipeBackUI {
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        findViewById(R.id.av_float_window).setVisibility(View.GONE);
        ActivityBind.getInstance().dismissFloat(TestTwoUI.this);


        VideoView vv = (VideoView) findViewById(R.id.vv);
        vv.setVideoURI(Uri.parse("http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4"));
        vv.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityBind.getInstance().onResume(TestTwoUI.this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ActivityBind.getInstance().onStop(TestTwoUI.this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askForPermission();
            } else {
                ActivityBind.getInstance().showFloat(TestTwoUI.this, "http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4");
                finish();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            ActivityBind.getInstance().funPress(TestTwoUI.this);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * 请求用户给予悬浮窗的权限
     */

    @TargetApi(Build.VERSION_CODES.M)
    public void askForPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(TestTwoUI.this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            ActivityBind.getInstance().showFloat(TestTwoUI.this, "http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4");
            finish();

        }
    }

    /**
     * 用户返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(TestTwoUI.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TestTwoUI.this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                //启动FxService
                ActivityBind.getInstance().showFloat(TestTwoUI.this, "http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4");

            }
            finish();

        }
    }


}
