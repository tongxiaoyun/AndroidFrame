package com.risenb.expand.imagepick.event;

import com.risenb.expand.imagepick.bean.Image;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface OnPhotoGridClickListener {
    void onCameraClick();

    void onPhotoClick(Image image, int positon);
}