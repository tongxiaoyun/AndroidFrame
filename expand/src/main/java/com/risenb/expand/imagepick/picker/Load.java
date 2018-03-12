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
public class Load {

    private PickerParams params;

    public Load() {
        this.params = new PickerParams();
    }

    public Load filter(PhotoFilter filter) {
        this.params.filter = filter;
        return this;
    }

    public Load showCamera(boolean showCamera) {
        this.params.showCamera = showCamera;
        return this;
    }


    public Load gridColumns(int columns) {
        if (columns > 0) {
            this.params.gridColumns = columns;
        }
        return this;
    }

    public SingleSelect single() {
        return new SingleSelect(params);
    }

    public MultiSelect multi() {
        return new MultiSelect(params);
    }
}
