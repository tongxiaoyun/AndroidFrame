package com.risenb.expandtest;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/16
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class BannerModel {
    private String imageUrl;
    private String mTips;

    public String getTips() {
        return mTips;
    }

    public void setTips(String tips) {
        mTips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
