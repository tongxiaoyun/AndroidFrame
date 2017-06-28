package com.risenb.expand.imagepick.picker;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PhotoFilter implements java.io.Serializable{
    public boolean showGif;

    public int minWidth;

    public int minHeight;

    // 单位 kb
    public int minSize;

    public static PhotoFilter build() {
        return new PhotoFilter();
    }

    public PhotoFilter showGif(boolean showGif) {
        this.showGif = showGif;
        return this;
    }

    public PhotoFilter minWidth(int imageMinWidth) {
        this.minWidth = imageMinWidth;
        return this;
    }

    public PhotoFilter minHeight(int imageMinHeight) {
        this.minHeight = imageMinHeight;
        return this;
    }

    public PhotoFilter minSize(int imageMinSize) {
        this.minSize = imageMinSize * 1024;
        return this;
    }
}
