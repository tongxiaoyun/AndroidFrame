package com.risenb.expand.imagepick.picker;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/27
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MultiSelect extends PhotoSelectBuilder {
    public MultiSelect(PickerParams params) {
        super(params);
        super.params.mode = SelectMode.MULTI;
    }

    public MultiSelect selectedPaths(ArrayList<String> paths) {
        super.params.selectedPaths = paths;
        return this;
    }

    public MultiSelect maxPickSize(int maxPickSize) {
        if(maxPickSize > 0) {
            super.params.maxPickSize = maxPickSize;
        }
        return this;
    }
}
