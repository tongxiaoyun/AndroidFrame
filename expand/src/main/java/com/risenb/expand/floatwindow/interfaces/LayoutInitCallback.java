package com.risenb.expand.floatwindow.interfaces;

import android.view.View;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/8
 * 描    述：悬浮框初始化回调
 * 修订历史：
 * ================================================
 */
public interface LayoutInitCallback {
    void initView(View v, String path);

    void reStart(View v, String path);

    void stop(View v);

    void destory(View v);
}
