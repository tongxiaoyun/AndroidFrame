package com.risenb.expand;

import android.view.View;

import com.risenb.expand.floatwindow.interfaces.LayoutInitCallback;
import com.risenb.expand.loading.model.LoadingM;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/6
 * 描    述：框架控制器
 * 修订历史：
 * ================================================
 */
public class m {
    private static  m instance;
    private LoadingM loadingM;
    private View floatViewLayout;
    private LayoutInitCallback callback;

    private m() {
        loadingM = new LoadingM();
    }

    public static m getInstance() {
        if (instance == null) {
            instance = new m();
        }
        return instance;
    }

    public LoadingM getLoadingM() {
        return loadingM;
    }

    public View getFloatView() {
        return floatViewLayout;
    }

    public void setFloatView(View floatViewLayout){
        this.floatViewLayout = floatViewLayout;
    }

    public LayoutInitCallback getCallback() {
        return callback;
    }

    public void setCallback(LayoutInitCallback callback) {
        this.callback = callback;
    }
}
