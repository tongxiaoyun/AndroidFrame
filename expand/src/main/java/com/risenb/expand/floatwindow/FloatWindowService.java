package com.risenb.expand.floatwindow;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.risenb.expand.R;
import com.risenb.expand.m;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/8
 * 描    述：悬浮窗口控制类
 * 修订历史：
 * ================================================
 */
public class FloatWindowService extends Service implements View.OnTouchListener {

    /**
     * 窗口初始化
     */
    public static final String FLOAT_INIT = "FloatWindowService_FLOAT_INIT";

    /**
     * 窗口显示
     */
    public static final String FLOAT_SHOW_WINDOW = "FloatWindowService_FLOAT_SHOW";

    /**
     * 窗口隐藏
     */
    public static final String FLOAT_DISMISS_WINDOW = "FloatWindowService_FLOAT_DISMISS_WINDOW";


    /**
     * activity中生命周期
     */
    public static final String FLOAT_ACTIVITY_ONRESUME = "FloatWindowService_FLOAT_ACTIVITY_ONRESUME";


    /**
     * activity中生命周期
     */
    public static final String FLOAT_ACTIVITY_ONSTOP = "FloatWindowService_FLOAT_ACTIVITY_ONSTOP";


    /**
     * 多功能键被按
     */
    public static final String FLOAT_FUN = "FloatWindowService_FLOAT_HOME";

    /**
     * 视频播放地址
     */
    public static final String VIDEO_URL = "FloatWindowService_VIDEO_URL";


    private boolean isVisiable = true;

    private String path = "";


    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private View mWindowView;
    private int mStartX;
    private int mStartY;
    private int mEndX;
    private int mEndY;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册广播
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        initWindowParams();

    }

    /***
     * 初始化窗口的数据
     */
    private void initWindowParams() {
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        // 更多type：https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_PHONE
        {
            String packname = FloatWindowService.this.getPackageName();
            PackageManager pm = FloatWindowService.this.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;


            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;

            }
            wmParams.gravity = Gravity.TOP | Gravity.LEFT;
        }
        wmParams.format = PixelFormat.TRANSLUCENT;
        // 更多falgs:https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_NOT_FOCUSABLE
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /***
     * 初始化窗口的数据
     */
    private void initView(String path) {
        mWindowView = m.getInstance().getFloatView();
        mWindowView.setOnTouchListener(this);
        m.getInstance().getCallback().initView(mWindowView, path);
    }

    /***
     * 绑定到界面
     */
    private void addWindowView2Window() {
        if (mWindowView != null) {
            mWindowManager.addView(mWindowView, wmParams);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (FLOAT_SHOW_WINDOW.equals(intent.getAction())) {
                addWindowView2Window();
                path = intent.getStringExtra(VIDEO_URL);
                initView(path);
            } else if (FLOAT_DISMISS_WINDOW.equals(intent.getAction())) {
                if (mWindowView != null) {
                    //移除悬浮窗口
                    if (mWindowView.getParent() != null) {
                        mWindowManager.removeView(mWindowView);
                        m.getInstance().getCallback().destory(mWindowView);
                    }
                }
            } else if (FLOAT_ACTIVITY_ONRESUME.equals(intent.getAction())) {
                if (!isVisiable && mWindowView != null) {
                    mWindowView.setVisibility(View.VISIBLE);
                    m.getInstance().getCallback().reStart(mWindowView, path);
                    isVisiable = true;
                }

            } else if (FLOAT_ACTIVITY_ONSTOP.equals(intent.getAction())) {

            } else if (FLOAT_FUN.equals(intent.getAction())) {
                if (mWindowView != null) {
                    m.getInstance().getCallback().stop(mWindowView);
                    mWindowView.setVisibility(View.GONE);
                    isVisiable = false;
                }

            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWindowView != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mWindowView);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getRawX();
                mStartY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mEndX = (int) event.getRawX();
                mEndY = (int) event.getRawY();
                if (needIntercept()) {
                    //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                    wmParams.x = (int) event.getRawX() - mWindowView.getMeasuredWidth() / 2;
                    wmParams.y = (int) event.getRawY() - mWindowView.getMeasuredHeight() / 2;
                    mWindowManager.updateViewLayout(mWindowView, wmParams);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (needIntercept()) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 是否拦截
     *
     * @return true:拦截;false:不拦截.
     */
    private boolean needIntercept() {
        if (Math.abs(mStartX - mEndX) > 30 || Math.abs(mStartY - mEndY) > 30) {
            return true;
        }
        return false;
    }


    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY) || TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示按了home键,程序到了后台
                    if (mWindowView != null) {
                        mWindowView.setVisibility(View.GONE);
                        m.getInstance().getCallback().stop(mWindowView);
                        isVisiable = false;
                    }
                }
            }
        }
    };


}
