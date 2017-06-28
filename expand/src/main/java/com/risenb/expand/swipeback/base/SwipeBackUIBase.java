package com.risenb.expand.swipeback.base;


import com.risenb.expand.swipeback.SwipeBackLayout;

/**
 * Created by tongxiaoyun on 2017/1/23.
 */

public interface SwipeBackUIBase {
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    void scrollToFinishActivity();
}
