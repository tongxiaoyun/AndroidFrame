package com.risenb.expand.banner;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/6/14
 * 描    述：BannerAdapter
 * 修订历史：
 * ================================================
 */
public abstract class BannerAdapter<T> {
    private List<T> mDataList;

    List<T> getDataList() {
        return mDataList;
    }

    protected BannerAdapter(List<T> dataList) {
        mDataList = dataList;
    }

    void setImageViewSource(ImageView imageView, int position) {
        bindImage(imageView, mDataList.get(position));
    }

    void selectTips(TextView tv, int position) {
        if (mDataList != null && mDataList.size() > 0)
            bindTips(tv, mDataList.get(position));
    }

    protected abstract void bindTips(TextView tv, T t);

    public abstract void bindImage(ImageView imageView, T t);


}