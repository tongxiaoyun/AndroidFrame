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
public class SingleSelect extends PhotoSelectBuilder {

    public SingleSelect(PickerParams params) {
        super(params);
        super.params.mode = SelectMode.SINGLE;
    }
}