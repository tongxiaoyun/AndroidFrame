package com.risenb.expand.loading.model;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/14
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class LoadingM {

    /**
     *
     * 加载动画
     * */
    private int indicatorId = -1;

    /**
     * 距离上边的距离
     */
    private int marginsTop ;
    /**
     * 距离下边的的距离
     */
    private int marginsBottom ;

    private int tabHeight ;

    public int getTabHeight() {
        return tabHeight;
    }

    public void setTabHeight(int tabHeight) {
        this.tabHeight = tabHeight;
    }

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public int getMarginsTop() {
        return marginsTop;
    }

    public void setMarginsTop(int marginsTop) {
        this.marginsTop = marginsTop;
    }

    public int getMarginsBottom() {
        return marginsBottom;
    }

    public void setMarginsBottom(int marginsBottom) {
        this.marginsBottom = marginsBottom;
    }
}
