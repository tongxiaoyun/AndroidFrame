package com.risenb.expand.imagepick;


import android.content.Context;
import android.widget.ImageView;

import com.risenb.expand.R;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述：图片加载器
 * 修订历史：
 * ================================================
 */
public abstract class PhotoPickerImageLoader<IV extends ImageView, PV extends ImageView> {

    /**
     * 创建列表图片
     * @return
     */
    public abstract IV onCreateGridItemView(Context context);

    /**
     * 加载选择列表图片
     * @param view
     * @param imagePath
     * @param tag
     * @param width
     * @param height
     */
    public abstract void loadGridItemView(IV view, String imagePath, int tag, int width, int height);

    /**
     * 执行加载图片
     * @param mCxt
     * @param tag
     */
    public abstract void resumeRequests(Context mCxt, int tag);

    /**
     * 暂停加载图片
     * @param mCxt
     * @param tag
     */
    public abstract void pauseRequests(Context mCxt, int tag);

    /**
     * 创建预览图片
     * @return
     */
    public abstract PV onCreatePreviewItemView(Context context);

    /**
     * 加载选择列表图片
     * @param view
     * @param imagePath
     * @param width
     * @param height
     */
    public abstract void loadPreviewItemView(PV view, String imagePath, int width, int height);

    /**
     * 手指缓慢滑动图片列表的时候不去加载图片
     * @return
     */
    public boolean pauseOnScroll() {
        return false;
    }

    /**
     * 手指猛地一滑动图片列表的时候不去加载图片
     * @return
     */
    public boolean pauseOnFling() {
        return true;
    }

    public void clearMemoryCache() {

    }

    /**
     * 默认占位图
     * @return
     */
    public int getDefaultPlaceHolder() {
        return R.drawable.default_error;
    }
}