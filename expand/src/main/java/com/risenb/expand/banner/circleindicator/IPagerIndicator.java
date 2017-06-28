package com.risenb.expand.banner.circleindicator;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/16
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface IPagerIndicator {

    ///////////////////////// ViewPager的3个回调
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);
    /////////////////////////

    /**
     * 当IPagerNavigator被添加到MagicIndicator时调用
     */
    void onAttachToMagicIndicator();

    /**
     * 当IPagerNavigator从MagicIndicator上移除时调用
     */
    void onDetachFromMagicIndicator();

    /**
     * ViewPager内容改变时需要先调用此方法，自定义的IPagerNavigator应当遵守此约定
     */
    void notifyDataSetChanged();
}
