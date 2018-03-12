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
public class PickerParams implements java.io.Serializable{

    public PhotoFilter filter;

    public boolean showCamera;

    // 选择模式
    public SelectMode mode;

    // 默认最大选择照片数量
    public int maxPickSize = 9;

    // 选择图片列表默认列数
    public int gridColumns = 3;

    public ArrayList<String> selectedPaths;

    public boolean showViedo;

}
