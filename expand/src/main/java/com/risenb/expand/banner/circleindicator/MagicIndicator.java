package com.risenb.expand.banner.circleindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/16
 * 描    述：整个框架的入口，核心
 * 修订历史：
 * ================================================
 */
public class MagicIndicator extends FrameLayout {
    private IPagerIndicator mNavigator;

    public MagicIndicator(Context context) {
        super(context);
    }

    public MagicIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mNavigator != null) {
            mNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    public void onPageSelected(int position) {
        if (mNavigator != null) {
            mNavigator.onPageSelected(position);
        }
    }

    public void onPageScrollStateChanged(int state) {
        if (mNavigator != null) {
            mNavigator.onPageScrollStateChanged(state);
        }
    }

    public IPagerIndicator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(IPagerIndicator navigator) {
        if (mNavigator == navigator) {
            return;
        }
        if (mNavigator != null) {
            mNavigator.onDetachFromMagicIndicator();
        }
        mNavigator = navigator;
        removeAllViews();
        if (mNavigator instanceof View) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView((View) mNavigator, lp);
            mNavigator.onAttachToMagicIndicator();
        }
    }
}

